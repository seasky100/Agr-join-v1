
var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();
var Page = function(){};

Page.showLoading = function(){

	Loading.show();
};

Page.hideLoading = function(){

	Loading.hide();
};
//企业LOGO、企业名称、所属行业、地区、展位（2C12）；compId, logo, compName, trade, expoNo, area, oldLogo, lastModify, container
Page.addCompany = function(compId, logo, compName, trade, area, updateDate, oldLogo, expoNo){
	
	if(area == null) {
		
		area = "";
	}
	
	new CompItem(compId, logo, compName, trade, expoNo, area, oldLogo, updateDate, $("#panel"));
};

Page.onFuncBtnClick = function(){
	
	//alert($("#myDialog"));
	if($("#myDialog").html() == null || $("#myDialog").html().trim() == "" || $("#myDialog").is(":hidden")){
	
		MyDialog.open("filter.html");
	}
	
};

//加载数据超时提醒
Page.loadCompFailed = function(){
	
	Loading.hide();
	//$("#panel").html("<div style="width:100%;text-align:center;margin-top:50px;">抱歉,企业数据加载失败...</div>");
	new Dialog("抱歉,企业数据加载失败",false,$("body"),2000);
};

/**
 *调用action 下载完成之后调用代理对象的方法替换默认图片
*/
Page.onFinishDownload = function(){
	
	// 调用委托类的更新方法
	fileLoader.downloadingFile.deligate.onFileFinishLoad();
	
	// 继续下载文件
	fileLoader.doTask();
};

/**
 *显示/隐藏更多按钮,并设置将要加载的页数
 */
Page.moreBtn = function(flag) {

	if(flag == "true") {

		//显示加载更多
		$("#switch").show();
	}else {

		$("#switch").hide();
	}
}

//绑定单击事件
$(function() {

	Button.loadAllButton({
		onClick : onBtnClick
	});
});

function onBtnClick(id, e) {

	if(id == "switch") {

		SDK.nextPage();
	}
}
