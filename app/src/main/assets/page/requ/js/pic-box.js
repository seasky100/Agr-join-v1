var PicBox = function (){

	this.idx = 0;
	this.idxViewWidth = 100;	
	
	this.view = null;
	this.idxView = null;
	this.init();
}

PicBox.prototype.init = function(){
	
	this.view = $('[sid=img-panel]');
	this.idxView = $("[sid=idx]");
}

PicBox.prototype.addImages = function(srcs){

	for(var i in srcs)
		new ProjImage(srcs[i], this.view);
		
	this.idxViewWidth = 100 / srcs.length;
	this.idxView.css("width", this.idxViewWidth + "%");
	this.slide = new Slide(this, this.view, this.view.find("[sid=element_slide]"), 0);	
	this.view.show();
}

PicBox.prototype.updateIdx = function(idx){
	
	this.idx = idx - 1;
	this.idxView.css("margin-left", this.idx * this.idxViewWidth + "%");
}
