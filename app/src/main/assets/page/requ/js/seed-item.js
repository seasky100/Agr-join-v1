/*项目种源企业*/
var Requ = function(id, name, info, tel, container){

	// 初始化属性
	this.id = id;
	this.name = name;
	this.info = info;
	this.tel = tel;
	// 定义视图和父容器
	this.view = null;
	this.container = container;
	
	// 执行初始化操作
	this.init();
}

Requ.template = null;

Requ.prototype.init = function(){

	if(Requ.template == null)	
		Requ.template = $("#requTemplate").html();
	
	this.view = this.container.append(Requ.template).children().last();

	this.view.attr("id", this.id);
	this.view.find("[sid=name]").text(this.name);
	this.view.find("[sid=requInfo]").text(this.info);
	
	this.attachEvent();
}

Requ.prototype.attachEvent = function(){

/*	this.view.find("[sid=name],[sid=requInfo]").on("click", this, function(e){

		var t = e.data;
		new HintDialog("是否直接电话联系", "拨号", "取消", $("body"), call, t.tel);
	});*/
	var id = this.id;
	var requName = this.name;
	var tel = this.tel;
	var time = lastModify;
	this.view.find("[sid=name],[sid=requInfo]").on("click", this, function(e){
		
		//打开需求详细页面
		SDK.openRequDetail(id, requName, tel, time);
	});
}
