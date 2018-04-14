<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<style>
.modal-body .row{margin:10px 0;}
</style>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 板块管理 <span class="c-gray en">&gt;</span> 版主列表 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">
	<div class="cl pd-5 bg-1 bk-gray mt-20">
	<span class="l">
		<button type="button" class="btn btn-success radius" id="bt_addModal" name=""><i class="Hui-iconfont">&#xe600;</i> 添加版主</button>
	</span>
	</div>
	<div class="mt-20">
		<div id="tab-system" class="HuiTab">
			<div class="tabBar cl"><span>外部板块</span><span>内部板块</span></div>
			<div class="tabCon">
				<table class="table table-border table-bordered table-hover table-bg">
					<thead>
						<tr class="text-c" style="background:#333;">
							<th width="40">版主ID</th>
							<th width="40">用户id</th>
							<th width="100">用户名称</th>
							<th width="40">板块id</th>
							<th width="100">板块名称</th>
							<th width="100">添加时间</th>
							<th width="50">操作</th>
						</tr>
					</thead>
					<tbody>
						<#list masterList as list>
							<tr class="text-c" tag="${list.master_id}">
								<td>${list.master_id}</td>
								<td>${list.user_id}</td>
								<td>${list.user_name}</td>
								<td>${list.plate_id}</td>
								<td>${list.plate_name}</td>
								<td>${list.add_time}</td>
								<td>
									<a href="javascript:void(0);" tag="${list.master_id}" class="btn radius master_delete" style="border:solid 1px #666;"><i class="Hui-iconfont" style="font-size:18px;">&#xe609;</i></a>
								</td>
							</tr>
						</#list>
					</tbody>
				</table>
			</div>
			<div class="tabCon">
				<table class="table table-border table-bordered table-hover table-bg">
					<thead>
						<tr class="text-c" style="background:#333;">
							<th width="40">版主ID</th>
							<th width="40">用户id</th>
							<th width="100">用户名称</th>
							<th width="40">板块id</th>
							<th width="100">板块名称</th>
							<th width="100">添加时间</th>
							<th width="50">操作</th>
						</tr>
					</thead>
					<tbody>
						<#list masterListin as list>
							<tr class="text-c" tag="${list.master_id}">
								<td>${list.master_id}</td>
								<td>${list.user_id}</td>
								<td>${list.user_name}</td>
								<td>${list.plate_id}</td>
								<td>${list.plate_name}</td>
								<td>${list.add_time}</td>
								<td>
									<a href="javascript:void(0);" tag="${list.master_id}" class="btn radius master_delete" style="border:solid 1px #666;"><i class="Hui-iconfont" style="font-size:18px;">&#xe609;</i></a>
								</td>
							</tr>
						</#list>
					</tbody>
				</table>
			</div>
		</div>
				
	</div>
</div>

<div id="modal_add" class="modal fade" tabindex="-1" data-backdrop="static" data-width="600">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h4 class="modal-title">添加版主</h4>
    </div>
    <div class="modal-body">
    	<div>
    		<div class="row cl" style="margin-bottom:10px;">
				<label class="form-label col-3">版主检索：</label>
				<div class="formControls col-9">
				    <input type="text" class="input-text" style="width:200px" placeholder="版主名称或电话号码" id="input_searchName" name="">
					<button type="button" class="btn btn-success radius" id="bt_search" name=""><i class="Hui-iconfont">&#xe665;</i></button>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-3">版主名称：</label>
				<div class="formControls col-9">
				    <label id="lable_addName" tag="" class="form-label" style="color:#F00;"></label>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-3">选择板块：</label>
				<div class="formControls col-9">
					<select id="plate_selectType" class="select" name="admin-role" style="float:left; width:100px; margin-right:20px;">
						<option value="0">外部板块</option>
						<option value="1">内部板块</option>
					</select>
					<select id="plate_select" class="select" name="admin-role" style="float:left; width:200px;">
						<#list plist as list>
							<option value="${list.plate_id}">${list.plate_name}</option>
						</#list>
					</select>
				</div>
			</div>
    	</div>
    </div>
    <div class="modal-footer">
    	<button type="button" class="btn btn-success radius" id="bt_add">提交</button>
        <button type="button" class="btn radius" data-dismiss="modal" aria-hidden="true">关闭</button>
    </div>
</div>
</body>

<@cc.html_foot>
<script type="text/javascript">
$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

var json1 = [<#list plist as list>{id:"${list.plate_id}",name:"${list.plate_name}"},</#list>];
var json2 = [<#list plistin as list>{id:"${list.plate_id}",name:"${list.plate_name}"},</#list>];

$(function(){
	$("#bt_addModal").click(function(){
		$("#lable_addName").html("");
		$("#lable_addName").attr("tag","");
		$("#modal_add").modal();
	});
	
	$("#bt_search").click(function(){
		var key = $("#input_searchName").val();
		if(key==""){
			layer.alert("请输入要查询的用户名或手机号！");
			return;
		}
		$.post("${bbs_url}xpabc/admin/plate/master/userSearch",{key:key},function(data){
			if(data==""){
				layer.alert("查不到该用户，请重新填写！");
			}else{
				$("#lable_addName").html(data.user_name);
				$("#lable_addName").attr("tag",data.user_id);
			}
		});
	});
	
	$("#plate_selectType").change(function(){
		var json = json1;
		if($(this).val()=="1"){
			json = json2;
		}
		$("#plate_select").html("");
		
		for(var i=0; i<json.length; i++){
			$("#plate_select").append("<option value='"+ json[i].id +"'>"+ json[i].name +"</option>");
		}
	});
	
	$("#bt_add").click(function(){
		var uid = $("#lable_addName").attr("tag");
		var isin = $("#plate_selectType").val();
		var plateid = $("#plate_select").val();
		if(uid==""){
			layer.alert("请添加用户！");
			return;
		}else{
			$.post("${bbs_url}xpabc/admin/plate/master/add",
					{userid:uid,
					isin:isin,
					plateid:plateid},
					function(data){
						if(data==1){
							window.location.reload();
						}
						if(data==-1){
							layer.alert("已经添加该用户为该板块的版主，请选择其他人！");
						}
						if(data==0){
							layer.alert("添加版主失败！");
						}
					});
		}
	});
	
	$(".master_delete").click(function(){
		var id = $(this).attr("tag");
		layer.confirm('确定要删除该版主吗？', {
		    btn: ['确定','取消']
		}, function(){
		    $.post("${bbs_url}xpabc/admin/plate/master/delete",
					{masterid:id},
					function(data){
						if(data==1){
							window.location.reload();
						}
						if(data==-1){
							layer.alert("该版主不存在！");
						}
						if(data==0){
							layer.alert("删除版主失败！");
						}
					});
		}, function(){
		    
		});
	});
});
</script>
</@cc.html_foot>
