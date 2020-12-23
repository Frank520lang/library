package com.example.library.pojo;

import lombok.Data;

@Data
public class AllBookWithState {

	private String bookId;
	private String bookName;
	private String country;
	private String space;
	private String theme;
	private String classification;
	private String introduction;
	private String author;
	private Integer number;
	//这里的staDate是上架时间。
	private Long staDate;
	//这个是指代当前这本书的状态
	private String state;
}
