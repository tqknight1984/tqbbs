<#macro html_head>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<LINK rel="Bookmark" href="/favicon.ico" >
<LINK rel="Shortcut Icon" href="/favicon.ico" />
<!--[if lt IE 9]>
<script type="text/javascript" src="${bbs_admin}lib/html5.js"></script>
<script type="text/javascript" src="${bbs_admin}lib/respond.min.js"></script>
<script type="text/javascript" src="${bbs_admin}lib/PIE_IE678.js"></script>
<![endif]-->
<link href="${bbs_admin}css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="${bbs_admin}css/H-ui.admin.css" rel="stylesheet" type="text/css" />
<link href="${bbs_admin}css/H-ui.doc.css" rel="stylesheet" type="text/css" />
<link href="${bbs_admin}lib/Hui-iconfont/1.0.1/iconfont.css" rel="stylesheet" type="text/css" />
<link href="${bbs_admin}css/style.css" rel="stylesheet" type="text/css" />
<#nested>
<title>小胖爱编程-后台管理</title>
</head>
</#macro>

<#macro html_foot>
<script type="text/javascript" src="${bbs_admin}lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="${bbs_admin}lib/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="${bbs_admin}lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript" src="${bbs_admin}lib/My97DatePicker/WdatePicker.js"></script> 
<script type="text/javascript" src="${bbs_admin}lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${bbs_admin}lib/bootstrap-modal/2.2.4/bootstrap-modalmanager.js"></script>
<script type="text/javascript" src="${bbs_admin}lib/bootstrap-modal/2.2.4/bootstrap-modal.js"></script>
<script type="text/javascript" src="${bbs_admin}js/H-ui.js"></script> 
<script type="text/javascript" src="${bbs_admin}js/H-ui.admin.js"></script>
<#nested>
</html>
</#macro>