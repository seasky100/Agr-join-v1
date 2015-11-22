var SDK = window.sdk;

var Page = function(){
	
}

Page.initPage = function(addr,editable){

	if(null != addr)
		$(".address").text(addr);

	if(editable) {

		var limitPanel = $(".num");
		var inputSize = $("textarea").val().length;
		var totalSize = limitPanel.text();
		limitPanel.text(totalSize - inputSize);

		$("textarea").bind("input",function(){

			var txt = $(this).val();
			var size = txt.length;
			var overplus = totalSize - size;
			if(overplus<=0){

				SDK.clientHint("最多只能输入100个字符！");
				txt = txt.substring(0,100);
				$(this).val(txt);
			}else{

				limitPanel.text(overplus);
			}

		});
	}else {

		$("textarea").attr("disabled",true);
	}
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
	
	setTimeout(function(){
	
		Page.hideHintDialog(flag);
	}, 1000);
}

Page.hideHintDialog = function(flag){

	$(".saveDialog").hide();
	
	if(flag)
		SDK.closePage();
}

Page.saveAddr = function(){

	var addr = $(".address").val();

	if(null != addr && "" != addr.trim())
		SDK.save(addr);
}
