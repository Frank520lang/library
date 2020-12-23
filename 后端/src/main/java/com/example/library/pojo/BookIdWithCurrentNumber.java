package com.example.library.pojo;

import lombok.Data;

@Data
public class BookIdWithCurrentNumber {

	private String bookId;
	private Integer number;
	private Integer currNumber;
}
