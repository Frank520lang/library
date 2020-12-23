package com.example.library.pojo;

public class AnnounceWithPage {
	private Long id;

	private String connent;

	private Long staDate;

	private String accountId;

	private int pageNumber;

	private int staIndex;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConnent() {
		return connent;
	}

	public void setConnent(String connent) {
		this.connent = connent;
	}

	public Long getStaDate() {
		return staDate;
	}

	public void setStaDate(Long staDate) {
		this.staDate = staDate;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getStaIndex() {
		return staIndex;
	}

	public void setStaIndex(int staIndex) {
		this.staIndex = staIndex;
	}

}
