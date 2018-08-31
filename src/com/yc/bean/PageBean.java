package com.yc.bean;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable {
	private static final long serialVersionUID = 7313229623726375795L;
	private Integer pages;
	private Integer pageSize;
	private Long total;
	private Long totalPage;
	private List<T> list;
	
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	public Integer getPages() {
		return pages;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public Long getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}
	public Integer getPrePage(){
		return (pages <= 1 ? 1 : pages-1);
	}
	
	public Integer getNextPage(){
		return (pages < totalPage ? pages+1 : pages);
	}
	@Override
	public String toString() {
		return "PageBean [pages=" + pages + ", pageSize=" + pageSize
				+ ", total=" + total + ", totalPage=" + totalPage + ", list="
				+ list + "]";
	}
	
	
}
