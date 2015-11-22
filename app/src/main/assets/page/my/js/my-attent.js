var SDK = window.sdk;
var Page = function(){}
var FILE = window.file;
var fileLoader = new FileLoader();

//清空列表
Page.cleanData = function(){

	$("#list").empty();
}

Page.showLoading = function(){

	Loading.show();
	//$("#list").hide();
}

Page.hideLoading = function(){

	Loading.hide();
//	$("#list").show();
}

//没有关注的专家的信息
Page.hintFindExpertFailed = function(){

	$(".error").show();
//	$("#list").hide();
}

//没有关注的企业的信息
Page.hintFindCompanyFailed = function(){

	$(".error").show();
//	$("#list").hide();
}

//添加关注的人id, name, logo, trade, type, attentTime, area, oldLogoName, updateDate, container
Page.addItem = function(id, name, logo, trade, type, attentTime, area, oldLogoName, updateDate){

	new AttentItem(id, name, logo, trade, type, attentTime, area, oldLogoName, updateDate, $("#list"));
}

Page.onFinishDownload = function(){
	
	// 调用委托类的更新方法
	fileLoader.downloadingFile.deligate.onFileFinishLoad();
	
	// 继续下载文件
	fileLoader.doTask();
}


Page.setMore = function (flag) {

	if(flag) {

		$("#switch").show();
	}else{

		$("#switch").hide();
	}
}

$(function() {

	Button.loadAllButton({
		onClick: OnBtnClick
	});
});

function OnBtnClick (id, e) {

	if(id == "switch") {

		SDK.nextPage();
	}
}