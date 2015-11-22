var Loading = function(){}

Loading.show = function(txt){
	
	var loadingView = $(".loading");

	if(loadingView.length == 0){
		var viewHtml = '<div class="loading">'+
							'<p class="loading-panel">'+
								'<span class="ico-box"><img class="ico" src="../img/loading.png" /></span>'+
								'<span class="txt">正在加载</span>'+
							'</p>'+
						'</div>';
		$("body").append(viewHtml);
		loadingView = $(".loading");
	}else{
		$(".loading .ico").show();
		loadingView.show();
	}
	
	$(".loading .ico").css("-webkit-animation-play-state", "running");
	
	if(txt)
		$(".loading .txt").html(txt);
}

Loading.hide = function(txt){
	
	var loadingView = $(".loading");
	
	if(loadingView.length >= 0){
	
		$(".loading .ico").css("-webkit-animation-play-state", "paused");
		$(".loading .ico").hide();
		loadingView.fadeOut(300);
	}
}

