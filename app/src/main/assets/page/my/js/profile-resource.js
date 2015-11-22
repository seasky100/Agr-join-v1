var sTitleBar = new STitleBar();
var sEvent = new SEvent();

/**
 * 页面在前台展示时，执行此操作
 */
SDKCall.onPageShow = function(){
	
	sTitleBar.setTitleBar(true,null, "我拥有");
}

function OnBtnClick(id, e){

	var  chk = e.view.children()[1];
	var  checkBox = $("ul li  div:odd");
	console.log(checkBox);
    $(checkBox).removeClass("chk-select").addClass("unchk-select");
	$(chk).removeClass("unchk-select").addClass("chk-select");
}

$(function(){
		   
	Button.loadAllButton({
		onClick: OnBtnClick
	});
});						    

