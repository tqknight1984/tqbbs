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
            <li class="step" style="z-index:1">
                <span class="step_num">2.</span>
                <span>修改密码</span>
                <span class="arrow_background"></span><span class="arrow_foreground"></span>
            </li>
            <li class="step step_current" style="z-index:0">
                <span class="step_num">3.</span>
                <span>完成</span>                
            </li>
        </ul>

        <div class="form_panle_passowrd">
            <div class="retrieve__title">
                <span class='tip_icon'></span><h3>修改密码成功</h3>
            </div>
            <div class="btn btn_c"><a href="javascript:login()">立即登录</a></div>

        </div>
    </div>


</div>


</@cc.html_index_top>

<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
<script>
	function login(){
		window.location = "${bbs_url}xpabc/user/toLogin.html";
	}
	
</script>
</@cc.html_js>