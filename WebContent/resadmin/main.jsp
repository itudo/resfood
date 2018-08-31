=<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<script >
	$(function(){
		var resfoodTreeData=[
				{"id" : 1,
					"text":"菜品上架",
					"attributes":{
						"url":"<iframe width='100%' height='100%' src='resadmin/resfood/rank.jsp' />"
					}
				},
				{"id" : 2,
					"text":"菜品浏览",
					"attributes":{
						"url":"<iframe width='100%' width='100%' height='100%' src='resadmin/resfood/show.jsp' />"
					}
				},
				{"id" : 3,
					"text":"销售排行",
					"attributes":{
						"url":"<iframe width='100%' height='100%' src='resadmin/resfood/rank.jsp' />"
					}
				}
			];
		var resuserTreeData=[
		     				{
		     					"text":"客户浏览",
		     					"attributes":{
		     						"url":"<iframe width='100%' height='100%' src='resadmin/resuser/rank.jsp' />"
		     					}
		     				},
		     				{"text":"客户贡献值排行",
		     					"attributes":{
		     						"url":"<iframe width='100%' height='100%' src='resadmin/resuser/resuserOrder.jsp' />"
		     					}
		     				}
		     			];
		var resorderTreeData=[
			     				{
			     					"text":"订单处理",
			     					"attributes":{
			     						"url":"<iframe width='100%' height='100%' src='resadmin/resorder/resorderManager.jsp' />"
			     					}
			     				}
			     			];
		var dataBaseTreeData=[
			     				{
			     					"text":"浏览表",
			     					"attributes":{
			     						"url":""
			     					}
			     				},{
			     					"text":"查询表",
			     					"attributes":{
			     						"url":""
			     					}
			     				},{
			     					"text":"备份",
			     					"attributes":{
			     						"url":""
			     					}
			     				},{
			     					"text":"还原",
			     					"attributes":{
			     						"url":""
			     					}
			     				}
			     			];
		var websiteTreeData=[
			     				{
			     					"text":"搜索引擎优化",
			     					"attributes":{
			     						"url":""
			     					}
			     				},{
			     					"text":"版权",
			     					"attributes":{
			     						"url":""
			     					}
			     				},{
			     					"text":"广告位管理",
			     					"attributes":{
			     						"url":""
			     					}
			     				},{
			     					"text":"在线客服",
			     					"attributes":{
			     						"url":""
			     					}
			     				},{
			     					"text":"开放数据API",
			     					"attributes":{
			     						"url":""
			     					}
			     				}
			     			];
		showTree("resfoodTree",resfoodTreeData);
		showTree("resuserTree",resuserTreeData);
		showTree("resorderTree",resorderTreeData);
		showTree("databaseTree",dataBaseTreeData);
		showTree("websiteTree",websiteTreeData);
		
		function showTree(treeId,treeData){
			$("#" + treeId).tree({
				data:treeData,
				 onClick:function(node){
					 if(node&&node.attributes){
						 openTab(node);
					 }
					
				} 
			})
		}
		
	})
	function openTab(node){
		if($("#tt").tabs("exists",node.text)){
			$("#tt").tabs("select",node.text);
		}else{
			$("#tt").tabs("add",{
				title:node.text,
				closable:true,
				content:node.attributes.url
			})
		}
	}
</script>
<body  class="easyui-layout">
	<div data-options="region:'north',split:true" style="height:100px;"></div>
    <div data-options="region:'south',split:true" style="height:100px;"></div>
    <div data-options="region:'east',title:'通知',split:true" style="width:150px;"></div>
    <div data-options="region:'west',title:'导航',split:true" style="width:150px;">
	    <div class="easyui-accordion" style="fit: true">
	        <div title="菜品管理" style="overflow:auto;padding:10px;">
	         	<ul id="resfoodTree" class="easyui-tree"></ul>
	        </div>
	         <div title="订单管理" style="overflow:auto;padding:10px;">
	         	<ul id="resorderTree" class="easyui-tree"></ul>
	        </div>
	         <div title="客户管理"  style="overflow:auto;padding:10px;">
         		<ul id="resuserTree" class="easyui-tree"></ul>
       		 </div>
	         <div title="数据字典管理" style="overflow:auto;padding:10px;">
	         	<ul id="databaseTree" class="easyui-tree"></ul>
	        </div>
	         <div title="网站管理"  style="overflow:auto;padding:10px;">
         		<ul id="websiteTree" class="easyui-tree"></ul>
       		 </div>
	    </div>
    </div>
   <div data-options="region:'center'" style="padding:5px;background:#eee;height: auto;">
    	<div id="tt"  class="easyui-tabs" data-options="fit:true,border:true">
		<div   title="欢迎" style="padding: 20px; height: 100%">
			welcine<br>
			welcine<br>
			welcine<br>
			welcine<br>
			
			welcine<br>welcine<br>welcine<br>welcine<br>welcine<br>welcine<br>welcine<br>
			welcine<br>welcine<br>welcine<br>welcine<br>welcine<br>welcine<br>welcine<br>welcine<br>welcine<br>welcine<br>
		</div>
	</div>
    </div>


	
<%@ include file="bottom.jsp" %>