<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_top_noLogin>
</@cc.html_top_noLogin>
<!--contentlogin-->
<div class="US-wrap clearfix">

    <@cc.html_user_left type=2></@cc.html_user_left>

    <div class="US-right fl">

        <@cc.html_user_right_top type1=2 type2=4></@cc.html_user_right_top>

        <div class="US-cont">

            <form>
            <div class="cont-perfile">
                <ul>
                    <li><p>昵称：</p><label><input type="text" id="nickname" style="width:250px" value="${user.nickname!}"></label></li>
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
			var nickname = $.trim($("#nickname").val());
			$.ajax({
				type: "POST",
				url : '${bbs_url}xpabc/userCenter/changeNickname',
    			data :{
    				nickname:nickname
    			},
    			success : function(data){
    				var code = data.code;
    				var msg = data.msg;
    				if(code=="0000"){
    					ZENG.msgbox.show(msg, 4, 3000);
    					setTimeout('location.reload()',1000);
    				}else{
    					ZENG.msgbox.show(msg, 1, 3000);
    				}
    			}
				
			});
		}
		<@cc.logout_js>
		</@cc.logout_js>
	</script>
</@cc.html_js>