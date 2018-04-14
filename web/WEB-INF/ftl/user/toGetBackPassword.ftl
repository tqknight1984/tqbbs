<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_index_top>
    <div class="content_pass">
        <div class="headline"><span class='headline_cont'>找回登录密码</span></div>
        <ul class="steps_bar steps_bar_dark cf">
            <li class="step step_current" style="z-index:2">
                <span class="step_num">1.</span>
                <span>确认账号</span>
                <span class="arrow_background"></span><span class="arrow_foreground"></span>
            </li>
            <li class="step" style="z-index:1">
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
                <input id="phone" type="text"><a href="javascript:getCode()" class='hqyzm' id="send">获取验证码</a>
            </div>
            <div class="form_group">
                <label for="">验证码 : </label>
                <input id="yzm" type="text" class='sm'>
            </div>
            <div class="btn"><a href="javascript:next()">下一步</a></div>
        </div>
    </div>


</div>

</@cc.html_index_top>

<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
<script>
	var t;
    var c = 60;
    var flag = true;
    		function getCode(){
				var phone = $.trim($('#phone').val());
				if(!(/^1[3,4,5,7,8]\d{9}$/.test(phone))){
					ZENG.msgbox.show("手机号格式有误", 1, 1000);
					return false;
				}
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
    						$("#send").attr("href","javascript:void(0)");
     						t=setInterval(timedCount, 1000);
     						flag = false;
    					}
    				}
     			});
			}
		function timedCount(){
			var text = c+"秒后可再次发送";
			$("#send").text(text);
			c=c-1;
			if(c<0){
				clearTimeout(t);
				c = 60;
				flag = true;
				$("#send").attr("href","javascript:getCode()");
     			$("#send").text("获取验证码");
			}
		}
		function next(){
			var phone = $.trim($('#phone').val()) ; 
			if(!(/^1[3,4,5,7,8]\d{9}$/.test(phone))){
				ZENG.msgbox.show("手机号格式有误", 1, 1000);
				return;
			}
			var yzm = $.trim($('#yzm').val()); 
			if(!is_exists(yzm)){
				ZENG.msgbox.show("请输入验证码", 1, 1000);
				return;
			}
			$.ajax({
     				type: "POST",
					url : '${bbs_url}xpabc/user/checkPhoneCode',
    				data :{
    					phone:phone,
    					yzm:yzm
    				},
    				success : function(data){
    					var code = data.code;
    					var msg = data.msg;
    					if(code=="1111"){
    						ZENG.msgbox.show(msg, 1, 3000);
							return false;
    					}else{
    						window.location="${bbs_url}xpabc/user/toUpdatePassword.htm";
    					}
    				}
     		});
		}
	
</script>
</@cc.html_js>