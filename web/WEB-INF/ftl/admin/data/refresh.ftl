<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<body>
    <nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 数据管理 <span class="c-gray en">&gt;</span> 刷新数据 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
    <a id="btRefresh" class="btn btn-success radius" style="line-height:1.6em;margin: 20px 20px;"><i class="Hui-iconfont">&#xe68f;</i><label>刷新</label></a>
    <a id="btSave" class="btn btn-success radius" style="line-height:1.6em;margin: 20px 20px;"><i class="Hui-iconfont">&#xe68f;</i><label>增量持久化到mysql</label></a>
</body>

<@cc.html_foot>
<script type="text/javascript">
    (function () {
        var ctx = "${request.contextPath}";

        $(document).ready(function () {
            $("#btRefresh").click(function () {
                $(this).find("label").text("正在加载数据");
                var that = this;
                $.get(ctx + "/admin/data/refresh", function (data) {
                    if (data.message) {
                        $(that).find("label").text("刷新");
                        alert(data.message);
                    }
                });
            });

            $("#btSave").click(function () {
                $(this).find("label").text("正在保存");
                var that = this;
                $.get(ctx + "/admin/data/save", function (data) {
                    if (data.message) {
                        $("body").append("<div>" + data.message + "</div>");
                    }
                });
            });
        });
    })();
</script>
</@cc.html_foot>
