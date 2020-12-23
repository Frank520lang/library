package com.example.library.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.beans.Books;
import com.example.library.beans.CurrBooks;
import com.example.library.mapper.BooksMapper;
import com.example.library.mapper.CurrBooksMapper;
import com.example.library.pojo.BooksMoudle;
import com.example.library.pojo.BooksStatus;
import com.example.library.pojo.SelectBooks;
import com.example.library.pojo.StateMessage;
import com.example.library.service.BookService;
import com.example.library.service.SelectBooksService;

@RestController
public class BooksController {
	@Resource
	private BooksMapper booksmapper;
	@Resource
	private SelectBooksService selectBooksService;
	@Resource
	private CurrBooksMapper currBooksMapper;
	@Resource
	private BookService bookService;

	@PostMapping("/insertbooks")
	public StateMessage insertBooks(@RequestBody Books books){
		StateMessage stateMessage = new StateMessage();
		if (bookService.insertBooks(books)) {
			stateMessage.setState("200");
			stateMessage.setMessage("插入成功");
		}
		return stateMessage;
	}

	@PostMapping("/updatebooks")
	public void updateBooks(@RequestBody Books books) {
		booksmapper.updateByBookIdSelective(books);
	}

	@PostMapping("/deletebooks")
	public void deleteBooks(@RequestBody Books books) {
		booksmapper.deleteByBookId(books.getBookId());
	}

	@PostMapping("/selectbooks")
	public Books selectBooks(@RequestBody Books books) {
		return booksmapper.selectByBookId(books.getBookId());
	}


	@PostMapping("/selectbooks/type/bookname")
	public BooksMoudle selectBooksByTypeAndBookname(@RequestBody SelectBooks selectBooks) {
		return selectBooksService.selectBooks(selectBooks);
	}

	@PostMapping("/selectbooks/status")
	public BooksStatus selectBooksStatus(@RequestBody SelectBooks selectBooks) {
		return selectBooksService.selectStatus(selectBooks);
	}

	@PostMapping("/selectbooks/curr")
	public List<CurrBooks> selectBooksCurr(@RequestBody SelectBooks selectBooks) {
		return currBooksMapper.selectByBookId(selectBooks);
	}
}
