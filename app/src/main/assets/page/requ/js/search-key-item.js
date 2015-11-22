var  SearchKeyItem = function(key, container){
	
	// 初始化属性
	this.key = key;
	
	// 定义视图和父容器
	this.view = null;
	this.container = container;
	
	// 执行初始化操作
	this.init();
};

SearchKeyItem.template = null; // 视图模板

SearchKeyItem.prototype.init = function(){
	
	if(SearchKeyItem.template == null)
		SearchKeyItem.template = $("#template").html();
	
	this.view = this.container.append(SearchKeyItem.template).children().last();
	this.view.attr("id", this.key);
	
	this.view.find("[sid=key]").text(this.key);
	this.view.find(".delete-icon").attr("id", this.key);
	
	// 绑定按钮事件	
	Button.create(this.view, {onClick: this.onItemClick});
	
	Button.create(this.view.find(".delete-icon"), {onClick: this.onDelItemClick});
}


SearchKeyItem.prototype.onItemClick = function(id, e){
	
	SDK.clickHistory(id);
}

SearchKeyItem.prototype.onDelItemClick = function(id, e){

	if(SDK.deleteHistory(id))
		e.view.parent().remove();
}