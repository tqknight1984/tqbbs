<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_top_noLogin>
</@cc.html_top_noLogin>
<!--contentlogin-->
<div class="US-wrap clearfix">

    <@cc.html_user_left type=4></@cc.html_user_left>

    <div class="US-right fl">

        <@cc.html_user_right_top type1=4 type2=2></@cc.html_user_right_top>

        <div class="US-cont">
		<#if (commentSize >0)>
            <div class="contentdiv">
                <ul>
                	<li class="cont-th"><i class="w4">标题</i><i class="w2">所在版块</i><i class="w2">作者</i><i class="w2">最后发表</i></li>
                	<#list comments as c>
                    	 <li class="cont-td"><i class="w4"><a href="${bbs_url}xpabc/article/show/${c.article_id}.htm">${c.article_title}</a></i><i class="w2"><a href="${bbs_url}xpabc/index/tobbsList/${c.plate_id}/1.htm">${c.plate_name}</a></i><i class="w2"><a href="javascript:void(0)">${c.article_user_name}</a></i><i class="w2">${c.last_time}<br />by <a href="${bbs_url}xpabc/index/tobbsUserList/0/${c.last_user_id}/1.htm">${c.last_user_name}</a></i></li>
                    </#list>
                </ul>
            </div>
            <div class="page-num">
                <ul>
                    <li>
                    	<a href="javascript:toPage(1)">首页</a>
                    	<a href="javascript:toPage(${page+1})">上一页</a>
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
			location.href = "${bbs_url}xpabc/userCenter/myComments/"+page+".htm";
		}
		<@cc.logout_js>
		</@cc.logout_js>
	</script>
</@cc.html_js>