var AttentItem = function(id, name, logo, trade, type, attentTime, area, oldLogoName, updateDate, container){
	
	this.id = id;
	this.name = name;
	this.logo = logo;//logo 的网络路径
	this.trade = trade;
	this.type = type;// 2 专家  1企业
	this.attentTime = attentTime;
	this.area = area;
	this.oldLogoName = oldLogoName;
	this.container = container;
	this.updateDate = updateDate;
	this.view = null;

	this.driver = FILE.getSysPath();

	this.fileName = this.logo.split("/").pop();//logo 的文件名

	this.init();
	//this.attachEvent();
}

AttentItem.template = null;
AttentItem.prototype.init = function(){
	
	if(AttentItem.template == null){

		AttentItem.template = $("#template").html();
	}
	if(this.type == 1) {

		this.local = this.driver + compLogoPath;//logo的本地存储路径
	}else if(this.type == 2) {

		this.local = this.driver + expertlogoPath;//logo的本地存储路径
	}

	// 给视图对象赋值
	this.view = this.container.append(AttentItem.template).children().last();

	this.view.attr("id" ,this.id);
	this.view.attr("updateDate" ,this.updateDate);
	this.view.attr("type" ,this.type);
	var  src = this.local+this.fileName;

	if(FILE.isFileExist(src) == true){

		this.view.find("[sid=logo]").attr("src",src);
	}else{

		if(this.type == 1) {

			this.view.find("[sid=logo]").attr("src",expertDefaultLogo);
		}else if(this.type == 2) {

			this.view.find("[sid=logo]").attr("src",compDefaultLogo);
		}

		this.loadIcon();
	}

	this.view.find("[sid=name]").text((this.name==null || this.name.trim() == "")?"暂未填写":this.name);
	
	this.view.find("[sid=area]").text((this.area==null || this.area.trim() == "")?"暂未填写":this.area);
	
	this.view.find("[sid=trade]").text((this.trade==null || this.trade.trim() == "")?"暂未填写":this.trade);
	this.view.find("[sid=attentTime]").text(formatDate_1(this.attentTime));

	//绑定单击事件
	Button.create(this.view, {onClick: this.onItemClick});
}

AttentItem.prototype.onItemClick = function(id, e){

	SDK.openDetail(parseInt(id), e.view.find("[sid=name]").text(), e.view.attr("updateDate"), parseInt(e.view.attr("type")));
}

AttentItem.prototype.loadIcon = function(){
	
	fileLoader.downloadFile(new DownloadFile( this,  this.local, this.fileName, this.logo, this.oldLogoName));
}

AttentItem.prototype.onFileFinishLoad = function(){

	if(FILE.isFileExist(this.local+this.fileName) == true){

		this.view.find("[sid=logo]").attr("src",this.local+this.fileName);
	}
}
