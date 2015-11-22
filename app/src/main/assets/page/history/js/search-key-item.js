var  SearchKeyItem = function(key,type, container){
	
	// 初始化属性
	this.key = key;
	this.type = type;
	
	// 定义视图和父容器
	this.view = null;
	this.container = container;
	
	// 执行初始化操作
	this.init();
};

SearchKeyItem.template = '<div class="item" display="button"><p class="content" sid="key"></p><p class="delete-icon"> </p></div>'; // 视图模板

SearchKeyItem.prototype.init = function(){
	
	this.view = this.container.append(SearchKeyItem.template).children().last();
	this.view.attr("id", this.key);//alert(this.type);
	this.view.attr("param", this.type);
	
	this.view.find("[sid=key]").text(this.key);
	this.view.find(".delete-icon").attr("id", this.key);
	
	// 绑定按钮事件	
	Button.create(this.view, {onClick: this.onItemClick});
	
	Button.create(this.view.find(".delete-icon"), {onClick: this.onDelItemClick});
};


SearchKeyItem.prototype.onItemClick = function(id, e){
	
	SDK.clickHistory(id, parseInt(e.view.attr("param")));
};

SearchKeyItem.prototype.onDelItemClick = function(id, e){
	
	if(SDK.deleteHistory(id, parseInt($(e.view).parents(".item").attr("param"))))
		e.view.parent().remove();
};