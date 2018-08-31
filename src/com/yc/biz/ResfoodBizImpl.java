package com.yc.biz;

import java.util.ArrayList;
import java.util.List;

import com.yc.bean.PageBean;
import com.yc.bean.Resfood;
import com.yc.dao.DBUtil;

public class ResfoodBizImpl {
	public List<Resfood> findResfood(Resfood resfood){
		String sql = "select * from resfood where 1=1 ";
		List<Object> param = new ArrayList<Object>();
		if(resfood != null){
			
		}
		if(resfood != null && resfood.getOrderBy() != null && resfood.getOrder() != null){
			sql += "order by " + resfood.getOrderBy() + " " + resfood.getOrder();
		}
		if(resfood != null && resfood.getPages() != null && resfood.getPageSize() != null){
			int pages = resfood.getPages();
			int pageSize = resfood.getPageSize();
			int start = (pages -1) * pageSize;
			sql += " limit ?,?";
			param.add(start);
			param.add(pageSize);
		}
		return DBUtil.list(Resfood.class,sql, param.toArray());
	}
	
	public Long findResfoodCount(Resfood resfood){
		String sql = "select count(*) as num from resfood where 1=1";
		List<Object> param = new ArrayList<Object>();
		if(resfood != null){
			
		}
		Object num = DBUtil.get(sql, param.toArray()).get("NUM");
		if(num != null){
			return Long.parseLong(num.toString());
		}else{
			return 0L;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageBean findResfoodByPage(Resfood resfood){
		long total = findResfoodCount(resfood);
		List<Resfood> list = findResfood(resfood);
		PageBean pageBean = new PageBean();
		pageBean.setList(list);
		pageBean.setPages(resfood.getPages());
		pageBean.setPageSize(resfood.getPageSize());
		pageBean.setTotal(total);
		long totalPage = total % resfood.getPageSize() == 0 ? total/resfood.getPageSize() :total/resfood.getPageSize() +1 ;
		pageBean.setTotalPage(totalPage);
		return pageBean;
	}

	public int updateResfoodInfo(Resfood resfood) {
		String sql="update resfood set fname=?,normprice=?,realprice=?,detail=?,fphoto=? where fid=? ";
		return DBUtil.doUpdate(sql, resfood.getFname(),resfood.getNormprice(),resfood.getRealprice(),resfood.getDetail(),resfood.getFphoto(),resfood.getFid());
	}

	public List<Resfood> findResfoodSellCountList(Resfood resfood) {
		String sql = "select resfood.fid as fid,fname,ifnull(sum(num),0) as sellcount "
				+ " from resfood "
				+ "left join resorderitem "
				+ " on resfood.fid=resorderitem.fid "
				+ " group by resfood.fid,fname ";
		if(resfood !=null && resfood.getOrderBy()!=null ){
			sql +=" order by " + resfood.getOrderBy()  + " " + resfood.getOrder();
		}
		
		if(resfood !=null && resfood.getPages() !=null &&  resfood.getPageSize() !=null){
			int pages = resfood.getPages();
			int pagesize = resfood.getPageSize();
			int start = (pages-1) * pagesize;
			sql +=" limit " + start + "," + pagesize;
			
		}
		return DBUtil.list(Resfood.class, sql);
	}

	public int insertResfood(Resfood resfood) {
		// TODO Auto-generated method stub
		String sql ="insert into resfood(fname,normprice,realprice,detail,fphoto) values(?,?,?,?,?)";
		return DBUtil.doUpdate(sql, resfood.getFname(),resfood.getNormprice(),resfood.getRealprice(),resfood.getDetail(),resfood.getFphoto());
	}
}
