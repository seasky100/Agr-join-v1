
var SDK = function(){}

SDK.openPage = function(page){
	//alert("open  page :" + page);
}

/*
* 测试过程
*/
$(function(){
	
	
	
	Page.hidePage();

	Page.initPage("http://www.findview.cn/static/thumb/proj_img/20130628/2013062811162335834_388_289.png", "王海", "123456", 2, "北京,东城", "北京东城区阳坊路288号5单元101", "13558956548", "4|宝贝", "1,3,4|none", "资金,场地");
		/*   
	Page.setHeadIcon("http://www.findview.cn/static/thumb/proj_img/20130628/2013062811162335834_388_289.png");
	
	Page.setName("王海"); 
	
	Page.setAccount("123456");
	
	Page.setSex(2);
		
	Page.setArea("北京,东城");
	
	Page.setAddr("北京东城区阳坊路288号5单元101");
	
	Page.setTel("13558956548");

	Page.setWork("种植,养殖");
	
	Page.setRequire("1,3,4|none");
	
	Page.setProvide("资金,场地");
	*/
	Page.showPage();
});
