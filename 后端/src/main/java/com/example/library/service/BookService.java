package com.example.library.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.library.beans.Books;
import com.example.library.beans.BooksExample;
import com.example.library.exception.CustomizeErrorCode;
import com.example.library.exception.CustomizeException;
import com.example.library.mapper.BooksMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookService {
	@Resource
	BooksMapper booksMapper;
	
	public boolean insertBooks(Books books) {
//		List<Books> list = booksMapper.selectAllBooks(books);
//		for (int i = 0; i < array.length; i++) {
//			
//		}
		boolean isTrue = false;
		BooksExample booksExample = new BooksExample();
		booksExample.createCriteria().andBookIdEqualTo(books.getBookId());
		long sum = booksMapper.countByExample(booksExample);
		if(sum == 0) {
			isTrue = true;
			booksMapper.insert(books);
			return isTrue;
		}else {
			log.info("" + CustomizeErrorCode.BOOKID_NOT_UNION.getMessage());
			throw new CustomizeException(CustomizeErrorCode.BOOKID_NOT_UNION);
		}
	}
}
