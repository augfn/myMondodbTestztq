package com.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="flow")
public class Flow implements Serializable{
	private static final long serialVersionUID = -5398521680650192043L;
	@Id
	@Field("runid")
	private String runid;
	@Field("type")
	private String type;//文件名称
	@Field("wjbt")
	private String wjbt;//文件标题
	@Field("wjlx")
	private String wjlx;//文件类型
	@Field("ztc")
	private String ztc;//主题词
	@Field("fjid")
	private String fjid;//附件ID
	@Field("fjname")
	private String fjname;//附件名称
	@Field("gwid")
	private String gwid;//正文ID
	@Field("cs")
	private String cs;//抄送
	@Field("swdwid")
	private String swdwid;//收文单位
	@Field("swdwname")
	private String swdwname;//收文单位名称
	@Field("wjbh")
	private String wjbh;//文件编号
	@Field("mj")
	private String mj;//文件密级
	@Field("fs")
	private String fs;//文件份数
	@Field("hj")
	private String hj;//紧急程度
	@Field("bz")
	private String bz;//备注
	@Field("lwdw")
	private String lwdw;//来文单位
	@Field("lwbh")
	private String lwbh;//来文编号
	@Field("blbm")
	private String blbm;//办理部门
	@Field("blr")
	private String blr;//办理人
	@Field("uservoer")
	private String uservoer;// 处理过程
	@Field("cjsj")
	private String cjsj;//创建时间
	public String getRunid() {
		return runid;
	}
	public void setRunid(String runid) {
		this.runid = runid;
	}
	public String getWjbt() {
		return wjbt;
	}
	public void setWjbt(String wjbt) {
		this.wjbt = wjbt;
	}
	public String getWjlx() {
		return wjlx;
	}
	public void setWjlx(String wjlx) {
		this.wjlx = wjlx;
	}
	public String getZtc() {
		return ztc;
	}
	public void setZtc(String ztc) {
		this.ztc = ztc;
	}
	public String getFjid() {
		return fjid;
	}
	public void setFjid(String fjid) {
		this.fjid = fjid;
	}
	public String getFjname() {
		return fjname;
	}
	public void setFjname(String fjname) {
		this.fjname = fjname;
	}
	public String getGwid() {
		return gwid;
	}
	public void setGwid(String gwid) {
		this.gwid = gwid;
	}
	public String getCs() {
		return cs;
	}
	public void setCs(String cs) {
		this.cs = cs;
	}
	public String getSwdwid() {
		return swdwid;
	}
	public void setSwdwid(String swdwid) {
		this.swdwid = swdwid;
	}
	public String getSwdwname() {
		return swdwname;
	}
	public void setSwdwname(String swdwname) {
		this.swdwname = swdwname;
	}
	public String getWjbh() {
		return wjbh;
	}
	public void setWjbh(String wjbh) {
		this.wjbh = wjbh;
	}
	public String getMj() {
		return mj;
	}
	public void setMj(String mj) {
		this.mj = mj;
	}
	public String getFs() {
		return fs;
	}
	public void setFs(String fs) {
		this.fs = fs;
	}
	public String getHj() {
		return hj;
	}
	public void setHj(String hj) {
		this.hj = hj;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getLwdw() {
		return lwdw;
	}
	public void setLwdw(String lwdw) {
		this.lwdw = lwdw;
	}
	public String getLwbh() {
		return lwbh;
	}
	public void setLwbh(String lwbh) {
		this.lwbh = lwbh;
	}
	public String getBlbm() {
		return blbm;
	}
	public void setBlbm(String blbm) {
		this.blbm = blbm;
	}
	public String getBlr() {
		return blr;
	}
	public void setBlr(String blr) {
		this.blr = blr;
	}
	public String getUservoer() {
		return uservoer;
	}
	public void setUservoer(String uservoer) {
		this.uservoer = uservoer;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCjsj() {
		return cjsj;
	}
	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}
	@Override
	public String toString() {
		return "Flow [runid=" + runid + ", type=" + type + ", wjbt=" + wjbt
				+ ", wjlx=" + wjlx + ", ztc=" + ztc + ", fjid=" + fjid
				+ ", fjname=" + fjname + ", gwid=" + gwid + ", cs=" + cs
				+ ", swdwid=" + swdwid + ", swdwname=" + swdwname + ", wjbh="
				+ wjbh + ", mj=" + mj + ", fs=" + fs + ", hj=" + hj + ", bz="
				+ bz + ", lwdw=" + lwdw + ", lwbh=" + lwbh + ", blbm=" + blbm
				+ ", blr=" + blr + ", uservoer=" + uservoer + ", cjsj=" + cjsj
				+ "]";
	}
	 
	
	
}
