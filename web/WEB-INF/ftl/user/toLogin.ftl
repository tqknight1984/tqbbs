<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_top_noLogin>
</@cc.html_top_noLogin>
<div class="w990">
    <!--contentlogin-->
    <div class="bbs-logis">
        <h1>小胖爱编程——登录</h1>
        <div class="logis-pannel">
           <p class="fl logimg"><img src="${bbs_url}/images/sale_03.jpg" alt=""></p>
           <div class="fr logpannel">
             <dl>
                <dd class="disnone" id="error_login"><p class="error_style" id="error_text"></p></dd>
                <dd><label>账号：</label><input type="text" placeholder="请填写用户账号" id="bbs_user"/></dd>
                <dd style="margin-bottom:5px"><label>密码：</label><input type="password" placeholder="请正确填写密码" id="bbs_pwd"/></dd>
                <dd style="text-align:right;margin:0 10px;height:15px" >
                    <span class="ma-l110"><a href="${bbs_url}xpabc/user/toGetBackPassword.htm">忘记密码?</a></span>
                </dd>
                <dd style="margin-top:5px"><label></label><input type="button" value="登录" onclick="checkUser()"/></dd>
                <dd><p class="ma-l70">还没有通行证账号，<a href="${bbs_url}xpabc/user/toRegister.htm" target="_blank">立即注册</a></p></dd>
              </dl>
               <div class="otherlogin">
              </div>
           </div>
        </div>
        
    </div>
</div>
<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
	<script>
		$(function(){
			document.onkeydown = function(e){
				var ev = document.all ? window.event : e;
				if(ev.keyCode==13) {
					checkUser();
				}
			}
		}); 
		var fromUrl = "${fromUrl}";
   		function checkUser(){
        	var $userID=eval(document.getElementById('bbs_user')).value,
            $userPWD=eval(document.getElementById('bbs_pwd')).value,
            $userErr=document.getElementById('error_login'),
            $userP=document.getElementById('error_text');
            if($userID==''){
                $userErr.style.display="block";
                 $userP.innerHTML='用户名不能为空';
                 return;
            }else{
                if($userPWD==''){
                	$userErr.style.display="block";
                    $userP.innerHTML='密码不能为空';
                    return;
                }
            }
            $.ajax({
				type: "POST",
				url : '${bbs_url}xpabc/user/login',
    			data :{
    				username:$userID,
    				password:$userPWD
    			},
    			success : function(data){
    				var code = data.code;
    				var msg = data.msg;
    				if(code=="0000"){
    					if(fromUrl!=""){
    						location.href = fromUrl;
    					}else{
    						location.href = "${bbs_url}xpabc/userCenter/mine/1.htm";
    					}
    				}else{
    					ZENG.msgbox.show(msg, 1, 3000);
    				}
    			}
				
			});	
    	}
	</script>
	
</@cc.html_js>