<#import "../common.ftl" as cc>
<@cc.html_head>
<link rel="stylesheet" href="${bbs_url}js/kindeditor-4.1.10/themes/default/default.css" />
<link rel="stylesheet" href="${bbs_url}js/kindeditor-4.1.10/plugins/code/prettify.css" />
<style>
.super-editor{margin-top:-10px;}
</style>
</@cc.html_head>
<@cc.html_index_top>
</@cc.html_index_top>
<#if isGet==1>
<!--contentlogin-->

<div class="com-publish">
       <div class="edit_head">标题：</div>
       <div class="title_wrap">
           <input id="article_title" type="text" class="cTxt" style="color:#000; border-color:#999;" onfocus="textFocus(this)" onblur="textBlur(this)" placeholder="请输入帖子主题，不超过35个字" value="${artMap.article_title!}">
           <span id="titleTips" class="gray">0/<i id="titleLength">35</i></span>
       </div class="edit_head">
       <div class="edit_head"><#if user_role==1>外部板块：<#else>板块列表：</#if></div>
       <p class="pType clearfix">
			<#list plateList as list>
			<#if list_index==0><input type="hidden" id="plate_checked" isin="0" value="${artMap.plate_id}" /></#if>
           	<a tag="${list.plate_id}" isin="0" href="javascript:void(0);" class="plateTab <#if artMap.plate_id==list.plate_id>selected</#if>">${list.plate_name}</a>
            </#list>
       </p>
       <#if user_role==1>
       <div class="edit_head">内部板块：</div>
       <p class="pType clearfix">
			<#list plateListin as list>
           	<a tag="${list.plate_id}" isin="1" href="javascript:void(0);" class="<#if artMap.plate_id==list.plate_id>selected</#if> plateTab">${list.plate_name}</a>
            </#list>
       </p>
       </#if>
   </div>

<div class="wrap super-editor">
    <div id="fast_post" class="rapitReply post_listA">
        <div class="fastReplyBox clearfix">
             <div class="fastReplyBox-form">
                <div class="intTxt">
                    <textarea id="article_content" name="content" style="width:988px;height:600px;visibility:hidden;">
                    </textarea>
                </div>
                 <div class="ft clearfix">
                    <input type="button" id="bt_update" class="btn-reply" value="更新文章" style="cursor:pointer;" />
                </div>
            </div>
           <div class="clear"></div>
        </div>
    </div>

</div>
<#else>
	<div id="fast_post" class="rapitReply post_listA">
        <div class="fastReplyBox clearfix">
             <div class="fastReplyBox-form">
                <div style="width:988px;height:600px;">该帖子您无权编辑！</div>
            </div>
           <div class="clear"></div>
        </div>
    </div>
</#if>

<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>

<#if isGet==1>
<script charset="utf-8" src="${bbs_url}js/kindeditor-4.1.10/kindeditor.js"></script>
<script charset="utf-8" src="${bbs_url}js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<script type="text/javascript">
	
	$(".plateTab").click(function(){
		$("#plate_checked").val($(this).attr("tag"));
		$("#plate_checked").attr("isin",$(this).attr("isin"));
		$(".plateTab").removeClass("selected");
		$(this).addClass("selected");
	});

    var editor;
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="content"]', {
			allowPreviewEmoticons : false,
			allowImageUpload : true,
			allowFileManager : true,
			allowImageRemote : true,//为true则上传图片时显示“网络图片”功能，为false则上传图片时不显示“网络图片”功能。
		    cssPath : '${bbs_url}js/kindeditor-4.1.10/plugins/code/prettify.css',//指定编辑器iframe document的CSS，用于设置可视化区域的样式。
			uploadJson : '${bbs_url}xpabc/article/imgup',
			resizeType : 0,//2或1或0，2时可以拖动改变文本编辑域的宽度和高度，1时只能改变高度，0时不能拖动。
			items : ['source', '|', 'undo', 'redo', '|', 'code', 'cut', 'copy', 'paste', 'plainpaste', 'wordpaste', '|', 
					'fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'justifyfull', 'insertorderedlist',
						'insertunorderedlist', 'indent', 'outdent', '|', 'emoticons', 'image', 'link', '|', 'fullscreen'],
			afterCreate : function() {
				this.loadPlugin('autoheight');
			}
		});
		
		$.post("${bbs_url}xpabc/article/getArticleContent",
				{articleid:${artMap.article_id!}},
				function(data){
					editor.insertHtml(data);
				});
		
		K("#bt_update").click(function(){
			
			var obj = $(this);
			var article_title = $("#article_title").val();
			if(article_title.replace(/ /g,"").length==0){
				ZENG.msgbox.show("标题不能为空，请填写标题！", 5, 3000);
				return;
			}
			
			if(editor.isEmpty()){
				ZENG.msgbox.show("文章内容不可为空，请输入帖子内容！", 5, 3000);
				return;
			}
			
			var cont = editor.html();
			var str_check = "<img src=\"data:image";
			if(cont.indexOf(str_check)>-1){
				ZENG.msgbox.show("不可以粘贴图片，请在本地上传图片！", 1, 3000);
				return;
			}
			
			obj.attr("disabled","disabled");
			
			$.post("${bbs_url}xpabc/article/update",
					{articleid:${artMap.article_id!},
					title:$("#article_title").val(),
					plate:$("#plate_checked").val(),
					isin:$("#plate_checked").attr("isin"),
					content:cont},
					function(data){
						if(data==-1){
							ZENG.msgbox.show("请首先登录！", 5, 3000);
							window.location.reload();
						}
						if(data==0){
							ZENG.msgbox.show("提交失败，请重新提交！", 5, 3000);
						}else{
							ZENG.msgbox.show("编辑成功！", 4, 3000);
							window.location.href="${bbs_url}xpabc/article/show/" + data + ".htm";
						}
						obj.removeAttr("disabled");
					});
		});
	});
</script>
</#if>

</@cc.html_js>
