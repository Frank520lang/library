package com.example.library.service;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.library.beans.UserExample;
import com.example.library.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduledTask {
	@Resource
	private UserMapper userMapper;

	@Scheduled(fixedRate = 60*60*24*1000, initialDelay = 1000)
	public void getCurrentTime() {
		
		UserExample userExample = new UserExample();
		userExample.createCriteria().andStateEqualTo(0);
		int deleteByExample = userMapper.deleteByExample(userExample);
		
		
		log.info(LocalDateTime.now() + "清理僵尸账户" + deleteByExample + "个");
	}
}
