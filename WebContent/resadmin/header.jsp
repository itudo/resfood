<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="resadmin/easyui153/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="resadmin/easyui153/themes/icon.css">
<link rel="stylesheet" type="text/css" href="resadmin/easyui153/demo.css">
<script type="text/javascript" src="resadmin/easyui153/jquery.min.js"></script>
<script type="text/javascript" src="resadmin/easyui153/jquery.easyui.min.js"></script>
<script src="resadmin/ckeditor/ckeditor.js"></script>
<title>订餐系统后台</title>
</head>
