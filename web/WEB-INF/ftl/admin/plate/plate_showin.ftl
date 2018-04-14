<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 板块管理 <span class="c-gray en">&gt;</span> 内部板块 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">
	<div class="cl pd-5 bg-1 bk-gray mt-20">
	<span class="l">
		<input type="text" class="input-text" style="width:250px" placeholder="输入板块名称" id="input_platename" name="">
		<button type="button" class="btn btn-success radius" id="bt_add" name=""><i class="Hui-iconfont">&#xe600;</i> 添加板块</button>
	</span>
	</div>
	<div class="mt-20">
	<table class="table table-border table-bordered table-hover table-bg">
		<thead>
			<tr class="text-c" style="background:#333;">
				<th width="80">ID</th>
				<th width="100">板块名称</th>
				<th width="40">添加时间</th>
				<th width="40">版主数量</th>
				<th width="90">删除</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<#list plist as list>
				<tr class="text-c" tag="${list.plate_id}">
					<td>${list.plate_id}</td>
					<td>${list.plate_name}</td>
					<td>${list.add_time}</td>
					<td>${list.master_count}</td>
					<td>${list.is_delete}</td>
					<td>
						<a href="javascript:void(0);" tag="${list.plate_id}" tag_name="${list.plate_name}" class="btn radius plate_update" style="border:solid 1px #666;"><i class="Hui-iconfont">&#xe6df;</i></a>
						<a href="javascript:void(0);" tag="${list.plate_id}" ind="${list_index}" class="btn radius plate_up" style="border:solid 1px #666;"><i class="Hui-iconfont">&#xe679;</i></a>
						<a href="javascript:void(0);" tag="${list.plate_id}" ind="${list_index}" inm="${plist?size}" class="btn radius plate_down" style="border:solid 1px #666;"><i class="Hui-iconfont">&#xe674;</i></a>
					</td>
				</tr>
			</#list>
		</tbody>
	</table>
	</div>
</div>

<div id="modal_edit" class="modal fade" tabindex="-1" data-backdrop="static" data-width="400">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h4 class="modal-title">修改板块名称</h4>
    </div>
    <div class="modal-body">
    	<div>
    		<div class="row cl" style="margin-bottom:10px;">
				<label class="form-label col-3">原板块名：</label>
				<div class="formControls col-9">
				    <label id="lable_pname1" class="form-label col-3" style="color:#F00;"></label>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-3">新版块名：</label>
				<div class="formControls col-9">
				    <input id="input_pname2" type="text" class="input-text" value="" placeholder="" name="member-name" >
				</div>
			</div>
    	</div>
    </div>
    <div class="modal-footer">
    	<button type="button" class="btn btn-success radius" id="bt_change">提交</button>
        <button type="button" class="btn radius" data-dismiss="modal" aria-hidden="true">关闭</button>
        <input type="hidden" id="input_chengeid" value="" />
    </div>
</div>
</body>

<@cc.html_foot>
<script type="text/javascript">
$(function(){
	$(".plate_update").click(function(){
		$("#lable_pname1").html($(this).attr("tag_name"));
		$("#input_chengeid").val($(this).attr("tag"));
		$("#input_pname2").val("");
		$("#modal_edit").modal();
	});
	
	$("#bt_add").click(function(){
		var pname = $("#input_platename").val();
		if(pname.replace(/ /g,"").length==0){
			layer.msg('请填写板块名称！', {icon: 2});
			return;
		}
		
		$.post("${bbs_url}xpabc/admin/plate/add",{plate_name:pname,isin:1},function(data){
			if(data==1){
				alert('添加板块成功！');
				window.location.reload();
			}else{
				alert(data);
			}
		});
	});
	
	$("#bt_change").click(function(){
		var pname = $("#input_pname2").val();
		if(pname.replace(/ /g,"").length==0){
			layer.msg('请填写板块名称！', {icon: 2});
			return;
		}
		
		$.post("${bbs_url}xpabc/plate/update",
			{plate_id:$("#input_chengeid").val(),
			plate_name:pname,
			isin:1},
			function(data){
				if(data==1){
					alert('修改板块成功！');
					window.location.reload();
				}else{
					alert(data);
				}
			});
	});
	
	$(".plate_up").click(function(){
		var ind = $(this).attr("ind");
		if(ind==0){
			layer.msg('该板块不能上移！', {icon: 2});
			return;
		}
		var id1 = $(this).attr("tag");
		var id2 = $(this).parent().parent().prev().attr("tag");
		$.post("${bbs_url}xpabc/admin/plate/order",
			{id_1:id1,
			id_2:id2,
			isin:1},
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
			layer.msg('该板块不能下移！', {icon: 2});
			return;
		}
		var id1 = $(this).attr("tag");
		var id2 = $(this).parent().parent().next().attr("tag");
		$.post("${bbs_url}xpabc/admin/plate/order",
			{id_1:id2,
			id_2:id1,
			isin:1},
			function(data){
				if(data==1){
					window.location.reload();
				}else{
					alert(data);
				}
			});
	});
});
</script>
</@cc.html_foot>
