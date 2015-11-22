var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();
var logoPath = "/findView/data/images/company/logo/";

var Page = function(){
	
}

Page.cleanData = function(){

	$("#list").empty();
}

Page.showLoading = function(){

	Loading.show();
}

Page.hideLoading = function(){

	Loading.hide();
}

Page.addItem = function(compId, logo, compName, trade, attent, area, lastModify){

	new CompItem(compId, logo, compName, trade, attent, area, lastModify, $("#list"));
}

Page.hintError = function(visble){
	
	if(visble)
		$(".error").show();		
	else
		$(".error").hide();
}

Page.onFinishDownload = function(){
	
	// 调用委托类的更新方法
	fileLoader.downloadingFile.deligate.onFileFinishLoad();
	
	// 继续下载文件
	fileLoader.doTask();
}

$(function(){
	
	$("#searchInput").bind("click", function(){
	
		SDK.openSearchPage();
	});
});



