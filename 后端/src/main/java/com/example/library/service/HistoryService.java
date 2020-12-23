package com.example.library.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.library.beans.CurrBooks;
import com.example.library.beans.Hisbooks;
import com.example.library.beans.HisbooksExample;
import com.example.library.beans.User;
import com.example.library.beans.UserExample;
import com.example.library.mapper.HisbooksMapper;
import com.example.library.mapper.UserMapper;
import com.example.library.pojo.HistoryQueryWithPage;
import com.example.library.pojo.HistoryWithBooks;
import com.example.library.pojo.PaginationWithHistory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HistoryService {
	
	@Resource
	private HisbooksMapper hisbooksMapper;
	
	@Resource
	private UserMapper userMapper;

	//返回分页数据；
	public PaginationWithHistory defaultQuery(HistoryQueryWithPage historyBooks) {
		// TODO Auto-generated method stub
		
		
		if(historyBooks.getSize() == null || historyBooks.getSize() < 0 ) {
			historyBooks.setSize(5);
		}
		
		if(historyBooks.getPage() != null && historyBooks.getPage() > 0) {
			int offset = historyBooks.getPage() == 1 ? 0 : (historyBooks.getSize()*(historyBooks.getPage()-1));
			historyBooks.setOffset(offset);
		}
		
		if(historyBooks.getLikeStr() != null && !"".equals(historyBooks.getLikeStr())) {
			historyBooks.setLikeStr("%" + historyBooks.getLikeStr() + "%");
		}
		
		//分页条目数
//		HisbooksExample hisbooksExample = new HisbooksExample();
//		hisbooksExample.createCriteria()
//				.andAccountIdEqualTo(historyBooks.getAccountId());
		long sum  = hisbooksMapper.countByLikeStr(historyBooks);
				
		PaginationWithHistory paginationWithHistory = new PaginationWithHistory();
		paginationWithHistory.setTotalPage(doTotalPage(sum));
		
		List<HistoryWithBooks> list = hisbooksMapper.selectByExampleWithBooks(historyBooks);
		
//		if(list.size() <= 0) {
//			
//			return paginationWithHistory;
//		}
		
		UserExample userExample = new UserExample();
		userExample.createCriteria()
				.andAccountIdEqualTo(historyBooks.getAccountId());
		List<User> selectByExample = userMapper.selectByExample(userExample);
		String name = null;
		
		if(selectByExample.size() > 0) {
			name = selectByExample.get(0).getName();
		}else {
			log.info("您的账户ID存在问题，请重新登录！！");
		}
		
		for(HistoryWithBooks tmpWithBooks : list) {
//			long endDateTime = System.currentTimeMillis();
//			long startDateTime = tmpWithBooks.getStaDate();
//			Integer subDays = (int)((endDateTime - startDateTime) / (1000*60*60*24));
//			tmpWithBooks.setRemainDate(30-subDays);
			tmpWithBooks.setName(name);
		}
		
		paginationWithHistory.setList(list);
		
		return paginationWithHistory;
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

	//更新书本的归还时间
	public void updateEndDate(CurrBooks tmpCurrBooks) {
		
		Hisbooks hisbooks = new Hisbooks();
		hisbooks.setEndDate(System.currentTimeMillis());
		
		HisbooksExample hisbooksExample = new HisbooksExample();
		hisbooksExample.createCriteria()
				.andBookIdEqualTo(tmpCurrBooks.getBookId())
				.andStaDateEqualTo(tmpCurrBooks.getStaDate())
				.andAccountIdEqualTo(tmpCurrBooks.getAccountId());
//		hisbooksMapper.updateByExample(hisbooks, hisbooksExample);
		
		hisbooksMapper.updateByExampleSelective(hisbooks, hisbooksExample);
	}

	//借书时插入借书记录
	public void inserHistory(CurrBooks currBooks) {
		// TODO Auto-generated method stub
		Hisbooks hisbooks = new Hisbooks();
		hisbooks.setAccountId(currBooks.getAccountId());
		hisbooks.setBookId(currBooks.getBookId());
		hisbooks.setStaDate(currBooks.getStaDate());
		hisbooksMapper.insertSelective(hisbooks);
	}

	
}
