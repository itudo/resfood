package com.yc.bean;

import java.io.Serializable;

public class Resorderitem implements Serializable {

	private static final long serialVersionUID = 2653390531118677876L;
	private Integer roiid;
	private Integer roid;
	private Integer fid;
	private double dealprice;
	private Integer num;
	private Double smallcount;
	private String fname;
	public String getFname() {
		return fname;
	}
	
	public Resorderitem() {
		super();
	}

	public Resorderitem(Integer roiid, Integer roid, Integer fid,
			double dealprice, Integer num, Double smallcount, String fname) {
		super();
		this.roiid = roiid;
		this.roid = roid;
		this.fid = fid;
		this.dealprice = dealprice;
		this.num = num;
		this.smallcount = smallcount;
		this.fname = fname;
	}



	public Resorderitem(Integer roiid, Integer roid, Integer fid) {
		super();
		this.roiid = roiid;
		this.roid = roid;
		this.fid = fid;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}
	public Double getSmallcount() {
		return smallcount;
	}
	public void setSmallcount(Double smallcount) {
		this.smallcount = smallcount;
	}
	public Integer getRoiid() {
		return roiid;
	}
	public void setRoiid(Integer roiid) {
		this.roiid = roiid;
	}
	public Integer getRoid() {
		return roid;
	}
	public void setRoid(Integer roid) {
		this.roid = roid;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public double getDealprice() {
		return dealprice;
	}
	public void setDealprice(double dealprice) {
		this.dealprice = dealprice;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "Resorderitem [roiid=" + roiid + ", roid=" + roid + ", fid="
				+ fid + ", dealprice=" + dealprice + ", num=" + num + "]";
	}
	
	
}
