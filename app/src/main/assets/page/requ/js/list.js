
var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();
var Page = function(){};
Page.cleanData = function(){

	$("#list").empty();
}

Page.showLoading = function(){

	Loading.show();
};

Page.hideLoading = function(){

	Loading.hide();
};
//添加技术需求requId, logo, name, hj, price, oldLogo,ownerName,lastModify,$("#list")
Page.addRequ = function(requId, logo, name, hj, price, oldLogo, ownerName,lastModify) {

	new RequItem(requId, logo, name, hj, price, oldLogo, ownerName, lastModify,$("#list"));
};


//加载数据超时提醒
Page.loadFailed = function(){
	
	$("#list").hide();
	new Dialog("抱歉,数据加载失败",false,$("body"),2000);
}

/**
 *调用action 下载完成之后调用代理对象的方法替换默认图片
*/
Page.onFinishDownload = function(){
	
	// 调用委托类的更新方法
	fileLoader.downloadingFile.deligate.onFileFinishLoad();
	
	// 继续下载文件
	fileLoader.doTask();
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
