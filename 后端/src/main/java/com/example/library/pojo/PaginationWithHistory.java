package com.example.library.pojo;

import java.util.List;

import lombok.Data;

@Data
public class PaginationWithHistory {

	private Long totalPage;
	private List<HistoryWithBooks> list;
}
