var SDK = window.sdk;
var names = "";
var ids = "";
var Page = function(){};

Page.showLoading = function(){

	Loading.show();
}

Page.hideLoading = function(){

	Loading.hide();
}


Page.clearData = function() {

	$("#area-panel").empty();
}
Page.loadItem = function(id, name, level, selectedId){

	new AreaItem(id, name, level, selectedId,  $("#area-panel"));
}

Page.hintFail = function(){

	$(".success").removeClass("success").addClass("fail");
	$(".saveDialog").show();
	setTimeout("dialogHide()",1000); 
}

Page.showHintDialog = function(flag){

	if(flag){
		
		$(".hintTxt").text("保存成功");
		$(".hintImg").removeClass("fail").addClass("success");
		$(".saveDialog").show();
	}else{
		
		$(".hintImg").text("保存失败");
		$(".hintTxt").removeClass("success").addClass("fail");
		$(".saveDialog").show();
	}
	
	setTimeout(function(){
		Page.hideHintDialog(flag);
	}, 1000);
}

Page.hideHintDialog = function(flag){

	$(".saveDialog").hide();
	
	if(flag)
		SDK.closePage();
}


function dialogHide(){
	$(".saveDialog").hide();
	SDK.closePage();
}

var AreaItem = function(id, name, level, selectedId, container){
	
	// 初始化属性
	this.id = id;
	this.name = name;
	this.level = level;
    this.selectedId = selectedId;

	// 定义视图和父容器
	this.view = null;
	this.container = container;
	
	// 执行初始化操作
	this.init();
};

AreaItem.template = null; // 视图模板

AreaItem.prototype.init = function(){
	
	if(AreaItem.template == null)
		AreaItem.template = $("#template").html();
		
	this.view = this.container.append(AreaItem.template).children().last();
	this.view.attr("id", this.id);
	this.view.attr("level", this.level);

	this.view.text(this.name);
	if(this.level < 3)
		this.view.addClass("arrow");

	if(this.id == this.selectedId) {

	    this.view.css("background","#848484");
	}
	// 绑定按钮事件	
	Button.create(this.view, {onClick: this.onItemClick});
}

AreaItem.prototype.onItemClick = function(id, e){

    names = names + $(e.view).text() + ",";
    ids = ids + $(e.view).attr("id") + ",";
	if((e.view).attr("level") < 3){

		SDK.openCity(parseInt(id), parseInt((e.view).attr("level")));
	} else {

		SDK.save(ids, names);
	}

}