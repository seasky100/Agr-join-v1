
var SDK = function(){}

SDK.save = function(addr){
	
	//alert("save:"+ addr);
}

SDK.closePage= function(){
	
	//alert("close");
}

$(function(){
	
	Page.setName("王海");
	Page.save();
	Page.showHintDialog(true);
});
