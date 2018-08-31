package com.yc.web.servlets;

import static com.yc.utils.YcConstants.ERROR_404;
import static com.yc.utils.YcConstants.ERROR_500;
import static com.yc.utils.YcConstants.LOGINADMIN;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yc.bean.Resadmin;
import com.yc.biz.ResAdminBizImpl;


@WebServlet("/backReAdmin.action")
public class BackReAdmin extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private ResAdminBizImpl rabi = new ResAdminBizImpl();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if("login".equals(op)){
				loginOp(request,response);
			}else if("logout".equals(op)){
				logoutOp(request,response);
			} else{
				response.sendRedirect(ERROR_404);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(ERROR_500);
		}
	}

	private void logoutOp(HttpServletRequest request,
			HttpServletResponse response) {
		
		
	}

	private void loginOp(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Resadmin reaAdmin = (Resadmin) super.parseParameterToT(request, Resadmin.class);
		
		String rand = (String) session.getAttribute("rand");
		String valcode = request.getParameter("valcode");
		if(!valcode.equals(rand)){
			request.setAttribute("msg", "valide code failed");
			request.getRequestDispatcher("backLogin.jsp").forward(request, response);
		}else{
			reaAdmin = rabi.login(reaAdmin);
			if(reaAdmin != null){
				session.setAttribute(LOGINADMIN, reaAdmin);
				response.sendRedirect("resadmin/main.jsp");
			}else{
				request.setAttribute("msg", "raname or password is wrong ,login failed");
				request.getRequestDispatcher("backLogin.jsp").forward(request, response);
			}
		}
	}
}
