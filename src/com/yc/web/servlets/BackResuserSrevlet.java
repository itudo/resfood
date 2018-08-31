package com.yc.web.servlets;

import static com.yc.utils.YcConstants.ERROR_404;
import static com.yc.utils.YcConstants.ERROR_500;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yc.bean.Resuser;
import com.yc.biz.ResuserBizImpl;


@WebServlet("/resadmin/resuser.action")
public class BackResuserSrevlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private ResuserBizImpl rubi = new ResuserBizImpl();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if("showUserContributionList".equals(op)){
				showUserContributionListOp(request,response);
			}else if("show".equals(op)){
				showOp(request,response);
			}else{
				response.sendRedirect(ERROR_404);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(ERROR_500);
			
		}
	}


	private void showOp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Resuser> list = rubi.findAll(page, rows, sort, order);
		int total = rubi.findCount();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total", total);
		map.put("rows",list);
		super.outJsonString(map, response);
	}


	private void showUserContributionListOp(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String,Object> map = rubi.findResuserContribution(page,rows,sort,order);
		super.outJsonString(map, response);
	}

}
