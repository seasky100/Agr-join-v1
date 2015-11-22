/***专家成果轮播图片**/
var ExpertImgItem = function(id, name, lastModify, src, container){
	
	// 初始化属性
	this.id = id;//成果id
	this.name = name;
	this.lastModify = lastModify;
	this.src = src;
	this.driver = FILE.getSysPath();
	this.local = this.driver + expertlogoPath;//专家图片的本地存储路径
	if(this.src != null && this.src.trim() != "")
		this.fileName = this.src.split("/").pop();//logo 的文件名
	else
		this.fileName = "null";
	
	// 定义视图和父容器
	this.view = null;
	this.container = container;
	
	// 执行初始化操作
	this.init();
}

ExpertImgItem.template = null; // 视图模板

ExpertImgItem.prototype.init = function(){

	if(ExpertImgItem.template == null)
		ExpertImgItem.template = "<div class='swiper-slide'><img  sid='expertlogo' src=''/><p sid='name'></p></div>";

	this.view = this.container.append(ExpertImgItem.template).children().last();
	
	var  path = this.local+this.fileName;

	if(FILE.isFileExist(path) == true){
		
		this.view.find('[sid=expertlogo]').attr("src", path);
	}else{
		
		this.view.find('[sid=expertlogo]').attr("src",expertDefaultLogo);
		this.loadIcon();
	}

    if(this.name == null || this.name.trim() == "")
        this.name = "暂未填写";
	this.view.find('[sid=name]').text(this.name);
	this.view.attr("lastModify",this.lastModify);
	this.view.attr("id",this.id);

	// 绑定按钮事件
	Button.create(this.view, {
		onClick : this.onItemClick
	});

}

ExpertImgItem.prototype.onItemClick = function(id, e) {

	SDK.openExpertDetail(parseInt(id), e.view.find('[sid=name]').text(), e.view.attr("lastModify"));
};

ExpertImgItem.prototype.onFileFinishLoad = function(){

	var path = this.local+this.fileName;
	if(FILE.isFileExist(path) == true){
		this.view.find('[sid=expertlogo]').attr("src",this.local+this.fileName);
	}
}
ExpertImgItem.prototype.loadIcon = function(){

	fileLoader.downloadFile(new DownloadFile( this, this.local, this.fileName, this.src,""));
}


