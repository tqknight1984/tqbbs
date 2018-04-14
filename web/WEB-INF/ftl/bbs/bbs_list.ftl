<#import "../common.ftl" as cc>
<@cc.html_head>
<style>
.current{
color:red !important;
}
</style>
</@cc.html_head>
<@cc.html_index_top>
</@cc.html_index_top>
<div class="wrap">
    <div class="category-wrap">
       <div id="category" class="w990 center clearfix pt10 relative bgWrite">
       
           <ul class="fl category_list">
               <li><a href="${bbs_url}xpabc/index/tobbsList/0/1.htm" >全部帖子</a></li>
               <li id="subjectClass" class="relative">
                   <a href="javascript:void(0)" class="">主题分类<em class="slide_down"></em></a>
                   <ul id="subjectClass_con" class="absolute" style="display:none;">
                   <#if plateList?? && plateList?size gt 0>
						<#list plateList as list>
						<li><a href="${bbs_url}xpabc/index/tobbsList/${list.plate_id!}/1.htm">${list.plate_name!}</a></li>
						</#list>
					</#if>
                   </ul>
               </li>
           </ul>
       </div>
    </div>
    <div id="topic_list" style="clear:both;">
        <form method="post" action="#">
            <table cellspacing="0" cellpadding="0" class="data_table">
                <thead>
                    <tr class="tr_s">
                        <td colspan="2"></td>
                        <td class="sign plate">版块</td>
                        <td class="author">作者</td>
                        <td class="nums">回复/查看</td>
                        <td class="lastpost">最后发表</td>
                    </tr>
                </thead>
                <tbody>
                <#if articleLists?? && articleLists?size gt 0>
						<#list articleLists as list>
							 <tr onmouseout="style.backgroundColor=''" onmouseover="style.backgroundColor='#f6fbff'">
	                        <td class="folder">
	                        <#if list.article_order?? && list.article_order!="0">
	                            <a href="#" title="全站置顶" class="a-top-all"></a>
	                          </#if>
	                         <#if list.is_elite?? && list.is_elite=="1">
	                            <a href="#" title="全站置顶" class="a-pick1"></a>
	                          </#if>
	                          <#if list.is_elite?? && list.article_order?? && list.article_order=="0" && list.is_elite=="0">
	                            <a href="#" title="普通贴" class="a-hot"></a>
	                          </#if>
	                        </td>
	                        <th class="title">
	                        <span class="checkbox_title">
	                             <a href="${bbs_url}xpabc/article/show/${list.article_id!}.htm" class="blue bold">${list.article_title!}</a>
	                        </span>
	                        </th>
	                        <td class="sign plate">
	                            <a href="#">${list.platename!}</a>
	                        </td>
	                        <td class="author">
	                            <cite>
	                                <a href="${bbs_url}xpabc/index/tobbsUserList/0/${list.userid!}/1.htm" target="_blank" class="user-online">${list.username!}</a> 
	                            </cite>
	                            <em>${list.add_time!}</em>
	                        </td>
	                        <td class="nums">
	                            <cite><strong>${list.backcount!}</strong></cite>
	                             <em>${list.article_lookcount!}</em>
	                        </td>
	                        <td class="lastpost">
	                            <cite>
	                                <a href="${bbs_url}xpabc/index/tobbsUserList/0/${list.backuserid!}/1.htm" target="_blank" class="user-online">${list.backname!}</a>
	                            </cite>
	                            <em>${list.backtime!}</em>
	                        </td>
	                    </tr>
						</#list>
					</#if>
                   
                </tbody>
                
            </table>
        </form> 
    </div>
<div class="forum_pages s_clear">
        <span class="page_back" id="page_back_bottom"><a href="javascript:toPage(1,${plateid})">首页</a></span>
        <span class="page_back" id="page_back_bottom"><a href="javascript:toPage(${page-1},${plateid})">上一页</a></span>
        <div class="pager">
        <#if pages gt 9>
	        <#if pages-page gt 9>
	               <#list page..(page+4) as p> 
	                  <a href="javascript:toPage(${p},${plateid})" <#if p==page>class="current"</#if>>${p}</a>
	              </#list>
	              .......
	              <#list (pages-4)..pages as p> 
	                  <a href="javascript:toPage(${p},${plateid})" <#if p==page>class="current"</#if>>${p}</a>
	              </#list>
	           <#else>
	               <#list (pages-9)..pages as p> 
	                  <a href="javascript:toPage(${p},${plateid})" <#if p==page>class="current"</#if>>${p}</a>
	              </#list>
	          
	         </#if>
	        <#else>
	             <#list 1..pages as p> 
	                  <a href="javascript:toPage(${p},${plateid})" <#if p==page>class="current"</#if>>${p}</a>
	              </#list>
	          
	      </#if>
          
           
            <a class="next" href="javascript:toPage(${page+1},${plateid})">下一页</a>
            <a  href="javascript:toPage(${pages},${plateid})">尾页</a>
        </div>
        <div class="fr">
            <div class="btn_post btn_bottom">
                <a href="${bbs_url}xpabc/article/add.htm">发表文章</a>
            </div>
        </div>
    </div></div>
    <@cc.html_js>
<script>
$(function(){
    //主题分类 hover 滑出菜单
    if (!$("#subjectClass>a").hasClass('current')){
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
    }
    //发帖按钮 hover 更多选择菜单
    $(".btn_post").hover(function(){
        $(this).children("a").next().show();
    },function(){
        $(this).children("a").next().hide();
    });
});
function toPage(page,plateid){
			var pages = ${pages};
			if(page>pages){
				page = pages;
			}
			if(page<=0){
				page = 1;
			}
			location.href = "${bbs_url}xpabc/index/tobbsList/"+plateid+"/"+page+".htm";
		}
</script>
</@cc.html_js>
<@cc.html_foot>
</@cc.html_foot>