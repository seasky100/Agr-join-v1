var Page = function(){};
var FILE = window.file;
var SDK = window.sdk;

//点击丰景资讯
function openSite(){

	SDK.openUrl("http://www.fundview.cn");
}

//${id}, ${title}, ${intro}, ${updateDate}, ${content}. ${imgUrl}
Page.initPage = function(id ,title, time, content, imgUrl){

	$("[sid=container]").attr("id", id);
	$("[sid=title]").text(title);
	$("[sid=time]").text(formatDate(time));
	$("[sid=panel]").html(content);
	new Image(imgUrl);
}

Page.showLoading = function(){
	
	Loading.show();
}

Page.hideLoading = function(){

	Loading.hide();
}

//加载数据超时提醒
Page.loadFailed = function(){

	Loading.hide();
	new Dialog("抱歉,数据加载失败",false,$("body"),2000);
}

var Image = function(src){

	this.src = src;
	this.init();
}

Image.prototype.init = function(){

	var template = '<img></img>';
	this.view = $(template);
	this.view.attr("src",this.src);
	this.view.prependTo("[sid=panel]");
	this.view.bind("error",this,this.hide);
}

Image.prototype.hide = function(e){
	var t = e.data;
	t.view.hide();
}