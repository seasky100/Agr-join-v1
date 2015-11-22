var RequBox = function (count){

	this.idx = 1;
	this.count = count;
	this.seeds = new Array();
	
	this.view = null;
	this.seedIdxView = null;
	this.seedCountView = null;
	
	this.init();
}

RequBox.prototype.init = function(){
	
	this.view = $('[sid=coms]');
	this.seedIdxView = $("[sid=seedIdx]");
	this.seedCountView = $("[sid=seedCount]");
	
	this.seedIdxView.text(this.idx);
	this.seedCountView.text(this.count);
}

RequBox.prototype.addRequ= function(id, name, info, tel){
	
	var seed = new Requ(id, name, info, tel,  this.view);
	this.seeds.push(seed);
	if(this.seeds.length == this.count)
		this.useSlide();	
}

RequBox.prototype.useSlide = function(){
	
	this.slide = new Slide(this, this.view, this.view.find("[sid=element_slide]"), 0);	
	this.view.show();
}

RequBox.prototype.updateIdx = function(idx){
	
	this.idx = idx;
	this.seedIdxView.text(this.idx);
}