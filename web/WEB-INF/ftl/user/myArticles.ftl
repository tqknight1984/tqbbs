<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_top_noLogin>
</@cc.html_top_noLogin>
<!--contentlogin-->
<div class="US-wrap clearfix">

    <@cc.html_user_left type=4></@cc.html_user_left>

    <div class="US-right fl">

        <@cc.html_user_right_top type1=4 type2=1></@cc.html_user_right_top>

        <div class="It_theme_Cont">
		<#if (articleSize >0)>
			<#list articles as a>
			<div class="itCont_main itCont_main_1">
                <div class="itCount_title">
                    <h3>
                        <a href="${bbs_url}xpabc/article/show/${a.article_id}.htm">${a.article_title}</a>
                        <a href="javascript:deleteAriticle(${a.article_id})" class='post-a' style="padding: 0px 10px;color:white">删除</a>
                        <a href="${bbs_url}xpabc/article/edit/${a.article_id}.htm" class='post-a' style="padding: 0px 10px;color:white;margin-right:3px">编辑</a>
                    </h3>
                    <div class="categories">
                        <strong>分类</strong>
                        <a href="${bbs_url}xpabc/index/tobbsList/${a.plate_id}/1.htm">${a.plate_name}</a>
                    </div>
                </div>
                <div class="itCont_content">
                	 ${a.article_content}
                </div>
                <div class="itCont_bottom">
                    <ul class='cf'>
                        <li>${a.add_time!}</li>
                        <li>浏览${a.lookCount!}</li>
                        <li class='last'>评论(${a.replyCount!})</li>
                    </ul>
                </div>
            </div>
            </#list>
            <div class="page-num">
                <div>
                    	<a href="javascript:toPage(1)">首页</a>
                    	<a href="javascript:toPage(${page+1})">上一页</a>
                    	<#list 1..pages as p> 
                    		<a href="javascript:toPage(${p})" <#if p==page>class="current"</#if>>${p}</a>
                    	</#list>
                    	
                    	<a href="javascript:toPage(${page+1})">下一页</a><a href="javascript:toPage(${page+1})">尾页</a>
            	</div>
            </div>
		<#else>
			<div class="contNone">
                <div>暂时没有内容</div>
            </div>	
		</#if>

        </div>

    </div>



</div>
<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
	<script>
		function toPage(page){
			var pages = ${pages};
			if(page>pages){
				page = pages;
			}
			if(page<=0){
				page = 1;
			}
			location.href = "${bbs_url}xpabc/userCenter/myArticles/"+page+".htm";
		}
		function deleteAriticle(id){
			qikoo.dialog.confirm('确定要删除吗？',function(){
				$.ajax({
					type: "POST",
					url : '${bbs_url}xpabc/userCenter/deleteArticle',
    				data :{
    					article_id:id
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
        	},function(){
        		
			});
		}
		<@cc.logout_js>
		</@cc.logout_js>
	</script>
</@cc.html_js>