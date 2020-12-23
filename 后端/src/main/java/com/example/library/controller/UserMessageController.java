package com.example.library.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.example.library.beans.User;
import com.example.library.beans.UserExample;
import com.example.library.exception.CustomizeErrorCode;
import com.example.library.exception.CustomizeException;
import com.example.library.mapper.UserMapper;
import com.example.library.pojo.StateMessage;
import com.example.library.service.UserService;
import com.example.library.utils.Md5Utils2;

@RestController
@RequestMapping("/user")
public class UserMessageController {

	@Resource
	private UserMapper userMapper;

	@Resource
	private UserService userService;

	@PostMapping("/editMessage")
	public StateMessage updateUserMessage(@RequestBody String str, HttpServletRequest request) throws CustomizeException {

		User user = JSON.parseObject(str, User.class);
//		System.out.println(str);
//		user.setAccountId(null);
		user.setPassword(null);
		user.setId(null);
		user.setRight(null);
		user.setState(null);
		user.setToken(null);
		
		userService.preHandle(request, user.getAccountId());

		User tmpUser = (User) request.getSession().getAttribute("user");
		if (tmpUser == null) {
			throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
		}

		StateMessage stateMessage = new StateMessage();

//		if (user.getPassword() != null && !"".equals(user.getPassword()))
//			user.setPassword(Md5Utils2.convertMD5(user.getPassword()));

		UserExample userExample = new UserExample();
		userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());

		userMapper.updateByExampleSelective(user, userExample);

		stateMessage.setState("200");
		stateMessage.setMessage("用户信息更新成功！！");
		return stateMessage;
	}

	// 这个方法已搁置
	@PostMapping("/editPassword")
	public StateMessage updatePassword(@RequestBody String str, HttpServletRequest request) {
		User user = JSON.parseObject(str, User.class);
//		user.setPassword(Md5Utils.code(user.getPassword()));
		userService.preHandle(request, user.getAccountId());

		User tmpUser = (User) request.getSession().getAttribute("user");
		if (tmpUser == null) {
			throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
		}
		
		user.setPassword(Md5Utils2.convertMD5(user.getPassword()));

		user.setBirthday(null);
		StateMessage stateMessage = new StateMessage();
		
		UserExample userExample = new UserExample();
		userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());

		userMapper.updateByExampleSelective(user, userExample);

		stateMessage.setState("200");

		stateMessage.setMessage("密码更新成功！！");
		return stateMessage;
	}

	@PostMapping("/message")
	public User getUserMessage(@RequestBody String str, HttpServletRequest request) {

		User tmpUser = JSON.parseObject(str, User.class);

		userService.preHandle(request, tmpUser.getAccountId());

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
		}

		UserExample userExample = new UserExample();
		userExample.createCriteria().andAccountIdEqualTo(tmpUser.getAccountId());
		List<User> list = userMapper.selectByExample(userExample);
		if (list.size() <= 0) {
			throw new CustomizeException(CustomizeErrorCode.SYS_ERROR);
		}
//		list.get(0).setPassword(Md5Utils2.convertMD5(list.get(0).getPassword()));
		return list.get(0);
	}
}
