<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_index_top>
    <div class="content_pass">
        <div class="headline"><span class='headline_cont'>找回登录密码</span></div>
        <ul class="steps_bar steps_bar_dark cf">
            <li class="step" style="z-index:2">
                <span class="step_num">1.</span>
                <span>确认账号</span>
                <span class="arrow_background"></span><span class="arrow_foreground"></span>
            </li>
            <li class="step step_current" style="z-index:1">
                <span class="step_num">2.</span>
                <span>修改密码</span>
                <span class="arrow_background"></span><span class="arrow_foreground"></span>
            </li>
            <li class="step" style="z-index:0">
                <span class="step_num">3.</span>
                <span>完成</span>
            </li>
        </ul>
        <div class="form_panle_passowrd">
            <div class="form_group">
                <label for="">手机号码 : </label>
                ${backUser.user_phone!}
            </div>
            <div class="form_group">
                <label for="">用户名 : </label>
                ${backUser.user_name!}
            </div>
            <div class="form_group">
                <label for="">新密码 : </label>
                <input type="password" id="password">
            </div>
            <div class="form_group">
                <label for="">确认密码 : </label>
                <input type="password" id="rePassword">
            </div>
            <div class="btn"><a href="javascript:next()">提交</a></div>
        </div>
    </div>
	<form id="form1" method="post" action="${bbs_url}xpabc/user/updateSuccess.htm">
		<input type="hidden" name="sign" value="success"/>
	</form>

</div>

</@cc.html_index_top>

<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
<script>
	
	function next(){
		var password = $.trim($('#password').val());
		var rePassword = $.trim($('#rePassword').val());
		 
		if(!is_exists(password)){
			ZENG.msgbox.show("密码不能为空", 1, 1000);
			return;
		}
		if(password!=rePassword){
			ZENG.msgbox.show("两次输入密码不一致", 1, 1000);
			return;
		}
			$.ajax({
     				type: "POST",
					url : '${bbs_url}xpabc/user/updatePassword',
    				data :{
    					password:password,
    					rePassword:rePassword,
    					username:"${backUser.user_name!}"
    				},
    				success : function(data){
    					var code = data.code;
    					var msg = data.msg;
    					if(code=="1111"){
    						ZENG.msgbox.show(msg, 1, 3000);
							return false;
    					}else{
    						$("#form1").submit();
    					}
    				}
     		});
	}
</script>
</@cc.html_js>