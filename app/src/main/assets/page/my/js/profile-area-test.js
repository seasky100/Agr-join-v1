
var SDK = function(){}

SDK.openCity = function(id){
	//alert("id:::::::"+ id + ":");
	Page.updateCity([{"id":0,"levels":0,"parentId":11,"name":"0name"},{"id":1,"levels":1,"parentId":12,"name":"1name"},{"id":2,"levels":2,"parentId":13,"name":"2name"},{"id":3,"levels":3,"parentId":14,"name":"3name"},{"id":4,"levels":4,"parentId":15,"name":"4name"}]);
}

SDK.save = function(area){
	//alert("save:"+ area );
	Page.onBackBtnClick();
}

SDK.closePage = function(){
	//alert("closePage" );
}

$(function(){
	
	Page.showLoading();
	
	Page.initPage([{"id":0,"levels":0,"parentId":11,"name":"0name"},{"id":33,"levels":1,"parentId":12,"name":"1name"},{"id":2,"levels":2,"parentId":13,"name":"2name"},{"id":3,"levels":3,"parentId":14,"name":"3name"},{"id":4,"levels":4,"parentId":15,"name":"4name"}]);
		
	setTimeout(function(){	
						
		Page.hideLoading();
	},2000);
	
});
