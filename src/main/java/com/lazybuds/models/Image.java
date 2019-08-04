package com.lazybuds.models;

public class Image {
	
	private String imageUrl;
	private String text;
	
	public Image(String imageUrl, String text) {
		super();
		this.imageUrl = imageUrl;
		this.text = text;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Image [imageUrl=" + imageUrl + ", text=" + text + "]";
	}
	
}
