package com.yc.web.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.yc.bean.Resfood;
import com.yc.biz.ResfoodBizImpl;
import com.yc.utils.FileUpload;

@WebServlet("/resadmin/resfood/saveResfood.action")
public class SaveResfoodServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			JspFactory fac = JspFactory.getDefaultFactory();
			PageContext pageContext = fac.getPageContext(this, request, response, null, false, JspWriter.DEFAULT_BUFFER, true);
			FileUpload fu = new FileUpload();
			Map<String,String> map = fu.uploadFiles(pageContext, request);
			String fname = map.get("fname");
			String detail = map.get("detail");
			double normprice = Double.parseDouble(map.get("normprice"));
			double realprice = Double.parseDouble(map.get("realprice"));
			String fphoto = map.get("weburl_fphoto");
			
			Resfood rf = new Resfood(fname,normprice,realprice,detail,fphoto);
			
			ResfoodBizImpl resfoodBiz = new ResfoodBizImpl();
			resfoodBiz.insertResfood(rf);
			
			request.getRequestDispatcher("show.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
