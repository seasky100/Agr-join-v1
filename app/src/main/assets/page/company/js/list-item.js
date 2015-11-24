var CompItem = function(compId, logo, compName, trade, expoNo, area, oldLogo, lastModify, container) {

	// 初始化属性
	this.id = compId;// 企业id
	this.logo = logo;// 网络路径 企业logo
	this.name = compName; // 企业名字
	this.trade = trade;// 企业所属的行业
	this.expoNo = expoNo; //展位号
	this.area = area;// 企业所属的地区
	this.oldLogo = oldLogo;//logo 老图片
	this.lastModify = lastModify;// 最后修改时间

	this.driver = FILE.getSysPath();
	this.local = this.driver + compLogoPath;// logo的本地存储路径
	if(this.logo != null && this.logo.trim() != "")
		this.fileName = this.logo.split("/").pop();// logo 的文件名

     	// 定义视图和父容器
     	this.view = null;
     	this.container = container;

     	// 执行初始化操作
     	this.init();
     };
     //行业：关注人
     CompItem.template = $("#company-template").html(); // 视图模板

     CompItem.prototype.init = function() {

     	if(CompItem.template == null) {

     		CompItem.template = $("#company-template").html();
     	}
	this.view = this.container.append(CompItem.template).children().last();
	this.view.attr("id", this.id);
	this.view.attr("lastModify", this.lastModify);

	var src = this.local + this.fileName;

	if (this.logo != null && this.logo.trim() != "") {
		if (FILE.isFileExist(src) == true) {

			this.view.find("[sid=logo]").attr("src", src);
		} else {

			this.view.find("[sid=logo]").attr("src", compDefaultLogo);
			this.loadIcon();
		}
		this.view.attr("logo", this.logo);
	} else {

		this.view.find("[sid=logo]").attr("src", compDefaultLogo);
	}
	
	this.name = isEmpty(this.name,"暂未填写");
	this.view.find("[sid=name]").text(this.name);
	
	this.area = isEmpty(this.area, "暂未填写");
	this.view.find("[sid=area]").text(this.area);
	
	if(this.expoNo == null) {
		
		this.expoNo = "";
	}else {
		
		this.expoNo = "展位(" + this.expoNo+")";
	}
	this.view.find("[sid=expoNo]").text(this.expoNo);
	
	this.trade = isEmpty(this.trade, "暂未填写");
	this.view.find("[sid=trade]").text("行业：" + this.trade);// 专家所属的行业

	// 绑定按钮事件
	Button.create(this.view, {
		onClick : this.onItemClick
	});
};

CompItem.prototype.onItemClick = function(id, e) {
	SDK.openCompDetail(parseInt(id), e.view.find("[sid=name]").text(), e.view
			.attr("lastModify"));
};

CompItem.prototype.onFileFinishLoad = function() {

	var src = this.local + this.fileName;
	if (FILE.isFileExist(src) == true) {
		this.view.find("img").attr("src", this.local + this.fileName);
	}
};
CompItem.prototype.loadIcon = function() {

	fileLoader.downloadFile(new DownloadFile(this, this.local, this.fileName, this.logo, this.oldLogo));
};
