<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_top_noLogin>
</@cc.html_top_noLogin>
<div class="w990">
    <!--contentlogin-->
    <div class="bbs-logis">
        <h1>小胖爱编程——新用户注册</h1>
        <div class="logis-pannel">
           <div class="logpannel" style="border:none" id="register-pannel">
             <dl id="email-register">
                <dd id="emailfirst">
                  <label>用&nbsp;&nbsp;户&nbsp;&nbsp;名：</label>
                  <input type="text" placeholder="请输入用户名" minlength="6" maxlength="18" id="usernameval"/>
                </dd>
                <dd class="disnone" id="first-error">
                  <p class="error_style"></p>
                </dd>
                <dd><label>密&nbsp;&nbsp;&nbsp;&nbsp;码：</label><input type="password" placeholder="请记住密码哦" minlength="6" id="pwdval"/></dd>
                <dd class="disnone" id="pwd-error"><p class="error_style"></p></dd>
                <dd><label>密码确认：</label><input type="password" placeholder="请再次输入密码哦" id="again_pwdval"/></dd>
                <dd class="disnone" id="pwd-againerr"><p class="error_style"></p></dd>
                <dd id="phone">
                  <label>手&nbsp;&nbsp;机&nbsp;&nbsp;号：</label>
                  <input type="text" placeholder="请正确填写手机号" id="phoneval" maxlength="11"/>
                </dd>
                <dd class="disnone" id="phone-error">
                  <p class="error_style"></p>
                </dd>
                 <dd id="phonecode"><label>激&nbsp;活&nbsp;码：</label>
                  <input type="text" style="width:100px;" id="codeval"/>
                  <input type="button" value="免费获取短信验证码" class="valbtn" disabled id="code_btn"/>
                </dd>
                <dd class="disnone" id="last-error">
                  <p class="error_style"></p>
                </dd>
                <dd><input type="button" value="同意以下协议并注册" style="letter-spacing:1px;width: 50%;margin-left:75px;margin-top:10px" id="registerbtn"/></dd>
              </dl>
                <textarea readonly="readonly" class="treaty_register">  小胖爱编程用户服务协议

                欢迎您加入小胖爱编程参加交流和讨论，小胖爱编程为公共社区，为维护网上公共秩序和社会稳定，请您自觉遵守以下条款:

                一、不得利用本站危害国家安全、泄露国家秘密，不得侵犯国家社会集体的和公民的合法权益，不得利用本站制作、复制和传播下列信息: 
                （一）煽动抗拒、破坏宪法和法律、行政法规实施的；
                （二）煽动颠覆国家政权，推翻社会主义制度的；
                （三）煽动分裂国家、破坏国家统一的；
                （四）煽动民族仇恨、民族歧视，破坏民族团结的；
                （五）捏造或者歪曲事实，散布谣言，扰乱社会秩序的；
                （六）宣扬封建迷信、淫秽、色情、赌博、暴力、凶杀、恐怖、教唆犯罪的；
                （七）公然侮辱他人或者捏造事实诽谤他人的，或者进行其他恶意攻击的；
                （八）损害国家机关信誉的；
                （九）其他违反宪法和法律行政法规的；
                （十）进行商业广告行为的。

                二、互相尊重，对自己的言论和行为负责。
                </textarea>
              </dl>
           </div>
           <div class="disnone suc_register">恭喜您，成功注册成为小胖爱编程的会员，愿你旅途愉快！</div>
           <div class="hasother">
            </div>
        </div>
    </div>
    </div>
</div>
<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
	<script src="${bbs_url}/js/bbsgis.js"></script>
	<script>
		$("#code_btn").css("background-color","#BBB");
		var t;
     	var c = 60;
     	var flag = true;
		$(function(){
			document.onkeydown = function(e){
				var ev = document.all ? window.event : e;
				if(ev.keyCode==13) {
					$("#registerbtn").click();
				}
			}
			$("#code_btn").click(function(){
				var phone = $("#phoneval").val();
     			$.ajax({
     				type: "POST",
					url : '${bbs_url}xpabc/user/getPhoneCode',
    				data :{
    					phone:phone
    				},
    				success : function(data){
    					var code = data.code;
    					var msg = data.msg;
    					if(code=="1111"){
    						ZENG.msgbox.show(msg, 1, 3000);
							return false;
    					}else{
    						$("#code_btn").attr("disabled",true);
     						$("#code_btn").css("background-color","#BBB");
     						t=setInterval(timedCount, 1000);
     						flag = false;
    					}
    				}
     			});
			});
		}); 
		function timedCount(){
			var text = c+"秒后可再次发送";
			$("#code_btn").val(text);
			c=c-1;
			if(c<0){
				clearTimeout(t);
				c = 60;
				flag = true;
				$("#code_btn").attr("disabled",false);
     			$("#code_btn").css("background-color","#E66700");
     			$("#code_btn").val("免费获取短信验证码");
			}
		}
		function checkUsername(){
			$("#usernameval").css("border-color","#DDD");
			var username = $.trim($('#usernameval').val()) ; 
			if(username == ''){
				$('#first-error').show();
				$("#first-error p").text('用户名不能为空');
				return false;
			}else{
				if(!(/(^[a-zA-Z0-9]{6,20}$)|(^[\u4e00-\u9fa5]{1,4}$)/.test(username))){
					$('#first-error').show();
					$("#first-error p").text('6-20位的数字、字母或1-4位中文汉字');
					return false;
				}
				$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/user/checkUsername',
    				data :{
    					username:username
    				},
    				success : function(data){
    					var code = data.code;
    					var msg = data.msg;
    					if(code=="1111"){
    						$('#first-error').show();
							$("#first-error p").text(msg);
							return false;
    					}else{
    						$("#usernameval").css("border-color","#66FF33");
    					}
    				}
				
				});
				return true;
			}		

		};
function checkPwd2(){
/*	var pwdRegx=;*/
	var pwdval=$.trim($('#pwdval').val());
	if(pwdval.length<6){
		$('#pwd-error').show();
		$('#pwd-error p').css('width','90%').text('密码至少6位数字、字母或者符号，建议混合使用。');
		return false;
	}
	return true;
};
function checkAgpwd2(){
	var pwdval=$.trim($('#pwdval').val());
	var againPwd=$.trim($('#again_pwdval').val());
	if(againPwd!=pwdval ||againPwd==''){
		$('#pwd-againerr').show();
		$('#pwd-againerr p').text('两次密码不一致，请谨慎填写哦。');
		return false;
	}
	return true;
};		
   		$('#registerbtn').on('click',function(){
   				if(!checkUsername()){
   					return;
   				}
   				if(!checkPwd2()){
   					return;
   				}
   				if(!checkAgpwd2()){
   					return;
   				}
				var username = $.trim($('#usernameval').val()) ;
				var password=$.trim($('#pwdval').val());
				var password2=$.trim($('#pwdval').val());
				var phone = $("#phoneval").val();
				var code = $("#codeval").val();
				$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/user/register',
    				data :{
    					username:username,
    					password:password,
    					password2:password2,
    					phone:phone,
    					code:code
    				},
    				success : function(data){
    					var code = data.code;
    					var msg = data.msg;
    					if(code=="0000"){
    						//$('#register-pannel').hide();
        					//$('.suc_register').show();
        					ZENG.msgbox.show(msg, 4, 3000);
        					setTimeout('location.href="${bbs_url}xpabc/userCenter/mine/1.htm"',1000);
        					
    					}else{
    						ZENG.msgbox.show(msg, 1, 3000);
    						return;
    					}
    				}
				
				});
     	})
     	function phoneCheck(){
     		$("#code_btn").attr("disabled",true);
     		$("#code_btn").css("background-color","#BBB");
     		$("#phoneval").css("border-color","#DDD");
			var phone = $.trim($('#phoneval').val()) ; 
			if(phone == ''){
				$('#phone-error').show();
				$("#phone-error p").text('手机号不能为空');
			}else{
				if(!(/^1[3,4,5,7,8]\d{9}$/.test(phone))){
					$('#phone-error').show();
					$("#phone-error p").text('手机号有误');
					return false;
				}
				$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/user/checkPhone',
    				data :{
    					phone:phone
    				},
    				success : function(data){
    					var code = data.code;
    					var msg = data.msg;
    					if(code=="1111"){
    						$('#phone-error').show();
							$("#phone-error p").text(msg);
							return false;
    					}else{
    						if(flag){
    							$("#phoneval").css("border-color","#66FF33");
    							$("#code_btn").attr("disabled",false);
    							$("#code_btn").css("background-color","#E66700");
    						}
    					}
    				}
				
				});
			}
     	}
	</script>	
	
</@cc.html_js>