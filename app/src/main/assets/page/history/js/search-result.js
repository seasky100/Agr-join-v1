var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();

var Page = function(){
	
};

Page.cleanData = function(){
	
	$("#searchResult-panel").html("");

};
Page.showLoading = function(){

	Loading.show();
};

Page.hideLoading = function(){

	Loading.hide();
};

//添加优质成果
Page.addAchv = function(achvId, firstImg, name, price, oldLogo,lastModify) {
	
	new AchvItem(achvId, firstImg, name, price, oldLogo,lastModify,$("[sid=achvPanel]"));
};

//添加技术需求
Page.addRequ = function(requId, logo, name, trade, price, oldLogo,lastModify) {
	
	new RequItem(requId, logo, name, trade, price, oldLogo,lastModify,$("[sid=requPanel]"));
};

Page.addExpert = function(expertId, logo, expertName, trade, attent, area, updateDate, oldFileName){
	
	if(area == null) {
		
		area = "";
	}

	new ExpertItem(expertId, logo, expertName, trade, attent, area,oldFileName, updateDate, $("[sid=expertPanel]"));
};

Page.addCompany = function(compId, logo, compName, trade, attent, area, updateDate, oldLogo){
	
	if(area == null) {
		
		area = "";
	}
	
	new CompItem(compId, logo, compName, trade, attent, area,oldLogo, updateDate, $("[sid=compPanel]"));
};


Page.hintError = function(visble){
	
	if(visble)
		$(".error").show();		
	else
		$(".error").hide();
};

Page.onFinishDownload = function(){
	
	// 调用委托类的更新方法
	fileLoader.downloadingFile.deligate.onFileFinishLoad();
	
	// 继续下载文件
	fileLoader.doTask();
};




