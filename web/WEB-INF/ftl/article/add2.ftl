<#import "../common.ftl" as cc>
<@cc.html_head>
<link rel="stylesheet" type="text/css" href="${bbs_url}js/editor.md/css/editormd.css"/>
</@cc.html_head>
<@cc.html_index_top>
</@cc.html_index_top>
<style>
.edit_head{font-size: 16px;margin: 10px;color: #666;}
.title_wrap{ margin-top:0;}
.zeng_msgbox_layer_wrap{z-index: 811211;}
.editormd-grid-table-row a.selected {
    color: #F00;
    background-color: #0099CC;
}
</style>
<div class="wrap">
   <div class="com-publish">
       <div class="edit_head">标题：</div>
       <div class="title_wrap">
           <input id="article_title" type="text" class="cTxt" style="color:#999; border-color:#999;" onfocus="textFocus(this)" onblur="textBlur(this)" placeholder="请输入帖子主题，不超过35个字">
           <span id="titleTips" class="gray">0/<i id="titleLength">35</i></span>
       </div class="edit_head">
       <div class="edit_head"><#if user_role==1>外部板块：<#else>板块列表：</#if></div>
       <p class="pType clearfix">
       		<#if plateid??><input type="hidden" id="plate_checked" isin="${isin}" value="${plateid}" /></#if>
			<#list plateList as list>
			<#if !plateid?? && list_index==0><input type="hidden" id="plate_checked" isin="0" value="${list.plate_id}" /></#if>
           	<a tag="${list.plate_id}" isin="0" href="javascript:void(0);" class="plateTab <#if list_index==0>selected</#if>">${list.plate_name}</a>
            </#list>
       </p>
       <#if user_role==1>
       <div class="edit_head">内部板块：</div>
       <p class="pType clearfix">
			<#list plateListin as list>
           	<a tag="${list.plate_id}" isin="1" href="javascript:void(0);" class="plateTab">${list.plate_name}</a>
            </#list>
       </p>
       </#if>
   </div>
   <div id="div_editormd" style="z-index: 811211;">
        <textarea style="display:none;"></textarea>
	</div>
   <div id="post_action" class="cle clearfix">
       <input type="button" id="bt_sub" href="javascript:void(0);" class="btn_post" value="发表文章" style="cursor:pointer;" />
   </div>
</div>
<!-- main end-->

<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
<script type="text/javascript" src="${bbs_url}js/editor.md/editormd.js"></script>
<script type="text/javascript">
	$(".plateTab").click(function(){
		$("#plate_checked").val($(this).attr("tag"));
		$("#plate_checked").attr("isin",$(this).attr("isin"));
		$(".plateTab").removeClass("selected");
		$(this).addClass("selected");
	});
	//标题文本框默认提示文字
	function textFocus(el) {
	  if (el.defaultValue == el.value) { el.value = ''; el.style.color = '#333'; }
	}
	function textBlur(el) {
	  if (el.value == '') { el.value = el.defaultValue; el.style.color = '#999'; }
	}
	
	//$("#plate_checked").val($(".plateTab").eq(0).attr("tag"));
	
	
	
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
		emoji : true,  //开启表情功能
		imageFormats : ["jpg", "JPG", "jpeg", "JPEG", "gif", "png", "bmp", "webp"],
		imageUploadURL : "${bbs_url}xpabc/article/imgup2",
        onKeyCtrlS: function () {
            localStorage.setItem(location.href + ":title", $("#article_title").val());
            localStorage.setItem(location.href + ":tag", $(".pType > a[tag].selected").attr("tag"));
            localStorage.setItem(location.href + ":md", testEditor.getMarkdown());
            ZENG.msgbox.show("本地保存成功！", 4, 1000);
        },
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
        }
	});

	//加载本地缓存
    function loadCache(index) {
		setTimeout(function () {
			try {
                var md = localStorage.getItem(location.href + ":md");
                testEditor.setMarkdown(md);
			    $("#article_title").val(localStorage.getItem(location.href + ":title"));
                $(".pType > a[tag='" + localStorage.getItem(location.href + ":tag") + "']").click();
			}
			catch (e) {
                if (index > 0) {
                    loadCache(index - 1);
				}
			}
        }, 250);
    }
    loadCache(5);

    function clearCache() {
		localStorage.removeItem(location.href + ":md");
        localStorage.removeItem(location.href + ":title");
        localStorage.removeItem(location.href + ":tag");
    }
	
	$("#bt_sub").click(function(){
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
		$.post("${bbs_url}xpabc/article/postnew",
				{title:$("#article_title").val(),
				plate:$("#plate_checked").val(),
				isin:$("#plate_checked").attr("isin"),
				content:cont,
				markdown:testEditor.getMarkdown()},
				function(data){
					if(data==-1){
						ZENG.msgbox.show("请首先登录！", 5, 3000);
						setTimeout("window.location.reload()",1000);
					}
					if(data==0){
						ZENG.msgbox.show("提交失败，请重新提交！", 5, 3000);
					}else{
                        clearCache();
						ZENG.msgbox.show("发表文章成功！", 4, 3000);
						setTimeout('window.location.href="${bbs_url}xpabc/article/show/' + data + '.htm"',1000);
					}
					obj.removeAttr("disabled");
				});
	});
	
</script>
</@cc.html_js>
