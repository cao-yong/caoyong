<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>comfortabo-list</title>
	<script type="text/javascript">
		var disableLast;
		//修改
		var updateSku = function(skuId){
			//市场价
			$('#m' + skuId).attr("disabled",false);
			//售价
			$('#p' + skuId).attr("disabled",false);
			//库存
			$('#s' + skuId).attr("disabled",false);
			//购买限制
			$('#l' + skuId).attr("disabled",false);
			//运费
			$('#f' + skuId).attr("disabled",false);
			//禁用上次的行
			if(disableLast && skuId != disableLast){
				//市场价
				$('#m' + disableLast).attr("disabled",true);
				//售价
				$('#p' + disableLast).attr("disabled",true);
				//库存
				$('#s' + disableLast).attr("disabled",true);
				//购买限制
				$('#l' + disableLast).attr("disabled",true);
				//运费
				$('#f' + disableLast).attr("disabled",true);
			}
			disableLast = skuId;
		}
		//保存
		var addSku = function(skuId){
			//市场价
			var m = $('#m' + skuId).attr("disabled",true).val();
			//售价
			var p = $('#p' + skuId).attr("disabled",true).val();
			//库存
			var s = $('#s' + skuId).attr("disabled",true).val();
			//购买限制
			var l = $('#l' + skuId).attr("disabled",true).val();
			//运费
			var f = $('#f' + skuId).attr("disabled",true).val();
			
			var url = '/sku/addSku.do';
			var params = {
				"id":skuId,
				"marketPrice" : m,
				"price" : p,
				"stock" : s,
				"upperLimit" : l,
				"deliveFee" : f
			};
			//发送post异步请求保存数据
			$.post(url, params, function(data){
				if(data.success){
					alert(data.msg);
				}else{
					alert(data.errMsg);
				}
			},'json')
			//ajax示例
//			$.ajax({
//				url:url,
//				type:'post'
//				data:data,
//				dataType:'json',
//				function(result){
//				}
//			});
		}
	</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 库存管理 - 列表</div>
	<div class="clear"></div>
</div>
<div class="body-box">
<form method="post" id="tableForm">
<table cellspacing="1" cellpadding="0" border="0" width="100%" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
			<th>商品编号</th>
			<th>商品颜色</th>
			<th>商品尺码</th>
			<th>市场价格</th>
			<th>销售价格</th>
			<th>库       存</th>
			<th>购买限制</th>
			<th>运       费</th>
			<th>是否赠品</th>
			<th>操       作</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
		<c:forEach items="${skus }" var="sku">
			<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
				<td><input type="checkbox" name="ids" value="73"/></td>
				<td>${sku.productId }</td>
				<td align="center">${sku.color.name }</td>
				<td align="center">${sku.size }</td>
				<td align="center"><input type="text" id="m${sku.id }" value="${sku.marketPrice }" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="p${sku.id }" value="${sku.price }" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="s${sku.id }" value="${sku.stock }" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="l${sku.id }" value="${sku.upperLimit }" disabled="disabled" size="10"/></td>
				<td align="center"><input type="text" id="f${sku.id }" value="${sku.deliveFee }" disabled="disabled" size="10"/></td>
				<td align="center">不是</td>
				<td align="center"><a href="javascript:updateSku('${sku.id }')" class="pn-opt">修改</a> | <a href="javascript:addSku('${sku.id }')" class="pn-opt">保存</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</form>
</div>
</body>
</html>