//expertId, logo, expertName, professionalTitle, trade,workUnit, dept,  area, updateDate,oldFileName, $("[sid=expertPanel]")
var ExpertItem = function(expertId, logo, expertName, professionalTitle, trade, area, updateDate, oldFileName, container) {
	// 初始化属性
	this.id = expertId;// 专家id
	this.logo = logo;// 网络路径 专家logo
	this.name = expertName; // 专家名字
	this.professionalTitle = professionalTitle;
	this.trade = trade;// 专家所属的行业
//	this.workUnit = workUnit;//单位
//	this.dept = dept;//部门
	this.area = area;// 专家所属的地区
	this.lastModify = updateDate;// 最后修改时间
	this.oldFileName = oldFileName;
	this.driver = FILE.getSysPath();
	this.local = this.driver + expertlogoPath;// logo的本地存储路径
	
	
	// 定义视图和父容器
	this.view = null;
	this.container = container;

	// 执行初始化操作
	this.init();
};
//行业：地区：
ExpertItem.template = null;
ExpertItem.prototype.init = function() {

	if(ExpertItem.template == null) {

		ExpertItem.template = $("#template").html();
	}
	if(this.logo == null) {
		
		this.logo = "";
	}

	if(this.logo == null || this.logo.trim() == "")
		this.fileName="null";
	else
		this.fileName = this.logo.split("/").pop();// logo 的文件名
	this.view = this.container.append(ExpertItem.template).children().last();
	this.view.attr("id", this.id);
	this.view.attr("lastModify", this.lastModify+"");

	var src = this.local + this.fileName;

	if (this.logo != null && this.logo.trim() != "") {
		if (FILE.isFileExist(src) == true) {

			this.view.find("[sid=logo]").attr("src", src);
		} else {

			this.view.find("[sid=logo]").attr("src", expertDefaultLogo);
			this.loadIcon();
		}
		this.view.attr("logo", this.logo);
	} else {

		this.view.find("[sid=logo]").attr("src", expertDefaultLogo);
	}
	this.name = isEmpty(this.name, "暂未填写");
	this.view.find("[sid=name]").text(this.name);
	if(this.professionalTitle != null)
	this.view.find("[sid=professionalTitle]").text(this.professionalTitle);

	this.area = isEmpty(this.area, "暂未填写");
	this.view.find("[sid=area]").text(this.area);
	
	this.trade = isEmpty(this.trade, "暂未填写");
	this.view.find("[sid=trade]").text("行业：" + this.trade);// 专家所属的行业

	// 绑定按钮事件
	Button.create(this.view, {
		onClick : this.onItemClick
	});
};

ExpertItem.prototype.onItemClick = function(id, e) {
	
	SDK.openExpertDetail(parseInt(id), e.view.find("[sid=name]").text(), e.view.attr("lastModify"));
};

ExpertItem.prototype.onFileFinishLoad = function() {

	var src = this.local + this.fileName;
	if (FILE.isFileExist(src) == true) {
		this.view.find("img").attr("src", this.local + this.fileName);
	}
};
ExpertItem.prototype.loadIcon = function() {

	fileLoader.downloadFile(new DownloadFile(this, this.local, this.fileName,
			this.logo, this.oldFileName));
};
