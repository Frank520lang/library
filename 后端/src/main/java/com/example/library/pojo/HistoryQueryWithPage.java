package com.example.library.pojo;

import lombok.Data;

@Data
public class HistoryQueryWithPage {

	private String accountId;
	private Integer page;
	private Integer size;
	private Integer offset;
	private String likeStr;
	private String country;
	private String space;
	private String theme;
	private String classification;
}
