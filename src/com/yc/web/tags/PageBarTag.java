package com.yc.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.yc.bean.PageBean;

public class PageBarTag extends TagSupport {

	private static final long serialVersionUID = -6485095433021811470L;
	private PageBean pageBean;
	private String href;
	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}
	public void setHref(String href) {
		this.href = href;
	}
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = super.pageContext.getOut();
		try {
			out.println("共"+pageBean.getTotal()+"条记录/每页"+pageBean.getPageSize()+"条记录 |");
			out.println("共"+pageBean.getTotalPage()+"页/当前第"+pageBean.getPages()+"页|");
			//topic.action?op=list&boardid=8&pages=1
			out.println("<a href='"+href+"&pages=1'>第一页</a>");
			out.println("<a href='"+href+"&pages="+pageBean.getPrePage()+"'>上一页</a>");
			out.println("<a href='"+href+"&pages="+pageBean.getNextPage()+"'>下一页</a>");
			out.println("<a href='"+href+"&pages="+pageBean.getTotalPage()+"'>最后一页</a>");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.EVAL_BODY_INCLUDE;
	}
}
