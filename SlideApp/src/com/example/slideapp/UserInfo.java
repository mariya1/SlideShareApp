package com.example.slideapp;

public class UserInfo {
	private String author = "";
	private String title = "";
	private String pubDate = "";
	private String guid = "";

	public UserInfo(String author, String title, String pubDate, String guid) {
		this.author = author;
		this.title = title;
		this.pubDate = pubDate;
		this.guid = guid;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public String getPubDate() {
		return pubDate;
	}

	public String getGuid() {
		return guid;
	}

}
