/**
 * 操作成功或者是失败的dialog
 * txt 表示dialog 中显示的文字,flag 表示成功或者是失败,根据成功或者是失败显示不同的图片,flag取值是true/false
*/
var Dialog = function(txt,flag,container,duration){
	
	this.txt = txt;
	this.flag = flag;
	this.container = container;
	this.duration = duration;
	
	this.init();
	this.close();
}

Dialog.template = "<div class='hintDialog'><div class='dialog'><div class='hintImg'></div><span class='hintTxt'></span></div><div class='main'></div></div>";
Dialog.prototype.init = function(){
	
	this.view = $(Dialog.template);
	this.view.find(".hintTxt").text(this.txt);
	if(this.flag){
		this.view.find(".hintImg").addClass("success").removeClass("fail");
	}else{
		this.view.find(".hintImg").addClass("fail").removeClass("success");
	}
	this.container.append(this.view);
}
Dialog.prototype.close = function(){
	var view = this.view;
	setTimeout(function(){view.hide();},this.duration);//时间应该由参数控制
}

/*选择对话框,根据选择的不同调用不同的操作*/
var HintDialog = function(hinttxt,ytxt,ntxt,container,callback,tel){
	
	this.hinttxt = hinttxt;
	this.ytxt = ytxt;
	this.ntxt = ntxt;
	this.container = container;
	this.callback = callback;
	this.tel = tel;
	
	this.init();
	this.attach();
	//this.close();
}
HintDialog.template = '<div class="call-panel"><div class="callDialog"><div class="call-title"></div><div class="func-btn"><span display="button" id="cancel-call"></span><span  id= "call" display="button"></span></div></div><div class="main"></div></div>';
HintDialog.prototype.init = function(){
	
	this.view = $(HintDialog.template);
	this.view.find(".call-title").text(this.hinttxt);
	this.view.find("#cancel-call").text(this.ntxt);//取消按钮
	this.view.find("#call").text(this.ytxt);//
	
	this.container.append(this.view);
}
HintDialog.prototype.attach = function(){

	this.view.find("span").last().on("click",this,function(e){
		var t = e.data;
		eval("t.callback(t.tel)");
		t.close();
	});
	this.view.find("span").first().on("click",this,function(e){
		var t = e.data;
		
		//eval(t.callback+"()");
		t.close();
	});
}
HintDialog.prototype.close = function(){
	this.view.hide();
	//setTimeout(function(){view.hide();},this.duration);//时间应该由参数控制
}
