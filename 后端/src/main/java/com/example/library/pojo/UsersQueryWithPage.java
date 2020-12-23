package com.example.library.pojo;

import lombok.Data;

@Data
public class UsersQueryWithPage {

	private String accountId;
	private Integer page;
	private Integer size;
	private Integer offset;
	private String likeStr;
}
