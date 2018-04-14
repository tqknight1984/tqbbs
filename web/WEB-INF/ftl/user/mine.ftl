<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_top_noLogin>
</@cc.html_top_noLogin>
<!--contentlogin-->
<div class="US-wrap clearfix">

    <@cc.html_user_left type=1></@cc.html_user_left>

    <div class="US-right fl">

        <div class="US-title">
            <h2>用户中心</h2>
        </div>

        <div class="US-cont">
		<#if commentSize gt 0>
            <div class="contentdiv">
                <ul>
                	<#list comments as c>
                    	<li class="cont-td"><i class="w1"><a href="#">${c.comment_user_name}</a> 给您评论, <a href="${bbs_url}xpabc/article/show/${c.article_id}.htm">${c.article_title}</a></i><i class="w2">${c.add_time}</i></li>
                    </#list>
                </ul>
            </div>
            <div class="page-num">
                <ul>
                    <li>
                    	<a href="javascript:toPage(1)">首页</a>
                    	<a href="javascript:toPage(${page-1})">上一页</a>
                    	<#list 1..pages as p> 
                    		<a href="javascript:toPage(${p})" <#if p==page>class="current"</#if>>${p}</a>
                    	</#list>
                    	
                    	<a href="javascript:toPage(${page+1})">下一页</a><a href="javascript:toPage(${page+1})">尾页</a>
                    </li>
                </ul>
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
			location.href = "${bbs_url}xpabc/userCenter/mine/"+page+".htm";
		}
		<@cc.logout_js>
		</@cc.logout_js>
	</script>
</@cc.html_js>