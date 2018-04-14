<#import "../common.ftl" as cc>
<@cc.html_head>
</@cc.html_head>
<@cc.html_top_noLogin>
</@cc.html_top_noLogin>
<!--contentlogin-->
<div class="US-wrap clearfix">

    <div class="US-left fl">

        <@cc.html_user_left type=2></@cc.html_user_left>

    </div>

    <div class="US-right fl">

        <@cc.html_user_right_top type1=2 type2=3></@cc.html_user_right_top>

        <div class="US-cont">

            <form>
            <div class="cont-perpic">
               <p class='col-gray3 row'>使用新头像</p>
               <p class='col-gray9 row'>请选择新的照片文件,文件需小于3MB</p>
               <div class="uploadbox">
                    <div class="uploadbtns fl">
                        <div class="fileInput">
                            <input type="file" name="upfile" id="upfile" class="upfile" onchange="document.getElementById('upfileResult').innerHTML=this.value" />
                            <input class="upFileBtn" type="button" value="上传图片" onclick="document.getElementById('upfile').click()" />
                        </div>
                        <span class="tip" id="upfileResult">支持jpg、png、bmp等格式。</span>

                    </div>
                    <div class="uppicshow fl">
                        <p class='tit'>当前照片</p>
                        <div class="pic">
                            <img src="<#if user.user_photo??&&user.user_photo!="">${bbs_img}${user.user_photo}</#if>" width="120px" height="120px" alt="">
                        </div>
                    </div>
                </div>
                <div class="upnewpic" style='display:block;'>
                    <p>重新选择图片</p>
                    <div class="picbox" id="imgdiv">
                        <img src="" width="120px" height="120px" id="imgShow" alt="">
                    </div>
                    <div class="btn ma-t30 ml20"><a href="javascript:change();" id="save">保存</a></div>
                </div>
            </div>
             </form>

        </div>

    </div>



</div>
<@cc.html_foot>
</@cc.html_foot>
<@cc.html_js>
	<script src="${bbs_url}js/uploadPreview.js" type="text/javascript"></script>
	<script src="${bbs_url}js/ajaxfileupload.js" type="text/javascript"></script>
	<script>
		window.onload = function () { 
            new uploadPreview({ UpBtn: "upfile", DivShow: "imgdiv", ImgShow: "imgShow" });
        }
         function change(){
         	$("#save").attr("disabled",true);
    		var filename = $("#upfile").val();
    		var b = isAllow(filename);
			if(!b){
				alert("图片格式有误,请确认!");
				return;
			}
        	$.ajaxFileUpload({  
            	url:'${bbs_url}xpabc/userCenter/changePhoto',  
            	secureuri:false,  
            	fileElementId:"upfile",  
            	dataType: 'text',  
            	success: function (data, status){
            		if(data=="success"){
            			location.reload();
            		}else if(data=="noLogin"){
            			alert("当前用户没有登录!");
            			$("#save").attr("disabled",false);
            		}else{
            			alert("上传失败");
            			$("#save").attr("disabled",false);
            		}
            	},  
            	error: function (data, status, e){  
                	alert("上传失败");
            		$("#save").attr("disabled",false);
            	}  
              	
        	});
    	}
    	function isAllow(filename){
			var suffx = filename.substring(filename.lastIndexOf("."));
    		suffx = suffx.toLowerCase();
    		var allowTypes = new Array(".jpg",".jpeg",".png",".gif");
			var b = false;
			for(i=0;i<allowTypes.length;i++){
				var allowType = allowTypes[i];
				if(allowType==suffx){
					b=true;
					break;
				}
			}
			return b;
		}
		<@cc.logout_js>
		</@cc.logout_js>
	</script>
</@cc.html_js>