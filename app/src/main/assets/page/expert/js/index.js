/*专家指标*/
var Index = function(complexIdx, capitalIdx, areaIdx, techIdx, attent, profitType, profitValue){
	// 初始化属性	
	this.complexIdx = complexIdx;
	this.areaIdx = areaIdx;
	this.techIdx = techIdx;
	this.attent = attent;
	this.profitType = profitType;
	this.profitValue = profitValue;
	this.capitalIdx = capitalIdx;
	
	// 定义视图和父容器
	this.view = null;

	// 执行初始化操作
	this.init();
}

Index.prototype.init = function(){
	
	this.view = $("[sid=index]");

	//精确到小数点后一位
	var value = this.complexIdx/10;
	var values = value.toString().split(".");
	if(values.length==1){
		var hot = values[0]+".0";
	}else{
		hot = values[0]+"."+values[1].charAt(0);
	}

	this.view.find("[sid=hot-icon]").addClass("hot-"+Math.round(hot));
	this.view.find("[sid=mark]").text(hot);                              
	this.view.find("[sid=capitalIdx]").addClass("level_"+Math.round(this.capitalIdx/16));
	this.view.find("[sid=areaIdx]").addClass("level_"+Math.round(this.areaIdx/16));
	this.view.find("[sid=techIdx]").addClass("level_"+Math.round(this.techIdx/16));
	this.view.find("[sid=attent]").text(this.attent);
	this.initProfit();
	
}

Index.prototype.initProfit = function(){

	if(this.profitValue == null || this.profitValue == ""){
		
		this.view.find("[sid=profitName]").text("行情良好");
	}else{
		
		var profitName = this.profitType == 1 ? "投资回报率" : "每亩年收益";
		this.view.find("[sid=profitName]").text(profitName);
		this.view.find("[sid=profitValue]").text(this.profitValue);
	}
}
