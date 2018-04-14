<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 用户管理 <span class="c-gray en">&gt;</span> 用户列表板块 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">
	<div class="text-l">
		<button type="button" class="btn btn-success" id="make" name=""><i class="Hui-iconfont">&#xe665;</i>初始化索引</button>
	</div>
	</br>
	<div class="text-l">
		<button type="button" class="btn btn-success" id="delMsg" name=""><i class="Hui-iconfont">&#xe6e2;</i>删除聊天记录</button>
	</div>
</div>
</body>

<@cc.html_foot>
<script type="text/javascript">
	$(function(){
		$("#make").click(function(){
			$.ajax({
				type: "POST",
				url : '${bbs_url}xpabc/admin/user/makeUsernameIndex',
    			success : function(data){
    				var code = data.code;
    				var msg = data.msg;
    				if(code=="0000"){
    					layer.msg(msg,{icon:1,time:1000});
    				}else{
    					layer.msg(msg,{icon:2,time:1000});
    				}
    			}
				
			});	
		});
		$("#delMsg").click(function(){
			$.ajax({
				type: "POST",
				url : '${bbs_url}xpabc/admin/user/delMsg',
    			success : function(data){
    				var code = data.code;
    				var msg = data.msg;
    				if(code=="0000"){
    					layer.msg(msg,{icon:1,time:1000});
    				}else{
    					layer.msg(msg,{icon:2,time:1000});
    				}
    			}
				
			});	
		});
	})
</script>
</@cc.html_foot>
