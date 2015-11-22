var sTitleBar = new STitleBar();
var sEvent = new SEvent();

/**
 * 页面首次加载，执行页面初始化操作
 */
SDKCall.onPageInit = function(id){

	sTitleBar.setTitleBar(true,"分享", "发送");
	
	
	Button.loadAllButton({
		onClick: OnBtnClick
	});
}


/*
 * 当点击了操作按钮之后，执行此操作
 */
SDKCall.onFunctionBtnClick = function(){

	$(".attent").show();
	setTimeout("attentHide()",1000); 
}

function attentHide(){

	$(".attent").hide();
	sEvent.closePage();
}

function OnBtnClick(id, e){
	var  chk = e.view.children()[0];
	var  checked = $(chk).attr("checked");
	if(checked=="checked"){
		 $(chk).removeAttr("checked");
		 $(chk).addClass("unchk-select").removeClass("chk-select");
	}else{
		$(chk).attr("checked",true);
		$(chk).addClass("chk-select").removeClass("unchk-select");
	}
}

