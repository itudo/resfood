<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<script type="text/javascript">
	$(function(){
		$('#resfoodListTable').datagrid({
			url:"resadmin/resfood.action?op=showFoofSellInfoList",
			pagination:true,
			pagSzie:100,
			pageList:[10,50,100,150,200],
			fitColumns:true,
			//title:"用户列表",
			idField:"fid",
			loadMsg:"正在努力为你加载数据",
			rownumbers:true,
			fit:true,
			nowrap:true,
			sortName:"sellcount",
			sortOrder:"desc",
			singleSelect:true,
			columns:[[
			          {field:"fid",title:"编号",width:40,sortable:true,align:'center'},
			          {field:"fname",title:"菜名",width:70,align:'center'},
			          {field:"sellcount",title:"销售次数",width:150,sortable:true,align:'center'}    
			          ]]
		});
	})
</script>
<body class="easyui-layout">
	<div data-options="region:'center' ,fit:true" style="height:100%">
		<table id="resfoodListTable" class="easyui-datagrid"></table>
	
	</div>
</body>
</html>