package com.yc.biz;

import java.util.ArrayList;
import java.util.List;

import com.yc.bean.Resadmin;
import com.yc.dao.DBUtil;
import com.yc.utils.Encrypt;


public class ResAdminBizImpl {

	public Resadmin login(Resadmin reaAdmin) {
		String sql = "select * from resadmin where raname = ? and rapwd = ?";
		List<Object> param = new ArrayList<Object>();
		param.add(reaAdmin.getRaname());
		param.add(Encrypt.md5(reaAdmin.getRapwd()));
		List<Resadmin> list = DBUtil.list(Resadmin.class, sql, param.toArray());
		if(list == null || list.size() == 0){
			return null;
		}
		return list.get(0);
	}
	

}
