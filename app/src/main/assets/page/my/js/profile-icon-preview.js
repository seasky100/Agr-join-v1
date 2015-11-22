var SDK = window.sdk;

$(function(){
	
	Button.create($("#funcPanel"), {onClick:onBtnClick});
});

var Page = function(){
	
};

Page.showLoading = function(){

	Loading.show("正在保存");
};

Page.hideLoading = function(){

	Loading.hide();
};

Page.picShow = function(path){

	if(null != path)
		$(".head-panel img").attr("src", path + "?" + new Date().getTime());
};

function onBtnClick(id, e){

	SDK.savePic(320, 240);
}


