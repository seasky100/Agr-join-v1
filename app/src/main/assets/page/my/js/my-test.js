
var SDK = function(){}

SDK.openPage = function(page){
	
	//alert("open  page :" + page);
}

/*
* 测试过程
*/
$(function(){
		   
	//设置头像	   
	Page.setMyIcon("../img/default-head-icon.jpg");
	
	//设置个人信息
	Page.setProfile("王海", "还没有登录丰景账号", ""); 
	
	//设置关注的项目数量
	Page.hintProjUpdate(5);
	
	//设置关注的人的数量
	Page.hintAttended(0);

});
