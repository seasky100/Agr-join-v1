var SDK = window.sdk;

var Page = function(){
	
}

Page.cleanData = function(){

	$("#list").empty();
}

Page.showLoading = function(){

	Loading.show();
}

Page.hideLoading = function(){

	Loading.hide();
}

Page.addItem = function(key){

	$(".no-history").hide();
	new SearchKeyItem(key, $("#list"));
}

Page.showNoHistory = function(){

	$(".no-history").show();
}
