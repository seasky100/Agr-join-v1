var sDialog = new SDialog();

$(document).ready(function(){ 
	
	Button.loadAllButton({
		onClick: OnBtnClick
	});
}); 

/**
 * 页面首次加载，执行页面初始化操作
 */
SDKCall.onPageInit = function(params){
			   
	$("#dialogPanel").bind("click", null, stopClick);

	document.onclick = function(){
		closeDialog();
	};
}

function OnBtnClick(id, e){
	//alert("点击："+id + "  "+e);
	var callback = id;
	sDialog.callback(callback+"()");
}

/**
 * 响应点击事件，并阻止事件冒泡
 */
function stopClick(event){
	
	event.preventDefault();
	var e=window.event || event;

	if(e.stopPropagation){
		e.stopPropagation();
	}else{
		e.cancelBubble = true;
	}  
}

function closeDialog(){
	
	sDialog.close();
}
