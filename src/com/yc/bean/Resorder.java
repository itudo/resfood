package com.yc.bean;

import java.io.Serializable;

public class Resorder extends CommonBean implements Serializable {

	private static final long serialVersionUID = -3848803853409661148L;
	private Integer roid;
	private Integer userid;
	private String  address;
	private String  tel;
	private String  ordertime;
	private String deliverytime;
	private String ps;
	private Integer status;
	
	public Integer getRoid() {
		return roid;
	}
	public void setRoid(Integer roid) {
		this.roid = roid;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public String getDeliverytime() {
		return deliverytime;
	}
	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}
	public String getPs() {
		return ps;
	}
	public void setPs(String ps) {
		this.ps = ps;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getStatusStr(){
		if(  status==1){
			return "已处理";
		}else if( status==0){
			return "未处理";
		}
		return "";
	}
	@Override
	public String toString() {
		return "Resorder [roid=" + roid + ", userid=" + userid + ", address="
				+ address + ", tel=" + tel + ", ordertime=" + ordertime
				+ ", deliverytime=" + deliverytime + ", ps=" + ps + ", status="
				+ status + "]";
	}
	
	

}
