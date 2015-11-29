var SDK = window.sdk;

var Page = function(){
	
}

Page.initPage = function(tel,editable){

	if(null != tel)
		$(".tel").val(tel);

	if(editable) {

		var input = $("input");
		var deleteIcon = input.next();

		if(input.val().length > 0)
			deleteIcon.show();
		else
			deleteIcon.hide();

		input.bind("input", function(e){

			if(input.val().length > 0)
				deleteIcon.show();
			else
				deleteIcon.hide();
		});

		deleteIcon.bind("click",function(){

			input.val("");
		});

	}else {

		$("input").attr("disabled",true);
	}
	$(".panel").show();
}

Page.showHintDialog = function(flag){

	if(flag == 2){
	
		$(".hintTxt").text("保存成功");
		$(".hintImg").removeClass("fail").addClass("success");
		$(".saveDialog").show();
	}else if(flag == 1){
	
		$(".hintTxt").text("手机号码被占用");
		$(".hintImg").removeClass("success").addClass("fail");
		$(".saveDialog").show();
	}else if(flag == 0) {

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

Page.saveTel = function(){

	var tel = $("#tel").val();
	if(null != tel && "" != tel.trim()){
		SDK.save(tel);
	}
}