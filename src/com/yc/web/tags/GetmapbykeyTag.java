package com.yc.web.tags;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class GetmapbykeyTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5027512577075238253L;
	private Object key;
	private String var;
	@SuppressWarnings("rawtypes")
	private Map map;
	@Override
	public int doStartTag() throws JspException {
		Object obj = null;
		//TODO:   将来要根据  map  反射得到键的类型，再来转换  key的类型.
		Integer k = new Integer(key.toString());
		obj = map.get(k);   //    List<Board>
		super.pageContext.setAttribute(var, obj);   //   pageContext, request, session, application
		return super.EVAL_BODY_INCLUDE;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setMap(Map map) {
		this.map = map;
	}

}
