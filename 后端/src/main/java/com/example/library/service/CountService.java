package com.example.library.service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.library.mapper.HisbooksMapper;

@Service
public class CountService {
	@Resource
	private HisbooksMapper hisbooksMapper;

	public List<List<Integer>> countBooks(String year, String month) {
		List<List<Integer>> list = new ArrayList<>();
		List<Integer> borrow = new ArrayList<>();
		List<Integer> back = new ArrayList<>();
		
		String dateStr = year + "-" + month + "-1";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long seconds = date.getTime();
//		System.out.println(date.getMonth());
		for (; date.getMonth()+1 == Integer.parseInt(month);) {
			borrow.add(hisbooksMapper.countBorrowByDate(seconds,seconds+60*60*24*1000l));
			back.add(hisbooksMapper.countReturnByDate(seconds,seconds+60*60*24*1000l));
			seconds += 60 * 60 * 24*1000l;
			date.setTime(seconds);
		}
//		Calendar cal = new GregorianCalendar(2020, 10, 1);
//		cal.get
		
//		Date date = new Date(year, month, day);
//		long seconds = cal.getTimeInMillis();
//		for (; cal.get(Calendar.MONTH + 1) == month;) {
//			borrow.add(hisbooksMapper.countBorrowByDate(seconds,seconds+60*60*24*1000));
//			back.add(hisbooksMapper.countReturnByDate(seconds,seconds+60*60*24*1000));
//			seconds += 60 * 60 * 24*1000;
//			cal.setTimeInMillis(seconds);
//		}
		list.add(borrow);
		list.add(back);
		return list;
	}
}
