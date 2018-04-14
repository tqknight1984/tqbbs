<#import "../common.ftl" as cc>
<@cc.html_head>
<style>
.hot-news a{text-decoration: underline;}
</style>
</@cc.html_head>
<@cc.html_index_top>
</@cc.html_index_top>
<div class="index-con cf">
    <div class="con-left">
        <div class="news cf">
            <div class="focus fl">
                <#--<ul class="bxslider" id="bxslider">-->
                <#--<#if  hotImgs?? && hotImgs?size gt 0>-->
                    <#--<#list hotImgs as item>-->
                        <#--<li><img src="${bbs_url}images/${item.url}" title="${item.title}" /></li>-->
                    <#--</#list>-->
                <#--</#if>-->
                <#--</ul>-->
                    <a href="http://www.xpabc.com/xpabc/article/show/376.htm">
                        <img src="${bbs_url}images/000.jpg" />
                    </a>
            </div>
            <div class="hot">
                <h3 class='index-title bg2'>最近文章<a href="${bbs_url}xpabc/index/tobbsList/0/1.htm">更多</a></h3>
                <div class="hot-news">
                    <ul>
                     <#if hotarticleLists?? && hotarticleLists?size gt 0>
						<#list hotarticleLists as hotlist>
                        <li><a href="${bbs_url}xpabc/article/show/${hotlist.articleid!}.htm">${hotlist.article_title}</a></li>
                        </#list>
                     </#if> 
                     </ul>

                </div>
            </div>
        </div>
        <div class="ads"><a href="http://www.315che.com" target="_blank"><img src="${bbs_url}images/315ad.jpg" alt=""></a></div>
        <div class="modules cf">
        <#if articleLists?? && articleLists?size gt 0>
						<#list articleLists as list>
            <div class="module">
                <h3 class='index-title bg${list.id!}'>${list.platename!}<a href="${bbs_url}xpabc/index/tobbsList/${list.plateid!}/1.htm">更多</a> </h3>
                <ul>
                <#if  list.content?? && list.content?size gt 0>
                             <#list list.content as contentlist>
                    <li><a href="${bbs_url}xpabc/article/show/${contentlist.articleid!}.htm">${contentlist.article_title!}</a></li>
                            </#list>
                  </#if> 
                </ul>
            </div>
         </#list>
		</#if>
        </div>
    </div>
   <div class="con-right">
   		<div class="con-right-box">
        	<h3 class="index-title  bg3">编程达人<a href="${bbs_url}xpabc/user/userList.htm">更多</a></h3>
            <ul class='con-right-list con-right-list2'>
            	<#list userList as u>
            		<li><i class="num num${u_index+1}">${u_index+1}</i><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm"><img src="<#if u??&&u.user_photo??&&u.user_photo!="">${bbs_img}${u.user_photo}<#else>${bbs_url}images/noavatar_medium.gif</#if>" alt="" width="50" height="50"></a><span class="name"><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm"><#if u.user_nickname??&&u.user_nickname!="">${u.user_nickname}<#else>${u.user_name}</#if></a></span><p><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm">文章数 (${u.articleSize})</a><a href='javascript:chatTo("${u.user_id}","${u.user_name}","<#if u??&&u.user_photo??&&u.user_photo!="">${bbs_img}${u.user_photo}<#else>${bbs_url}images/noavatar_medium.gif</#if>");' class='chat-line chat-online'></a></p></li>
            		<#if u_index==9><#break></#if>
            	</#list>
            </ul>
        </div>
        <div class="con-right-box">
            <h3 class="index-title  bg2">推荐精华</h3>
            <ul class='con-right-list' style="height:355px">
               <#if jinghuaLists?? && jinghuaLists?size gt 0>
						<#list jinghuaLists as jinghualist>
                        <li><a href="${bbs_url}xpabc/article/show/${jinghualist.articleid!}.htm">${jinghualist.article_title}</a></li>
                        </#list>
                </#if> 
            </ul>
        </div>
    </div>
</div>      

  <@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
<script>
      $('.bxslider').bxSlider({
        mode: 'fade',
        captions: true,
        auto: true 
    	});

    $(document).ready(function(){
        $('h3#letterseq a').on('click',function(){
            $this=$(this).index();
             $(this).addClass('cur').siblings().removeClass('cur');
             $('.pf_blist .lettershow').eq($this).show().siblings().hide();
        })
    })
</script>
</@cc.html_js>