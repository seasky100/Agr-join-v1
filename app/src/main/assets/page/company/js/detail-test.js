var SDK = function(){}

SDK.call = function(tel){
	//alert(" cal:" + tel);
}

SDK.openProjCalculator = function(){
	//alert(" openProjCalculator:");
	Page.initCalculator(111, 111, "areaName", "inputType", 10000, "亩", "basis", "investmentreturn", "musttodo");
}

SDK.openProjDetails = function(id){
	//alert(" openProjDetails:" + id);
}

SDK.openAnalysisDetail = function(id){
	//alert(" openAnalysisDetail:" + id);
}

/*项目详细信息页面测试*/
$(function(){
		   
    //complexIdx, capitalIdx, areaIdx, techIdx, attent, profitType, profit
	Page.setProjIndex(83, 89, 78, 45, 54, 1, "22");   
		
	// 显示loading
	Page.init(1, "www", "aaaa", 48, "ada");
	
	
	/*for(var i = 0; i < 9; i++){
		
		Page.addImage(i+1,"http://www.findview.cn/static/thumb/proj_img/20120402/2012040217294814022_388_289.jpg", "天南星" + i);
	}
	Page.slideImg();
	
	Page.addAnalysis("项目分析",1,"山东",2,"1",2312,"万元");
	for(var i = 0; i < 9; i++){
		
		Page.addSeed(i,i,"大堂狐业"+i,"山东"+i,"0000000"+i);
	}
	Page.initSeedIndex();
	Page.slideSeeds();
	Page.attachCallEvent();
	Page.showFuncDialog();
	// 数据加载完成隐藏loading
	setTimeout(function(){	
						
		Page.initFinish();
	},2000);
	
	*/
	
	Page.setCalculator(1,1, "山东", 2, "1", 231, "万");
		
	
	//MyDialog.open("area.html");
	
	
	//setTimeout(function(){
	/*$(".areaPanel").show();
		$(".areaPanel").load("area.html");
	//$(".areaPanel").show();
		//$(".areaPanel").load("area.html");
	//},2000);
	

	
	/*var box = new PicBox(imageArray);*/
	//box.view.appendTo($("[sid=box-panel]"));
});





