var Page = function(){}
var SDK = window.sdk;

function openSite(){
	
	SDK.openPage("http://www.fundview.cn");
}

Page.showLoading = function(){

	Loading.show();
}

Page.hideLoading = function(){

	Loading.hide();
}

function openDail(obj) {
	
	var tel = $(obj).text();//alert(tel);
	SDK.dail(tel);
}

function openMail(obj) {
	
	var email = $(this).text();
	SDK.mailTo("admin@fundview.cn");
}

function openWebSite(obj) {
	
	var url = $(this).text();
	SDK.openPage("http://www.fundview.cn");
}