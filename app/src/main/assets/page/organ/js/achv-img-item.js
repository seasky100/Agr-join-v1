/***专家成果轮播图片**/
var AchvImgItem = function(id, name, lastModify, src, container){
	
	// 初始化属性
	this.id = id;//成果id
	this.name = name;
	this.lastModify = lastModify;
	this.src = src;
	this.driver = FILE.getSysPath();
	this.local = this.driver + achvLogoPath;//成果图片的本地存储路径
	this.fileName = this.src.split("/").pop();//logo 的文件名
	
	// 定义视图和父容器
	this.view = null;
	this.container = container;
	
	// 执行初始化操作
	this.init();
}

AchvImgItem.template = null; // 视图模板

AchvImgItem.prototype.init = function(){

	if(AchvImgItem.template == null)
		AchvImgItem.template = "<div class='swiper-slide'><img  sid='logo' src=''/><p sid='name'>aa</p></div>";

	this.view = this.container.append(AchvImgItem.template).children().last();
	
	var  path = this.local+this.fileName;

	if(FILE.isFileExist(path) == true){
		
		this.view.find('[sid=logo]').attr("src", path);
	}else{
		
		this.view.find('[sid=logo]').attr("src",achvDefaultLogo);
		this.loadIcon();
	}

	this.view.find('[sid=name]').text(this.name);
	this.view.attr("lastModify",this.lastModify);
	this.view.attr("id",this.id);

	// 绑定按钮事件
	Button.create(this.view, {
		onClick : this.onItemClick
	});

}

AchvImgItem.prototype.onItemClick = function(id, e) {

	SDK.openAchvDetail(parseInt(id), e.view.find('[sid=name]').text(), e.view.attr("lastModify"));
};

AchvImgItem.prototype.onFileFinishLoad = function(){

	var path = this.local+this.fileName;
	if(FILE.isFileExist(path) == true){
		this.view.find('[sid=logo]').attr("src",this.local+this.fileName);
	}
}
AchvImgItem.prototype.loadIcon = function(){

	fileLoader.downloadFile(new DownloadFile( this, this.local, this.fileName, this.src,""));
}


