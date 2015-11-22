var Page = function(){}
var SDK = window.sdk;
var id ;
$(function(){

	//为前面的丰景咨询添加单击事件
$('[sid=findview-infor]').click(function(){

		SDK.openMsgInforList(parseInt(id));
	});
})

/**设置最前面的丰景资讯内容**/
Page.setFindviewInfor = function(reqTime, title, count,sid){

	id = sid;
	if(null != reqTime)
		$('[sid=findview-last-msg-time]').text(formatDate(reqTime));

	//设置丰景资讯的标题
	if(title != null && title.trim() != "")
		$('[sid=findview-last-msg-title]').text(title);

	if(count == 0){
		$('[sid=findview-count]').hide();
	}else{
		$('[sid=findview-count]').show();

		$('[sid=findview-count]').text(count);
	}

}

