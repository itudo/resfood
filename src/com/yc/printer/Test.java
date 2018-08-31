package com.yc.printer;

import java.util.ArrayList;
import java.util.List;

import com.yc.bean.Resorderitem;

public class Test {
	public static void main(String[] args) {
		List<Resorderitem> goods=new ArrayList<Resorderitem>();
		goods.add(new Resorderitem(1,1,1));
		goods.add(new Resorderitem(2,2,2));
		goods.add(new Resorderitem(3,3,3));
		
		SalesTicket stk=new SalesTicket(goods,"源辰信息","201705230010","3","38400","38400","0");
		Printer p=new Printer(stk);
		p.printer();
	}
}