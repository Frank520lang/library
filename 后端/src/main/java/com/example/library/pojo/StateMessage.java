package com.example.library.pojo;

import lombok.Data;

@Data
public class StateMessage {

	private String state;
	private String message;
	private String token;
	private Integer right;
	
	
	
	public StateMessage(String state, String message) {
		this.state = state;
		this.message = message;
	}



	public StateMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
}
