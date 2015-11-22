var SDK = window.sdk;

var Page = function(){}
/**
 * 页面首次加载，执行页面初始化操作
 */
Page.init = function(){
	
	Loading.show();
}

Page.initFinish = function(content, tel){
	$("[sid=content]").html(content);
	
	//alert(tel);
	if(tel == null || tel == "暂未填写" || tel.trim() =="") {
		
		$("[sid=func]").hide();
	}else {
		
		$("[sid=func]").show();
		$("#msg").attr("tel", tel);//alert(tel);
	}
	Button.loadAllButton({
		onClick: OnBtnClick
	});
	//$("[sid=func]").show();
	
	Loading.hide();
}

function OnBtnClick(id, e){
	
	var tel = $("#"+id).attr("tel");//alert(tel);
	if (id="msg")
		new HintDialog("是否直接电话联系","拨号","取消",$("body"),call,tel);
}

function call(tel){

	SDK.call(tel);
}
