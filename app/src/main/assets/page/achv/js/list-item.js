var AchvItem = function(achvId, logo, name, trade, price, ownerName, oldLogo, lastModify, container) {

	// 初始化属性
	this.id = achvId;// 成果id
	this.logo = logo;// 网络路径 成果logo
	this.name = name; // 成果名字
	this.trade = trade;// 成果所属的行业
	this.price = price;// 成果价格
	this.ownerName = ownerName;//成果拥有者名称
	this.oldLogo = oldLogo;
	this.lastModify = lastModify;// 最后修改时间

	this.driver = FILE.getSysPath();
	this.local = this.driver + achvLogoPath;// logo的本地存储路径
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

AchvItem.template = null; // 视图模板

AchvItem.prototype.init = function() {

	if (AchvItem.template == null)
		AchvItem.template = $("#achv-template").html();

	this.view = this.container.append(AchvItem.template).children().last();
	this.view.attr("id", this.id);
	this.view.attr("lastModify", this.lastModify);

	var src = this.local + this.fileName;

	if (this.logo != null && this.logo.trim() != "") {
		if (FILE.isFileExist(src) == true) {

			this.view.find("[sid=logo]").attr("src", src);
		} else {

			this.view.find("[sid=logo]").attr("src", achvDefaultLogo);
			this.loadIcon();
		}
		this.view.attr("logo", this.logo);
	} else {

		this.view.find("[sid=logo]").attr("src", achvDefaultLogo);
	}
	// this.view.find("[sid=logo]").attr("src", this.logo);
	this.view.find("[sid=name]").text(this.name);
	this.view.find("[sid=trade]").text("应用行业：" + isEmpty(this.trade,"暂未填写"));
	this.view.find("[sid=price]").text(this.price==0?"面议":"￥" + this.price+"万");
	this.view.find("[sid=ownerName]").text(this.ownerName);// 成果拥有者

	// 绑定按钮事件
	Button.create(this.view, {
		onClick : this.onItemClick
	});
};

AchvItem.prototype.onItemClick = function(id, e) {
	SDK.openAchvDetail(parseInt(id), e.view.find("[sid=name]").text(), e.view
			.attr("lastModify"));
};

AchvItem.prototype.onFileFinishLoad = function() {

	var src = this.local + this.fileName;
	if (FILE.isFileExist(src) == true) {
		this.view.find("img").attr("src", this.local + this.fileName);
	}
};
AchvItem.prototype.loadIcon = function() {

	fileLoader.downloadFile(new DownloadFile(this, this.local, this.fileName, this.logo,
			this.oldLogo));
};
/** 检测字符串是否为空, 空 自动填充为指定的字符串* */
function isEmpty(source, defaultStr) {

	if (source == null || source.trim() == "") {

		source = defaultStr;

	}
	return source;
}