package com.yc.web.servlets;

import static com.yc.utils.YcConstants.LOGINUSER;
import static com.yc.utils.YcConstants.SESSIONCART;
import static com.yc.utils.YcConstants.SESSIONTOTAL;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yc.bean.CartItem;
import com.yc.bean.PageBean;
import com.yc.bean.Resorder;
import com.yc.bean.Resorderitem;
import com.yc.bean.Resuser;
import com.yc.biz.ResorderBizImpl;



@WebServlet("/resuser/resorder.action")
public class ResorderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private ResorderBizImpl resorderBiz = new ResorderBizImpl();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if ("toFillForm".equals(op)) {
				toFillFormOp(request, response);
			}else if("makeOrder".equals(op)){
				makeOrderOp( request, response);
			}else if("showOrders".equals(op)){
				showOrdersOp( request, response);
			}else if( "showOrderDetail".equals(op)){
				showOrderDetailOp( request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showOrderDetailOp(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String roid = request.getParameter("roid");
		Resorder resorder = new Resorder();
		resorder.setRoid(Integer.parseInt(roid));
		
		List<Resorderitem> resorderitem = resorderBiz.findResorderItem(resorder);
		jm.setCode(1);
		jm.setObj(resorderitem);
		super.outJsonString(jm, response);
	}

	@SuppressWarnings("rawtypes")
	private void showOrdersOp(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		Resuser resuser = (Resuser) session.getAttribute(LOGINUSER);
		Resorder resorder = new Resorder();
		resorder.setUserid(resuser.getUserid());
		resorder.setOrderBy(" ordertime ");
		resorder.setOrder(" desc ");
		int pages = 1 ;
		int pageSize = 5;
		/*
		if(resorder != null && resorder.getPages()!=null){
			pages = Integer.parseInt(request.getParameter("pages"));
		}
		if(resorder != null && resorder.getPageSize() != null){
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}*/
		resorder.setPages(pages);
		resorder.setPageSize(pageSize);
		PageBean pageBean = resorderBiz.find(resorder);
		request.setAttribute("resorderPageBean", pageBean);
		request.getRequestDispatcher("/WEB-INF/pages/showOrders.jsp").forward(request, response);
	}

	private void makeOrderOp(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<Integer,CartItem> shopCart = (Map<Integer, CartItem>) session.getAttribute(SESSIONCART);
		
		Resuser resuser = (Resuser) session.getAttribute(LOGINUSER);
		Resorder resorder = (Resorder) super.parseParameterToT(request, Resorder.class);
		
		try {
			resorderBiz.makeOrder(resorder, shopCart, resuser);
			session.removeAttribute(SESSIONCART);
			session.setAttribute(SESSIONTOTAL, 0.0);
			request.getRequestDispatcher("/WEB-INF/pages/seeYou.jsp").forward(request,response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("msg", "make order failed,please contact the administrator...");
			request.getRequestDispatcher("/WEB-INF/pages/checkOut.jsp").forward(request, response);
		}
	}

	private void toFillFormOp(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if(session.getAttribute(SESSIONCART) == null || ((Map)session.getAttribute(SESSIONCART)).size() <= 0){
			request.setAttribute("msg", "cart should not be empty");
			request.getRequestDispatcher("/WEB-INF/pages/shopCart.jsp").forward(request, response);
		}else{
			request.getRequestDispatcher("/WEB-INF/pages/checkOut.jsp").forward(request, response);
		}
	}

}
