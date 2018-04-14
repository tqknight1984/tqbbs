<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_top_noLogin>
</@cc.html_top_noLogin>
<!--contentlogin-->
<div class="US-wrap clearfix">

    <@cc.html_user_left type=2></@cc.html_user_left>

    <div class="US-right fl">

        <@cc.html_user_right_top type1=2 type2=2></@cc.html_user_right_top>

        <div class="US-cont">

            <form>
            <div class="cont-perfile">
                <ul>
                    <li><p>原始密码：</p><label><input type="password" id="password" placeholder="原始密码" style="width:250px"></label></li>
                    <li><p>新密码：</p><label><input type="password" id="newPassword" placeholder="请输入新密码" style="width:250px"></label></li>
                    <li><p>重复新密码：</p><label><input type="password" id="password2" placeholder="请再次输入新密码" style="width:250px"></label></li>
                    <li><p>&nbsp;</p><a href="javascript:change()">确定并保存</a></li>
                </ul>
            </div>
            </form>

        </div>

    </div>



</div>
<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
	<script>
		function change(){
			var password = $.trim($("#password").val());
			var newPassword = $.trim($("#newPassword").val());
			var password2 = $.trim($("#password2").val());
			if(password==""){
				alert("原密码不能为空!");
				return;
			}
			if(newPassword==""){
				alert("新密码密码不能为空!");
				return;
			}
			if(newPassword.length<6){
				alert("新密码长度不能小于6位!");
				return;
			}
			if(password2==""||password2!=newPassword){
				alert("两次密码不一致!");
				return;
			}
			$.ajax({
				type: "POST",
				url : '${bbs_url}xpabc/userCenter/changePassword',
    			data :{
    				password:password,
    				newPassword:newPassword,
    				password2:password2
    			},
    			success : function(data){
    				var code = data.code;
    				var msg = data.msg;
    				alert(msg);
    				if(code=="0000"){
    					location.reload();	
    				}
    			}
				
			});
		}
		<@cc.logout_js>
		</@cc.logout_js>
	</script>
</@cc.html_js>