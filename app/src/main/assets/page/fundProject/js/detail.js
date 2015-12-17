var SDK = window.sdk;
var dialog;

var Page = function(){};

//项目初始
Page.initBasic = function(name, jd, logo, compname, compId, price, projField, summary){
	

	if (logo != null && logo.trim() != "") {

		$("[sid=logo]").attr("src",logo);

	}else {

		$("[sid=logo]").attr("src",compDefaultLogo);
	}


	$("#name").text(isEmpty(name,"暂未填写"));
	

	$("#jd").text(isEmpty(jd,"暂未填写"));
	$("#compname").text(isEmpty(compname,"暂未填写"));
	$("#compname").click(function() {

		SDK.findOwner(parseInt(compId),1,compname);
	});
	if(price <= 0)
		price = "面议";
	else
		price ="￥" + price +  "万";
	$("#price").text(price);

	if(projField == null || projField.trim() == "") {

		$("field").hide();
	}else {

		$("#projField").text(projField);
	}

	if(summary == null || summary.trim() == "") {

		$("intro-panel").hide();
	}else {

		$("#intro").text(summary);
	}
};

/** 检测字符串是否为空, 空 自动填充为指定的字符串* */
function isEmpty(source, defaultStr) {

	if (source == null || (source+"").trim() == "") {

		source = defaultStr;

	}
	return source;
}


Page.showLoading = function(){

	Loading.show();
	//$("body div:not(.loading)").hide();
}

Page.hideLoading = function(){

	Loading.hide();
	//$("body div:not(.loading)").show();
}

/** 设置标题栏 **/
Page.setTitlebar = function(title) {

	SDK.setTitle(title);
}