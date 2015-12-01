var Page = function(){}
var SDK = window.sdk;

function update() {

	SDK.update();
}


function openDail(obj) {

	var tel = $(obj).text();//alert(tel);
	SDK.dail(tel);
}

function openMail(obj) {

	var email = $(this).text();
	SDK.mailTo(email);
}

function openWebSite(obj) {

	var url = $(this).text();
	SDK.openPage("http://www.fundview.cn");
}