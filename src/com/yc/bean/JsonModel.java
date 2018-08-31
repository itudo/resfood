package com.yc.bean;

import java.io.Serializable;

public class JsonModel implements Serializable {

	private static final long serialVersionUID = 9106902882602433353L;
	private Integer code;
	private Object obj;
	private String errmsg;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	@Override
	public String toString() {
		return "JsonModel [code=" + code + ", obj=" + obj + ", errmsg="
				+ errmsg + "]";
	}
	
}
