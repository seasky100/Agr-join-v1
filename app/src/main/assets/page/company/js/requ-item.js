/**
 * 技术需求 Item requId, logo, name, hj, price, oldLogo,ownerName,lastModify,$("#list")
 */
var RequItem = function(requId, name, hj, price, ownerName, lastModify, container) {

	// 初始化属性
	this.id = requId;// 需求id
	this.name = name; // 需求名字
	this.hj = hj;// 需求行业
	this.price = price;// 需求价格
	this.ownerName = ownerName;//拥有者名称
	this.lastModify = lastModify;// 最后修改时间

	// 定义视图和父容器
	this.view = null;
	this.container = container;

	// 执行初始化操作
	this.init();
};
//￥388.0万元   行业：
RequItem.template = null; // 视图模板

RequItem.prototype.init = function() {

	if(RequItem.template == null) {

		RequItem.template = $("#requ-template").html();
	}
	this.view = this.container.append(RequItem.template).children().last();
	this.view.attr("id", this.id);
	this.view.attr("lastModify", this.lastModify);
	
	this.name = isEmpty(this.name, "暂未填写");
	this.view.find("[sid=name]").text(this.name);
	
	this.trade = isEmpty(this.trade, "暂未填写");
	this.view.find("[sid=hj]").text("生产环节："+this.hj);

	this.ownerName = isEmpty(this.ownerName, "暂未填写");
    this.view.find("[sid=ownerName]").text(""+this.ownerName);
	
	if(this.price == null || this.price == 0 ) {
		
		this.view.find("[sid=price]").text("面议");
	}else{
		
		this.view.find("[sid=price]").text("￥" + this.price + "万");
	}

	// 绑定按钮事件
	Button.create(this.view, {
		onClick : this.onItemClick
	});
};

RequItem.prototype.onItemClick = function(id, e) {
	SDK.openRequDetail(parseInt(id), e.view.find("[sid=name]").text(), e.view
			.attr("lastModify"));
};

