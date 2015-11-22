				
var ScrollView = function(){
	
	this.bufferTop = 80; 
	this.bufferBottom = 80; 
	this.loadingBoxSize = 30;
	
	this.doLoading = false;
	this.winHeight = $("[sid=scrollPanel]").outerHeight();
	this.scrollView = $("[sid=scrollView]");
	
	this.scrollViewHeight = this.scrollView.outerHeight();
	this.maxBottomOffset = this.scrollViewHeight - this.winHeight + this.bufferBottom;
	this.top = -this.scrollViewHeight + this.winHeight;
	this.defaultTop = this.top;
	this.init();
};

ScrollView.prototype.init = function(){
	
	// 滑动到最后一屏
	this.scrollView.css({ top: this.top});//- (scrollView.outerHeight() - lastItem.outerHeight());//var lastItem = scrollView.find("[sid=sItem]:last-child");
	
	this.scrollView.bind("movestart", this, this.moveStartHandler);
	this.scrollView.bind("move", this, this.moveHandler);
	this.scrollView.bind("moveend", this, this.moveendHandler);
}

ScrollView.prototype.moveStartHandler = function(e){
}

ScrollView.prototype.moveHandler = function(e){
	
	var t = e.data; 
	writeLog(t.top + e.deltaY);
	
	var newTop = t.top + e.deltaY;
	
	if(newTop > t.bufferTop)
		t.top = t.bufferTop
	else if(newTop < -t.maxBottomOffset)
		t.top = -t.maxBottomOffset;
	else
		t.top = newTop;
	
	t.scrollView.css({ top: t.top });
}

ScrollView.prototype.moveendHandler = function(e){
	
	var t = e.data;
	
	if(t.top > 0 || t.top < t.defaultTop){
		
		if(t.top > 0){
		
			if(t.top > t.loadingBoxSize){
				t.top = t.loadingBoxSize;
				t.scrollView.animate({ top: t.top }, 100, 'linear');
				if(!t.doLoading)
					t.doLoadMore();
			}else{
				
				t.top = 0;
				t.scrollView.animate({ top: t.top }, 100, 'linear');
			}
		}
		
		if(t.top < t.defaultTop){
				
			t.top = t.defaultTop;
			t.scrollView.animate({ top: t.top }, 100, 'linear');
		}
	}else{

		if(e.velocityY > 0.1){
		
			t.top = t.top + 800;
			if(t.top > 0)
				t.top = 0;
			t.scrollView.animate({ top: t.top }, 300, 'linear');
		}
		
		if(e.velocityY < -0.1){
			
			t.top = t.top - 800;
			if(t.top < t.defaultTop)
				t.top = t.defaultTop;
			t.scrollView.animate({ top: t.top }, 300, 'linear');
		}
	}
}

ScrollView.prototype.doLoadMore = function(){
	
	this.doLoading = true;
	//$("[sid=loadMore]").show()
	$("[sid=loadingIcon]").show();
	$("[sid=loadingTxt]").html("正在加载...");
	
	loadMore();
}

ScrollView.prototype.onLoadMoreFinished = function(newItem){
	
	this.doLoading = false;
	
	if(newItem != null){
			//alert(1212);
		this.scrollView.prepend(newItem);	
		
		var addHeight = newItem.outerHeight();
		if(this.scrollViewHeight - this.loadingBoxSize >= this.winHeight)
			this.top = this.top - addHeight;
		else
			this.top = this.top - this.loadingBoxSize;
			this.scrollViewHeight = this.scrollView.outerHeight();
			this.maxBottomOffset = this.scrollViewHeight - this.winHeight + this.bufferBottom;
			this.defaultTop = -this.scrollViewHeight + this.winHeight;
	}else{

		this.top = this.top - this.loadingBoxSize;
	}
	
	this.scrollView.css({ top: this.top });
	$("[sid=loadingIcon]").hide();
	$("[sid=loadingTxt]").html("松开加载更多...");
}

ScrollView.prototype.addItem = function(newItem){
	
	if(newItem == null)
		return;
		
	this.scrollView.append(newItem);
	this.scrollViewHeight = this.scrollView.outerHeight();
	this.maxBottomOffset = this.scrollViewHeight - this.winHeight + this.bufferBottom;
	this.top = -this.scrollViewHeight + this.winHeight;
	this.defaultTop = this.top;
	this.scrollView.css({ top: this.top });
}

function writeLog(log){
	
	$("#log").html(log);
}

/*
var colors = ['red', 'blue', 'green', 'yellow', 'salmon', 'purple'];	
var count = 10;
		
function writeLog(log){
	
	$("#log").html(log);
}

var scrollView = null;

$(function() {
	
	scrollView = new ScrollView();
});*/

function loadMore(){
	
	//setTimeout(function(){
	
		//从sdk获得数据
		var itemCount = $("[sid=scrollView]").children().first().attr("id");
		var msgInfor = SDK.getHistoryInfor(itemCount,1);
		writeLog("finish!!!");
	//}, 1000);
	
}
function add(){
	
	//var newItem = $("<div class='item'  sid='sItem' style='background-color:"+colors[count%6]+"'>sss"+(count++)+"<br/>sss<br/>sss<br/>sss<br/>sss<br/>sss<br/>sss<br/>sss<br/>sss<br/></div>");
	//scrollView.addItem(newItem);
	Page.addNewInfor(111,1111, '../img/search_nocontent.png',new Date().getTime(),new Date().getTime())
}