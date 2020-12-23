package com.example.library.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.library.beans.Books;

import com.example.library.mapper.BooksMapper;
import com.example.library.mapper.CurrBooksMapper;
import com.example.library.pojo.BooksMoudle;
import com.example.library.pojo.BooksStatus;
import com.example.library.pojo.SelectBooks;

@Service
public class SelectBooksService {
	@Resource
	BooksMapper booksMapper;
	@Resource
	CurrBooksMapper currBooksMapper;

	public BooksMoudle selectBooks(SelectBooks selectBooks) {
		selectBooks.setStrIndex((selectBooks.getPageNumber() - 1) * 5);
		List<Books> list = new ArrayList<>();
		if (selectBooks.getClassification()!=null && selectBooks.getClassification().equals("")) {
			selectBooks.setClassification(null);
		}
		if (selectBooks.getCountry()!=null &&selectBooks.getCountry().equals("")) {
			selectBooks.setCountry(null);
		}
		if (selectBooks.getSpace()!=null && selectBooks.getSpace().equals("")) {
			selectBooks.setSpace(null);
		}
		if (selectBooks.getTheme()!=null && selectBooks.getTheme().equals("")) {
			selectBooks.setTheme(null);
		}
		list = booksMapper.selectByTypeAndBookname(selectBooks);
		BooksMoudle booksMoudle = new BooksMoudle();
		booksMoudle.setList(list);
		booksMoudle.setCount(booksMapper.selectCountByTypeAndBookname(selectBooks));
		return booksMoudle;
	}

	public BooksStatus selectStatus(SelectBooks selectBooks) {

		selectBooks.setStrIndex((selectBooks.getPageNumber() - 1) * 5);
		if (selectBooks.getClassification()!=null && selectBooks.getClassification().equals("")) {
			selectBooks.setClassification(null);
		}
		if (selectBooks.getCountry()!=null &&selectBooks.getCountry().equals("")) {
			selectBooks.setCountry(null);
		}
		if (selectBooks.getSpace()!=null && selectBooks.getSpace().equals("")) {
			selectBooks.setSpace(null);
		}
		if (selectBooks.getTheme()!=null && selectBooks.getTheme().equals("")) {
			selectBooks.setTheme(null);
		}
		List<Books> list = new ArrayList<>();
		list = booksMapper.selectByTypeAndBookname(selectBooks);
		List<SelectBooks> list2 = new ArrayList<SelectBooks>();
		for (int i = 0; i < list.size(); i++) {
			Books books = list.get(i);
			SelectBooks selectBooks2 = new SelectBooks();
			selectBooks2.setId(books.getId());
			selectBooks2.setAuthor(books.getAuthor());
			selectBooks2.setBookId(books.getBookId());
			selectBooks2.setBookName(books.getBookName());
			selectBooks2.setClassification(books.getClassification());
			selectBooks2.setCountry(books.getCountry());
			selectBooks2.setIntroduction(books.getIntroduction());
			selectBooks2.setNumber(books.getNumber());
			selectBooks2.setSpace(books.getSpace());
			selectBooks2.setStaDate(books.getStaDate());
			selectBooks2.setTheme(books.getTheme());
			if (currBooksMapper.selectCountByBookId(selectBooks2) != books.getNumber()) {
				selectBooks2.setStatus(1);
			}
			list2.add(selectBooks2);
		}
		BooksStatus booksStatus = new BooksStatus();
		booksStatus.setList(list2);
		booksStatus.setTotal(booksMapper.selectCountByTypeAndBookname(selectBooks));
		return booksStatus;
	}
}
