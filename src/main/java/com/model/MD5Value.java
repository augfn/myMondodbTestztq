package com.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document(collection="MD5Site")
public class MD5Value implements Serializable{
	private static final long serialVersionUID = 5495387421414208887L;
	@Id
	@Field("id")
	private String id;//主键id
	@Field("mid")
	private String mid;//主表id
	@Field("MD5")
	private String MD5;//附件MD5值
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMD5() {
		return MD5;
	}
	public void setMD5(String mD5) {
		MD5 = mD5;
	}
	@Override
	public String toString() {
		return "MD5Value [id=" + id + ", mid=" + mid + ", MD5=" + MD5 + "]";
	}
	
	
}
