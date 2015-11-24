var SDK = window.sdk;
var Page = function(){};

Page.showLoading = function(){

	Loading.show();
};

Page.hideLoading = function(){

	Loading.hide();
};
//专家名称 + 职称 、单位 + 部门 、行业、地区
Page.addItem = function(id, logo, name, jd, compName, invest){

	new FundItem(id, logo, name, jd, compName, invest, $("#panel"));
};

Page.hintError = function(visble){
	
	if(visble)
		$(".error").show();		
	else
		$(".error").hide();
};


$(function(){

	
	$("#searchInput").bind("click", function(){
	
		SDK.openSearchPage();
	});
});


/**
 *显示/隐藏更多按钮,并设置将要加载的页数
 */
Page.moreBtn = function(flag) {

	if(flag=="true") {

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

//加载数据超时提醒
Page.loadExpertFailed = function(){

	new Dialog("抱歉,专家数据加载失败",false,$("body"),2000);
};