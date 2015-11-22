var SDK = window.sdk;

$(function(){
	
	Button.create($("#takePhoto"), {onClick: function(id, e){

		SDK.takePhoto();
	}});
	
	Button.create($("#fromLocal"), {onClick: function(id, e){
													  
		SDK.takeLocalPic();
	}});
	
	Button.create($("#close"), {onClick: function(id, e){
												   
		SDK.closePage();
	}});
});

var Page = function(){
	
};

Page.setHeadIcon = function(icon){

	if(null != icon )
		$("#head").attr("src", icon + "?" + new Date().getTime());
	else
		$("#head").attr("src", "../img/default-head-icon.jpg");
};

Page.changePageStatus = function(status){

	if(1 == status)
		$("#close").text("完成");
	else
		$("#close").text("取消");
};

