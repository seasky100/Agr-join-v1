var distanceY = 50;
var panel ,companys;
$(function(){
	panel = new Slide($(".panel ul")[0]);
	companys = new Slide($(".coms ul")[0]);
});

var  Slide = function(ul){//updateIndex 是一个方法,用来修改索引块的显示
	this.startPointX = 0;//手指触屏的横坐标
	this.startPointY = 0;//手指触屏的纵坐标
	this.endPointX = 0;//手指离开屏幕的横坐标
	this.endPointY = 0;//手指离开屏幕的纵坐标
	this.distance = 0;//手指离开屏幕前移动的距离
	this.totalDistance = 0;//ul移动的距离
	this.view = ul;//表示将要移动的元素
	this.timer = null;
	this.attachEvent();
}
Slide.load = function(){
	$("[action=slide]").each(function(index,ele){
		new Slide(ele);
	});
}
Slide.prototype = {
	attachEvent: function(){
		
		$(this.view).delegate("li","touchstart",this ,this.start);
		$(this.view).delegate("li","touchmove",this ,this.move);
		$(this.view).delegate("li","touchend",this ,this.end);
	},
	start: function(e){
		
		var that = e.data;
		if(that.timer!=null){
			clearInterval(that.timer);
		}
		that.startPointX = 0;
		that.startPointY = 0;
		that.endPointX = 0;
		that.endPointY = 0;
		that.distance = 0;
		var touch = e.originalEvent.targetTouches[0];
		that.startPointX = touch.pageX;
		that.startPointY = touch.pageY;
		
		//$(".p1").text("手指接触屏幕坐标"+that.startPointX+"  "+that.startPointY+"  total:"+that.totalDistance);
	},
	move:function(e){

		e.preventDefault();//阻止默认行为
		
		var that = e.data;
		if(that.timer!=null){
			clearInterval(that.timer);
		}

		//var touch = e.originalEvent;
		var touch = e.originalEvent.targetTouches[0];
		
		that.endPointX = touch.pageX;
		that.endPointY = touch.pageY;
		var target = that.view;
		if((Math.abs(that.endPointY - that.startPointY))<distanceY){
			e.stopPropagation();//停止冒泡
			that.distance = that.endPointX - that.startPointX;
			if(that.distance>20){
				
				var distance = Math.abs(that.distance)-that.totalDistance;
				$(target).css("-webkit-transform","translate("+distance+"px)");
			
			}else if(that.distance<-20){
			    
				var distance = Math.abs(that.distance)+that.totalDistance;
				$(target).css("-webkit-transform","translate(-"+distance+"px)");
			}
		}else{
		$(target).css("-webkit-transform","translate(-"+that.totalDistance+"px)");
		}
		
	},
	end:function(e){
	
		//手指离开屏幕的时候触发的事件
		e.preventDefault();//阻止默认行为
		var that = e.data;
		var startX = 0;//表示移动的开始位置
		
		if(that.timer!=null){
			clearInterval(that.timer);
		}
		
		var index = $(this).index();
		var target = that.view;
		//$(".p").text("正在执行end方法:"+Math.abs(that.endPointY - that.startPointY));
		//alert(Math.abs(that.endPointY - that.startPointY));
		//if(Math.abs(that.endPointY - that.startPointY) < distanceY && (Math.abs(that.endPointY - that.startPointY)<Math.abs(that.endPointX - that.startPointX))){
		//if((Math.abs(that.endPointY - that.startPointY)<Math.abs(that.endPointX - that.startPointX))){
		if((Math.abs(that.endPointY - that.startPointY))<distanceY){
			e.stopPropagation();//停止冒泡
			if(that.distance>20){
				
				//向右移动
				//如果是第一个元素,向右移动的时候自动回到原来的位置
				//$(".p1").text("手指向右移动了第一个元素:");
				if($(this).is(":first-child")){
					that.timer = setInterval(function(){
						if(that.distance<10){
							target.css("-webkit-transform","translate(0px)");
						}else{
							//$(".p").text("手指移动结束时的距离:"+that.distance);
							that.distance-=10;
							$(target).css("-webkit-transform","translate("+that.distance+"px)");
						}
					},1);  
					updateIndex(-1,this)
				}else{
					//如果不是第一个元素
					var li = $(target).children("li")
					var  padding_left = parseInt(li.css("padding-left"));
					var  padding_right = parseInt(li.css("padding-right"));
					var  width = li.width()+padding_left+padding_right;
					startX = that.totalDistance - Math.abs(that.distance);
					that.totalDistance-=width;
					//$(".p1").text("向右移动的不是第一个:"+startX+" "+that.totalDistance);
					that.timer = setInterval(function(){
						if(startX>that.totalDistance){
							$(target).css("-webkit-transform","translate(-"+startX+"px)");startX-=10;
						}else{
							$(target).css("-webkit-transform","translate(-"+that.totalDistance+"px)");
						}
					},10); 
					updateIndex(index-2,this);
				}
				
			}else if(that.distance<-20){
					
				//向左移动
				if($(this).is(":last-child")){
		
					//如果是最后一个元素
					var li = $(target).children("li")
					var  padding_left = parseInt(li.css("padding-left"));
					var  padding_right = parseInt(li.css("padding-right"));
					var  width = li.width()+padding_left+padding_right;
					startX = that.totalDistance +Math.abs(that.distance);
					//$(".p").html("向左移动到了最后一个元素了:"+startX+"  "+that.distance+"");
					that.timer = setInterval(function(){
						if(startX>that.totalDistance){
							startX-=10;
							$(target).css("-webkit-transform","translate(-"+startX+"px)");
						}else{
							$(target).css("-webkit-transform","translate(-"+that.totalDistance+"px)");
						}
					},10);
					updateIndex(index-1,this);
					
				}else{
					//不是最后一个元素的时候
					var li = $(target).children("li")
					var  padding_left = parseInt(li.css("padding-left"));
					var  padding_right = parseInt(li.css("padding-right"));
					var  width = li.width()+padding_left+padding_right;
					//$(".p1").text(li.width()+padding_left+padding_right);
					startX = that.totalDistance  - that.distance;
					that.totalDistance += width;
					//$(".p1").text(" startX:"+startX+"  that.totalDistance:"+that.totalDistance+"    width:"+width);
					that.timer = setInterval(function(){
						if(startX<that.totalDistance){//alert(startX);
							startX+=10;
							$(target).css("-webkit-transform","translate(-"+startX+"px)");
						}else{
							
							$(target).css("-webkit-transform","translate(-"+that.totalDistance+"px)");
						}
					},10); 
					updateIndex(index,this);
				}
			}else{
				$(target).css("-webkit-transform","translate(-"+that.totalDistance+"px)");
			}
		}
	}
}

function updateIndex(index,ele){

	if($(ele).parent().parent().hasClass("panel")){
		//图片区域
		var idx = index-0+1;
		$(".pics .count p").css("background-color","#BCBCBC");
		$(".pics .count p:eq("+idx+")").css("background-color","#00A0E9");
	}else if($(ele).parent().parent().hasClass("coms")){
		var idx = index-0+2;
		$(".p").text(idx);
		var length = $(ele).parent().children().length;
		$(".seeds .title-bar .right").text(idx+"/"+length)
	}
}
/*
//种源企业的滑动事件
$(function(){

	//种源
	//prev 点击事件
	$(".prev").click(function(){

		//查看下一个
		btnClick($(this).next("div"),true);
	});

	//next 点击事件
	$(".next").click(function(){

		//查看上一个
		btnClick($(this).prev("div"),false);
	});
});


function	btnClick(ele,flag){

	if(companys.timer!=null){
		 clearInterval(companys.timer);
	}
	
	var container = $(ele).children("ul");
	var children = container.children("li");
	var length = children.length;
	
	//var  target = $(".coms ul li");
	//var  li = $(".coms ul li");
	//var  padding_left = parseInt(li.css("padding-left"));
	//var  padding_right = parseInt(li.css("padding-right"));
	//var  width = li.width()+padding_left+padding_right;
	if(flag){

		//,显示上一张
		if(companys.totalDistance<=0){
			alert("这已经是第一个了");
		}else{
			var  padding_left = parseInt(children.css("padding-left"));
			var  padding_right = parseInt(children.css("padding-right"));
			var  width = children.width()+padding_left+padding_right;
			startX = companys.totalDistance - Math.abs(companys.distance);
			companys.totalDistance-=width;
			$(".p").text("向右移动的不是第一个:"+startX+" "+companys.totalDistance);
			companys.timer = setInterval(function(){
				if(startX>companys.totalDistance){
					$(container).css("-webkit-transform","translate(-"+startX+"px)");startX-=1;
				}else{
					$(container).css("-webkit-transform","translate(-"+that.totalDistance+"px)");
				}
			},1); 
			var index = $(".seeds .title-bar .right").text().split("/")[0];
			$(".seeds .title-bar .right").text(index-1+"/"+length)
		}
	}else{
		
		//向左滑动,显示下一张
		var li = children.filter(":visible");
		var last = $(children[length-1]);
		var  padding_left = parseInt(children.css("padding-left"));
		var  padding_right = parseInt(children.css("padding-right"));
		var  width = children.width()+padding_left+padding_right;
		if(companys.totalDistance>=width*(length-1)){
			alert("已经是最后一个了");
		}else{
			startX = companys.totalDistance  - companys.distance;
			companys.totalDistance += width;
			//$(".p").text(" startX:"+startX+"  that.totalDistance:"+that.totalDistance+"    width:"+width);
			companys.timer = setInterval(function(){
				if(startX<companys.totalDistance){//alert(startX);
					startX+=10;
					$(container).css("-webkit-transform","translate(-"+startX+"px)");
				}else{
					
					$(container).css("-webkit-transform","translate(-"+companys.totalDistance+"px)");
				}
			},10); 
			var index = $(".seeds .title-bar .right").text().split("/")[0];
			$(".seeds .title-bar .right").text(index-0+1+"/"+length)
		}
	
	}
}

*/