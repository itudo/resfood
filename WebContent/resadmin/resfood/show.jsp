<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>

<script type="text/javascript">
	var editFlag=undefined;
	$(function(){
	var editor = CKEDITOR.replace('detail');
	
	$("#resfoodListTable").datagrid({
			url:'resadmin/resfood.action?op=showFoodList',
			pagination:true,
			 pageSize:100,
			 pageList:[50,100,150,200],
			 fitColumns:true,
			 idField:"fid",
			 loadMsg:"正在努力为您加载数据",
			 rowsnumbers:true,
			 fit:true,
			 nowrap:true,
			 singleSelect:true,
			 sortName:"fid",
			 sortOrder:"desc",
			 toolbar:[{
				 text:"上架新菜品",
				 iconCls:'icon-add',
				 handler:function(){
					 //$('#dlg').form('clear');
					 $('#dlg').dialog('open').dialog('center').dialog('setTitle','新菜品上架');
				 }
			 },'-',{
				 text:"修改",
				 iconCls:"icon-edit",
				 // handler相当于onclick
				 handler:function(){
					 //选中一行进行编辑
					 var rows=$("#resfoodListTable").datagrid('getSelections');
					 if(rows.length==1){
						 //选中一行的话触发事件，如果当前状态为编辑状态，则退出编辑状态
						 if(editFlag!=undefined){
							 $("#resfoodListTable").datagrid('endEdit',editFlag); //结束编辑，传入之前编辑的行
						 }
						 if(editFlag==undefined){
							 var index=$("#resfoodListTable").datagrid('getRowIndex',rows[0]);  //获取选定行的索引
							 $("#resfoodListTable").datagrid('beginEdit',index); //开启编辑并传入要编辑的行
							 editFlag=index;
						 }
					 }
				 }
			 },'-',{
				 text:"保存",
				 iconCls:"icon-save",
				 handler:function(){
					 $("#resfoodListTable").datagrid('endEdit',editFlag); //会触发onAfterEdit事件，在那里写更新代码
				 }
			 },'-',{
				 text:"撤销",
				 iconCls:"icon-redo",
				 handler:function(){
					 editFlag=undefined;
					 $("#resfoodListTable").datagrid('rejectChanges');
				 }
			 },'-'
			 ],
			 columns:[[
			           {field:'fid',title:'菜编号',width:100,sortable:true,align:'center'},
			           {field:'fname',title:'菜名',width:100,align:'center',editor:{
			        	   //设置为可编辑
			        	   type:'validatebox',  //设置编辑格式
			        	   options:{
			        		   required:true    //设置编辑规则属性
			        	   }
			           }},{field:'normprice',title:'折前价格',width:100,sortable:true,align:'center',editor:{
			        	   //设置为可编辑
			        	   type:'validatebox',  //设置编辑格式
			        	   options:{
			        		   required:true    //设置编辑规则属性
			        	   }
			           }},
			           {field:'realprice',title:'现价',width:100,sortable:true,align:'center',editor:{
			        	   //设置为可编辑
			        	   type:'validatebox',  //设置编辑格式
			        	   options:{
			        		   required:true    //设置编辑规则属性
			        	   }
			           }},{
			        	   field:'_operate',
			        	   title:'操作',
			        	   width:150,
			        	   align:'center',
			        	   formatter:function(val,row,index){
			        		   var str="";
			        		   return str;
			        	   }
			           }
			          /*  {field:'detail',title:'详情',width:100} */
			       ]],
			       
			       //当点击结束编辑时，会自动触发onAfterEdit事件，则这个事件处理代码被激活
			       onAfterEdit:function(rowIndex,rowData,changes){
			    	   //在添加完毕endEdit，保存时触发
			    	   editFlag=undefined;  //重置
			    	   //TODO：发请求
			    	   $.ajax({
			    		   url:'resadmin/resfood.action?op=updateResfoodInfo',
			    		   data:rowData,
			    		   type:'POST',
			    		   complete:function(data){
			    			   if(data.code==1){
			    				   $.messager.alert('提示','成功','info');
			    			   }
			    		   }
			    	   });
			       },
			       onDblClickCell:function(rowIndex,field,value){
			    	   //双击该行修改内容
			    	   if(editFlag!=undefined){
			    		   $("#resfoodListTable").datagrid('endEdit',editFlag);  //结束编辑，传入之前编辑的行
			    	   }
			    	   if(editFlag==undefined){
			    		   $("#resfoodListTable").datagrid('beginEdit',rowIndex);  //开启编辑并传入要编辑的行
			    		   editFlag=rowIndex;
			    	   }
			       },
			       onContextMenu:function(rowIndex,field,value){
			    	   //右击该行
			    	   alert("a");
			       }
			       
		});
	});

	
</script>
<body class="easyui-layout">
	<div data-options="region:'center'" style="height:70%">
		<table id="resfoodListTable"></table>
	
	</div>
	
	<div id="dlg" class="easyui-dialog" style="width:600px; height: 300px; padding: 10px 20px " closed="true" buttons="#dlg-buttons">
		<div class="ftitle">新菜品上架</div>
			<form id="fm" method="post" action="resadmin/resfood/saveResfood.action" enctype="multipart/form-data" novalidate>
				<div>
					<label>菜品名：</label>
					<input id="fname"  name="fname" class="easyui-textbox" required="true">
				</div>
				<div>
					<label>价格：</label>
					<input id="normprice"  name="normprice" class="easyui-textbox" required="true">
				</div>
				<div>
					<label>特价：</label>
					<input id="realprice" name="realprice" class="easyui-textbox" required="true">
				</div>
				<div>
					<label>详情：</label>
					<textarea class="ckeditor" id="detail" name="detail"></textarea>
				</div>
				
				<div class="fitem">
					<label>图片：</label>
					<input id="fphoto" name="fphoto" type="file">
				</div>
				
				<div class="fitem">
					<input type="submit" value="添加">
				</div>
			
			</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" id="saveResfood" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="" style="width:90px">Save</a>
		<a href="javascript:void(0)"  class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">Cancel</a>
		
	</div>

</body>
</html>