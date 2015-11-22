/***需求轮播图片**/
var ImgItem = function(src, container){
	
	// 初始化属性	
	this.src = src;
	this.driver = FILE.getSysPath();
	this.local = this.driver + productLogoPath;//需求图片的本地存储路径
	this.fileName = this.src.split("/").pop();//logo 的文件名
	
	// 定义视图和父容器
	this.view = null;
	this.container = container;
	
	// 执行初始化操作
	this.init();
}

ImgItem.template = null; // 视图模板

ImgItem.prototype.init = function(){

	if(ImgItem.template == null)
		ImgItem.template = "<img  class='swiper-slide' src=''/>";
	
	this.view = this.container.append(ImgItem.template).children().last();
	
	var  path = this.local+this.fileName;

	if(FILE.isFileExist(path) == true){
		
		this.view.attr("src", path);
	}else{
		
		this.view.attr("src",productDefaultLogo);
		this.loadIcon();
	}
}

ImgItem.prototype.onFileFinishLoad = function(){

	var path = this.local+this.fileName;
	if(FILE.isFileExist(path) == true){
		this.view.attr("src",this.local+this.fileName);
	}
}
ImgItem.prototype.loadIcon = function(){

	fileLoader.downloadFile(new DownloadFile( this, this.local, this.fileName, this.src,""));
}


