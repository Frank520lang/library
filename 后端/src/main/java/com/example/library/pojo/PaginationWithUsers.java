package com.example.library.pojo;

import java.util.List;

import com.example.library.beans.User;

import lombok.Data;

@Data
public class PaginationWithUsers {

	private Long totalPage;
	private List<User> list;
}
