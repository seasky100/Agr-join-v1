var SDK = window.sdk;

var Page = function(){}
/**
 * 页面首次加载，执行页面初始化操作
 */
Page.init = function(){
	
	Loading.show();
}

Page.initFinish = function(info,tradeNames,time, join, tel){
	if(info == null || info.trim() == "") {
		
		$("[sid=infoPanel]").hide();
	}else{
		
		$("[sid=info]").html(info);
	}
	
	$("[sid=tradeNames]").html(isEmpty(tradeNames,"暂未填写"));
	if (time != null ) {

		var date = new Date();
		date.setTime(time);
		var timeStr = date.getFullYear() + "-" + date.getMonth() + "-"
				+ date.getDate() ;
		$("[sid=time]").html(timeStr);
	}
	$("[sid=join]").html(isEmpty(join,"暂未填写"));
	
	if(tel != null && !tel.trim() == "") {
		
//		/alert(tel);
		$("[sid=msg]").attr("tel",tel);
		
		Button.loadAllButton({
			onClick: OnBtnClick
		});
		$("[sid=func]").show();
	}
	Loading.hide();
}


Page.showLoading = function() {

	Loading.show();
}

Page.hideLoading = function() {

	Loading.hide();
}
function OnBtnClick(id, e){
	
	var tel = $("[sid=msg]").attr("tel");
	if (id="msg")
		new HintDialog("是否直接电话联系","拨号","取消",$("body"),call,tel);
}

function call(tel){

	SDK.call(tel);
}

/** 检测字符串是否为空, 空 自动填充为指定的字符串* */
function isEmpty(source, defaultStr) {

	if (source == null || source.trim() == "") {

		source = defaultStr;

	}
	return source;
}