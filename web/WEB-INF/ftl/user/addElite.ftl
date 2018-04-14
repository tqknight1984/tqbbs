<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_top_noLogin>
</@cc.html_top_noLogin>
<!--contentlogin-->
<div class="US-wrap clearfix">

    <@cc.html_user_left type=5></@cc.html_user_left>

    <div class="US-right fl">

        <@cc.html_user_right_top type1=5 type2=1></@cc.html_user_right_top>
		<div class="search_wrap">
            <div class="search_box cf">
                <span class='tit'>搜索</span>
                <div class="search cf">
                    <form action="">
                        <input type="text" placeholder='请输入url' class='text1' id="search_url"><input type="button" id="search_btn" onclick="parseUrl()" value='搜索' class='btn'>
                    </form>
                </div>
                
            </div>   
            
        </div>
        <div class="It_theme_Cont">
            <div class="itCont_main w400">
                <div class="itCount_title">
                    <h3>
                        <span  id="title"></span><span class='wonderful' id="wonderful_icon" style="display:none">精</span>
                    </h3>
                </div>
                <ul class='itCont_mlist'>
                    <li>作者 : <span id="author"></span></li>
                    <li>时间 : <span id="add_time"></span></li>
                </ul>
            </div>
            <div class="btn_wonderful" id="add_btn" >
            	<input type="hidden" value="" id="a_id">
                <input type="button" value="添加精品" class="valbtn" disabled id="addElite" style="color:white"/>
            	<input type="button" value="删除精品" class="valbtn" disabled id="deleteElite" style="color:white"/>
            </div>                   
        </div>

    </div>



</div>
<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
	<script>
		$(function(){
     		$("#deleteElite").css("background-color","#BBB");
     		$("#addElite").css("background-color","#BBB");
     		$("#addElite").click(function(){
     			makeElite(1);
     		});
     		$("#deleteElite").click(function(){
     			makeElite(2);
     		});
		});
		function parseUrl(){
			$("#title").text("");
    		$("#author").text("");
    		$("#add_time").text("");
    		$("#a_id").val("");
    		$("#wonderful_icon").css("display","none");
			var url = $.trim($("#search_url").val());
			if(!is_exists(url)){
				alert("请输入文章链接");
				return;
			}
			$.ajax({
				type: "POST",
				url : '${bbs_url}xpabc/userCenter/parseUrlForAddElite',
    			data :{
    				url:url
    			},
    			success : function(data){
    				var code = data.code;
    				var msg = data.msg;
    				if(code=="0000"){
    					$("#title").text(data.title);
    					if(data.is_elite=="0"){
    						$("#wonderful_icon").css("display","none");
							$("#addElite").attr("disabled",false);
     						$("#addElite").css("background-color","#E66700");
    					}else if(data.is_elite=="1"){
    						$("#wonderful_icon").css("display","inline");
    						$("#deleteElite").attr("disabled",false);
     						$("#deleteElite").css("background-color","#E66700");
    					}
    					$("#author").text(data.author);
    					$("#add_time").text(data.add_time);
    					$("#a_id").val(data.article_id);
    				}else{
    					alert(msg);
    				}
    			}
				
			});
		}
		function makeElite(type){
			var id = $("#a_id").val();
			if(!is_exists(id)){
				id=0;
			}
			if(type==1){
				$("#addElite").attr("disabled",true);
     			$("#addElite").css("background-color","#BBB");
			}else if(type==2){
				$("#deleteElite").attr("disabled",true);
     			$("#deleteElite").css("background-color","#BBB");
			}
			$.ajax({
				type: "POST",
				url : '${bbs_url}xpabc/userCenter/makeElite',
    			data :{
    				type:type,
    				id:id
    			},
    			success : function(data){
    				var code = data.code;
    				var msg = data.msg;
    				alert(msg);
    				if(code=="0000"){
    					location.reload();
    				}else{
    					if(type==1){
							$("#addElite").attr("disabled",false);
     						$("#addElite").css("background-color","#E66700");
						}else if(type==2){
							$("#deleteElite").attr("disabled",false);
     						$("#deleteElite").css("background-color","#E66700");
						}
    				}
    			}
				
			});
		}	
		<@cc.logout_js>
		</@cc.logout_js>
		function is_exists(obj){
			obj = $.trim(obj);
			if(obj==""||obj==null||obj==undefined){
				return false;
			}else{
				return true;
			}
		}
	</script>
</@cc.html_js>