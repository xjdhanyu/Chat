package com.chat.model;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * @author HANYU Created on 2014-6-18.
	 */
	private static final long serialVersionUID = 8725899772448279687L;
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
