
var SDK = window.sdk;

$(function() {

	Button.loadAllButton({
		onClick : onBtnClick
	});
});

var checkCode;
var Page = function() {

};

Page.loadData = function(inputId, inputValue) {

	$("#"+inputId).val(inputValue);
}
function onBtnClick(id, e) {

	if("submit"==id) {

		//设置密码
		var password = $("#password").val();
		var repassword = $("#repassword").val();
		
		
		if(password == null || password.trim() == "") {
			
			SDK.clientHint("密码不能为空");
			return;
			
		}else if(repassword == null || repassword.trim() == "" || password != repassword){
			
			SDK.clientHint("密码两次输入不一致,请重新输入");
			$("#repassword").val("");
			return;
		}

		SDK.updatePassword(password);
	}else if("next"==id){

		//第二步中选择下一步,判断手机号是否为空,和验证码是为空
		var phone = $("#phone").val();
		var checkCode1 = $("#checkCode").val();
		if(phone == null || phone.trim() == '') {

			SDK.clientHint("手机号不能为空");
			return ;
		}

		if(checkCode1 == null || checkCode1.trim() == '') {

			SDK.clientHint("手机验证码不能为空");
			return ;
		}

		if(checkCode != checkCode1) {

			SDK.clientHint("手机验证码不正确,请重新输入");
			 $("#checkCode").val("")
            return ;
		}

		SDK.setPreference("forget_password_username", phone);

		$("#seq-panel li").removeClass("current");
		$("#seq-panel li:eq(1)").addClass("current");
		$("#password-panel").show();
		$("#phone-panel").hide();


	}
}

var sendCode = function() {

	//获取手机验证码
	var phone = $("#phone").val();
	if(phone == null || phone.trim() == "") {

	    SDK.clientHint("用户名不能为空");
	}else {
	    SDK.getCode(phone);
	    //禁用获取手机验证码的btn
	    $("#getCode").removeAttr("onclick");
	}
}
//等待120 秒
Page.wait120Seconds = function(checkCode1) {

	checkCode = checkCode1;
	var time = 120;
	$("#phone").attr("readonly",true);
	var timer = setInterval(function() {

			time -- ;
			$("#getCode").text(time + "秒后重新获取...");

			if(time <= 0) {

				clearInterval(timer);
				$("#phone").attr("readonly",false);
				$("#getCode").text("重新获取验证码");
				$("#getCode").attr("onclick","sendCode();");
			}
    	}, 1000);
}

Page.sendFailed = function() {

	 $("#getCode").attr("onclick","sendCode();");
}
