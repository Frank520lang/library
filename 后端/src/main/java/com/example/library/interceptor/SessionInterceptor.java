package com.example.library.interceptor;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.library.beans.User;
import com.example.library.beans.UserExample;
import com.example.library.mapper.UserMapper;

/**
 * Created by codedrinker on 2019/5/16.
 */
//@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

//    @PostMapping("/*")
//    public boolean preHandle(@RequestParam("accountId") String accountId) {
//    	System.out.println(accountId);
//    	
//    	return false;
//    }
    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0) {
        	for(Cookie cookie : cookies) {
        		if(cookie.getName().equals("token"));
        		String token = cookie.getValue();
        		UserExample userExample = new UserExample();
        		userExample.createCriteria().andTokenEqualTo(token);
        		
        		List<User> users = userMapper.selectByExample(userExample);
        		
        		if(users.size() > 0) {
        			request.getSession().setAttribute("user", users.get(0));
        		}
        	}
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
