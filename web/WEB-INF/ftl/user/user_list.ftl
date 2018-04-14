<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_index_top>
    <div class="index-con cf">

        <div class="con-right1 phlist" style="width:100%;">
           <div class="con-right-box">
                <h3 class="index-title  bg3">排行榜</h3>
                <ul class='con-right-list con-right-list2' style="float:left;width:33%;padding:0;">
                	<#list list1 as u>
                		<li><i class="num num7">${(u_index+1)*3-2}</i><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm"><img src="<#if u??&&u.user_photo??&&u.user_photo!="">${bbs_img}${u.user_photo}<#else>${bbs_url}images/noavatar_medium.gif</#if>" alt="" width="50" height="50"></a><span class="name"><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm"><#if u.user_nickname??&&u.user_nickname!=""&&!is_inner>${u.user_nickname}<#else>${u.user_name}</#if></a></span><p><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm">文章数 (${u.articleSize})<a href='javascript:chatTo("${u.user_id}","${u.user_name}","<#if u??&&u.user_photo??&&u.user_photo!="">${bbs_img}${u.user_photo}<#else>${bbs_url}images/noavatar_medium.gif</#if>");' class='chat-line chat-online'></a></p></li>
                	</#list>
                </ul>
                <ul class='con-right-list con-right-list2' style="float:left;width:33%;padding:0;">
					<#list list2 as u>
                		<li><i class="num num7">${(u_index+1)*3-1}</i><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm"><img src="<#if u??&&u.user_photo??&&u.user_photo!="">${bbs_img}${u.user_photo}<#else>${bbs_url}images/noavatar_medium.gif</#if>" alt="" width="50" height="50"></a><span class="name"><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm"><#if u.user_nickname??&&u.user_nickname!=""&&!is_inner>${u.user_nickname}<#else>${u.user_name}</#if></a></span><p><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm">文章数 (${u.articleSize})<a href='javascript:chatTo("${u.user_id}","${u.user_name}","<#if u??&&u.user_photo??&&u.user_photo!="">${bbs_img}${u.user_photo}<#else>${bbs_url}images/noavatar_medium.gif</#if>");' class='chat-line chat-online'></a></p></li>
                	</#list>                   
                </ul>
                <ul class='con-right-list con-right-list2' style="float:left;width:33%;padding:0;">
                	<#list list3 as u>
                		<li><i class="num num7">${(u_index+1)*3}</i><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm"><img src="<#if u??&&u.user_photo??&&u.user_photo!="">${bbs_img}${u.user_photo}<#else>${bbs_url}images/noavatar_medium.gif</#if>" alt="" width="50" height="50"></a><span class="name"><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm"><#if u.user_nickname??&&u.user_nickname!=""&&!is_inner>${u.user_nickname}<#else>${u.user_name}</#if></a></span><p><a href="${bbs_url}xpabc/index/tobbsUserList/0/${u.user_id!}/1.htm">文章数 (${u.articleSize})<a href='javascript:chatTo("${u.user_id}","${u.user_name}","<#if u??&&u.user_photo??&&u.user_photo!="">${bbs_img}${u.user_photo}<#else>${bbs_url}images/noavatar_medium.gif</#if>");' class='chat-line chat-online'></a></p></li>
                	</#list>
                </ul>
            </div>
        </div>
        

        
    </div>


</div>
</@cc.html_index_top>

<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
</@cc.html_js>