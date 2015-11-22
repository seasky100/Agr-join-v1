/**
 * 企业产品 Item requId, logo, name, hj, price, oldLogo,ownerName,lastModify,$("#list")
 */
var ProductItem = function(id, logo, name, unit, price, oldLogo, ownerName, lastModify, container) {

	// 初始化属性
	this.id = id;// 产品id
	this.logo = logo;// 网络路径 需求的第一张图片
	this.name = name; // 名字
	this.unit = unit;// 规格
	this.price = price;// 价格
	this.oldLogo = oldLogo;//的老图片
	this.ownerName = ownerName;//拥有者名称
	this.lastModify = lastModify;// 最后修改时间

	this.driver = FILE.getSysPath();
	this.local = this.driver + productLogoPath;// logo的本地存储路径
	if(this.logo != null && this.logo.trim() != "")
		this.fileName = this.logo.split("/").pop();// logo 的文件名
	else
		this.fileName = "null";
	// 定义视图和父容器
	this.view = null;
	this.container = container;

	// 执行初始化操作
	this.init();
};
//￥388.0万元   行业：
ProductItem.template = null; // 视图模板

ProductItem.prototype.init = function() {

	if(ProductItem.template == null) {

		ProductItem.template = $("#product-template").html();
	}
	this.view = this.container.append(ProductItem.template).children().last();
	this.view.attr("id", this.id);
	this.view.attr("lastModify", this.lastModify);

	var src = this.local + this.fileName;

	if (this.logo != null && this.logo.trim() != "") {
		if (FILE.isFileExist(src) == true) {

			this.view.find("[sid=logo]").attr("src", src);
		} else {

			this.view.find("[sid=logo]").attr("src", productDefaultLogo);
			this.loadIcon();
		}
		this.view.attr("logo", this.logo);
	} else {

		this.view.find("[sid=logo]").attr("src", requDefaultLogo);
	}
	
	this.name = isEmpty(this.name, "暂未填写");
	this.view.find("[sid=name]").text(this.name);

	this.name = isEmpty(this.unit, "暂未填写");
	this.view.find("[sid=unit]").text("规格：" + this.unit);

	this.ownerName = isEmpty(this.ownerName, "暂未填写");
    this.view.find("[sid=ownerName]").text(this.ownerName);
	
	if(this.price == null || this.price == 0 ) {
		
		this.view.find("[sid=price]").text("面议");
	}else{
		
		this.view.find("[sid=price]").text("￥" + this.price  + "元");
	}

	// 绑定按钮事件
	Button.create(this.view, {
		onClick : this.onItemClick
	});
};

ProductItem.prototype.onItemClick = function(id, e) {
	SDK.openProductDetail(parseInt(id), e.view.find("[sid=name]").text(), e.view
			.attr("lastModify"));
};

ProductItem.prototype.onFileFinishLoad = function() {

	var src = this.local + this.fileName;
	if (FILE.isFileExist(src) == true) {
		this.view.find("img").attr("src", this.local + this.fileName);
	}
};
ProductItem.prototype.loadIcon = function() {

	fileLoader.downloadFile(new DownloadFile(this, this.local, this.fileName,
			this.logo, this.oldLogo));
};
