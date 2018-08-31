package com.yc.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yc.bean.Resuser;
import com.yc.dao.DBUtil;
import com.yc.utils.Encrypt;

public class ResuserBizImpl {
	
	public Resuser login(Resuser resuser){
		String sql = "select * from resuser where username = ? and pwd = ?";
		List<Object> param = new ArrayList<Object>();
		param.add(resuser.getUsername());
		param.add(Encrypt.md5(resuser.getPwd()));
		List<Resuser> list = DBUtil.list(Resuser.class, sql, param.toArray());
		if(list == null || list.size() == 0){
			return null;
		}
		return list.get(0);
	}

	public List<Resuser> findAll(int page, int rows, String sort, String order) {
		String sql = "select * from resuser where 1=1";
		if(sort !=null && order !=null){
			sql +=" order by " + sort + " " + order + " ";
		}
		List<Resuser> list = DBUtil.list(Resuser.class, sql);
		return list;
	}

	public int findCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Map<String, Object> findResuserContribution(int page, int rows,
			String sort, String order) {
		//int count = findCount();
		String sql = "select resuser.userid as userid,username,ifnull( sum(dealprice*num),0) as dealcount"
				+" from resuser left join resorder on resuser.userid = resorder.userid left join resorderitem on resorderitem.roid = resorder.roid "
				+" group by userid,username ";
		if(sort !=null && order !=null){
			sql +=" order by " + sort + " " + order + " ";
		}
		
		int start = (page-1)  * rows;
		sql +=" limit " + start + "," + rows;
		List<Resuser> list = DBUtil.list(Resuser.class, sql);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("total", list.size());
		map.put("rows",list);
		return map;
	}
}
