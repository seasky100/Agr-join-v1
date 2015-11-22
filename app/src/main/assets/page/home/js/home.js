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
Page.clearData = function() {

	$("#achv-list").html("");
	$("#requ-list").html("");
}

Page.hideSlideImg = function() {

	$("#slide-panel").hide();
}
//添加优质成果
Page.addAchv = function(achvId, logo, name, trade, price, ownerName, oldLogo, lastModify){

	new AchvItem(achvId, logo, name, trade, price, ownerName, oldLogo, lastModify, $("#achv-list"));
};

//添加技术需求
Page.addRequ = function(requId, logo, name, hj, price, oldLogo, ownerName,lastModify) {

	new RequItem(requId, logo, name, hj, price, oldLogo, ownerName, lastModify,$("#requ-list"));
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

Page.onDownloadError = function(){
	// 调用委托类的更新方法
	fileLoader.downloadingFile.deligate.onFileLoadError();

	// 继续下载文件
	fileLoader.doTask();
};


//绑定单击事件
$(function() {

	Button.loadAllButton({
		onClick : onBtnClick
	});
});

function onBtnClick(id, e) {

	if(id == "achv") {
		
		SDK.openAchvList();
	}else if(id=="requ") {
		
		SDK.openRequList();
	}else if(id=="comp") {
		
		SDK.openCompList();
	}else if(id=="expert") {
		
		SDK.openExpertList();
	}else if(id=="organ") {

        SDK.openOrganList();
    }else if(id=="product") {

		 SDK.openProductList();
	 }


}

Page.clearImg = function() {

	$("#img-container").html();
}
Page.addImg = function(img) {

	if(img != null && img.trim() != "") {

		new ImgItem(img,$("#img-container"));
	}
}

Page.initSwiper= function() {

    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true
    });
}

/** 加载成果失败**/
Page.loadAchvFailed = function() {

	$("#achv-panel").hide();
}

/** 加载需求失败**/
 Page.loadRequFailed = function() {

 	$("#requ-panel").hide();
 }
