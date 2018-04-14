<#import "../common.ftl" as cc>
<@cc.html_head>
<link rel="stylesheet" href="${bbs_url}js/kindeditor-4.1.10/themes/default/default.css" />
<link rel="stylesheet" href="${bbs_url}js/kindeditor-4.1.10/plugins/code/prettify.css" />
</@cc.html_head>
<@cc.html_top>
</@cc.html_top>

<style>
	.txt{width:780px;}
	.lang-html{background:#E4E4E4;}
</style>

<!--contentlogin-->
<div class="wrap">
    <div class="forum_pages s_clear">
    	
        
        <div class="fr">
        	<div class="searchContainer" style="width:240px;">
	            <input id="key" name="ksWord" type="text" placeholder="请输入搜索关键字"><a class="searchbtn" id="searchbtn" ></a>  
	        </div>
        </div>
    </div>
    <div class="main viewthread">
        <div class="contain">
            <table>
                <tbody>
                    <tr>
                        <td class="postauthor">
                            <div class="hm">
                                <span class="xg1">查看:</span>
                                <span class="xi1">${artMap.lookCount!}</span>
                                <span class="pipe">|</span>
                                <span class='xg1'>回复:</span>
                                <span class="xi1">${artMap.replyCount!}</span>
                            </div>
                        </td>
                        <td class="posttoppci">
                            <h2 class="ts">${artMap.article_title}</h2>
                        </td>
                        <td>
                        	<div class="btn_post btn_bottom" style="float:right; height:35px; line-height:35px; margin-top:8px; margin-right:5px;">
					            <a class="xpabc_fatie" href="${bbs_url}xpabc/article/add.htm">发帖</a>
					        </div>
                        </td>
                    </tr>
                    <tr class="ad">
                        <td class="postauthor"></td>
                        <td class="posttoppci"></td>
                        <td class="posttoppci"></td>
                    </tr>
                </tbody>
            </table>
            <table>
                <tbody>
                    <tr>
                        <td class="postauthor">
                            <div class="auth">
                                <div class="auti">
                                    <a href="#" target="_blank">${artMap.article_username!}</a>
                                </div>
                            </div>
                            <div class="geren">
                                <div class="man">
                                    <a href="#"><img src="${artMap.article_userphoto!}" alt="" class="photo"></a>
                                </div>
                            </div>
                            <div class="user-aten">
                                <ul>
                                    <li><span><a href="#">0</a></span>粉丝</li>
                                    <li><span><a href="#">0</a></span>精华</li>
                                    <li class="last"><span><a href="#">${artMap.article_tcount!}</a></span>帖子</li>
                                </ul>
                            </div>
                            <dl class="otherinfo">
                                <dt>积分</dt>
                                <dd>0</dd>
                                <dt>性别</dt>
                                <dd>保密</dd>
                                <dt>注册时间</dt>
                                <dd>${artMap.article_userRegtime?date}</dd>
                            </dl>
                        </td>
                        <td class="postcontent">
                            <div class="pi">
                                <div class="pti">
                                    <div class="authi">
                                        <span>发表于 ${artMap.add_time?date}</span>
                                        <span class='pipe'>|</span>
                                        <a href="#">只看该楼主</a>
                                    </div>
                                    <div class="post-floor">
                                        <em>楼主</em>
                                    </div>
                                </div>
                            </div>
                            <div class="postmessage defaultpost">
                                <div class="t-msgfont">
                                    <div class="firsttxt txt">
                                    	${artMap.article_content}
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <#if comList??>
                    <#list comList as list>
                    	<tr>
	                        <td class="postauthor">
	                            <div class="auth">
	                                <div class="auti">
	                                    <a href="#" target="_blank">${list.userInfo["user_name"]!}</a>
	                                </div>
	                                <div class="geren">
		                                <div class="man">
		                                    <a href="#"><img src="${list.userInfo["user_photo"]!}" alt="" class="photo"></a>
		                                </div>
		                            </div>
		                            <div class="user-aten">
		                                <ul>
		                                    <li><span><a href="#">0</a></span>粉丝</li>
		                                    <li><span><a href="#">0</a></span>精华</li>
		                                    <li class="last"><span><a href="#">${list.userInfo["article_tcount"]!}</a></span>帖子</li>
		                                </ul>
		                            </div>
		                            <dl class="otherinfo">
		                                <dt>积分</dt>
		                                <dd>0</dd>
		                                <dt>性别</dt>
		                                <dd>保密</dd>
		                                <dt>注册时间</dt>
		                                <dd>${list.userInfo["reg_time"]?date}</dd>
		                            </dl>
	                            </div>
	                        </td>
	                        <td class="postcontent" style="width:822px;">
	                            <div class="pi">
	                                <div class="pti">
	                                    <div class="authi">
	                                        <span>发表于 ${list.com1["add_time"]?date}</span>
	                                        <span class='pipe'>|</span>
	                                        <a href="#">只看该楼主</a>
	                                    </div>
	                                    <div class="post-floor">
	                                        <em>楼主</em>
	                                    </div>
	                                </div>
	                            </div>
	                            <div class="postmessage defaultpost">
	                                <div class="t-msgfont">
	                                    <div class="firsttxt txt">
	                                    	${list.com1["com_content"]!}
	                                    </div>
	                                </div>
	                            </div>
	                            <div class="addcar"></div>
	                            <div class="core_reply_content">
	                                <ul>
	                                	<#if list.com2??>
	                                		<#list list.com2 as clist>
	                                			<li class="first-noborder">
		                                        <a href="#" class="lzl_p_p"><img src="${clist.user_photo!}" alt=""></a>
		                                        <div class="lzl_cnt">
		                                            <a href="#" class='at'>${clist.user_name} : </a>
		                                            <span class='lzl_content_main'>${clist.com_content}</span>
		                                            <div class="lzl_content_reply">
		                                                <span class="lzl_time">${clist.add_time}</span>
		                                            </div>
		                                        </div>
		                                    </li>
	                                		</#list>
	                                	</#if>
	                                </ul>
	                            </div>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td class="post-bottom-l"></td>
	                        <td class="post-bottom-r">
	                            <div class="post-fun">
	                               <div class="post-operation">
	                                   <a href="javascript:void(0);" class="reply fast-reply">回复本楼</a>
	                                </div>
	                            </div>
	                            <div class="div_reply_lou" style="display:none;">
		                            <div style="position:relative;">
		                                   <textarea id="txt_com_${list.com1["com_id"]!}" class="com_content" name="content" style="width:822px;height:100px;visibility:hidden;"></textarea>
		                            </div>
		                            <div class="btn_reply">
		                                <a href="javascript:void(0);" tag="${list_index}" com="${list.com1["com_id"]!}" class="btn-reply send_comContent">回复</a>
		                            </div>
	                            <div>
	                        </td>
	                    </tr>
	                    <tr class="ad">
	                        <td class="postauthor"></td>
	                        <td class="posttoppci"></td>
	                    </tr>
                    </#list>
                    </#if>
                    
                    
                </tbody>
            </table>
        </div>
    </div>


    <div class="forum_pages s_clear">
    </div>

    <div id="fast_post" class="rapitReply post_listA">
        <div class="fastReplyBox clearfix">
            <div class="fastReplyBox-form">
                <div class="intTxt">
                    <textarea id="comment_content" name="content" style="width:988px;height:180px;visibility:hidden;"></textarea>
                </div>
                <div class="ft clearfix" style="width:970px;">
                    <a href="javascript:void(0);" id="bt_sub" class="btn-reply">发表回复</a>
                </div>
            </div>
        </div>
    </div>

</div>

<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
<script charset="utf-8" src="${bbs_url}js/kindeditor-4.1.10/kindeditor.js"></script>
<script charset="utf-8" src="${bbs_url}js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<script type="text/javascript">
	KindEditor.ready(function(K) {
		editor = K.create('#comment_content', {
			allowFileManager : false,
			allowPreviewEmoticons : false,
			allowImageUpload : true,
			allowImageRemote : true,//为true则上传图片时显示“网络图片”功能，为false则上传图片时不显示“网络图片”功能。
		    cssPath : '${bbs_url}js/kindeditor-4.1.10/plugins/code/prettify.css',//指定编辑器iframe document的CSS，用于设置可视化区域的样式。
			uploadJson : '${bbs_url}xpabc/article/imgup',
			resizeType : 0,//2或1或0，2时可以拖动改变文本编辑域的宽度和高度，1时只能改变高度，0时不能拖动。
			items : ['undo', 'redo', '|', 'code', 'cut', 'copy', 'paste', 'plainpaste', 'wordpaste', '|', 
					'fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', 'insertorderedlist',
						'insertunorderedlist', 'indent', 'outdent', '|', 'emoticons', 'image', 'link', '|', 'fullscreen'],
			afterCreate : function() {
				this.loadPlugin('autoheight');
			}
		});
		
		K("#bt_sub").click(function(){
			if(editor.isEmpty()){
				alert("回复内容不可为空，请输入回复内容！");
				return;
			}
			$.post("${bbs_url}xpabc/comment/add",
					{articleId:${artMap.article_id},
					comId:0,
					content:editor.html()},
					function(data){
						if(data==-1){
							alert("您还未登录，请首先登录！");
							$('.popup-box,.overlay').show();
							return;
						}
						if(data==1){
							alert("成功！");
							window.location.reload();
						}else{
							alert("提交失败！");
						}
					});
		});
		
		var arr_com = new Array();
		$(".com_content").each(function(index, domEle){
			editor2 = K.create(domEle, {
				allowFileManager : false,
				allowPreviewEmoticons : false,
				allowImageUpload : true,
				allowImageRemote : true,//为true则上传图片时显示“网络图片”功能，为false则上传图片时不显示“网络图片”功能。
			    cssPath : '${bbs_url}js/kindeditor-4.1.10/plugins/code/prettify.css',//指定编辑器iframe document的CSS，用于设置可视化区域的样式。
				uploadJson : '${bbs_url}xpabc/article/imgup',
				resizeType : 0,//2或1或0，2时可以拖动改变文本编辑域的宽度和高度，1时只能改变高度，0时不能拖动。
				items : ['undo', 'redo', '|', 'code', 'cut', 'copy', 'paste', 'plainpaste', 'wordpaste', '|', 
						'fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
							'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', 'insertorderedlist',
							'insertunorderedlist', 'indent', 'outdent', '|', 'emoticons', 'image', 'link', '|', 'fullscreen'],
				afterCreate : function() {
					this.loadPlugin('autoheight');
				}
			});
			
			arr_com.push(editor2);
		});
		
		K(".send_comContent").click(function(){
			
			var i = $(this).attr("tag");
			var com_id = $(this).attr("com");
			var obj = arr_com[i];
			
			if(obj.isEmpty()){
				alert("回复内容不可为空，请输入回复内容！");
				return;
			}
			$.post("${bbs_url}xpabc/comment/add",
					{articleId:${artMap.article_id},
					comId:com_id,
					content:obj.html()},
					function(data){
						if(data==-1){
							alert("您还未登录，请首先登录！");
							return;
						}
						if(data==1){
							window.location.reload();
						}else{
							alert("提交失败！");
						}
					});
		});
		
	});
	
	$(".fast-reply").click(function(){
		var obj=$(this).parents(".post-fun").next();
		if(obj.is(":hidden")){
			obj.show();
		}else{
			obj.hide();
		}
		
	});
	
	$("#searchbtn").click(function(){
	var keyword = $("#key").val();
	location.href="${bbs_url}xpabc/search/toSearch?ksWord="+keyword;
});
</script>
</@cc.html_js>
