var SDK = window.sdk;

var Page = function(){
	
}

Page.initPage = function(professionalTitle){

	if(null != name)
		$(".name").val(professionalTitle);
		
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
	
	$(".panel").show();
}

Page.showHintDialog = function(flag){

	if(flag){
		
		$(".hintTxt").text("保存成功");
		$(".hintImg").removeClass("fail").addClass("success");
		$(".saveDialog").show();
	}else{
		
		$(".hintImg").text("保存失败");
		$(".hintTxt").removeClass("success").addClass("fail");
		$(".saveDialog").show();
	}
	
	setTimeout(function(){
		Page.hideHintDialog(flag);
	}, 1000);
}

Page.hideHintDialog = function(flag){

	$(".saveDialog").hide();
	
	if(flag)
		SDK.closePage();
}

Page.saveProfessionalTitle = function(){

	var nickname = $("#name").val();
	
	if(null != nickname && "" != nickname.trim())
		SDK.save(nickname);
}

