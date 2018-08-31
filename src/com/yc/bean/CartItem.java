package com.yc.bean;

import java.io.Serializable;

public class CartItem implements Serializable {

	private static final long serialVersionUID = 4504467454602268472L;
	private Resfood resfood;
	private Integer count;
	private Double smallCount;
	
	public Double getSmallCount(){
		smallCount = resfood.getRealprice() * count;
		return smallCount;
	}

	public Resfood getResfood() {
		return resfood;
	}

	public void setResfood(Resfood resfood) {
		this.resfood = resfood;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}
