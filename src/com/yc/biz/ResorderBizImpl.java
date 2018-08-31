package com.yc.biz;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yc.bean.CartItem;
import com.yc.bean.PageBean;
import com.yc.bean.Resfood;
import com.yc.bean.Resorder;
import com.yc.bean.Resorderitem;
import com.yc.bean.Resuser;
import com.yc.dao.DBUtil;

public class ResorderBizImpl {
	@SuppressWarnings("unused")
	public void makeOrder(Resorder resorder,Map<Integer,CartItem> cart,Resuser resuser) throws SQLException{
		String sql = "insert into resorder(userid,address,tel,ordertime,deliverytime,ps,status) "
				+ "values(?,?,?,now(),date_add(now(), interval 1 hour),?,0)";
		Connection con = DBUtil.getConn();
		try {
			con.setAutoCommit(false);
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, resuser.getUserid() + "");
			pst.setString(2, resorder.getAddress());
			pst.setString(3, resorder.getTel());
			pst.setString(4, resorder.getPs());
			pst.executeUpdate();
			sql = "select max(roid) as roid from resorder";
			pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			int roid = 0;
			if(rs.next()){
				roid = rs.getInt(1);
			}
			for (Map.Entry<Integer, CartItem> entry : cart.entrySet()) {
				int fid = entry.getKey();
				CartItem ci = entry.getValue();
				Resfood resfood = ci.getResfood();
				int count = ci.getCount();
				Double smallCount = ci.getSmallCount();
				sql = "insert into resorderitem(roid,fid,dealprice,num) values(?,?,?,?)";
				pst = con.prepareStatement(sql);
				pst.setString(1, roid + "");
				pst.setString(2, fid + "");
				pst.setString(3, resfood.getRealprice() + "");
				pst.setString(4, count + "");
				pst.executeUpdate();
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			con.rollback();
			throw e;
		}finally{
			if(con != null){
				con.setAutoCommit(true);
				con.close();
			}
		}
		
		
	}
	
	public List<Resorderitem> findResorderItem(Resorder resorder){
		String sql = "select roiid,roid,resorderitem.fid,fname,dealprice,num,dealprice*num as smallcount"
				+ " from resorderitem"
				+ " left join resfood on resorderitem.fid = resfood.fid"
				+ "  where roid = " + resorder.getRoid();
		List<Resorderitem> list = DBUtil.list(Resorderitem.class, sql);
		return list;
	}
	
	public List<Resorder> findResorder(Resorder resorder){
		String sql = "select roid,userid,address,tel,date_format(ordertime,'%Y-%c-%d %H:%i:%s')as ordertime,date_format(deliverytime,'%Y-%c-%d %H:%i:%s')as deliverytime ,ps,status from resorder where 1=1";
		List<Object> params = new ArrayList<Object>();
		if(resorder != null){
			if(resorder.getUserid() != null && !"".equals(resorder.getUserid())){
				sql += " and userid = ?";
				params.add(resorder.getUserid());
			}
			if(resorder.getStatus() != null && !"".equals(resorder.getStatus())){
				sql += " and status = ?";
				params.add(resorder.getStatus());
			}
		}
		if(resorder != null && resorder.getOrderBy() != null){
			sql += " order by " + resorder.getOrderBy() + " "+ resorder.getOrder() + " ";
		}
		if(resorder != null && resorder.getPages() != null){
			int start = (resorder.getPages() - 1) * resorder.getPageSize();
			sql += " limit " + start+ "," + resorder.getPageSize();
		}
		return DBUtil.list(Resorder.class, sql, params.toArray());
	}
	
	public Integer findCount(Resorder resorder){
		String sql = "select count(*) as total from resorder where 1=1";
		List<Object> params = new ArrayList<Object>();
		if(resorder != null){
			if(resorder.getUserid() != null && !"".equals(resorder.getUserid())){
				sql += " and userid = ?";
				params.add(resorder.getUserid());
			}
			if(resorder.getStatus() != null && !"".equals(resorder.getStatus())){
				sql += " and status = ?";
				params.add(resorder.getStatus());
			}
		}
		int count = Integer.parseInt(DBUtil.get(sql,params.toArray()).get("TOTAL").toString());
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageBean find(Resorder resorder){
		int total = findCount(resorder);
		List<Resorder> list = findResorder(resorder);
		PageBean pageBean = new PageBean();
		if(resorder != null && resorder.getPages() != null){
			pageBean.setPages(resorder.getPages());
			pageBean.setPageSize(resorder.getPageSize());
		}
		pageBean.setList(list);
		pageBean.setTotal(new Long(total));
		return pageBean;
	}
	
	
	public void transfer(Resorder resorder){
		String sql = "update resorder set status=1 where roid=" + resorder.getRoid();
		DBUtil.doUpdate(sql);
	}
}
