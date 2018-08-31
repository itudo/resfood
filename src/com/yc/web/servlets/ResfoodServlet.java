package com.yc.web.servlets;

import static com.yc.utils.YcConstants.ERROR_404;
import static com.yc.utils.YcConstants.ERROR_500;
import static com.yc.utils.YcConstants.RESFOOD;
import static com.yc.utils.YcConstants.SESSIONCART;
import static com.yc.utils.YcConstants.SESSIONTOTAL;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yc.bean.CartItem;
import com.yc.bean.PageBean;
import com.yc.bean.Resfood;
/**
 * Servlet implementation class ResfoodServlet
 */
@WebServlet("/resfood.action")
public class ResfoodServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			if("details".equals(op)){
				detailsOp(req,resp);
			}else if("addCart".equals(op)){
				addCartOp(req,resp);
			}else if("clearCart".equals(op)){
				clearCartOp(req,resp);
			}else if("changeCount".equals(op)){
				changeCountOp(req,resp);
			}else if("toCart".equals(op)){
				toCartOp(req,resp);
			}else{
				resp.sendRedirect(ERROR_404);
			}
		} catch (Exception e) {
			resp.sendRedirect(ERROR_500);
			e.printStackTrace();
		}
	}

	private void toCartOp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/shopCart.jsp").forward(req, resp);
	}

	/**
	 * 修改订购数
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void changeCountOp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		int fid = Integer.parseInt(req.getParameter("fid"));
		int num = Integer.parseInt(req.getParameter("num"));
		
		Map<Integer,CartItem> cart = (Map<Integer, CartItem>) session.getAttribute(SESSIONCART);
		CartItem ci = cart.get(fid);
		ci.setCount(ci.getCount() + num);
		if(ci.getCount() <= 0){
			cart.remove(fid);
		}else{
			cart.put(fid, ci);
		}
		ci.getSmallCount();
		session.setAttribute(SESSIONCART, cart);
		double total = countTotal(cart);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("cartItem", ci);
		jm.setCode(1);
		jm.setObj(map);
		super.outJsonString(jm, resp);
	}

	/**
	 * 清空购物车
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void clearCartOp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		session.removeAttribute(SESSIONCART);
		session.removeAttribute(SESSIONTOTAL);
		req.getRequestDispatcher("/WEB-INF/pages/shopCart.jsp").forward(req, resp);
	}

	/**
	 * 添加到购物车
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void addCartOp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//取出商品
		int fid = Integer.parseInt(req.getParameter("fid"));
		Resfood resfood = getResfoodFromSession(fid);
		
		Map<Integer,CartItem> cart = new HashMap<Integer, CartItem>();
		if(session.getAttribute(SESSIONCART) != null){
			cart = (Map<Integer, CartItem>) session.getAttribute(SESSIONCART);
		}
		
		CartItem ci = null;
		if(!cart.containsKey(fid)){
			ci = new CartItem();
			ci.setResfood(resfood);
			ci.setCount(1);
		}else{
			ci = cart.get(fid);
			ci.setCount(ci.getCount() + 1);
		}
		cart.put(fid, ci);
		
		session.setAttribute(SESSIONCART, cart);
		double total = countTotal(cart);
		session.setAttribute(SESSIONTOTAL, total);
		req.getRequestDispatcher("/WEB-INF/pages/shopCart.jsp").forward(req, resp);
	}

	/**
	 * 计算总价
	 * @param cart
	 * @return
	 */
	private double countTotal(Map<Integer, CartItem> cart) {
		double total = 0.0;
		for (Map.Entry<Integer, CartItem> entry : cart.entrySet()) {
			total += entry.getValue().getSmallCount();
		}
		return total;
	}

	/**
	 * 商品详情
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void detailsOp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int fid = Integer.parseInt(req.getParameter("fid"));
		Resfood resfood = getResfoodFromSession(fid);
		session.setAttribute(RESFOOD, resfood);
		req.getRequestDispatcher("/WEB-INF/pages/details.jsp").forward(req, resp);
	}

	/**
	 * 根据fid到session的pageBean中查这个商品
	 * @param fid
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Resfood getResfoodFromSession(int fid){
		PageBean pageBean = (PageBean) session.getAttribute("pageBean");
		List<Resfood> list = pageBean.getList();
		for(Resfood rf : list){
			if( rf.getFid() == fid){
				return rf;
			}
		}
		return null;
	}
	
	
}
