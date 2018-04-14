<#import "../common.ftl" as cc>
<@cc.html_head>
    <style>
        .imgBox {
            position: relative;
            display: block;
            width: 400px;
            height: 300px;
            margin: 12px auto;
        }

        .help {
            box-sizing: border-box;
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            padding: 120px 24px 32px 24px;
            color: #999999;
            font-size: 16px;
            text-align: center;
            z-index: 0;
        }

        .imgPre {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            z-index: 0;
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

        #saveBt {
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
            background: #1f9fff;
            cursor: pointer;
        }

        #saveBt:active {
            background: #2daaff;
        }

        #messageBox {
            position: relative;
            display: none;
            max-width: 400px;
            margin: 12px auto;
            color: #666666;
            font-size: 14px;
            line-height: 24px;
        }

        #messageBox.success {
            color: #1f9fff;
        }

        #messageBox.warning {
            color: #ff971b;
        }

    </style>
</@cc.html_head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 添加焦点图 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">

</div>
</body>
    <form id="hotImgForm" action="${bbs_url}xpabc/admin/hotImg/insert" method="post" enctype="multipart/form-data" target="submitIF">
        <div class="imgBox">
            <div class="help">点击白色区域添加图片，点击灰色区域输入标题</div>
            <img src="" class="imgPre">
            <input type="file" name="img" class="imgInput" >
            <input type="text" name="title" class="titleInput" >
        </div>
        <div id="messageBox"></div>
        <div id="saveBt">保存</div>
    </form>

    <iframe name="submitIF" style="display: none;">
    </iframe>
<@cc.html_foot>
<script type="text/javascript">
    (function () {
        var hotImgForm;
        var imgPre;
        var imgInput;
        var titleInput;
        var saveBt;
        var messageBox;
        var submitIF;

        $(document).ready(function () {
            hotImgForm = $("#hotImgForm");
            imgPre = $(".imgPre");
            imgInput = $("input[name=img]");
            titleInput = $("input[name=title]");
            messageBox = $("#messageBox");
            saveBt = $("#saveBt");
            submitIF = $("iframe[name=submitIF]");

            imgInput.change(function () {
                if (this.files.length > 0) {
                    if (this.files[0].size > 1024 * 1024) {
                        messageBox.warning("图片太大");
                    }
                    else {
                        imgPre.attr("src", getObjectURL(this.files[0]));
                        messageBox.hide();
                    }
                }
                else {
                    imgPre.attr("src", "");
                }
            });

            messageBox.success = function (msg) {
              messageBox.attr("class", "success");
              messageBox.text(msg);
                messageBox.slideDown();
            };

            messageBox.warning = function (msg) {
                messageBox.attr("class", "warning");
                messageBox.text(msg);
                messageBox.slideDown();
            };

            messageBox.hide = function () {
                messageBox.attr("class", "");
                messageBox.text("");
                messageBox.slideUp();
            };

            saveBt.click(function () {
                if (checkForm()) {
                    hotImgForm.submit();
                }
            });

            submitIF.load(function () {
                try {
                    var res = JSON.parse($(window.frames["submitIF"].document).find("body").text());
                    if (res.success) {
                        messageBox.success(res.message);
                        imgInput.val("");
                        imgPre.attr("src", "");
                        titleInput.val("");
                    }
                    else {
                        messageBox.warning(res.message);
                    }
                }
                catch (e) {
                    messageBox.warning("保存失败");
                }
            });
        });

        function checkForm() {
            if (imgInput[0].files.length == 0) {
                messageBox.warning("请点击白色区域添加图片");
                return false;
            }

            if (titleInput.val() == "") {
                messageBox.warning("请点击灰色区域输入标题");
                return false;
            }
            messageBox.hide();
            return true;
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