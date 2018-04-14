<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<script type="text/javascript" src="lib/PIE_IE678.js"></script>
<![endif]-->
<link href="${bbs_admin}css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="${bbs_admin}css/H-ui.login.css" rel="stylesheet" type="text/css" />
<link href="${bbs_admin}css/style.css" rel="stylesheet" type="text/css" />
<link href="${bbs_admin}lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>后台登录 - 小胖爱编程</title>
</head>
<body>
<input type="hidden" id="TenantId" name="TenantId" value="" />
<div class="loginWraper">
  <div id="loginform" class="loginBox">
    <form class="form form-horizontal" action="#" method="post" id="form1">
      <div class="row cl">
        <label class="form-label col-3"><i class="Hui-iconfont">&#xe60d;</i></label>
        <div class="formControls col-8">
          <input id="username" name="username" type="text" placeholder="账户" class="input-text size-L">
        </div>
      </div>
      <div class="row cl">
        <label class="form-label col-3"><i class="Hui-iconfont">&#xe60e;</i></label>
        <div class="formControls col-8">
          <input id="password" name="password" type="password" placeholder="密码" class="input-text size-L">
        </div>
      </div>
      <div class="row">
        <div class="formControls col-8 col-offset-6">
          <input name="" id="login" type="button" class="btn btn-success radius size-L" value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;">
          <input name="" type="reset" class="btn btn-default radius size-L" value="&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;">
        </div>
      </div>
    </form>
  </div>
</div>
<script type="text/javascript" src="${bbs_admin}lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="${bbs_admin}js/H-ui.js"></script> 
<script type="text/javascript">
		$(function(){
			document.onkeydown = function(e){
				var ev = document.all ? window.event : e;
				if(ev.keyCode==13) {
					$("#login").click();
				}
			}
		}); 
	$("#login").click(function(){
		var username = $.trim($('#username').val());
		var password = $.trim($('#password').val());
		if(!is_exists(username)){
			alert("请输入用户名");
			return;
		}
		if(!is_exists(password)){
			alert("请输入密码");
			return;
		}
			$.ajax({
				type: "POST",
				url : '${bbs_url}xpabc/adminUser/login',
    			data :{
    				username:username,
    				password:password
    			},
    			success : function(data){
    				var code = data.code;
    				var msg = data.msg;
    				if(code=="0000"){
    					location.href = "${bbs_url}xpabc/admin/index.htm";
    				}else{
    					alert(msg);
    				}
    			}
				
			});	
		
	});
function is_exists(obj){
	obj = $.trim(obj);
	if(obj==""||obj==null||obj==undefined){
		return false;
	}else{
		return true;
	}
}
</script> 
</body>
</html>