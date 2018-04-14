<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_top>
</@cc.html_top>
<style>
	td{ border:1px solid #000;}
</style>
<div class="wrap cl pageinfo"  style="height:800px;">
<h2>板块列表</h2>
<div>
<table>
	<tr>
		<td style="width:100px;">板块id</td>
		<td style="width:200px;">板块名称</td>
		<td style="width:100px;">是否被删除</td>
		<td style="width:200px;">操作</td>
	</tr>
	<#list plist as list>
		<tr tag="${list.plate_id}">
			<td>${list.plate_id}</td>
			<td>${list.plate_name}</td>
			<td>${list.is_delete}</td>
			<td>
				<a href="javascript:void(0);" tag="${list.plate_id}" tag_name="${list.plate_name}" class="plate_update">修改</a>
				<a href="javascript:void(0);" tag="${list.plate_id}" ind="${list_index}" class="plate_up">上移</a>
				<a href="javascript:void(0);" tag="${list.plate_id}" ind="${list_index}" inm="${plist?size}" class="plate_down">下移</a>
			</td>
		</tr>
	</#list>
</table>
</div>
<div>
	<input type="text" id="p_name" style="border:1px solid #000;" />
    <input type="button" id="sub" style="border:1px solid #000;" value="添加板块" />
</div>
<div>
	<p id="p_id">------0------</p>
	<input type="text" id="p_name_update" style="border:1px solid #000;" />
    <input type="button" id="sub_update" tag="0" style="border:1px solid #000;" value="修改板块" />
</div>

</div>

<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
<script type="text/javascript">
	$("#sub").click(function(){
		var pname = $("#p_name").val();
		if(pname.replace(/ /g,"").length==0){
			alert("请填写板块名称！");
			return;
		}
		
		$.post("${bbs_url}xpabc/plate/add",{plate_name:pname,isin:0},function(data){
			if(data==1){
				alert("成功！");
				window.location.reload();
			}else{
				alert(data);
			}
		});
	});
	
	$(".plate_update").click(function(){
		$("#p_id").html("-------" + $(this).attr("tag") + "-------");
		$("#p_name_update").val($(this).attr("tag_name"));
		$("#sub_update").attr("tag",$(this).attr("tag"));
	});
	
	$("#sub_update").click(function(){
		var pname = $("#p_name_update").val();
		if(pname.replace(/ /g,"").length==0){
			alert("请填写板块名称！");
			return;
		}
		
		$.post("${bbs_url}xpabc/plate/update",
			{plate_id:$(this).attr("tag"),
			plate_name:pname,
			isin:0},
			function(data){
				if(data==1){
					alert("成功！");
					window.location.reload();
				}else{
					alert(data);
				}
			});
	});
	
	$(".plate_up").click(function(){
		var ind = $(this).attr("ind");
		if(ind==0){
			alert("该板块不能上移！");
			return;
		}
		var id1 = $(this).attr("tag");
		var id2 = $(this).parent().parent().prev().attr("tag");
		$.post("${bbs_url}xpabc/plate/order",
			{id_1:id1,
			id_2:id2,
			isin:0},
			function(data){
				if(data==1){
					window.location.reload();
				}else{
					alert(data);
				}
			});
	});
	
	$(".plate_down").click(function(){
		var ind = parseInt($(this).attr("ind"));
		var inm = parseInt($(this).attr("inm"));
		if(ind+1==inm){
			alert("该板块不能下移！");
			return;
		}
		var id1 = $(this).attr("tag");
		var id2 = $(this).parent().parent().next().attr("tag");
		$.post("${bbs_url}xpabc/plate/order",
			{id_1:id2,
			id_2:id1,
			isin:0},
			function(data){
				if(data==1){
					window.location.reload();
				}else{
					alert(data);
				}
			});
	});
	
</script>
</@cc.html_js>
