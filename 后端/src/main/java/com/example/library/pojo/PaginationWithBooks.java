package com.example.library.pojo;

import java.util.List;

import lombok.Data;

@Data
public class PaginationWithBooks {

	private Long totalPage;
	private List<AllBookWithState> list;
}
