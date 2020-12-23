package com.example.library.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.example.library.beans.Books;
import com.example.library.beans.BooksExample;
import com.example.library.beans.BooksExample.Criteria;
import com.example.library.beans.CurrBooks;
import com.example.library.beans.CurrBooksExample;
import com.example.library.beans.User;
import com.example.library.beans.UserExample;
import com.example.library.exception.CustomizeErrorCode;
import com.example.library.exception.CustomizeException;
import com.example.library.mapper.BooksMapper;
import com.example.library.mapper.CurrBooksMapper;
import com.example.library.mapper.UserMapper;
import com.example.library.pojo.AllBookWithState;
import com.example.library.pojo.BookIdWithCurrentNumber;
import com.example.library.pojo.CurrentWithBooks;
import com.example.library.pojo.HistoryQueryWithPage;
import com.example.library.pojo.PaginationWithBooks;

@Service
public class CurrentService {

	@Resource
	private CurrBooksMapper currBooksMapper;
	
	@Resource
	private BooksMapper booksMapper;
	
	@Resource
	private HistoryService historyService;
	
	@Resource
	private MailService mailService;
	
	@Resource
	private UserMapper userMapper;

	//获取当前用户借阅的书籍详情
	public List<CurrentWithBooks> getCurrentBooks(User tmpUser) {
		// TODO Auto-generated method stub
		
		CurrBooksExample currBooksExample = new CurrBooksExample();
		currBooksExample.createCriteria().andAccountIdEqualTo(tmpUser.getAccountId());
	 	List<CurrentWithBooks> list =  currBooksMapper.getCurrentBooks(currBooksExample);
	 	
	 	for(CurrentWithBooks tmpWithBooks : list) {
			long endDateTime = System.currentTimeMillis();
			long startDateTime = tmpWithBooks.getStaDate();
			Integer subDays = (int)((endDateTime - startDateTime) / (1000*60*60*24));
			tmpWithBooks.setRemainDate(30-subDays);
		}
	 	
		return list;
	}

	//还书时，删除当前借阅表的数据
	public void doBackBooks(CurrBooks tmpCurrBooks) {
		// TODO Auto-generated method stub
		//可能存在数据已经被删除。
		CurrBooksExample currBooksExample = new CurrBooksExample();
		currBooksExample.createCriteria()
				.andBookIdEqualTo(tmpCurrBooks.getBookId())
				.andStaDateEqualTo(tmpCurrBooks.getStaDate())
				.andAccountIdEqualTo(tmpCurrBooks.getAccountId());
		
		currBooksMapper.deleteByExample(currBooksExample);
	}

	//借书操作时，插入记录到curr_books
	public void doBorrow(CurrBooks currBooks) {
		// TODO Auto-generated method stub
		CurrBooksExample currBooksExample = new CurrBooksExample();
		currBooksExample.createCriteria().andBookIdEqualTo(currBooks.getBookId());
		long sum = currBooksMapper.countByExample(currBooksExample);
		
		Books book = booksMapper.selectByBookId(currBooks.getBookId());
		
		if(sum < book.getNumber()) {
			currBooksMapper.insert(currBooks);
			historyService.inserHistory(currBooks);
		}else {
			throw new CustomizeException(CustomizeErrorCode.NO_BOOK);
		}
		
	}

	//返回该用户当前总共借了几本书
	public Long getCurrentCount(CurrBooks currBooks) {
		// TODO Auto-generated method stub
		
		CurrBooksExample currBooksExample = new CurrBooksExample();
		currBooksExample.createCriteria().andAccountIdEqualTo(currBooks.getAccountId());
		long sum = currBooksMapper.countByExample(currBooksExample);
		
		return sum;
	}

	//放回添加了书本状态的书本详情
	public PaginationWithBooks getAllBookWithState(HistoryQueryWithPage historyQueryWithPage) {
		// TODO Auto-generated method stub
		
		if(historyQueryWithPage.getSize() == null || historyQueryWithPage.getSize() < 0 ) {
			historyQueryWithPage.setSize(5);
		}
		
		if(historyQueryWithPage.getPage() != null && historyQueryWithPage.getPage() > 0) {
			int offset = historyQueryWithPage.getPage() == 1 ? 0 : (historyQueryWithPage.getSize()*(historyQueryWithPage.getPage()-1));
			historyQueryWithPage.setOffset(offset);
		}
		
		if(historyQueryWithPage.getPage() == null || historyQueryWithPage.getPage() < 0) {
			historyQueryWithPage.setOffset(0);
		}
		
		
		
		//!"".equals(historyQueryWithPage)
		BooksExample booksExample = new BooksExample();
		Criteria createCriteria = booksExample.createCriteria();
		if(historyQueryWithPage.getCountry() != null && !"".equals(historyQueryWithPage.getCountry()))
			createCriteria.andCountryEqualTo(historyQueryWithPage.getCountry());
		if(historyQueryWithPage.getSpace() != null && !"".equals(historyQueryWithPage.getSpace()))
			createCriteria.andSpaceEqualTo(historyQueryWithPage.getSpace());
		if(historyQueryWithPage.getClassification() != null && !"".equals(historyQueryWithPage.getClassification()))
			createCriteria.andClassificationEqualTo(historyQueryWithPage.getClassification());
		if(historyQueryWithPage.getTheme() != null && !"".equals(historyQueryWithPage.getTheme()))
			createCriteria.andThemeEqualTo(historyQueryWithPage.getTheme());
		if(historyQueryWithPage.getLikeStr() != null && !"".equals(historyQueryWithPage.getLikeStr()))
			createCriteria.andBookNameLike("%" + historyQueryWithPage.getLikeStr() + "%");
		
		RowBounds rowBounds = new RowBounds(historyQueryWithPage.getOffset(), historyQueryWithPage.getSize());
		
//		List<Books> selectByExample = booksMapper.selectByExample(booksExample);
		
		List<AllBookWithState> allBook = booksMapper.selectByExampleWithRowboundsOnDefine(booksExample, rowBounds);
//		HashSet<BookIdWithIndex> allBookSet = new HashSet<>();
		
		//将books的状态初始化为可借
		for(int i = 0; i < allBook.size(); i++) {
			allBook.get(i).setState("borrow");
//			allBookSet.add(new BookIdWithIndex(allBook.get(i).getBookId(), i));
		}
		//找出当前用户借阅的书籍
		CurrBooksExample currBooksExample = new CurrBooksExample();
		currBooksExample.createCriteria().andAccountIdEqualTo(historyQueryWithPage.getAccountId());
		List<CurrBooks> currentBooks = currBooksMapper.selectByExample(currBooksExample);
		
		//找出当前借出的书籍已经达到最大数目的bookId;
		List<BookIdWithCurrentNumber> currentBooksCount = currBooksMapper.getCurrentBooksNumber();
		
		//已借
		ArrayList<String> borrowed = new ArrayList<>();
		//不可借
		ArrayList<String> noBorrow = new ArrayList<>();
		
		
		
		//将不能借阅的书籍放入list方便遍历
		for(BookIdWithCurrentNumber bookIdWithCurrentNumber : currentBooksCount) {
			if(bookIdWithCurrentNumber.getCurrNumber() >= bookIdWithCurrentNumber.getNumber())
				noBorrow.add(bookIdWithCurrentNumber.getBookId());
		}
		//修改为不可借的状态
		for(int i = 0; i < allBook.size(); i++) {
			for(String bookIdString : noBorrow) {
				if(bookIdString.equals(allBook.get(i).getBookId())) {
					allBook.get(i).setState("noBorrow");
					break;
				}	
			}
		}
		
		//将当前用户借阅的书籍的book_id提取到list里方便遍历
				for(CurrBooks currBooks : currentBooks) {
					borrowed.add(currBooks.getBookId());
				}
				
				//修改为已借阅的状态
				for(int i = 0; i < allBook.size(); i++) {
					for(String bookIdString : borrowed) {
						if(bookIdString.equals(allBook.get(i).getBookId())) {
							allBook.get(i).setState("borrowed");
							break;
						}	
					}
				}
//		BooksExample tmpBooksExample = new BooksExample();
		long sum = booksMapper.countByExample(booksExample);
		
		PaginationWithBooks paginationWithBooks = new PaginationWithBooks();
		paginationWithBooks.setTotalPage(doTotalPage(sum));
		paginationWithBooks.setList(allBook);
		
		return paginationWithBooks;
	}
	
	public Long doTotalPage(Long sum) {
		long totalPage = 0;
		if(sum%5 != 0) {
			totalPage = sum /5 + 1;
		}else {
			totalPage = sum / 5;
		}
		return totalPage;
	}

	public boolean remainTimeInfo(User user) {
		// TODO Auto-generated method stub
		
		CurrBooksExample currBooksExample = new CurrBooksExample();
		currBooksExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
	 	List<CurrentWithBooks> list =  currBooksMapper.getCurrentBooks(currBooksExample);
	 	
	 	UserExample userExample = new UserExample();
	 	userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
	 	List<User> tmpUser = userMapper.selectByExample(userExample);
	 	
	 	for(CurrentWithBooks tmpWithBooks : list) {
			long endDateTime = System.currentTimeMillis();
			long startDateTime = tmpWithBooks.getStaDate();
			Integer subDays = (int)((endDateTime - startDateTime) / (1000*60*60*24));
			tmpWithBooks.setRemainDate(30-subDays);
		}
	 	
	 	List<CurrentWithBooks> sumList = new ArrayList<>();
	 	
	 	for(CurrentWithBooks tmpWithBooks : list) {
	 		if(tmpWithBooks.getRemainDate() <= 3) {
	 			sumList.add(tmpWithBooks);
	 		}
	 	}
	 	
	 	if(sumList.size() > 0) {
	 		//主题
	 	      String subject = "来自图书管理系统的提醒归还邮件";
	 	      
	 	      String context = "<p>您在本馆借阅的书籍：";
	 	      
	 	      for(CurrentWithBooks tmpWithBooks : sumList) {
	 	    	  String tmpString = tmpWithBooks.getBookName()  + " ";
	 	    	  context = context + tmpString;
	 	      }
	 	      context = context + "，不足三天就到归还时间了。</p>" + "<p>小书提醒您要及时归还哦</p>";
	 	      
	 	      //发送激活邮件
	 	      mailService.sendHtmlMail (tmpUser.get(0).getEmail(),subject,context);
	 	}

      
		return true;
		
	}
}
