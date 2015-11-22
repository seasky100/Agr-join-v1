
var SDK = function(){}

SDK.loadMoreProjs = function(){
	
	Page.cleanData();
	
	for(var i = 0; i < 9; i++){
		Page.addItem(i+1, 1, "http://www.findview.cn/static/thumb/proj_img/20120402/2012040217294814022_388_289.jpg", "test" + i, 1, "56%", 250, 68, new Date().getTime());
	}
	
	Page.switchVisble(true);
}

SDK.openProjDetail = function(id){

	//alert("open proj page :" + id);
}

SDK.openSearchPage = function(){

	//alert("open search page!");
}

SDK.setFilterType = function(type){

	//alert("setting filter type:" + type);
}

var FILE = function(){}

FILE.getSysPath = function(id){

	return "/";
}

FILE.isFileExist = function(src){

	return true;
}

/*
* 测试过程
*/
$(function(){
		   
	// 显示过滤对话框
	Page.onFuncBtnClick();

	// 显示loading
	Page.showLoading();
	
	for(var i = 0; i < 9; i++){
		
		Page.addItem(i+1, 1, "http://www.findview.cn/static/thumb/proj_img/20120402/2012040217294814022_388_289.jpg", "天南星" + i, i%2, "56%", 250, 68, new Date().getTime());
	}
	
	// 显示switch
	Page.switchVisble(true);
	
	// 数据加载完成隐藏loading
	setTimeout(function(){	
						
		Page.hideLoading();
	},2000);

});
