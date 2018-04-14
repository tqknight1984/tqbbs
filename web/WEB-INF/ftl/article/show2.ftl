<#import "../common.ftl" as cc>
<@cc.html_head>
<link rel="stylesheet" href="${bbs_url}js/kindeditor-4.1.10/themes/default/default.css" />
<link rel="stylesheet" href="${bbs_url}js/kindeditor-4.1.10/plugins/code/prettify.css" />
<link rel="stylesheet" type="text/css" href="${bbs_url}js/editor.md/css/editormd.css"/>
</@cc.html_head>
<@cc.html_index_top>
<style>
	.txt{width:780px;}
	.prettyprint{background:#E4E4E4;white-space:normal;overflow:scroll;}
	.itCont_content_contain{text-indent:0em;}
	.itCont_content{width: 700px !important;}
</style>

</@cc.html_index_top>
    <div class="wrap-box cf">
        <div class="fl wrap-left">
            <div class="borderbox">
                <div class="content-title">个人信息<span class="rightbox"></span></div>
                <div class="facewrapper">
                    <div class="faceinner">
                        <a href="${bbs_url}xpabc/index/tobbsUserList/0/${artMap.user_id!}/1.htm"><img class="facePic" src="${artMap.article_userphoto!}" width="120" height="120"></a>
                        <a href="${bbs_url}xpabc/index/tobbsUserList/0/${artMap.user_id!}/1.htm" class="userName" title="${artMap.article_username!}">${artMap.article_username!}</a>
                    </div>
                </div>
                <ul class="actionBox cf">
                    <#--<li class="actionItem">
                        粉丝(10)
                    </li>-->
                   <li class="actionItem">
                        精华文章<a href="#">(${user_elite_size})</a>
                    </li>
                </ul>
                <ul class="statisticsBox">
                    <#--<li class="statisticsItem">今日访问：<em id="DayVisitCount">1</em></li>-->
                    <li class="statisticsItem">总访问量：<em id="AllVisitCounter">${artMap.lookCount!}</em></li>
                    <li class="statisticsItem">开博时间：${artMap.article_userRegtime?date}</li>
                    <#--<li class="statisticsItem">博客排名：589</li>-->
                </ul>
                
            </div>
        </div>
        <div class="fr wrap-right">
            <div class="It_theme_Cont">
                <div class="itCont_main pl-15 pb-20">
                    <div class="itCount_title">
                        <h3>
                            <a href="#">${artMap.article_title}</a><#if artMap.article_order??&&artMap.article_order!="0"><a href="javascript:void(0)" class="a-top-all"></a></#if><#if artMap.is_elite??&&artMap.is_elite=="1"><span class='wonderful' id="wonderful_icon">精</span></#if>
                            <#if is_master??&&is_master><a href="${bbs_url}xpabc/article/edit/${artMap.article_id}.htm" class='post-a' style="padding: 0px 10px;margin:0px 10px;color:white">编辑</a></#if>
                            <#if is_master??&&is_master><a href="javascript:makeTop(${artMap.article_id},1)" class='post-a' style="padding: 0px 10px;margin:0px 10px;color:white">顶</a></#if>
                            <#if is_master??&&is_master&&artMap.article_order??&&artMap.article_order!="0"><a href="javascript:makeTop(${artMap.article_id},2)" class='post-a' style="padding: 0px 10px;margin:0px 10px;color:white">删顶</a></#if>
                            <#if is_master??&&is_master&&artMap.is_elite??&&artMap.is_elite=="0"><a href="javascript:makeElite(${artMap.article_id},1)" class='post-a' style="padding: 0px 10px;margin:0px 10px;color:white">精</a></#if>
                            <#if is_master??&&is_master&&artMap.is_elite??&&artMap.is_elite=="1"><a href="javascript:makeElite(${artMap.article_id},2)" class='post-a' style="padding: 0px 10px;margin:0px 10px;color:white">删精</a></#if>
                        </h3>
                    </div>
                    <div class="itCont_content" id="itCont_content">
                        <div class="itCont_content_contain">
                        	${artMap.article_content}
                        </div>
                    </div>
                    <div class="itCont_bottom">
                        <ul class='cf'>
                            <li>${artMap.add_time!}</li>
                            <li>浏览${artMap.lookCount!}</li>
                            <li><a href="#">评论(${artMap.replyCount!})</a></li>
                            <li class='last'>分类 <a href="${bbs_url}xpabc/index/tobbsList/${artMap.plate_id}/1.htm">${artMap.article_plateName!}</a></li>
                        </ul>
                    </div>
                </div>
                <div class="itCont_comments">
                    <div class="itCont_comments_list">
                        <form action="">
                            <div class="tit">
                                <span>网友评论</span>
                            </div>
                            
                            <#if comList??>
		                    <#list comList as list>
		                    	<div class="articlecomments-cell">
	                                <div class="comments-infor">
	                                    <a href="" class="replyName">${list.userInfo["user_name"]!}</a>
	                                    <div class="fr">
	                                        <span class='time'>${list.com1["add_time"]}</span>
	                                    </div>
	                                </div>
	                                <div class="comments-content">
	                                	${list.com1["com_content"]!}
	                                </div>
	                            </div>
		                    </#list>
		                    </#if>
		                    
                        </form>
                    </div>
                    <div class="totop cf">
                    </div>
                    <div class="post-articlecomments">
                        <div class="tit"><span>发布评论</span></div>
                        <div class="postcontent" style="padding:0;">
                            <textarea id="comment_content" name="content" style="width:778px;height:180px;visibility:hidden;"></textarea>
                        </div>
                        <div class="btn"><a id="bt_sub" href="javascript:void(0);">提交</a></div>
                    </div>
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
			items : ['code', 'cut', 'copy', 'paste', 'plainpaste', 'wordpaste', '|', 
					'fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', 'insertorderedlist',
						'insertunorderedlist', 'indent', 'outdent', '|', 'emoticons', 'link', '|', 'fullscreen'],
			afterCreate : function() {
				this.loadPlugin('autoheight');
			}
		});
		
		K("#bt_sub").click(function(){
			if(editor.isEmpty()){
				ZENG.msgbox.show("回复内容不可为空，请输入回复内容！", 5, 3000);
				return;
			}
			$.post("${bbs_url}xpabc/comment/add",
					{articleId:${artMap.article_id},
					comId:0,
					content:editor.html()},
					function(data){
						if(data==-1){
							ZENG.msgbox.show("请首先登录！", 5, 3000);
							return;
						}
						if(data==1){
							ZENG.msgbox.show("提交成功！", 4, 3000);
							window.location.reload();
						}else{
							ZENG.msgbox.show("提交失败，请重新提交！", 5, 3000);
							return;
						}
					});
		});
		
	});
	
	$("#searchbtn").click(function(){
		var keyword = $("#key").val();
		location.href="${bbs_url}xpabc/search/toSearch?ksWord="+keyword;
	});
	function makeElite(id,type){
		if(!is_exists(id)){
			id=0;
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
				if(code=="0000"){
					ZENG.msgbox.show(msg, 4, 3000);
					setTimeout('location.reload()',1000);
				}else{
					ZENG.msgbox.show(msg, 1, 3000);
				}
			}
			
		});
	}
	function makeTop(id,type){
		if(!is_exists(id)){
			id=0;
		}
		$.ajax({
			type: "POST",
			url : '${bbs_url}xpabc/userCenter/makeTop',
			data :{
				type:type,
				id:id
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