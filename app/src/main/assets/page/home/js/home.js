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

	$("#company-list").html("");
	$("#expert-list").html("");
	$("#achv-list").html("");
	$("#requ-list").html("");
	$("#proj-list").html("");
	$("#product-list").html("");
}

Page.hideSlideImg = function() {

	$("#slide-panel").hide();
}

//企业LOGO、企业名称、所属行业、地区、展位（2C12）；compId, logo, compName, trade, expoNo, area, oldLogo, lastModify, container
Page.addCompany = function(compId, logo, compName, trade, area, updateDate, oldLogo, expoNo){

	if(area == null) {

		area = "";
	}

	new CompItem(compId, logo, compName, trade, expoNo, area, oldLogo, updateDate, $("#company-list"));
};

//专家名称 + 职称 、单位 + 部门 、行业、地区
Page.addExpert = function(expertId, logo, expertName, professionalTitle, trade, /*workUnit, dept, */area, updateDate, oldFileName){
	if(area == null) {

		area = "";
	}

	new ExpertItem(expertId, logo, expertName, professionalTitle, trade,/*workUnit, dept,  */area, updateDate,oldFileName, $("#expert-list"));
};


//添加优质成果
Page.addAchv = function(achvId, logo, name, trade, price, ownerName, oldLogo, lastModify){

	new AchvItem(achvId, logo, name, trade, price, ownerName, oldLogo, lastModify, $("#achv-list"));
};

//添加技术需求
Page.addRequ = function(requId, logo, name, hj, price, oldLogo, ownerName,lastModify) {

	new RequItem(requId, logo, name, hj, price, oldLogo, ownerName, lastModify,$("#requ-list"));
};

//添加id, logo, name, unit, price, oldLogo, ownerName, lastModify, container
Page.addProduct = function(id, logo, name, unit, price, oldLogo, ownerName,lastModify) {

	new ProductItem(id, logo, name, unit, price, oldLogo, ownerName, lastModify,$("#product-list"));
};


//专家名称 + 职称 、单位 + 部门 、行业、地区
Page.addItem = function(id, logo, name, jd, compName, invest){

	new FundItem(id, logo, name, jd, compName, invest, $("#proj-list"));
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
	}else if(id=="fundProject") {

    	SDK.openFundProjectList();
    }else if(id="guestExpert") {

        SDK.openGuestExpert();
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

/** 加载企业失败**/
Page.loadCompanyFailed = function() {

	$("#company-panel").hide();
}

/** 加载专家失败**/
Page.loadExpertFailed = function() {

	$("#expert-panel").hide();
}

/** 加载成果失败**/
Page.loadAchvFailed = function() {

	$("#achv-panel").hide();
}

/** 加载需求失败**/
 Page.loadRequFailed = function() {

 	$("#requ-panel").hide();
 }

 /** 加载产品失败**/
  Page.loadProductFailed = function() {

  	$("#product-panel").hide();
  }

 /** 加载项目失败**/
  Page.loadProjFailed = function() {

  	$("#proj-panel").hide();
  }
