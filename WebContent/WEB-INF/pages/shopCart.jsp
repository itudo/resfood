<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>


<script type="text/javascript">
function changeCount( num,fid  ){
	$.ajax({
		url:"resfood.action?op=changeCount",
		type:"POST",
		data: "fid="+fid+"&num="+num,
		dataType:"JSON",
		success:function(data){
			if(   data.code==1 ){
				if(  data.obj.cartItem.count<=0  ){
					$("#tr_"+fid).remove(  );
				}else{
					$("#count_"+fid).html(  data.obj.cartItem.count );
					//alert(  data.obj.cartItem.smallCount  ); 
					$("#smallCount_"+fid).html(  data.obj.cartItem.smallCount );
				}
				$("#total").html(   data.obj.total);
					
			}
		}
	});
}
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="#FFFFFF" height="100%">
	<tr valign="top">
		<td>
		<table width="98%" border="0" cellspacing="1" cellpadding="2"
			align="center">
			<tr valign="bottom">
				<td height="30"><img
					src="images/cart.gif"> <font
					color="#000000">您的购物车中有以下商品</font></td>
			</tr>
		</table>
		<table width="98%" border="0" cellspacing="2" cellpadding="0"
			align="center">
			<tr bgcolor=#808000>
				<td height="1" bgcolor="#999999"></td>
			</tr>
		</table>
		<table width="98%" border="0" cellspacing="2" cellpadding="0"
			align="center">
			<tr>
				<td height="5"></td>
			</tr>
		</table>
		<table  width="98%" border="0" cellspacing="0" cellpadding="0"
			align="center">
			<tr>
				<td>
				<table width="100%" border="0" align="CENTER" cellpadding="2"
					cellspacing="1" bgcolor="#c0c0c0" id="cart">
					<tr bgcolor="#dadada">
						<td height="22" width="50">
						<div align="CENTER"><font color="#000000">编号</font></div>
						</td>
						<td width="610" height="22">
						<div align="CENTER"><font color="#000000">商品名称</font></div>
						</td>
						<td height="22" width="104">
						<div align="CENTER"><font color="#000000">单价</font></div>
						</td>
						<td height="22" width="100">
						<div align="CENTER"><font color="#000000">数量</font></div>
						</td>
						<td width="116" height="22">
						<div align="CENTER"><font color="#000000">金额</font></div>
						</td>
					</tr>

			<c:forEach items="${sessionCart }" var="entry">
					<tr bgcolor="#ffffff" id="tr_${entry.key }">
						<td width="50" align="center" height="22"><font
							color="#000000">${entry.key }</font></td>
						<td align="center" height="22"><font color="#000000">${entry.value.resfood.fname }</font>
						<input type="hidden" name="fid" value="${entry.key }"></td>
						<td width="104" align="center" height="22"><font
							color="#000000">￥${entry.value.resfood.realprice }</font></td>
						<td width="100" class="hh" align="center" height="22">
						
								<a href="javascript:changeCount(-1,${entry.key} )">-</a>	
										<span id="count_${entry.key }" >${entry.value.count }</span>
								<a href="javascript:changeCount(1,${entry.key })">+</a>
							
						</td>
						<td width="116" class="bb" align="center" height="22"><font
							color="#000000">￥
							
							<span id="smallCount_${entry.key }"> ${entry.value.smallCount } </span>
							
							</font></td>
					</tr>
    		</c:forEach>




					<tr bgcolor="#dadada">
						<td width="50" height="22" align="center"><font
							color="#000000">合计</font></td>
						<td height="22" align="center"><font color="#000000">-</font></td>
						<td width="104" height="22" align="center"><font
							color="#000000">-</font></td>
						<td width="100" class="hh" height="22" align="center"><font
							color="#000000">-</font></td>
						<td width="116" class="bb" align="center" height="22"><font
							color="#000000">￥
							
							    <span id="total">${sessionTotal }</span> 
							     
							      </font></td>
					</tr>
				</table>
				<br>
				<table width="300" border="0" cellspacing="1" cellpadding="4"
					align="CENTER" bgcolor="#c0c0c0">
					<tr bgcolor="#dadada">
						<td height="10" align="center"><a href="resfood.action?op=clearCart"><font
							color="#000000">清空购物车</font></a></td>
						<td height="10" align="center" style="cursor:hand" ><font
							color="#000000"><a href="index.jsp">继续购物</a></font></td>
						<td height="10" align="center" style="cursor:hand" ><font
							color="#000000"><a href="resuser/resorder.action?op=toFillForm">生成订单</a></font></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>


<%@ include file="bottom.jsp" %>

