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

import com.yc.bean.Resfood;
import com.yc.biz.ResfoodBizImpl;


@WebServlet("/resadmin/resfood.action")
public class BackResfoodSrevlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	private ResfoodBizImpl rfbi = new ResfoodBizImpl();
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if("showFoodList".equals(op)){
				showFoodListOp(request,response);
			}else if("showFoofSellInfoList".equals(op)){
				showFoofSellInfoListOp(request,response);
			}else if("updateResfoodInfo".equals(op)){
				updateResfoodInfoOp(request,response);
			}else{
				response.sendRedirect(ERROR_404);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(ERROR_500);
			
		}
	}


	
	private void updateResfoodInfoOp(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		Resfood resfood = (Resfood) super.parseParameterToT(request, Resfood.class);
		
		rfbi.updateResfoodInfo(resfood);
		map.put("code", 1);	
		super.outJsonString(map, response);
	}

	
	
	private void showFoofSellInfoListOp(HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map map = new HashMap();
		try {
			Resfood resfood = (Resfood) super.parseParameterToT(request, Resfood.class);
			resfood.setOrderBy(this.sort);
			resfood.setOrder(this.order);
			resfood.setPages(this.page);
			resfood.setPageSize(this.rows);
			List<Resfood> listResfood = rfbi.findResfoodSellCountList(resfood);
			Long total = rfbi.findResfoodCount(resfood);
			map.put("total", total);
			map.put("rows", listResfood);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.outJsonString(map, response);
	}

	
	
	private void showFoodListOp(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		try {
			Resfood resfood = (Resfood) super.parseParameterToT(request, Resfood.class);
			resfood.setOrderBy(this.sort);
			resfood.setOrder(this.order);
			resfood.setPages(this.page);
			resfood.setPageSize(this.rows);
			List<Resfood> listResfood = rfbi.findResfood(resfood);
			Long total = rfbi.findResfoodCount(resfood);
			map.put("total", total);
			map.put("rows", listResfood);
			session.setAttribute("listResfood", listResfood);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.outJsonString(map, response);
		
	}

}
