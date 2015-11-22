
var SDK = function(){}

SDK.save = function(addr){
	//alert("save:"+ addr);
}

$(function(){
	
	
	Page.setAddr("北京青年汇家园4号1213\n将哈哈");
	Page.save();
	Page.showHintDialog(true);
	
	setTimeout(function(){	
						
		Page.hideHintDialog();
	},2000);
});
