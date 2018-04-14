<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 用户管理 <span class="c-gray en">&gt;</span> 用户列表板块 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">
	<div class="text-l">
		用户类型：
		<span class="select-box" style="width:150px">
			<select name="select_role" id="select_role" class="select" value="${role!"-1"}">
				<option value="-1" <#if role??&&role==-1>selected</#if>></option>
				<option value="0" <#if role??&&role==0>selected</#if>>普通用户</option>
				<option value="1" <#if role??&&role==1>selected</#if>>内部用户</option>
			</select>
		</span>
		用户状态
		<span class="select-box" style="width:150px">
			<select name="select_state" id="select_state" class="select">
				<option value="-1" <#if state??&&state==-1>selected</#if>></option>
				<option value="0" <#if state??&&state==0>selected</#if>>已启用</option>
				<option value="1" <#if state??&&state==1>selected</#if>>已锁定</option>
				<option value="9" <#if state??&&state==9>selected</#if>>已删除</option>
			</select>
		</span>
		<input type="text" class="input-text" style="width:250px" placeholder="输入用户名" id="select_username" name="username" value="${username!""}">
		<button type="button" class="btn btn-success" id="search" name=""><i class="Hui-iconfont">&#xe665;</i> 搜用户</button>
	</div>
	<div class="mt-20">
	<table class="table table-border table-bordered table-hover table-bg">
		<thead>
			<tr class="text-c" style="background:#333;">
				<th width="80">ID</th>
				<th width="80">用户名</th>
				<th width="80">手机号</th>
				<th width="80">用户类型</th>
				<th width="80">注册时间</th>
				<th width="80">最后登录时间</th>
				<th width="80">用户状态</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<#list users as user>
				<tr class="text-c" tag="${user.user_id}">
					<td>${user.user_id!}</td>
					<td>${user.user_name!}</td>
					<td>${user.user_phone!}</td>
					<td>
						<#if user.role??&&user.role==0>
							普通用户
						<#elseif user.role??&&user.role==1>
							内部用户
						</#if>
					</td>
					<td>
						<#if user.reg_time??>
							${user.reg_time?string("yyyy-MM-dd HH:mm:ss")}
						</#if>
					</td>
					<td>
						<#if user.log_time??>
							${user.log_time?string("yyyy-MM-dd HH:mm:ss")}
						</#if>
					</td>
					<td>
						<#if user.user_state??&&user.user_state==0>
							<span class="label label-success radius">已启用</span>
						<#elseif user.user_state??&&user.user_state==1>
							<span class="label radius">已锁定</span>
						<#elseif user.user_state??&&user.user_state==9>
							<span class="label radius">已删除</span>
						</#if>
					</td>
					
					<td>
					<#if user.user_state!=9>
						<a href="javascript:editUser(${user.user_id});" tag="${user.user_id}" class="btn radius"><i class="Hui-iconfont">&#xe60d;</i></a>
						<#if user.user_state==0>
							<a href="javascript:deleteUser(${user.user_id});" tag="${user.user_id}" class="btn radius"><i class="Hui-iconfont">&#xe60e;</i></a>
						<#elseif user.user_state==1>
							<a href="javascript:recoverUser(${user.user_id});" tag="${user.user_id}" class="btn radius"><i class="Hui-iconfont">&#xe605;</i></a>
						</#if>
						<a href="javascript:removeUser(${user.user_id});" tag="${user.user_id}" class="btn radius"><i class="Hui-iconfont">&#xe6a6;</i></a>
						<a href="javascript:editUsername(${user.user_id});" tag="${user.user_id}" class="btn radius"><i class="Hui-iconfont">&#xe6df;</i></a>
					</#if>
					</td>
				</tr>
			</#list>
		</tbody>
	</table>
	</div>
	<#if (pages>1)>
	<div class="mt-20" style="text-align:center">
		<div id="page11"></div>
	</div>
	</#if>
</div>

<div id="modal_edit" class="modal fade" tabindex="-1" data-backdrop="static" data-width="400">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h4 class="modal-title">修改用户权限</h4>
    </div>
    <div class="modal-body">
    	<div>
    		<div class="row cl">
				<label class="form-label col-3">当前类型：</label>
				<div class="formControls col-9">
				    <label id="role_name" class="form-label col-3" style="color:#F00;"></label>
				</div>
			</div>
    		<div class="row cl" style="margin-bottom:10px;">
				<label class="form-label col-3">用户类型：</label>
				<div class="formControls col-9">
				     <span class="select-box">
						<select name="role" id="role" class="select">
							<option value="0">普通用户</option>
							<option value="1">内部用户</option>
						</select>
					</span>
				</div>
			</div>
    	</div>
    </div>
    <div class="modal-footer">
    	<button type="button" class="btn btn-success radius" id="bt_change">提交</button>
        <button type="button" class="btn radius" data-dismiss="modal" aria-hidden="true">关闭</button>
        <input type="hidden" id="user_id" value=""/>
    </div>
</div>
<div id="modal_edit_username" class="modal fade" tabindex="-1" data-backdrop="static" data-width="400">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h4 class="modal-title">修改用户名</h4>
    </div>
    <div class="modal-body">
    	<div>
    		<div class="row cl">
				<label class="form-label col-3">当前用户名：</label>
				<div class="formControls col-9">
				    <label id="old_username" class="form-label col-9" style="color:#F00;"></label>
				</div>
			</div>
    		<div class="row cl" style="margin-bottom:10px;">
				<label class="form-label col-3">新用户名：</label>
				<div class="formControls col-9">
				     <input type="text" class="input-text" style="width:250px" id="edit_username" >
				</div>
			</div>
    	</div>
    </div>
    <div class="modal-footer">
    	<button type="button" class="btn btn-success radius" id="bt_change_username">提交</button>
        <button type="button" class="btn radius" data-dismiss="modal" aria-hidden="true">关闭</button>
        <input type="hidden" id="user_id_username" value=""/>
    </div>
</div>
</body>

<@cc.html_foot>
<script type="text/javascript">
$(function(){
	$("#bt_change").click(function(){
		var role=$("#role").val();
		var id = $("#user_id").val();
			$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/admin/user/editUser',
    				data :{
    					user_id:id,
    					role:role
    				},
    				success : function(data){
    					location.reload();
    				}
				
				});
		
	});
	$("#bt_change_username").click(function(){
		var username=$("#edit_username").val();
		var id = $("#user_id_username").val();
			$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/admin/user/changeUsername',
    				data :{
    					user_id:id,
    					username:username
    				},
    				success : function(data){
    					var code = data.code;
    					var msg = data.msg;
    					if(code=="0000"){
    						window.location.reload();
    					}else{
    						alert(msg);
    					}
    				}
				
				});
		
	});
	$("#search").click(function(){
		search();
	});
});
function editUser(id){
				$("#user_id").val(id);
				$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/admin/user/getUserInfo',
    				data :{
    					user_id:id
    				},
    				success : function(data){
    					var role = data.role;
    					var role_name="";
    					if(role==1){
    						role_name="内部用户";
    					}else if(role==0){
    						role_name="普通用户";
    					}
    					$("#role_name").text(role_name);
    				}
				
				});
				$("#modal_edit").modal();
}
function editUsername(id){
				$("#user_id_username").val(id);
				$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/admin/user/getUserInfo',
    				data :{
    					user_id:id
    				},
    				success : function(data){
    					var username = data.user_name;
    					$("#old_username").text(username);
    				}
				
				});
				$("#modal_edit_username").modal();
}
function deleteUser(id){
	layer.confirm('角色锁定须谨慎，确认要锁定吗？',function(index){
				$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/admin/user/deleteUser',
    				data :{
    					user_id:id
    				},
    				success : function(data){
    					layer.msg('已锁定!',{icon:1,time:1000});
    					location.reload();
    				}
				});
	});
}
function removeUser(id){
	layer.confirm('角色删除须谨慎，确认要删除吗？',function(index){
				$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/admin/user/removeUser',
    				data :{
    					user_id:id
    				},
    				success : function(data){
    					layer.msg('已删除!',{icon:1,time:1000});
    					location.reload();
    				}
				});
	});
}
function recoverUser(id){
	layer.confirm('确认要解锁吗？',function(index){
				$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/admin/user/recoverUser',
    				data :{
    					user_id:id
    				},
    				success : function(data){
    					layer.msg('已解锁!',{icon:1,time:1000});
    					location.reload();
    				}
				});
	});
}
<#if (pages>1)>
laypage({
    cont: 'page11',
    pages: ${pages}, //可以叫服务端把总页数放在某一个隐藏域，再获取。假设我们获取到的是18
    curr: ${page}, 
    jump: function(e, first){ //触发分页后的回调
        if(!first){ //一定要加此判断，否则初始时会无限刷新
            toPage(e.curr);
        }
    }
});
</#if>
function search(){
	var username = $("#select_username").val();
	var role = $("#select_role").val();
	var state = $("#select_state").val();
	var url = '${bbs_url}xpabc/admin/user/show.htm?role='+role+'&state='+state+'&username='+username;
	location.href = url;
}
function toPage(page){
	var username = "${username!""}";
	var role = ${role!-1};
	var state = ${state!-1};
	var url = '${bbs_url}xpabc/admin/user/show.htm?role='+role+'&state='+state+'&username='+username+'&page='+page;
	location.href = url;
}
</script>
</@cc.html_foot>
