package com.example.library.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.example.library.beans.User;
import com.example.library.exception.CustomizeErrorCode;
import com.example.library.exception.CustomizeException;
import com.example.library.pojo.PaginationWithUsers;
import com.example.library.pojo.StateMessage;
import com.example.library.pojo.UsersQueryWithPage;
import com.example.library.service.AdminService;
import com.example.library.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminConterller {
	
	@Resource
	private AdminService adminService;
	
	@Resource
	private UserService userService;

	@PostMapping("/userList")
	public PaginationWithUsers hasUserList(@RequestBody String str, HttpServletRequest request) {
		
//		System.out.println(str);
		UsersQueryWithPage usersWithPage = JSON.parseObject(str, UsersQueryWithPage.class);
//		System.out.println(usersWithPage.getPage());
		
		userService.preHandle(request, usersWithPage.getAccountId());
		
		User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
        	throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
		
		PaginationWithUsers list = adminService.getAllUser(usersWithPage, user);
//		PaginationWithUsers list = adminService.getAllUser(usersWithPage);
		
		return list;
	}
	
	@PostMapping("/logout")
	public StateMessage logoutUser(@RequestBody String str) {
//		String deleteId = (String)JSON.parse(accountId);
		User user = JSON.parseObject(str, User.class);
	 	StateMessage stateMessage = adminService.deleteUser(user.getAccountId());
		return stateMessage;
	}
}
