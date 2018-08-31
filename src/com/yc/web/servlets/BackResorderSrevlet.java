package com.yc.web.servlets;

import static com.yc.utils.YcConstants.ERROR_404;
import static com.yc.utils.YcConstants.ERROR_500;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yc.bean.Resadmin;
import com.yc.bean.Resorder;
import com.yc.bean.Resorderitem;
import com.yc.biz.ResorderBizImpl;
import com.yc.printer.SalesTicket;
import com.yc.printer.Printer;


@WebServlet("/resadmin/resorder.action")
public class BackResorderSrevlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private ResorderBizImpl robi = new ResorderBizImpl();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if("showOrderList".equals(op)){
				showOrderListOp(request,response);
			}else if("showOrderitemList".equals(op)){
				showOrderitemListOp(request,response);
			}else if("printOrder".equals(op)){
				printOrderOp(request,response);
			}else if("transfer".equals(op)){
				transferOp(request,response);
			}else{
				response.sendRedirect(ERROR_404);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(ERROR_500);
			
		}
	}


	private void transferOp(HttpServletRequest request,HttpServletResponse response) throws IOException {
		try {
			Resorder resorder = (Resorder) super.parseParameterToT(request, Resorder.class);
			this.robi.transfer(resorder);
			jm.setCode(1);
			
		} catch (Exception e) {
			e.printStackTrace();
			jm.setCode(0);
			jm.setErrmsg(e.getMessage());
		}
		super.outJsonString(jm, response);
	}


	private void printOrderOp(HttpServletRequest request,
			HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, IOException {
		Resadmin resadmin = null;
		String admin = "admin";
		if(session.getAttribute("login_resadmin") !=null){
			resadmin = (Resadmin) session.getAttribute("login_resadmin");
			admin = resadmin.getRaname();
		}
		
		try {
			Resorder resorder = (Resorder) super.parseParameterToT(request, Resorder.class);
			List<Resorderitem> list = robi.findResorderItem(resorder);
			double count = 0;
			for(int i=0;i<list.size();i++){
				Resorderitem ritem = list.get(i);
				count += ritem.getDealprice()*ritem.getNum();
			}
			SalesTicket stk = new SalesTicket(list,admin,list.get(0).getRoid()+"",list.size()+"",count+"",count+"","0");
			Printer p = new Printer(stk);
			p.printer();
			jm.setCode(1);
		} catch (Exception e) {
			e.printStackTrace();
			jm.setCode(0);
			jm.setErrmsg(e.getMessage());
		}
		super.outJsonString(jm, response);
	}

	/**
	 * 显示订单详情列表数据
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void showOrderitemListOp(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map map = new HashMap();
		try {
			Resorder resorder = (Resorder) super.parseParameterToT(request, Resorder.class);
			
			List<Resorderitem> list = robi.findResorderItem(resorder);
			map.put("total", list.size());
			map.put("rows", list);
			if(session.getAttribute("listResorder")!=null){
				List<Resorder> listResorder =  (List<Resorder>) session.getAttribute("listResorder");
				for (Resorder ro : listResorder) {
					if(ro.getRoid() == resorder.getRoid()){
						map.put("msg", ro.getPs());
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.outJsonString(map, response);
	}

	/**
	 * 后台显示订单列表
	 * 1.按状态 2.下单时间显示
	 * @param request
	 * @param response
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private void showOrderListOp(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		try {
			Resorder resorder = (Resorder) super.parseParameterToT(request, Resorder.class);
			resorder.setOrderBy(this.sort);
			resorder.setOrder(this.order);;
			resorder.setPages(this.page);;
			resorder.setPageSize(this.rows);
			List<Resorder> list = robi.findResorder(resorder);
			int total = robi.findCount(null);
			map.put("total", total);
			map.put("rows", list);
			session.setAttribute("listResorder", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.outJsonString(map, response);
		
	}

}
