/***企业最新产品轮播图片**/
var ImgItem = function(id, name, lastModify, src, localLogo, container){
	
	// 初始化属性
	this.id = id;//成果id
	this.name = name;
	this.lastModify = lastModify;
	this.src = src;
//	this.localLogo = localLogo;
//	this.driver = FILE.getSysPath();
//	this.local = this.driver + productLogoPath;//成果图片的本地存储路径
//	this.fileName = this.src.split("/").pop();//logo 的文件名
	
	// 定义视图和父容器
	this.view = null;
	this.container = container;
	
	// 执行初始化操作
	this.init();
}

ImgItem.template = null; // 视图模板

ImgItem.prototype.init = function(){

	if(ImgItem.template == null)
		ImgItem.template = "<div class='swiper-slide' ><img  sid='prodlogo' src='' onerror='this.src=productDefaultLogo;'/><p sid='name'></p></div>";
	
	this.view = this.container.append(ImgItem.template).children().last();
	this.view.find('[sid=prodlogo]').attr("src", this.src);
//	var  path = this.local+this.fileName;

//	if(FILE.isFileExist(path) == true){
//
//		this.view.find('[sid=prodlogo]').attr("src", path);
//	}else{
//
//		this.view.find('[sid=prodlogo]').attr("src",productDefaultLogo);
//		this.loadIcon();
//	}


	this.view.attr("id", this.id);
	this.view.find('[sid=name]').text(this.name);
	this.view.attr("lastModify",this.lastModify);
	// 绑定按钮事件
	Button.create(this.view, {
		onClick:this.onItemClick
	});
}

ImgItem.prototype.onItemClick = function(id, e) {

	SDK.openProductDetail(parseInt(id), e.view.find('[sid=name]').text(), e.view.attr("lastModify"));
};
//ImgItem.prototype.onFileFinishLoad = function(){
//
//	var path = this.local+this.fileName;
//	if(FILE.isFileExist(path) == true){
//		this.view.find('[sid=prodlogo]').attr("src",this.local+this.fileName);
//	}
//}
//ImgItem.prototype.loadIcon = function(){
//
//	fileLoader.downloadFile(new DownloadFile( this, this.local, this.fileName, this.src, this.localLogo));
//}


