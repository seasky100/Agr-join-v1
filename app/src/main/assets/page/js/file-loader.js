var DownloadFile = function(deligate ,local, fileName, urlPath, oldFileName){

		this.deligate = deligate;//委托对象
		this.local = local;//本地目录
		this.fileName = fileName;//文件名
		this.urlPath = urlPath;//网络路径
		this.oldFileName = oldFileName;
};
	
	
/**
 * 定义 FileLoader 对象
 */
var  FileLoader = function(){
	
	this.running = false;
	this.array = new Array();
	this.downloadingFile = null;//正在下载的文件
};


/**
 * 启动文件下载任务
 */
FileLoader.prototype.doTask = function(){
	
	this.downloadingFile = this.array.shift();
	if(this.downloadingFile == null){
		this.running = false;
		return;
	}
			
	//调用action执行下载
	FILE.fileLoad(this.downloadingFile.urlPath, this.downloadingFile.local, this.downloadingFile.fileName, this.oldFileName);
	
};


/**
 * 添加文件
 */
FileLoader.prototype.downloadFile = function(aFile){
	
	this.array.push(aFile);

	if(!this.running){
		this.running = true;
		this.doTask();
	}
}
