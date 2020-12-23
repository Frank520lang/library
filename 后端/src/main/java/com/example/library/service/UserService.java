package com.example.library.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.example.library.beans.User;
import com.example.library.beans.UserExample;
import com.example.library.mapper.UserMapper;
import com.example.library.utils.Md5Utils2;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;
    
    @Resource
    private MailService mailService;

    public User login(User user){
    	UserExample userExample = new UserExample();
    	userExample.createCriteria()
    			.andAccountIdEqualTo(user.getAccountId())
    			.andPasswordEqualTo(user.getPassword());

    	List<User> users = userMapper.selectByExample(userExample);
    	User tmpUser = null;
    	if(users.size() > 0 && users.get(0).getState() == 1) {
    		tmpUser = users.get(0);
    		return tmpUser;
    	}else {
    		return tmpUser;
    	}
    }
    
    
    //accountId验证
    public boolean checkAccountIdunion(String accountId) {
    	boolean isUnion = false;
    	
    	UserExample userExample = new UserExample();
    	userExample.createCriteria().andAccountIdEqualTo(accountId);
    	long count = userMapper.countByExample(userExample);
    	
    	if(count == 0) {
    		isUnion = true;
    		return isUnion;
    	}
    	
    	return isUnion;
    }
    
    public void register(User user) {
//        userDao.register(user);
        //获取激活码
        String code = user.getToken();
//        System.out.println("code:"+code);
        //主题
        String subject = "来自图书管理系统的激活邮件";
        //user/checkCode?code=code(激活码)是我们点击邮件链接之后根据激活码查询用户，如果存在说明一致，将用户状态修改为“1”激活
        //上面的激活码发送到用户注册邮箱
//        String context = "<a href=\"http://100.2.41.219:8081/user/checkCode?code="+code+"\">激活请点击:"+code+"</a>";
//        String context = "<a href=\"http://10.10.104.215:8081/user/checkCode?code="+code+"\">激活请点击:"+code+"</a>";
//        String context = "<a href=\"http://10.10.104.197:8081/user/checkCode?code="+code+"\">激活请点击:"+code+"</a>";
//        String context = "<a href=\"http://10.10.104.197:8080/user/checkCode?code="+code+"\">激活请点击:"+code+"</a>";
        
//        String context = "<a href=\"http://100.2.228.163:8080/user/checkCode?code="+code+"\">激活请点击:"+code+"</a>";
		String context = "<a href=\"http://100.2.218.210:8080/user/checkCode?code=" + code + "\">激活请点击:" + code
				+ "</a>";
        //发送激活邮件
        mailService.sendHtmlMail (user.getEmail(),subject,context);
    }
    
    //激活账户时的验证code
    public Boolean checkCode(String code) {
    	
    	boolean isTrue = false;
    	
    	UserExample userExample = new UserExample();
    	userExample.createCriteria()
    			.andTokenEqualTo(code);
    	
    	List<User> users = userMapper.selectByExample(userExample);
    	
    	if(users.size() > 0) {
    		User updateUser = new User();
    		updateUser.setId(users.get(0).getId());
    		updateUser.setState(1);
    		userMapper.updateByPrimaryKeySelective(updateUser);
    		isTrue = true;
    	}
    	
    	return isTrue;
    }
    
    public boolean preHandle(HttpServletRequest request, String accountId) {
    	UserExample userExample = new UserExample();
		userExample.createCriteria().andAccountIdEqualTo(accountId);
		
		List<User> users = userMapper.selectByExample(userExample);
		
		if(users.size() > 0) {
			request.getSession().setAttribute("user", users.get(0));
		}
    	return false;
    }


	public boolean forgetPassword(User tmpUser) {
		
		boolean isTrue = false;
		UserExample userExample = new UserExample();
		userExample.createCriteria()
				.andAccountIdEqualTo(tmpUser.getAccountId())
				.andEmailEqualTo(tmpUser.getEmail());
		List<User> list = userMapper.selectByExample(userExample);
		
		if(list.size() > 0) {
			String subject = "来自图书管理系统的密码提示邮件";
	        
			String context = "<p>您的账户是：" + list.get(0).getAccountId() + "</p>" + "<p>您的密码是：" + Md5Utils2.convertMD5((list.get(0).getPassword())) + "</p>";
	        //发送激活邮件
	        mailService.sendHtmlMail(list.get(0).getEmail(),subject,context);
	        
	        isTrue = true;
		}
		return isTrue;
	}

}
