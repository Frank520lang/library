package com.example.library.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.example.library.beans.CurrBooksExample;
import com.example.library.beans.User;
import com.example.library.beans.UserExample;
import com.example.library.beans.UserExample.Criteria;
import com.example.library.exception.CustomizeErrorCode;
import com.example.library.exception.CustomizeException;
import com.example.library.mapper.CurrBooksMapper;
import com.example.library.mapper.UserMapper;
import com.example.library.pojo.PaginationWithUsers;
import com.example.library.pojo.StateMessage;
import com.example.library.pojo.UsersQueryWithPage;

@Service
public class AdminService {
	
	@Resource
	private UserMapper userMapper;
	
	@Resource
	private CurrBooksMapper currBooksMapper;

	public PaginationWithUsers getAllUser(UsersQueryWithPage usersWithPage, User user) {
		
		if(usersWithPage.getSize() == null || usersWithPage.getSize() < 0 ) {
			usersWithPage.setSize(5);
		}
		
		// TODO Auto-generated method stub
		if(usersWithPage.getPage() != null && usersWithPage.getPage() > 0) {
			int offset = usersWithPage.getPage() == 1 ? 0 : (usersWithPage.getSize()*(usersWithPage.getPage()-1));
			usersWithPage.setOffset(offset);
		}
		
		if(usersWithPage.getPage() == null || usersWithPage.getPage() < 0) {
			usersWithPage.setOffset(0);
		}
		
		
		
		UserExample userExample = new UserExample();
		Criteria createCriteria = userExample.createCriteria();
		
		createCriteria.andAccountIdNotEqualTo(user.getAccountId());
		if(usersWithPage.getLikeStr() != null && !"".equals(usersWithPage.getLikeStr())) {
			createCriteria.andNameLike("%" + usersWithPage.getLikeStr() + "%");
			userExample.or().andAccountIdLike("%" + usersWithPage.getLikeStr() + "%");
		}
		
		RowBounds rowBounds = new RowBounds(usersWithPage.getOffset(), usersWithPage.getSize());
		List<User> list = userMapper.selectByExampleWithRowbounds(userExample, rowBounds);
		
		long sum = userMapper.countByExample(userExample);
		
		PaginationWithUsers paginationWithUsers = new PaginationWithUsers();
		paginationWithUsers.setList(list);
		paginationWithUsers.setTotalPage(doTotalPage(sum));
		return paginationWithUsers;
		
	}


	public StateMessage deleteUser(String accountId) {
		// TODO Auto-generated method stub
		StateMessage stateMessage = new StateMessage();
		
		UserExample userExample = new UserExample();
		userExample.createCriteria().andAccountIdEqualTo(accountId);
		
		long countByExample = userMapper.countByExample(userExample);
		
		if(countByExample >= 1) {
			CurrBooksExample currBooksExample = new CurrBooksExample();
			currBooksExample.createCriteria().andAccountIdEqualTo(accountId);
			long countByCurr = currBooksMapper.countByExample(currBooksExample);
			if(countByCurr > 0) {
				throw new CustomizeException(CustomizeErrorCode.NOT_LOGOUT);
			}else {
				userMapper.deleteByExample(userExample);
				stateMessage.setMessage("注销用户成功！");
				stateMessage.setState("200");
			}
		}else {
			throw new CustomizeException(CustomizeErrorCode.NO_ACCOUNTID);
		}
		return stateMessage;
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

}
