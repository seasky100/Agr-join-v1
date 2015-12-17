/**
 * 技术需求 Item requId, logo, name, hj, price, oldLogo,ownerName,lastModify,$("#list")
 */
var RequItem = function(requId, logo, name, hj, price, oldLogo, ownerName, lastModify, container) {

	// 初始化属性
	this.id = requId;// 需求id
	this.logo = logo;// 网络路径 需求的第一张图片
	this.name = name; // 需求名字
	this.hj = hj;// 需求行业
	this.price = price;// 需求价格
	this.oldLogo = oldLogo;//需求的老图片
	this.ownerName = ownerName;//拥有者名称
	this.lastModify = lastModify;// 最后修改时间

//	this.driver = FILE.getSysPath();
//	this.local = this.driver + requLogoPath;// logo的本地存储路径
//	if(this.logo != null && this.logo.trim() != "")
//		this.fileName = this.logo.split("/").pop();// logo 的文件名
//	else
//		this.fileName = "null";
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

//	var src = this.local + this.fileName;
//
//	if (this.logo != null && this.logo.trim() != "") {
//		if (FILE.isFileExist(src) == true) {
//
//			this.view.find("[sid=logo]").attr("src", src);
//		} else {
//
//			this.view.find("[sid=logo]").attr("src", requDefaultLogo);
//			this.loadIcon();
//		}
//		this.view.attr("logo", this.logo);
//	} else {
//
//		this.view.find("[sid=logo]").attr("src", requDefaultLogo);
//	}
//

	this.view.find("[sid=logo]").attr("src", this.logo);
	this.name = isEmpty(this.name, "暂未填写");
	this.view.find("[sid=name]").text(this.name);
	
	this.trade = isEmpty(this.trade, "暂未填写");
	this.view.find("[sid=hj]").text("生产环节："+this.hj);

	this.ownerName = isEmpty(this.ownerName, "暂未填写");
    this.view.find("[sid=ownerName]").text(this.ownerName);
	
	if(this.price == null || this.price == 0 ) {
		
		this.view.find("[sid=price]").text("面议");
	}else{
		
		this.view.find("[sid=price]").text("￥" + this.price  + "万");
	}

	// 绑定按钮事件
	Button.create(this.view, {
		onClick : this.onItemClick
	});

	//图片加载失败监听
    this.view.find("[sid=logo]").bind("error", this.view, this.loadDefaultIcon);
};
RequItem.prototype.loadDefaultIcon = function(e) {

	var view = e.data;
	view.find("[sid=logo]").attr("src", requDefaultLogo);
};
RequItem.prototype.onItemClick = function(id, e) {
	SDK.openRequDetail(parseInt(id), e.view.find("[sid=name]").text(), e.view
			.attr("lastModify"));
};

RequItem.prototype.onFileFinishLoad = function() {

	var src = this.local + this.fileName;
	if (FILE.isFileExist(src) == true) {
		this.view.find("img").attr("src", this.local + this.fileName);
	}
};
RequItem.prototype.loadIcon = function() {

	fileLoader.downloadFile(new DownloadFile(this, this.local, this.fileName,
			this.logo, this.oldLogo));
};
