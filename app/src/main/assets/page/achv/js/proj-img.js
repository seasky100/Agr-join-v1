
var ProjImage = function(src, container){
	
	// 初始化属性	
	this.src = src;
	this.driver = FILE.getSysPath();
	this.local = this.driver + picPath;//项目图片的本地存储路径
	this.fileName = this.src.split("/").pop();//logo 的文件名
	
	// 定义视图和父容器
	this.view = null;
	this.container = container;
	
	// 执行初始化操作
	this.init();
}

ProjImage.template = null; // 视图模板

ProjImage.prototype.init = function(){

	if(ProjImage.template == null)
		ProjImage.template = "<img class='img_slide slide' sid='element_slide'/>";
	
	this.view = this.container.append(ProjImage.template).children().last();
	
	var  path = this.local+this.fileName;

	if(FILE.isFileExist(path) == true){
		
		this.view.attr("src", path);
	}else{
		
		this.view.attr("src","../img/projDefault.jpg");
		this.loadIcon();
	}
	//this.view.attr("src", this.src);
}

ProjImage.prototype.onFileFinishLoad = function(){

	var path = this.local+this.fileName;
	if(FILE.isFileExist(path) == true){
		this.view.attr("src",this.local+this.fileName);
	}
}
ProjImage.prototype.loadIcon = function(){

	fileLoader.downloadFile(new DownloadFile( this, this.local, this.fileName, this.src));
}


