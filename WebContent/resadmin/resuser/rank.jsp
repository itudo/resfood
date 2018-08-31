<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<script type="text/javascript">
	$(function(){
		$('#resuserListTable').datagrid({
			url:"resadmin/resuser.action?op=showUserContributionList",
			pagination:true,
			pagSzie:100,
			pageList:[10,50,100,150,200],
			fitColumns:true,
			title:"用户列表",
			idField:"usrid",
			rownumbers:true,
			fit:true,
			nowrap:true,
			sortName:"dealcount",
			sortOrder:"desc",
			singleSelect:true,
			columns:[[
			          {field:"userid",title:"编号", width:140},
			          {field:"username",title:"用户名",width:140},
			          {field:"dealcount",title:"消费金额",width:140}
			          ]]
		});
	});
</script>
<body class="easyui-layout">
	<table id="resuserListTable"></table>
</body>
</html>