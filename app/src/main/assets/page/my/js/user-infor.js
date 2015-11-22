var Page = function(){}
var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();

//群成员logo保存地址 
var MemberlogoPath = "/findView/data/images/Member/";

var requires = new Array("找项目","找种源","找资金","找合作");

var src;
var statusNow;
var userId;

$(function(){
	Button.loadAllButton({
		onClick: onBtnClick
	});
});

Page.showLoading = function(){
	
	Loading.show();
}

Page.hideLoading = function(){
	
	Loading.hide();
}

Page.initPage = function(uid, account, name, area, require, provide, logo, status){

	userId = uid;
	
	if(null != account && account.trim() != ""){
		
		$('.base-info').find('[sid=account]').text("丰景号:  "+account).show();
	}else{
		
		$('.base-info').find('[sid=account]').hide();
	}	
	
	if(null != name)
		$('.base-info').find('[sid=name]').text(name);	
		
	if(null != area){
		
		if(area.indexOf(","))
			$('.info').find('[sid=area]').text(area.replace(","," "));
		else
			$('.info').find('[sid=area]').text(area);
	}

	if(null != require){
		if(require.indexOf("|") > -1){		
			var txt = new Array();
			var attrs = require.split("|");
			if("none" != $.trim(attrs[0])){
				var selects = $.trim(attrs[0]).split(",");
				for(var i = 0 ; i < selects.length; i++)
					txt.push(requires[selects[i]-1]);
			}
			if("none" != $.trim(attrs[1]))
				txt.push("其他");
							
			if(txt.length != 0)
				$('.info').find("[sid=require]").text(txt.toString());
		 }else{
		 	
	 		$('.info').find("[sid=require]").text(require);
		 }
	}

	if(null != provide)
		$('.info').find('[sid=provide]').text(provide);						
			
	if(null != logo){
		var driver = FILE.getSysPath();
		var local = driver + MemberlogoPath;//logo的本地存储路径
		var fileName = logo.split("/").pop();//logo 的文件名
	
		src = local + fileName;
		if(FILE.isFileExist(src) == true){			
			$(".base-info img").attr("src", src);
		}else{
			$(".base-info img").attr("src","../img/default_avatar.png");
			loadIcon(logo, local, fileName);
		}
	}
	
	statusNow = status;
	if(null != status){
		if(status == 0)
			$("#attention").text("关注");
		else
			$("#attention").text("取消关注");
	}
}


Page.showHintDialog = function(flag){

	if(flag){
		if(statusNow == 0)
			$(".hintTxt").text("关注成功");
		else
			$(".hintTxt").text("取消成功");
		$(".hintImg").removeClass("fail").addClass("success");
		$(".saveDialog").show();	
	}else{
		if(statusNow == 0)
			$(".hintTxt").text("关注失败");
		else
			$(".hintTxt").text("取消关注失败");
		$(".hintImg").removeClass("success").addClass("fail");
		$(".saveDialog").show(); 
	}
	
	setTimeout(function(){
	
		Page.hideHintDialog(flag);
	}, 1000);
}


Page.hideHintDialog = function(flag){

	$(".saveDialog").hide();
	
	if(statusNow == 0){
		if(flag)
			SDK.openMyAttentUser();//跳转到我关注的人的界面	
	}else{
		if(flag){
			$("#attention").text("关注");
			statusNow = 0;
		}
	}

}

function loadIcon(urlPath,local,fileName){
	
	//执行下载
	SDK.downLogo("onFinishDownload",local, fileName, urlPath);
}

function onFinishDownload(){
	
	if(FILE.isFileExist(src) == true)
		$(".base-info img").attr("src",src);
}

function onBtnClick(id, e){
	
		var ele = $(e.view);
		
		if(id == "attention"){
			if(ele.text() == "关注"){
				SDK.attentUser(userId);
			}else if(ele.text() == "取消关注"){
				SDK.cancelAttentUser(userId);
			}
		}else if(id = "chat"){
			SDK.chat(userId);
		}
}

Page.hideFunc = function(){
	
	$("[sid=funcBtn]").hide();
}

Page.showFunc = function(){
	
	$("[sid=funcBtn]").show();
}