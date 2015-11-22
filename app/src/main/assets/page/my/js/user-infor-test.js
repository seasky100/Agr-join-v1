var SDK = function(){}

SDK.openMyAttentUser = function(){
	//alert(1);
	//Page.showHintDialog(true);
}

SDK.downLogo= function(callback,local, fileName, urlPath){
	//alert(urlPath);
	onFinishDownload();
}

SDK.attentUser= function(id){
	
	Page.showHintDialog(true);
}

SDK.cancelAttentUser= function(id){
	
	Page.showHintDialog(true);
}

var FILE = function(){}


FILE.getSysPath= function(){
	
	return "/sdcard/";
}

FILE.isFileExist= function(src){
	
	return false;
}

$(function(){
	
	Page.initPage("fj1001", "王海", "北京,朝阳", "1,2|金钱", "技术", "www.findvie.com/1.jpg", 1);

});
