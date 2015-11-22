var InforItem = function(id, title, logo, introduction, reqTime, publishTime, updateDate, container){
	
	// 初始化属性
	this.id = id;
	this.title = title;
	this.logo = logo;
	this.introduction = introduction;
	this.reqTime = reqTime;
	this.publishTime = publishTime;
	this.updateDate = updateDate;
	this.driver = FILE.getSysPath();
	this.local = this.driver + fundviewInfoPath;// infor 中图片的本地存储路径
	if(this.logo == null || this.logo.trim() == "") {

		this.fileName = "null";
	}else {

		this.fileName = this.logo.split("/").pop();// infor 中图片的文件名
	}
	
	// 定义视图和容器
	this.view = null;
	this.container = container;

	// 执行初始化操作
	this.init();
};

InforItem.template = null; // 视图模板

InforItem.prototype.init = function(){
	
	// 获取模板，创建item对象的视图对象
	if(InforItem.template == null)
		InforItem.template = $("#template").html();
	
	this.view = this.container.prepend(InforItem.template).children().first();

	this.view.attr("id", this.id);
	this.view.attr("updateDate", this.updateDate);
	this.view.find("[sid=title]").text(this.title);
	this.view.find("[sid=publishTime]").text(formatDate(this.publishTime));
	var reqTime = formatDate(this.reqTime);

	if(reqTime==""){

		this.view.find("[sid=reqTime]").hide();
	}else{

		this.view.find("[sid=reqTime]").text(reqTime);
	}
	
	var publishTime = formatDate(this.publishTime);

	if(publishTime==""){

		this.view.find("[sid=publishTime]").hide();
	}else{

		this.view.find("[sid=publishTime]").text(publishTime);
	}
	
	
	var  src = this.local+this.fileName;

	if(FILE.isFileExist(src) == true){
		
		this.view.find("[sid=logo]").attr("src", src);
		this.view.find("[sid=logo]").error(function() {

			$(this).attr("src",fundviewInforDefaultLogo);
		});
	}else{
		
		this.view.find("[sid=logo]").attr("src",fundviewInforDefaultLogo);
		this.loadIcon();
	}
	
	this.view.find("[sid=introduction]").html(this.introduction);
	var t = this;

	Button.create(this.view, {onClick: this.OnBtnClick})
}

//打開咨訊詳細
InforItem.prototype.OnBtnClick = function(id,e){

	SDK.openDetail(parseInt(id), e.view.find("[sid=title]").text(), e.view.attr("updateDate"));
}
InforItem.prototype.onFileFinishLoad = function(){

	var src = this.local+this.fileName;
	if(FILE.isFileExist(src) == true){

		this.view.find("[sid=logo]").attr("src",this.local+this.fileName);
	}
}

InforItem.prototype.loadIcon = function(){

	fileLoader.downloadFile(new DownloadFile( this,  this.local,this.fileName, this.logo,""));
}

