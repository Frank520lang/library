package com.example.library.pojo;
import java.util.List;

public class BooksStatus {
	private List<SelectBooks> list;
	private int total;
	public List<SelectBooks> getList() {
		return list;
	}
	public void setList(List<SelectBooks> list) {
		this.list = list;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
}
