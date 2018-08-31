<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.yc.utils.*,java.util.*"%>
<%@include file="../header.jsp" %>
<%
	FileUpload fu = new FileUpload();
	Map<String,String> map = fu.uploadFiles(pageContext, request);
	session.setAttribute("mapPic", map);
	response.setContentType("text/html; charset=UTF-8");
	String callback = request.getParameter("CKEditorFuncNum");
	out.println("<script type=\"text/javascript\">");
	out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + map.get("weburl_upload") + "','')");
	out.println("</script>");
	out.flush();
%>
<%@include file="../bottom.jsp" %>