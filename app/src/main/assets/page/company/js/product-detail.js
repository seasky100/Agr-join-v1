
var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();

var Page = function() {

}

Page.initSwiper= function() {
var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true
    });
}

Page.addImg = function(img) {

	if(img != null && img.trim() != "") {

		new ImgItem(img,$("#img-container"));
	}
}

Page.initImg = function(imgSize,height) {

	if(imgSize == 0) {

		$("#img-container").parent().hide();
	}else {

		$("#img-container").parent().height(height);
	}
}

// 项目初始
Page.init = function(id, name,compName, compId, unit, price, materialDesc, techDesc, intro) {

	$("[sid=name]").text(isEmpty(name,"暂未填写"));
	$("[sid=unit]").text(isEmpty(unit,"暂未填写"));
	$("[sid=price]").text(price==0?"面议":"￥" + price + "元");
	$("[sid=materialDesc]").text(isEmpty(materialDesc,"暂未填写"));
	$("[sid=intro]").text(isEmpty(intro,"暂未填写"));
	$("[sid=techDesc]").text(isEmpty(techDesc,"暂未填写"));
	$("#ownerName").text(isEmpty(compName,"暂未填写"));
	$("#ownerName").attr("ownerId",compId);//拥有者标识
	Button.loadAllButton({
		onClick: OnBtnClick
	});
}


Page.showLoading = function() {

	Loading.show();
}

Page.hideLoading = function() {

	Loading.hide();
}



/**
 * 调用action 下载完成之后调用代理对象的方法替换默认图片
 */
Page.onFinishDownload = function() {

	// 调用委托类的更新方法
	fileLoader.downloadingFile.deligate.onFileFinishLoad();
//	onFileFinishLoad();
	// 继续下载文件
	fileLoader.doTask();
}


// 加载数据超时提醒
Page.loadFailed = function() {

	Loading.hide();
	$("[sid=mainDiv]").hide();
	$("[sid=hint]").show();
}


Page.closePage = function() {

	setTimeout(function() {

		SDK.closePage();

	}, 1000);
}

/** 检测字符串是否为空, 空 自动填充为指定的字符串* */
function isEmpty(source, defaultStr) {

	if (source == null || source.trim() == "") {

		source = defaultStr;

	}
	return source;
}

function OnBtnClick(id,e) {
	if(id == "owner") {


		//点击成果页面的拥有者
		var ownerId = $("#ownerName").attr("ownerId");
		//SDK.findOwner(parseInt(ownerId),parseInt(type),$("#" + id).text());
		SDK.findOwner(parseInt(ownerId),$("#ownerName").text());
	}
}