<#import "../common.ftl" as cc>
<@cc.html_head>
<link rel="stylesheet" type="text/css" href="${bbs_url}js/editor.md/css/editormd.css"/>
<style>
.fastReplyBox-form{width:988px;}
.super-editor{margin-top:-10px;}
.mod-dialog-bg{z-index: 811211;}
.editormd-grid-table-row a.selected {
    color: #F00;
    background-color: #0099CC;
}
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
                    <div id="div_editormd" style="z-index: 811211;">
					    <textarea style="display:none;">${artMap.article_markdown!}</textarea>
					</div>
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
<script type="text/javascript" src="${bbs_url}js/editor.md/editormd.js"></script>
<script type="text/javascript">
	
	$(".plateTab").click(function(){
		$("#plate_checked").val($(this).attr("tag"));
		$("#plate_checked").attr("isin",$(this).attr("isin"));
		$(".plateTab").removeClass("selected");
		$(this).addClass("selected");
	});

    var testEditor = editormd("div_editormd", {
		width: "100%",
		height: 500,
		markdown : "",
		saveHTMLToTextarea : true,      // 保存 HTML 到 Textarea
		path : '${bbs_url}js/editor.md/lib/',
		dialogLockScreen : false,   // 设置弹出层对话框不锁屏，全局通用，默认为 true
		//dialogShowMask : false,     // 设置弹出层对话框显示透明遮罩层，全局通用，默认为 true
		//dialogDraggable : false,    // 设置弹出层对话框不可拖动，全局通用，默认为 true
		dialogMaskOpacity : 0.4,    // 设置透明遮罩层的透明度，全局通用，默认值为 0.1
		dialogMaskBgColor : "#333", // 设置透明遮罩层的背景颜色，全局通用，默认为 #fff
		imageUpload : true,
		imageFormats : ["jpg", "JPG", "jpeg", "JPEG", "gif", "png", "bmp", "webp"],
		imageUploadURL : "${bbs_url}xpabc/article/imgup2",
		toolbarIcons : function() {
            // Or return editormd.toolbarModes[name]; // full, simple, mini
            // Using "||" set icons align right.
            return ["undo", "redo", "|", 
            "bold", "italic", "quote","|",
            "h1", "h2", "h3", "|", 
            "code", "preformatted-text", "code-block", "|", 
            "list-ul", "list-ol", "|", 
            "hr", "del", "pagebreak", "|",
            "link", "image", "|", 
            "table", "datetime", "emoji", 
            "||", "watch", "fullscreen", "preview", "testIcon"]
        },
	});
	
	$("#bt_update").click(function(){
		
		var obj = $(this);
		var article_title = $("#article_title").val();
		if(article_title.replace(/ /g,"").length==0){
			ZENG.msgbox.show("标题不能为空，请填写标题！", 5, 3000);
			return;
		}
		
		var cont = $(".editormd-preview").html();
		
		if(cont==""){
			ZENG.msgbox.show("文章内容不可为空，请输入帖子内容！", 5, 3000);
			return;
		}
		
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
				content:cont,
				markdown:testEditor.getMarkdown()},
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
</script>
</#if>

</@cc.html_js>
