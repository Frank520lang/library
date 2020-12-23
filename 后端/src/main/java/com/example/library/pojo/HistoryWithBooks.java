package com.example.library.pojo;

import lombok.Data;

@Data
public class HistoryWithBooks {

	//用来接收his_books和books内连接查询的结果。
	private String bookId;
	private String bookName;
	private String accountId;
	private String name;
	private Long staDate;
	private Long endDate;
	private Integer remainDate;
	
}
