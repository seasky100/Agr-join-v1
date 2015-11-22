
var SDK = window.sdk;

$(function() {

	Button.loadAllButton({
		onClick : onBtnClick
	});
});

var Page = function() {

};

Page.setMyIcon = function(icon) {
	if (null != icon)
		$("#myIcon").attr("src", icon);
	else 
		$("#myIcon").attr("src", "../img/default-head-icon.jpg");
};

Page.setProfile = function(username, name, type) {
	
	if(username == null || $.trim(username) == '') {
		
		$("#username").text('未登录');
		$("#name").text('登录后享受更多特权');
	}else {
		if(name == null || name.trim() == "")
			name="完善资料";
		$("#name").text(name);
		$("#username").text(username);
	}

	if(type > 2) {//未登录的和专家/企业可以有关注

		$("#attent-panel").hide();
	}
};

function onBtnClick(id, e) {

	if("login"==id) {
		
		var name = $("#username").val();
		var password = $("#password").val();
		
		
		if(name != null && name.trim() != "" && password != null && password.trim() != "") {
			
			SDK.login(name,password);
			
		}else {
			
			SDK.failHint("用户名或密码不能为空");
		}
	}else if("forgetPassword"==id){
		
		SDK.forgetPassword();
	}else if("reg"==id) {
		
		
	}else if("wx"==id) {
		
		
	}else {
		
		SDK.openPage(id);
	}
}


