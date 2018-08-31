package com.yc.web.servlets;

import static com.yc.utils.YcConstants.ERROR_404;

import static com.yc.utils.YcConstants.ERROR_500;
import static com.yc.utils.YcConstants.LOGINPAGE;
import static com.yc.utils.YcConstants.LOGINUSER;
import static com.yc.utils.YcConstants.REGPAGE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yc.bean.Resuser;
import com.yc.biz.ResuserBizImpl;


@WebServlet( urlPatterns={"/resuser.action","/resuser/resuser.action"})
public class ResuserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private ResuserBizImpl rubi = new ResuserBizImpl();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if("login".equals(op)){
				loginOp(request,response);
			}else if("logout".equals(op)){
				logoutOp(request,response);
			}else if("isResuserLogin".equals(op)){
				isResuserLoginOp(request,response);
			}else if("toLogin".equals(op)) {
				toLoginOp( request,response);
			} else if("toReg".equals(op)) {
				toRegOp( request,response);
			} else{
				response.sendRedirect(ERROR_404);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(ERROR_500);
		}
	}
	
	private void toRegOp(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(REGPAGE).forward(request, response);
		
	}

	private void toLoginOp(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(LOGINPAGE).forward(request, response);
		
	}

	private void isResuserLoginOp(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Resuser user = (Resuser) session.getAttribute(LOGINUSER);
		if(user == null){
			jm.setCode(0);
		}else{
			user.setPwd("");
			jm.setCode(1);
			jm.setObj(user);
		}
		super.outJsonString(jm, response);
	}
	/**
	 * 用户退出
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void logoutOp(HttpServletRequest request,HttpServletResponse response) throws IOException {
		session.removeAttribute(LOGINUSER);
		response.sendRedirect("../index.jsp");
	}

	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void loginOp(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Resuser resuser = (Resuser) super.parseParameterToT(request,Resuser.class);
		String rand = (String) session.getAttribute("rand");
		String valcode = request.getParameter("valcode");
		if(!valcode.equals(rand)){
			request.setAttribute("msg", "valide code failed");
			request.getRequestDispatcher(LOGINPAGE).forward(request, response);
		}else{
			resuser = rubi.login(resuser);
			if(resuser != null){
				session.setAttribute(LOGINUSER, resuser);
				request.getRequestDispatcher("/index.jsp").forward(request, response);
			}else{
				request.setAttribute("msg", "username or password is wrong ,login failed");
				request.getRequestDispatcher(LOGINPAGE).forward(request, response);
			}
		}
	}

}
