package com.example.library.pojo;

import lombok.Data;

@Data
public class CurrentWithBooks {

	private String bookId;
	private String bookName;
	private String country;
	private String space;
	private String theme;
	private String classification;
	private String introduction;
	private String author;
	private Integer number;
	private Long staDate;
	private String accountId;
	private Integer remainDate;
}
