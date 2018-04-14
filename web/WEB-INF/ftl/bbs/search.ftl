<#import "../common.ftl" as cc>
<@cc.html_head>
<style>
.current{
color:red !important;
}
</style>
</@cc.html_head>
<@cc.html_top>
</@cc.html_top>
<div class="bbs-search-box">
    <div class="b-s-wrap">
        <div class="b-s-logo">
            <span>搜索</span>
        </div>
        <div class="search-box clearfix">
    
              <form action="${bbs_url}xpabc/search/toSearch" id="searchform1" method="get">
                <div class="s-inp-wrap">
                    <input type="text" name="ksWord" <#if ksWord??>value="${ksWord}"
							<#else>
							value=""
						</#if> maxlength="50" id="txtkeyword" class="s-inp-text-box">
                </div>
                <input type="button" value="搜索" class="input-orange s-inp-btn" onclick="$('#searchform1').submit()">
            </form>
    
        </div>
    </div>
</div>
<div class="wrap">
    <div id="topic_list" class="ma-t10 bt1">
        <form method="post" action="#">
            <table cellspacing="0" cellpadding="0" class="data_table">
                <thead>
                    <tr class="tr_s">
                        <td colspan="2" class="title">版块主题</td>
                        <td class="sign plate">版块</td>
                        <td class="author">作者</td>
                    </tr>
                </thead>
                <#if list?? && list?size gt 0>
						<#list list as list>
						<tbody>
                        <tr onmouseout="style.backgroundColor=''" onmouseover="style.backgroundColor='#f6fbff'">
                        <td class="folder">
                            <a href="#" title="全站置顶" class="a-hot"></a>
                        </td>
                        <th class="title">
                            <span class="checkbox_title">
                                 <a href="${bbs_url}xpabc/article/show/${list.id}.htm" class="topicurl blue bold">${list.title}</a>
                            </span>
                        </th>
                        <td class="sign plate">
                            <a href="#">${list.platename}</a>
                        </td>
                        <td class="author">
                            <cite>
                                <a href="${bbs_url}xpabc/index/tobbsUserList/0/${list.userid!}/1.htm" target="_blank" class="user-online">${list.username}</a>
                            </cite>
                            <em>${list.addtime}</em>
                        </td>
                    </tr>
                </tbody>
						</#list>
				</#if>
                
               
            </table>
        </form> 
    </div>
<div class="forum_pages s_clear">
         <span class="page_back" id="page_back_bottom"><a href="javascript:toPage(1,<#if ksWord??>'${ksWord}'
							<#else>
							""
						</#if>)">首页</a></span>
		<span class="page_back" id="page_back_bottom"><a href="javascript:toPage(${page-1},<#if ksWord??>'${ksWord}'
							<#else>
							""
						</#if>)">上一页</a></span>
         <div class="pager">
         
         <#if pages gt 9>
          <#if pages-page gt 9>
               <#list page..(page+4) as p> 
                  <a href="javascript:toPage(${p},<#if ksWord??>'${ksWord}'
							<#else>
							""
						</#if>)" <#if p==page>class="current"</#if>>${p}</a>
              </#list>
              .......
              <#list (pages-4)..pages as p> 
                  <a href="javascript:toPage(${p},<#if ksWord??>'${ksWord}'
							<#else>
							""
						</#if>)" <#if p==page>class="current"</#if>>${p}</a>
              </#list>
           <#else>
               <#list (pages-9)..pages as p> 
                  <a href="javascript:toPage(${p},<#if ksWord??>'${ksWord}'
							<#else>
							""
						</#if>)" <#if p==page>class="current"</#if>>${p}</a>
              </#list>
          
         </#if>
         <#else>
	             <#list 1..pages as p> 
	                  <a href="javascript:toPage(${p},<#if ksWord??>'${ksWord}'
							<#else>
							""
						</#if>)" <#if p==page>class="current"</#if>>${p}</a>
	              </#list>
	          
	      </#if>   
           
            <a class="next" href="javascript:toPage(${page+1},<#if ksWord??>'${ksWord}'
							<#else>
							""
						</#if>)">下一页</a>
            <a  href="javascript:toPage(${pages},<#if ksWord??>'${ksWord}'
							<#else>
							""
						</#if>)">尾页</a>
         
         
         
         
        </div>
    </div>

<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
<script>
$(function(){
    //主题分类 hover 滑出菜单
    /*if (!$("#subjectClass>a").hasClass('current')){
        $("#subjectClass").hover(function(){
            $("#subjectClass_con").show();
            $("#subjectClass>a").addClass("focus");
        },function(){
            $("#subjectClass_con").hide();
            $("#subjectClass>a").removeClass("focus");
        })
    }else{
        $("#subjectClass").hover(function(){
            $("#subjectClass_con").show();
        },function(){
            $("#subjectClass_con").hide();
        })
    }*/
    //发帖按钮 hover 更多选择菜单
    /*$(".btn_post").hover(function(){
        $(this).children("a").next().show();
    },function(){
        $(this).children("a").next().hide();
    });*/
});
    function toPage(page,ksWord){
			var pages = ${pages};
			if(page>pages){
				page = pages;
			}
			if(page<=0){
				page = 1;
			}
			
			location.href = "${bbs_url}xpabc/search/toSearch?page="+page+"&&ksWord="+ksWord;
		}
</script>
</@cc.html_js>