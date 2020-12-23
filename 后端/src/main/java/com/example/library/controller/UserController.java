package com.example.library.controller;

import java.util.List;
import java.util.UUID;

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
import com.example.library.mapper.UserMapper;
import com.example.library.pojo.CurrentWithBooks;
import com.example.library.pojo.InfoWithTime;
import com.example.library.pojo.StateMessage;
import com.example.library.service.CurrentService;
import com.example.library.service.UserService;
import com.example.library.utils.Md5Utils2;

import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserMapper userMapper;

	@Resource
	private UserService userService;
	
	@Resource
	private CurrentService currentService;
	
	//测试用的
//	@GetMapping("/mess")
//	public StateMessage doGet(
//				@PathParam(value = "id") String id,
//				@PathParam(value = "month") String month
//	) {
//		
////		throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
////		StateMessage stateMessage = new StateMessage();
////		stateMessage.setState(id);
////		stateMessage.setMessage(month);
////		return stateMessage;
//	}
	
	//登录
	@PostMapping("/login")
	public StateMessage doLogin(
			@RequestBody String str
//			HttpServletResponse response
	){
		User tmpUser = JSON.parseObject(str, User.class);
		
//		System.out.println(Md5Utils.code("LTK@ltk0814"));
//		tmpUser.setPassword(Md5Utils.code(tmpUser.getPassword()));
		tmpUser.setPassword(Md5Utils2.convertMD5(tmpUser.getPassword()));
		System.out.println(tmpUser.getPassword());
		
		StateMessage stateMessage = new StateMessage();
		
		User user = userService.login(tmpUser);
		if(user != null){
//			Cookie cookie = new Cookie("token", user.getToken());
//			//cookie.setPath("/");
//			//cookie.set
////			cookie.setDomain(".");
//			cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
//			response.addCookie(cookie);
			
			stateMessage.setRight(user.getRight());
			stateMessage.setToken(user.getToken());
			stateMessage.setState("200");
			stateMessage.setMessage("登录成功！！");
			return stateMessage;
		}else {
			log.error("callback get login error,{}", tmpUser);
			// 登录失败，重新登录
			throw new CustomizeException(CustomizeErrorCode.ACCOUNTID_OR_PASSWORD_NOT_TRUE);
//			return new StateMessage("500","meiyou1");
		}
		
	}
	
	//忘记密码
	@PostMapping("/forgetPassword")
	public StateMessage doMailWithPassword(@RequestBody String str) {
		
		User tmpUser = JSON.parseObject(str, User.class);
		boolean isTrue = userService.forgetPassword(tmpUser);
		
		StateMessage stateMessage = new StateMessage();
		if(isTrue) {
			stateMessage.setMessage("账户信息已发送到您的邮箱！！");
			stateMessage.setState("200");
			return stateMessage;
		}else {
			throw new CustomizeException(CustomizeErrorCode.EMAIL_OR_ACCOUNTID_ERROR);
		}
		
	}
	
	
	//注册
	@PostMapping("/sign")
	public StateMessage doSign(@RequestBody String str){

		User user = JSON.parseObject(str, User.class);
		
		StateMessage stateMessage = new StateMessage();
		
		String accountId = user.getAccountId();
		if(!userService.checkAccountIdunion(accountId)) {
			throw new CustomizeException(CustomizeErrorCode.ACCOUNTID_NOT_UNION);
		}
		//创建token
		String token = UUID.randomUUID().toString();
		user.setToken(token);
		//使用Md5给密码加密
//		旧的
//		String password = Md5Utils.code(user.getPassword());
//		新的
		String password = Md5Utils2.convertMD5(user.getPassword());
		user.setPassword(password);
		//未激活账户插入数据库
		userMapper.insertSelective(user);
		//发送待激活邮件
		userService.register(user);
		
		stateMessage.setState("200");
		stateMessage.setMessage("用户创建成功，请去邮箱激活！！");
		return stateMessage;
	}
	
	
	//账户激活
	@PostMapping("/checkCode")
	public StateMessage checkCode(@RequestBody String str) throws CustomizeException {
		User tmpUser = JSON.parseObject(str, User.class);
		System.out.println(tmpUser.getToken());
		StateMessage stateMessage = new StateMessage();
		if( userService.checkCode(tmpUser.getToken())) {
			stateMessage.setMessage("账户激活成功");
			stateMessage.setState("200");
			return stateMessage;
		}else {
			throw new CustomizeException(CustomizeErrorCode.ACCOUNTID_NOT_USE);
		}
	}
	
	//借书归还提醒
	@PostMapping("/infoWithTime")
	public StateMessage sendInfo(@RequestBody String str, HttpServletRequest request) {
		
		System.out.println(str);
		User user = JSON.parseObject(str, User.class);
		
		userService.preHandle(request, user.getAccountId());
		
		User tmpUser = (User) request.getSession().getAttribute("user");
        if (tmpUser == null) {
        	throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        
		boolean isTrue = currentService.remainTimeInfo(user);
		
		StateMessage stateMessage = new StateMessage();
		if(isTrue) {
			stateMessage.setMessage("借书信息已发送到您的邮箱！！");
			stateMessage.setState("200");
			return stateMessage;
		}else {
			stateMessage.setMessage("借书信息没发送到您的邮箱！！");
			stateMessage.setState("500");
			return stateMessage;
		}
		
	}
}
