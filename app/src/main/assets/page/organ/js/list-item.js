var OrgItem = function(orgId, logo, name, area, expoNo, updateDate, oldFileName, container) {

	// 初始化属性
	this.id = orgId;// 机构id
	this.logo = logo;// 网络路径 专家logo
	this.name = name; // 专家名字
	this.area = area;// 专家所属的地区
	this.lastModify = updateDate;// 最后修改时间
	this.expoNo = expoNo;
	this.oldFileName = oldFileName;
	this.driver = FILE.getSysPath();
	this.local = this.driver + orgLogoPath;// logo的本地存储路径
	
	
	// 定义视图和父容器
	this.view = null;
	this.container = container;

	// 执行初始化操作
	this.init();
};
//行业：地区：
OrgItem.template = null;
OrgItem.prototype.init = function() {

	if(OrgItem.template == null) {

		OrgItem.template = $("#template").html();
	}
	if(this.logo == null) {
		
		this.logo = "";
	}
	this.fileName = this.logo.split("/").pop();// logo 的文件名
	this.view = this.container.append(OrgItem.template).children().last();
	this.view.attr("id", this.id);
	this.view.attr("lastModify", this.lastModify+"");

	var src = this.local + this.fileName;

	if (this.logo != null && this.logo.trim() != "") {
		if (FILE.isFileExist(src) == true) {

			this.view.find("[sid=logo]").attr("src", src);
		} else {

			this.view.find("[sid=logo]").attr("src", orgDefaultLogo);
			this.loadIcon();
		}
		this.view.attr("logo", this.logo);
	} else {

		this.view.find("[sid=logo]").attr("src", orgDefaultLogo);
	}
	this.name = isEmpty(this.name, "暂未填写");
	this.view.find("[sid=name]").text(this.name);
	this.view.find("[sid=expoNo]").text(this.expoNo);

	this.area = isEmpty(this.area, "暂未填写");
	this.view.find("[sid=area]").text(this.area);

	// 绑定按钮事件
	Button.create(this.view, {
		onClick : this.onItemClick
	});
};

OrgItem.prototype.onItemClick = function(id, e) {

	SDK.openOrgDetail(parseInt(id), e.view.find("[sid=name]").text(), e.view.attr("lastModify"));
};

OrgItem.prototype.onFileFinishLoad = function() {

	var src = this.local + this.fileName;
	if (FILE.isFileExist(src) == true) {
		this.view.find("img").attr("src", this.local + this.fileName);
	}
};
OrgItem.prototype.loadIcon = function() {

	fileLoader.downloadFile(new DownloadFile(this, this.local, this.fileName,
			this.logo, this.oldFileName));
};
