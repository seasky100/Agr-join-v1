var myUpArrow = null;
var mySlipObj = null;

var slideObj = function(){
	ai.hideUrl();
	var bar_list_div = ai.i("bar_list_div"),
		bar_list     = ai.i("bar_list"),
		minus        = ai.ovb.ios() && ai.ovb.safari() && !ai.ovb.ipad() ? -20 :0,
		up_arrow	 = ai.i("up_arrow"),
		up_text      = ai.i("up_text"),
		list  = ai.i("list");
		up_ing       = false,
	  down_ing       = false;
	  
	bar_list.style.height =  ai.wh() - minus +"px";
	myUpArrow = up_arrow;

	function update() {
		if(this.xy > 10 && up_ing == false){
			up_ing = true;
			down_ing = false;
			up_arrow.style['webkitTransitionDuration'] = '300ms';	
			up_arrow.style['webkitTransform'] = 'rotate(-180deg)';
			up_text.innerHTML = "松开即可刷新";
			this.up_range = 0;
		}else if(this.xy < 10 && down_ing == false){
			down_ing = true;
			up_ing = false;
			up_arrow.style['webkitTransitionDuration'] = '300ms';	
			up_arrow.style['webkitTransform'] = 'rotate(0deg)';
			up_text.innerHTML = "下拉刷新";
			this.up_range = 60;
		}
	}
	function loading() {
		if(this.up_range == 0){
			var that = this;
			mySlipObj = that;
			
			up_text.innerHTML= 'Loading...';
			up_arrow.style['webkitTransitionDuration'] = '0ms';	
			up_arrow.className += ' loading';
			//setTimeout(function () {
				now = new Date(); 
				//var newli = '<li>这是 '+now.getHours()+' : '+now.getMinutes()+' : '+now.getSeconds()+'&nbsp;&nbsp;加载的新内容</li><li>这是 '+now.getHours()+' : '+now.getMinutes()+' : '+now.getSeconds()+'&nbsp;&nbsp;加载的新内容</li><li>这是 '+now.getHours()+' : '+now.getMinutes()+' : '+now.getSeconds()+'&nbsp;&nbsp;加载的新内容</li>';
				//var bar_list_ul_html = list.innerHTML;
				//list.innerHTML = $(".template").html() + bar_list_ul_html;
				loadMoreData();
			//}, 200);
		}
	}
	var slipjs_yuxiang = slip('px', bar_list_div, {
			up_range: 60,
			 moveFun: update,
			  endFun: loading
	});
	
}

function  notifySlideLoadDataFinished(){
	
	//$(".list").prepend($(".template").html());
	//var view = $(".list").children().first();
	//view.find("img").attr("src","../img/default-head-icon.jpg");
	myUpArrow.style['webkitTransform'] = 'rotate(0deg)';
	myUpArrow.className = 'up_down_arrow';
	mySlipObj.up_range = 60;
	mySlipObj.refresh();
}