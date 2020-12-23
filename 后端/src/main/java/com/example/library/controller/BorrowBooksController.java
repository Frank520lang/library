package com.example.library.controller;

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
import com.example.library.pojo.HistoryQueryWithPage;
import com.example.library.pojo.PaginationWithBooks;
import com.example.library.pojo.StateMessage;
import com.example.library.service.CurrentService;
import com.example.library.service.HistoryService;
import com.example.library.service.UserService;

@RestController
public class BorrowBooksController {
	
	@Resource
	private HistoryService historyService;
	
	@Resource
	private CurrentService currentService;
	
	@Resource
	private UserService userService;

	@PostMapping("/borrowBooks")
	public StateMessage borrowBooks(@RequestBody String str, HttpServletRequest request) throws CustomizeException {
		
		CurrBooks currBooks = JSON.parseObject(str, CurrBooks.class);
		
		userService.preHandle(request, currBooks.getAccountId());
		
		User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
        	throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        
		long sum = currentService.getCurrentCount(currBooks);
		StateMessage stateMessage = new StateMessage();
		if(sum >= 3) {
			stateMessage.setState("500");
			stateMessage.setMessage("您已经借了三本书了！！");
			return stateMessage;
		}else {
			currentService.doBorrow(currBooks);
//			historyService.inserHistory(currBooks);
			stateMessage.setState("200");
			stateMessage.setMessage("借书成功！！");
		}
		
		return stateMessage;
	}
	
	@PostMapping("/allBook")
	public PaginationWithBooks getAllBook(@RequestBody String str, HttpServletRequest request){
		
//		User user = (User) request.getSession().getAttribute("user");
//        if (user == null) {
//        	throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
////            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
//        }
		
//		System.out.println(accountId + " " + page);
		System.out.println(str);
		HistoryQueryWithPage historyQueryWithPage = JSON.parseObject(str, HistoryQueryWithPage.class);
		
		userService.preHandle(request, historyQueryWithPage.getAccountId());
		
		User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
        	throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
		
		PaginationWithBooks paginationWithBooks = currentService.getAllBookWithState(historyQueryWithPage);
		return paginationWithBooks;
	}
}
