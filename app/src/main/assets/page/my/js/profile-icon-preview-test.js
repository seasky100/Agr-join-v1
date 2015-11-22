
var SDK = function(){}

SDK.savePic = function(width, height){
	//alert("savePic"+ width + ":" + height);
}

$(function(){
	
	Page.showLoading();
	
	Page.picShow("http://img0.bdstatic.com/img/image/shouye/syrx.jpg");
	
	setTimeout(function(){	
						
		Page.hideLoading();
	},2000);
});
