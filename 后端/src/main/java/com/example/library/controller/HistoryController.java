package com.example.library.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.example.library.beans.CurrBooks;
import com.example.library.beans.User;
import com.example.library.exception.CustomizeErrorCode;
import com.example.library.exception.CustomizeException;
import com.example.library.pojo.CurrentWithBooks;
import com.example.library.pojo.HistoryQueryWithPage;
import com.example.library.pojo.PaginationWithHistory;
import com.example.library.pojo.StateMessage;
import com.example.library.service.CurrentService;
import com.example.library.service.HistoryService;
import com.example.library.service.UserService;

@RestController
public class HistoryController {
	
	@Resource
	private HistoryService historyService;
	
	@Resource
	private CurrentService currentService;
	
	@Resource
	private UserService userService;
	
	//处理获取查询当前该用户借阅书籍的请求
	@PostMapping("/currentBooks")	
	public List<CurrentWithBooks> currentBooks(@RequestBody String str, HttpServletRequest request) {
		//用户验证
        
		User tmpUser = JSON.parseObject(str, User.class);
//		CurrBooksExample currBooksExample = new CurrBooksExample();
//		currBooksExample.createCriteria().andAccountIdEqualTo(tmpUser.getAccountId());
		
		userService.preHandle(request, tmpUser.getAccountId());
		
		User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
        	throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
		
		List<CurrentWithBooks> list =  currentService.getCurrentBooks(tmpUser);
//		remainDate
		
		return list;
	}	
	
	//处理获取当前用户借阅历史的请求
	@PostMapping("/historyBooks")
	public PaginationWithHistory getHistoryBooks(@RequestBody String str, HttpServletRequest request){
        
//		System.out.println(str);
		HistoryQueryWithPage historyBooks = JSON.parseObject(str, HistoryQueryWithPage.class);

		userService.preHandle(request, historyBooks.getAccountId());
		
		User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
        	throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
		
		PaginationWithHistory paginationWithHistory = historyService.defaultQuery(historyBooks);
		return paginationWithHistory;
	}

	//处理当前用户归还某本书的请求
	@PostMapping("/booksBack")
	public StateMessage bookBack(@RequestBody String str) {
		CurrBooks tmpCurrBooks = JSON.parseObject(str, CurrBooks.class);
		//删除当前借阅表中的该记录
		currentService.doBackBooks(tmpCurrBooks);
		//跟新历史表中该借阅记录的end_state
		historyService.updateEndDate(tmpCurrBooks);
		StateMessage stateMessage = new StateMessage();
		stateMessage.setState("200");
		stateMessage.setMessage("还书成功！！");
		return stateMessage;
	}
}
