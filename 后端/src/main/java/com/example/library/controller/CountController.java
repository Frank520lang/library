package com.example.library.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.service.CountService;

@RestController
public class CountController {

	@Resource
	private CountService countService;

	@GetMapping("/countbooksByTimes")
	public List<List<Integer>> currentBooks(@PathParam(value = "year") String year,
			@PathParam(value = "month") String month) {
		return countService.countBooks(year, month);
//		return countService.countBooks(year, month);
	}

}
