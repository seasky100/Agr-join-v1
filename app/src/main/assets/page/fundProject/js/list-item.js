var FundItem = function(id, logo, name, jd, compName, invest, container) {
	// 初始化属性
	this.id = id;// 专家id
	this.logo = logo;// 网络路径 专家logo
	this.name = name; // 专家名字
	this.jd = jd;
	this.compName = compName;
	this.invest = invest;// 专家所属的行业

	// 定义视图和父容器
	this.view = null;
	this.container = container;

	// 执行初始化操作
	this.init();
};
//行业：地区：
FundItem.template = null;
FundItem.prototype.init = function() {

	if(FundItem.template == null) {

		FundItem.template = $("#fund-template").html();
	}

	this.view = this.container.append(FundItem.template).children().last();
	this.view.attr("id", this.id);

	if (this.logo != null && this.logo.trim() != "") {

		this.view.find("[sid=logo]").attr("src", this.logo);
	}else {

		this.view.find("[sid=logo]").attr("src", compDefaultLogo);
	}

	this.name = isEmpty(this.name, "暂未填写");
	this.view.find("[sid=name]").text(this.name);

	this.jd = isEmpty(this.jd, "暂未填写");
	this.view.find("[sid=jd]").text(this.jd);
	
	this.compName = isEmpty(this.compName, "暂未填写");
	this.view.find("[sid=ownerName]").text(this.compName);// 专家所属的行业
;
	this.view.find("[sid=invest]").text(this.invest <=  0 ? "面议":("￥" + this.invest+"万"));// 专家所属的行业

	// 绑定按钮事件
	Button.create(this.view, {
		onClick : this.onItemClick
	});
};

FundItem.prototype.onItemClick = function(id, e) {
	
	SDK.openDetail(parseInt(id), e.view.find("[sid=name]").text());
};

