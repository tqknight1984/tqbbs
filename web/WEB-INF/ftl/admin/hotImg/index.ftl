<#import "../common.ftl" as cc>
<@cc.html_head>
    <style>
        .box {
            position: relative;
            display: block;
            width: 550px;
            margin: 12px auto;
        }

        .box:after {
            content: "";
            position: relative;
            display: block;
            visibility: hidden;
            height: 0;
            clear: both;
        }

        .imgBox {
            position: relative;
            overflow: hidden;
            display: block;
            float: left;
            width: 400px;
            height: 300px;
        }

        .optionBox {
            position: relative;
            display: block;
            float: left;
            width: 150px;
            height: 300px;
        }

        .imgPre {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            z-index: 0;
        }

        .changeVisible {
            position: absolute;
            top: 0;
            right: -2px;
            width: 64px;
            height: 64px;
            z-index: 1;
            cursor: pointer;
        }

        .imgInput {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            border: none;
            outline: none;
            opacity: 0;
            z-index: 1;
        }

        .titleInput {
            box-sizing: border-box;
            position: absolute;
            bottom: 0;
            left: 0;
            width: 100%;
            height: 32px;
            padding: 8px 12px;
            border: none;
            outline: none;
            color: #ffffff;
            font-size: 16px;
            line-height: 16px;
            background: rgba(0, 0, 0, .4);
            z-index: 2;
        }

        .bt {
            position: relative;
            display: block;
            width: 4rem;
            height: 32px;
            border-radius: 5px;
            margin: 12px auto;
            color: #ffffff;
            font-size: 16px;
            line-height: 32px;
            text-align: center;
            cursor: pointer;
        }

        .saveBt {
            background: #1f9fff;
        }

        .saveBt:active {
            background: #2daaff;
        }

        .messageBox {
            position: relative;
            display: none;
            float: left;
            width: 550px;
            margin: 12px auto;
            color: #666666;
            font-size: 14px;
            line-height: 24px;
        }

        .messageBox.success {
            color: #1f9fff;
        }

        .messageBox.warning {
            color: #ff971b;
        }

        .hide {
            display: none;
        }

    </style>
</@cc.html_head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 焦点图管理 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<#if  hotImgs?? && hotImgs?size gt 0>
    <#list hotImgs as item>
    <form action="${bbs_url}xpabc/admin/hotImg/update" method="post" enctype="multipart/form-data" target="submitIF_${item_index}">
        <div class="box">
            <div class="imgBox">
                <input type="hidden" name="id" value="${item.id}" >
                <img src="${bbs_url}images/${item.url}" class="imgPre">
                <input type="file" name="img" class="imgInput" >
                <img src="${bbs_url}images/ic_${(item.visible == 1) ? string('', 'dis')}visible.png" class="changeVisible">
                <input type="hidden" name="visible" class="visibleInput" value="${item.visible}" data-value-old="${item.visible}" >
                <input type="text" name="title" class="titleInput" value="${item.title}" data-value-old="${item.title}">
            </div>
            <div class="optionBox">
                <div class="bt saveBt hide">保存</div>
            </div>
            <div class="messageBox"></div>
        </div>
        <iframe name="submitIF_${item_index}" class="submitIF" style="display: none;">
        </iframe>
    </form>
    </#list>
</#if>

</body>

<@cc.html_foot>
<script type="text/javascript">
    (function () {

        var icVisibleUrl = "${bbs_url}images/ic_visible.png";
        var icDisvisibleUrl = "${bbs_url}images/ic_disvisible.png";

        $(document).ready(function () {

            $("input[name=img]").change(function () {
                if (this.files.length > 0) {
                    var messageBox = $(this).parent().parent().find(".messageBox");
                    if (this.files[0].size > 1024 * 1024) {
                        warningMessage(messageBox, "图片太大");
                        $(this).val("");
                    }
                    else {
                        $(this).parent().find(".imgPre").attr("src", getObjectURL(this.files[0]));
                        hideMessageBox(messageBox);
                    }
                    checkChange($(this).parent().parent());
                }
                else {
                    $(this).parent().find(".imgPre").attr("src", "");
                }
            });

            $("input[name=title]").on("input", function () {
                checkChange($(this).parent().parent());
            });

            $(".changeVisible").click(function () {
                var visibleInput = $(this).parent().find("input[name=visible]");
                var newVisible = 1 - visibleInput.val();
                visibleInput.val(newVisible);
                $(this).attr("src", newVisible == 0 ? icDisvisibleUrl : icVisibleUrl);
                checkChange($(this).parent().parent());
            });

            $(".saveBt").click(function () {
                var box = $(this).parent().parent();
                if (isChange(box)) {
                    box.parent().submit();
                }
            });

            $(".submitIF").load(function () {
                var messageBox = $(this).parent().find(".messageBox");
                try {
                    var res = JSON.parse($(window.frames[$(this).attr("name")].document).find("body").text());
                    if (res.success) {
                        var box = $(this).parent();
                        box.find(".preImg").attr("src", res.data.url);

                        var imgInput = box.find(".imgInput");
                        imgInput.val("");

                        box.find(".titleInput").attr("data-value-old", res.data.title);
                        box.find(".changeVisible").attr("src", res.data.visible == 1 ? icVisibleUrl : icDisvisibleUrl);
                        box.find(".visibleInput").attr("data-value-old", res.data.visible);
                        checkChange(box);

                        successMessage(messageBox, res.message);
                    }
                    else {
                        warningMessage(messageBox, res.message);
                    }
                }
                catch (e) {
                    warningMessage(messageBox, "保存失败");
                }
            });

        });

        function successMessage(messageBox, message) {
            messageBox.attr("class", "messageBox success");
            messageBox.text(message);
            messageBox.slideDown();
        }

        function warningMessage(messageBox, message) {
            messageBox.attr("class", "messageBox warning");
            messageBox.text(message);
            messageBox.slideDown();
        }

        function hideMessageBox(messageBox) {
            messageBox.attr("class", "messageBox");
            messageBox.text("");
            messageBox.slideUp();
        }

        function checkChange(box) {
            if (isChange(box)) {
                $(box).find(".saveBt").removeClass("hide");
            }
            else {
                $(box).find(".saveBt").addClass("hide");
            }
        }

        function isChange(box) {
            var imgInput = box.find(".imgInput");
            var titleInput = box.find(".titleInput");
            var visibleInput = box.find("input[name=visible]");
            return imgInput[0].files.length > 0
                    || titleInput.val() != titleInput.attr("data-value-old")
                    || visibleInput.val() != visibleInput.attr("data-value-old");
        }

        function getObjectURL(file) {
            var url = null ;
            if (window.createObjectURL!=undefined) { // basic
                url = window.createObjectURL(file) ;
            } else if (window.URL!=undefined) { // mozilla(firefox)
                url = window.URL.createObjectURL(file) ;
            } else if (window.webkitURL!=undefined) { // webkit or chrome
                url = window.webkitURL.createObjectURL(file) ;
            }
            return url ;
        }

    })();
</script>
</@cc.html_foot>