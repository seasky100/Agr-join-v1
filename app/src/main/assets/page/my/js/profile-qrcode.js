var SDK = window.sdk;

$(function(){

//	Button.loadAllButton({
//		onClick: onBtnClick
//	});
});

var Page = function(){}
Page.initPage = function(qrCodeImg,logo,account, name) {
	$("#qrCode").attr("src",qrCodeImg);

	if(logo == null || logo.trim() == "") {

		logo = "../img/default-head-icon.jpg";
	}

	$("#logo").attr("src",logo);
	$("#account").text(account);
	$("#name").text(name);
}
