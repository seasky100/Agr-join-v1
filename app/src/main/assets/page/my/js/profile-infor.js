var SDK = window.sdk;

var Page = function(){}

Page.initPage = function(infor, type, editable){

	if(infor != null && infor.trim() != '') {

		$("#infor").val(infor);
	}

	if(type == 1) {

		$("#infor").attr("placeholder","请输入企业经营范围...");
	}else if(type == 2) {

		$("#infor").attr("placeholder","请输入专家研究领域...");
	}else if(type == 6) {

		$("#infor").attr("placeholder","请输入您的从事行业...");
	}

	if(!editable) {

		$("#infor").attr("disabled",true);
	}


}

Page.saveInfor = function(){

	var infor = $("#infor").val();
	SDK.save( infor);
}

Page.showHintDialog = function(flag){

	if(flag){
	
		$(".hintTxt").text("保存成功");
		$(".hintImg").removeClass("fail").addClass("success");
		$(".saveDialog").show();
	}else{
	
		$(".hintTxt").text("保存失败");
		$(".hintImg").removeClass("success").addClass("fail");
		$(".saveDialog").show();
	}
	
	setTimeout(function(){	Page.hideHintDialog(flag);},1000);
}

Page.hideHintDialog = function(flag){

	$(".saveDialog").hide();
	if(flag)
		SDK.closePage();
}

function onBtnClick(id, e){

	var  checked = $("#" + id + "-v").attr("class");	
	if("chk-select" == checked)
	 	$("#" + id + "-v").addClass("unchk-select").removeClass("chk-select");
	else
		$("#" + id + "-v").addClass("chk-select").removeClass("unchk-select");
	
}
