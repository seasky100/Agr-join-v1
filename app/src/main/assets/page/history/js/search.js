var SDK = window.sdk;

var Page = function(){
	
};

Page.cleanData = function(){

	$("#list").empty();
};

Page.showLoading = function(){

	Loading.show();
};

Page.hideLoading = function(){

	Loading.hide();
};

Page.addItem = function(key, type){

	$(".no-history").hide();
	new SearchKeyItem(key, type,$("#list"));
};

Page.showNoHistory = function(){

	$(".no-history").show();
};
