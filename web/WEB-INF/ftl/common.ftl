<#macro html_head title="小胖爱编程" description="" keywords="">
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=7" />
    <meta name="Keywords" content="${keywords?replace('&nbsp',' ')}">
    <meta name="Description" content="${description?replace('&nbsp',' ')}">
    <title>${title?replace("&nbsp"," ")}</title>
    <link rel="stylesheet" href="${bbs_url}css/style.css"/>
    <link rel="stylesheet" href="${bbs_url}css/jquery.bxslider.min.css"/>
    <#nested>
</head>
<body>
</#macro>

<#macro html_top>
<div class="minitop">
    <div class="mini-main">
        <div class="top-link">
            <a id="AddFavorite" href="javascript:void(0);" onclick="addFavorite();">收藏</a>
        </div>
        <ul class="top-login">
        	<#if user??>
        		<li>您好！</li>
            	<li>|</li>
            	<li><a target="_self" href="${bbs_url}xpabc/userCenter/mine/1.htm"><#if user.user_nickname??&&user.user_nickname!="">${user.user_nickname}<#elseif user.user_name??&&user.user_name!="">${user.user_name}<#else>无名氏</#if></a></li>
            <#else>
            	<li><a target="_self" href="${bbs_url}xpabc/user/toLogin">登录</a></li>
            	<li>|</li>
            	<li><a target="_self" href="${bbs_url}xpabc/user/toRegister.htm">注册</a></li>
        	</#if>
        </ul>
    </div>
</div>
<div class="header">
    <div class="header-main">
        <div class="clearfix">
            <div class="logo"><a target="_self" href="${bbs_url}xpabc/index/index.htm"><img src="${bbs_url}images/logo.jpg" title="小胖爱编程" alt="小胖爱编程" width='200'></a></div>
           <div class="nav">
	     <div class="nav-link">
                <ul>
                    <li><a href="${bbs_url}xpabc/index/learnindex.htm">学习交流</a></li>
                    <#if user??>
                     <#if user.role?? && user.role==1>
                     <li><a href="${bbs_url}xpabc/index/inlearnindex.htm">项目日志</a></li>
                     </#if>
                     </#if>
                </ul>
               
            </div>
	   </div>
            <div class="bbscat">
                <ul>
                </ul>
                
            </div>
        </div>
        <i class="logo-shadow1"></i>
        <i class="logo-shadow2"></i>
    </div>
    <div class="header-blue"></div>
</div>
</#macro>
<#macro html_top_noLogin>
<div class="minitop">
    <div class="mini-main">
        <div class="top-link">
            <a id="AddFavorite" href="javascript:void(0);" onclick="addFavorite();">收藏</a>
        </div>
    </div>
</div>
<div class="header">
    <div class="header-main">
        <div class="clearfix">
            <div class="logo"><a target="_self" href="${bbs_url}xpabc/index/index.htm" target="_blank"><img src="${bbs_url}images/logo.jpg" title="中国汽车消费网" alt="中国汽车消费网" width='200'/></a></div>
           <div class="nav">
	     <div class="nav-link">
                <ul>
                    <li><a href="${bbs_url}xpabc/index/learnindex.htm">学习交流</a></li>
                    <#if user??>
                     <#if user.role?? && user.role==1>
                     <li><a href="${bbs_url}xpabc/index/inlearnindex.htm">项目日志</a></li>
                     </#if>
                     </#if>
                </ul>
            </div>
	   </div>
            <div class="bbscat">
                <ul>
                </ul>
                
            </div>
        </div>
        <i class="logo-shadow1"></i>
        <i class="logo-shadow2"></i>
    </div>
    <div class="header-blue"></div>
</div>
</#macro>
<#macro html_index_top>
<!--HRADER-->
<div class="minitop">
    <div class="mini-main">
        <div class="top-link">
            <a id="AddFavorite" href="javascript:void(0);" onclick="addFavorite();">收藏</a>
        </div>
       <ul class="top-login">
           <#if user??>
        		<li>您好！</li>
            	<li>|</li>
            	<li><a target="_self" href="${bbs_url}xpabc/userCenter/mine/1.htm"><#if user.user_nickname??&&user.user_nickname!="">${user.user_nickname}<#elseif user.user_name??&&user.user_name!="">${user.user_name}<#else>无名氏</#if></a></li>
            <#else>
            	<li><a target="_self" href="${bbs_url}xpabc/user/toLogin">登录</a></li>
            	<li>|</li>
            	<li><a target="_self" href="${bbs_url}xpabc/user/toRegister.htm">注册</a></li>
        	</#if>
       </ul>
    </div>
</div>
<div class="header">
    <div class="header-main" style="background-image: url(${bbs_url}images/code.png); background-repeat:no-repeat;background-position:center right">
        <div class="clearfix">
            <div class="logo">
            	<a target="_self" href="${bbs_url}xpabc/index/index.htm"><img src="${bbs_url}images/logo.jpg" title="小胖爱编程" alt="小胖爱编程" width='200'/></a>
          	</div>
        <div class="nav">
	     <div class="nav-link">
            <ul>
                <li><a href="${bbs_url}xpabc/index/learnindex.htm">学习交流</a></li>
                <#if user??>
                 <#if user.role?? && user.role==1>
                 <li><a href="${bbs_url}xpabc/index/inlearnindex.htm">项目日志</a></li>
                 </#if>
                 </#if>
            </ul>
        </div>
	   </div>
            <div class="bbscat">
                <ul>
                </ul>
                
            </div>
        </div>
        <i class="logo-shadow1"></i>
        <i class="logo-shadow2"></i>
    </div>
    <div class="header-blue"></div>
</div>

<div class="w990">
    <div id="nav" class='pageinfo'>
        <p class="tit">
          <#if urlList?? && urlList?size gt 0>
			<#list urlList as list>
		      <#if list_index==urlList?size-1>
                    <a  href="${bbs_url}${list.url}" >${list.urlname}</a>  
               <#else>
                    <a  href="${bbs_url}${list.url}" >${list.urlname}</a>   » 
                </#if>
            </#list>
          </#if>
          
          <#if urlMap??>
          		<#list urlMap?keys as key>
          			<#if key_index==urlMap?size-1>
                    	<a  href="${bbs_url}${urlMap[key]}" >${key}</a>  
               		<#else>
                    	<a  href="${bbs_url}${urlMap[key]}" >${key}</a>   » 
                	</#if>
          		</#list>
          </#if>
          
        </p>
        <div class="fr search-box">
            <div class="fl" style="line-height:0">
                    <form target="_self" method="get" id="searchForm" action="${bbs_url}xpabc/search/toSearch" >
                        <input autocomplete="off" name="ksWord"  id="ksWord" class="inpTxt searchlog" type="text" placeholder="输入关键词搜索">
                        <input  class="inpBtn" type="submit" value="搜索" onclick="$('#searchForm').submit()">
                    </form>
            </div>
        </div>
    </div>

<#nested>        
</#macro>
<#macro html_foot>
<!--footer-->
<div class="footer">
    <p>
        <a href="http://www.315che.com/help/0.htm">关于我们</a> |
        <a href="http://www.315che.com/help/1.htm">联系我们</a> |
        <a href="http://www.315che.com/help/2.htm">广告服务</a> |
        <a href="http://www.315che.com/help/3.htm">友情链接</a> |
        <a href="http://www.315che.com/help/4.htm">网站地图</a> |
        <a href="http://www.315che.com/help/5.htm">招聘信息</a> |
        <a href="http://www.315che.com/help/6.htm">法律声明</a> |
        <a href="http://www.315che.com/help/7.htm">车友会</a> |
        <a href="#">加盟我们</a>
    </p>
    <p class="copyright">Copyright © 2005-2020 中国汽车消费网 备案号:沪icp备15049022号</p>
    <p>
        <a href="javascript:void(0);"><img src="http://www.315che.com/images/sh_gongshang.jpg" alt=""></a>
        <a href="javascript:void(0);"><img src="http://www.315che.com/images/zx110.jpg" alt=""></a>
        <a href="javascript:void(0);"><img src="http://www.315che.com/images/sh_110.jpg" alt=""></a>
        <a href="http://www.itrust.org.cn/yz/pjwx.asp?wm=1792781713"><img src="http://www.315che.com/images/itrust_315che.jpg" alt=""></a>
    </p>
</div>


</#macro>

<#macro login_modal>
<!--modal登录窗口-->
<div id="fast_overlay" class="overlay" style="height:100%;zoom:1;opacity: 0.5; filter:alpha(opacity=50)"></div>
<div id="fast_login" class="popup-box login-box" style="margin-top: 150px; zoom:1; opacity: 1;">
    <div class="top">
        <div class="categroy">
            <h2>中国汽车消费网</h2>
            <ul class="ctfigure">
                <li class="ct_current">快速登录</li>
                <li>注册</li>
            </ul>
        </div>
        <b class="close"></b>
    </div>
    <div class="logis-pannel" id="fastlogin">
        <div class="logpannel" style="margin:auto;border:none">
          <dl>
            <dd class="disnone" id="error-login"><p class="error_style" id="error-text"></p></dd>
            <dd><label>用户名：</label><input type="text" placeholder="请填写用户名" id="bbs-user"/></dd>
            <dd><label>密码：</label><input type="password" placeholder="请正确填写密码哦" id="bbs-pwd"/></dd>
            <dd><label></label><input type="button" value="登录" onclick="login()"/></dd>
          </dl>
        </div>
        <div class="hasother" style="min-height:315px">
          <p style="margin-top:20px">其他登录方式:</p>
          <span><a href="javascript:void(0);" onclick="javascript:document.getElementById('logintype2').value='1';document.getElementById('form1').submit();" style=" font-weight:normal; background:url(http://img.315che.com/hd/2013Imgs/images/oother-login.png) -110px -50px no-repeat; padding-left:23px; height:26px; line-height:26px; display:inline-block;">QQ登录</a></span>
          <span><a href="https://api.weibo.com:443/oauth2/authorize?client_id=2019549328&amp;redirect_uri=http%3A%2F%2Fbbs.315che.com%2Fforums%2Fweibocomplete2.htm&amp;response_type=code&amp;display=default" target="_parent" style=" font-weight:normal; background:url(http://img.315che.com/hd/2013Imgs/images/oother-login.png) -110px -23px no-repeat; padding-left:23px; height:26px; line-height:26px; display:inline-block;">微博登录</a></span>
        </div>
    </div>
    <div class="logis-pannel disnone" id="fastregister">
        <div class="logpannel" style="margin:auto;border:none">
          <dl id="email-register">
                <dd class="disnone" id="phonefirst">
                  <label>用&nbsp;&nbsp;户&nbsp;&nbsp;名：</label>
                  <input type="text" placeholder="请填写用户名" id="usernameval" maxlength="11"/>
                </dd>
                <dd class="disnone" id="first-error">
                  <p class="error_style"></p>
                </dd>
                <dd><label>密&nbsp;&nbsp;&nbsp;&nbsp;码：</label><input type="password" placeholder="请记住密码哦" minlength="6" id="pwdval"/></dd>
                <dd class="disnone" id="pwd-error"><p class="error_style"></p></dd>
                <dd><label>密码确认：</label><input type="password" placeholder="请再次输入密码哦" id="again_pwdval"/></dd>
                <dd class="disnone" id="pwd-againerr"><p class="error_style"></p></dd>
                <dd><label></label><input type="button" value="注册成为新用户" style="letter-spacing:0;"/></dd>
          </dl>
        </div>
        <div class="hasother" style="min-height:315px">
          <p style="margin-top:20px">其他登录方式:</p>
          <span><a href="javascript:void(0);" onclick="javascript:document.getElementById('logintype2').value='1';document.getElementById('form1').submit();" style=" font-weight:normal; background:url(http://img.315che.com/hd/2013Imgs/images/oother-login.png) -110px -50px no-repeat; padding-left:23px; height:26px; line-height:26px; display:inline-block;">QQ登录</a></span>
          <span><a href="https://api.weibo.com:443/oauth2/authorize?client_id=2019549328&amp;redirect_uri=http%3A%2F%2Fbbs.315che.com%2Fforums%2Fweibocomplete2.htm&amp;response_type=code&amp;display=default" target="_parent" style=" font-weight:normal; background:url(http://img.315che.com/hd/2013Imgs/images/oother-login.png) -110px -23px no-repeat; padding-left:23px; height:26px; line-height:26px; display:inline-block;">微博登录</a></span>
        </div>
    </div>
</div>
</#macro>
<#macro login_modal_js>
	<script>
		var afterLoginFunc = "";
    //快速登录
    $(document).ready(function(){
        $('.ctfigure li').on('click',function(){
            $this=$(this).index();
            $(this).addClass('ct_current').siblings().removeClass('ct_current');
            if($this=='0'){
                $('#fastlogin').show();
                $('#fastregister').hide();;
            }
            else{
                $('#fastregister').show();
                $('#fastlogin').hide();
            }
        })
        $('#phrigs').on('click',function(){
            $('#phonefirst,#phonechange,#phonecode').show();
            $('#emailfirst,#emailchange,#mailcode,#first-error,#pwd-error,#pwd-againerr,#last-error').hide();
        })
        $('#emailrigs').on('click',function(){
            $('#emailfirst,#emailchange,#mailcode').show();
            $('#phonefirst,#phonechange,#phonecode,#first-error,#pwd-error,#pwd-againerr,#last-error').hide();
        })
        $('.close').on('click',function(){
            $('.popup-box,.overlay').hide();
        })
        
        
    })
    
    //打开快速登录弹窗
    function fast_login_open(){
    	$('.popup-box,.overlay').show();
    }
    
    //关闭快速登录弹框
    function fast_login_close(){
    	$('.popup-box,.overlay').hide();
    }
    function login(){
        	var $userID=eval(document.getElementById('bbs-user')).value,
            $userPWD=eval(document.getElementById('bbs-pwd')).value,
            $userErr=document.getElementById('error-login'),
            $userP=document.getElementById('error-text');
            if($userID==''){
                $userErr.style.display="block";
                 $userP.innerHTML='用户名不能为空';
                 return;
            }else{
                if($userPWD==''){
                	$userErr.style.display="block";
                    $userP.innerHTML='密码不能为空';
                    return;
                }
            }
            $.ajax({
				type: "POST",
				url : '${bbs_url}xpabc/user/login',
    			data :{
    				username:$userID,
    				password:$userPWD
    			},
    			success : function(data){
    				var code = data.code;
    				var msg = data.msg;
    				alert(msg);
    				if(code=="0000"){
    					$('.popup-box,.overlay').hide();
    					eval(document.getElementById('bbs-user')).value="";
    					eval(document.getElementById('bbs-pwd')).value="";
    					if(afterLoginFunc==""){
    						return;
    					}
    					fast_login_close();
    					var  func=eval(afterLoginFunc);
    					new func();
    				}
    			}
				
			});	
    	}
</script>
</#macro>
<#macro html_js>
<!-- JavaScript codes -->
<script src="${che_url}js/jquery-1.11.2.min.js"></script>
<script src="${bbs_url}js/focus.js"></script>
<script src="${bbs_url}js/bbsgis.js"></script>
<script src="${che_url}js/jquery.bxslider.min.js"></script>
<script src="${bbs_url}js/msgbox.js"></script>
<script src="${bbs_url}wsChat/chat.js"></script>
<script src="${bbs_url}js/jquery.cookie.js"></script>
<script>
	var chat;
    $(document).ready(function(){
        $('h3#letterseq a').on('click',function(){
            $this=$(this).index();
             $(this).addClass('cur').siblings().removeClass('cur');
             $('.pf_blist .lettershow').eq($this).show().siblings().hide();
        });
        var uid = $.cookie("uid");
        if(!is_exists(uid)){
        	uid = uuid();
        	$.cookie('uid',uid, {path: '/', domain: '.xpabc.com'}); 
        }
        chat = SocketClient("${ws_url}",uid);
        //$.ajax({
		//		type: "POST",
		//		url : '${bbs_url}xpabc/user/getToken',
    	//		success : function(data){
    	//			var code = data.code;
    	//			if(code=="0000"){
    	//				var uid = data.uid;
    	//				var token = data.token;
    	//				SocketClient("${ws_url}",uid,token);
    	//			}
    	//		}
				
		//	});
    });
    function is_exists(obj){
		obj = $.trim(obj);
		if(obj==""||obj==null||obj==undefined){
			return false;
		}else{
			return true;
		}
	}
    function uuid() {
    	var s = [];
    	var hexDigits = "0123456789abcdef";
    	for (var i = 0; i < 36; i++) {
        	s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    	}
    	s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    	s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    	s[8] = s[13] = s[18] = s[23] = "-";

    	var uuid = s.join("");
    	return uuid;
	}
	function chatTo(uId,uName,uImg){
		chat.addUserChat(uId, uName, uImg,true);
	}
</script>
<#nested>
</body>
</html>
</#macro>
<#macro html_user_left  type=0>
	<div class="US-left fl">

        <div class="US-info">
            <ul class="clearfix">
                <li><a href="${bbs_url}xpabc/userCenter/mine/1.htm"><img src="<#if user??&&user.user_photo??&&user.user_photo!="">${bbs_img}${user.user_photo}<#else>${bbs_url}images/noavatar_medium.gif</#if>" height="70" width="70" alt=""></a></li>  
                <li><p><#if user.user_nickname??&&user.user_nickname!="">${user.user_nickname}<#elseif user.user_name??&&user.user_name!="">${user.user_name}<#else>无名氏</#if></p><p><a href="javascript:logout()">[注销]</a></p></li>
            </ul>
            <dl>
            	<span><b><a href="#">0</a></b><em>金币</em></span>
                <span class="span1"><b><a href="#">${articleSize}</a></b><em>文章</em></span>
                <span class="span1"><b><a href="#">${eliteSize}</a></b><em>精华</em></span>
            </dl>  
        </div>

        <div class="US-list">
            <h3>用户中心</h3>
            <ul>
                <li><a href="${bbs_url}xpabc/userCenter/mine/1.htm" <#if type==1>class="current"</#if>><i class="icon1" ></i><span>短消息</span><b> &gt; </b></a></li>
                <li><a href="${bbs_url}xpabc/userCenter/toChangePassword.htm" <#if type==2>class="current"</#if>><i class="icon2" ></i><span>个人设置</span><b> &gt; </b></a></li>
                <!--<li><a href="#" <#if type==3>class="current"</#if>><i class="icon3" ></i><span>积分</span></a><b> &gt; </b></li>-->
                <li><a href="${bbs_url}xpabc/userCenter/myArticles/1.htm" <#if type==4>class="current"</#if>><i class="icon4" ></i><span>我的文章</span><b> &gt; </b></a></li>
            </ul>
        <#if user??&&userPlates??<#--&&userPlates?size gt 0-->>
            <h3>版块中心</h3>
            <ul>
            	<li><a href="${bbs_url}xpabc/userCenter/toAddElite.htm" <#if type==5>class="current"</#if>><i class="icon4"></i><span>精华管理</span><b> &gt; </b></a></li>
            	<li><a href="#" <#if type==6>class="current"</#if>><i class="icon4" ></i><span>精华列表</span><b> &gt; </b></a></li>
            </ul>
        </#if>
        </div>
		
    </div>
</#macro>
<#macro html_user_right_top  type1=0 type2=0>
	<#if type1=1>
		
	<#elseif type1=2>
		<div class="US-title">
            <h2>
            	<#if type2==2>修改密码</#if>
            	<#if type2==3>修改头像</#if>
            	<#if type2==4>修改昵称</#if>
            </h2>
            <div class="US-tab">
                <#--<a href="personalfile.html" <#if type2==1>class="current"</#if>>个人档案</a>-->
                <a href="${bbs_url}xpabc/userCenter/toChangePassword.htm" <#if type2==2>class="current"</#if>>修改密码</a>
                <a href="${bbs_url}xpabc/userCenter/toChangePhoto.htm"<#if type2==3>class="current"</#if>>修改头像</a>
                <a href="${bbs_url}xpabc/userCenter/toChangeNickname.htm"<#if type2==4>class="current"</#if>>修改昵称</a>
            </div>
        </div>
	
    <#elseif type1=3>
    	
    <#elseif type1=4>
    	<div class="US-title">
            <h2>
            	<#if type2==1>我的主题</#if>
            	<#if type2==2>我的评论</#if>
            	<#if type2==3>精华文章</#if>
            	<a href="${bbs_url}xpabc/article/add.htm" class='post-a'>发表文章</a>
            </h2>
            <p class="p2">
                <!--<span>文章：<a href="">${articleSize}</a></span> 
                <span>精华：<a href="">${eliteSize}</a></span>
                <span>收藏贴：<a href="">0</a></span>
                <span>短消息：<a href="">0</a></span>
                <span>新通知：<a href="">0</a></span>-->
            </p>
            <div class="US-tab">
                <a href="${bbs_url}xpabc/userCenter/myArticles/1.htm" <#if type2==1>class="current"</#if>>我的主题</a>
                <a href="${bbs_url}xpabc/userCenter/myComments/1.htm" <#if type2==2>class="current"</#if>>我的评论</a>
                <a href="${bbs_url}xpabc/userCenter/myElite/1.htm" <#if type2==3>class="current"</#if>>精华文章</a>
                <#--<a href="javascript:void(0)" <#if type2==4>class="current"</#if>>我的推荐</a>
                <a href="javascript:void
                (0)" <#if type2==5>class="current"</#if>>我的收藏</a>
                <a href="javascript:void(0)" <#if type2==6>class="current"</#if>>评分记录</a>
                <a href="javascript:void(0)" <#if type2==7>class="current"</#if>>访问记录</a>-->
            </div>
        </div>
    <#elseif type1=5>
    	<div class="US-title">
            <h2>
            	精华管理
            </h2>
        </div>
    </#if>
     
</#macro>
<#macro logout_js>
	function logout(){
		$.ajax({
				type: "POST",
				url : "${bbs_url}xpabc/user/logout",
    			success : function(data){
    				var code = data.code;
    				var msg = data.msg;
    				if(code=="0000"){
    					location.href = "${bbs_url}xpabc/user/toLogin";
    				}
    			}
				
		});	
	}
</#macro>