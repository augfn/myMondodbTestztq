package com.util;


import java.io.InputStream;

public class PostParam {

	private String name;
	private String contentType;
	private String value;
	private String fileName;
	private InputStream in;
	
	/**
	 * 
	 * @param name 字段名称
	 * @param contentType 字段类型
	 * @param value 字段值
	 */
	public PostParam(String name, String contentType, String value) {
		this.name = name;
		this.contentType = contentType;
		this.value = value;
	}
	
	/**
	 * 
	 * @param name 字段名称
	 * @param contentType 字段类型
	 * @param fileName 文件名称
	 * @param in 读入流
	 */
	public PostParam(String name, String contentType, String fileName,
			InputStream in) {
		this.name = name;
		this.contentType = contentType;
		this.fileName = fileName;
		this.in = in;
	}

	public String getName() {
		return name;
	}
	public String getContentType() {
		if(contentType == null){
			contentType = "application/octet-stream";
		}
		return contentType;
	}
	public String getValue() {
		return value;
	}
	public String getFileName() {
		return fileName;
	}
	public InputStream getIn() {
		return in;
	}
}
