var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();

var Page = function() {

}

function call(tel) {

	SDK.call(tel);
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
Page.init = function(id, title, achvNo, price, researchStatus, isFavorite, ownerName, ownerId, ownerType, zrType) {

	$("[sid=name]").text(isEmpty(title,"暂未填写"));
	$("[sid=achvNo]").text(isEmpty(achvNo,"暂未填写"));

	$("[sid=price]").text(price==0?"面议":"￥" + price+"万");
	$("[sid=research-status]").text("研究状态：" + isEmpty(researchStatus,"暂未填写"));
	$("[sid=ownerName]").text(isEmpty(ownerName,"暂未填写"));
	$("[sid=ownerName]").attr("ownerId",ownerId);//拥有者标识
	$("[sid=ownerName]").attr("type",ownerType);//拥有者类型 1企业 2专家
	$("[sid=zr-type]").text(isEmpty(zrType,"暂未填写"));

	$("#favorite").attr("achvId",id);
	$("#favorite").attr("favoriteStatus",isFavorite);//关注状态

	//收藏
	if(isFavorite == 1) {

		$("#favorite img").attr("src","../img/coll_filled.png");
		$("#favorite-text").text("已收藏");
	}else {

		$("#favorite img").attr("src","../img/coll.png");
	}
	Button.loadAllButton({
		onClick: OnBtnClick
	});
}


Page.initBasic = function(material, materialType, prod, prodType, trade, hj, problem, synbal, cxDesc, jd, apply, evaluate, patent) {

	$("[sid=material]").text(isEmpty(material,"暂未填写"));//适用原料
	$("[sid=materialType]").text(isEmpty(materialType,"暂未填写"));//适用原料类型
	$("[sid=prod]").text(isEmpty(prod,"暂未填写"));//适用产品
	$("[sid=prodType]").text(isEmpty(prodType,"暂未填写"));
	$("[sid=trade]").text(isEmpty(trade,"暂未填写"));
	$("[sid=hj]").text(isEmpty(hj,"暂未填写"));
	$("[sid=problem]").text(isEmpty(problem,"暂未填写"));
	$("[sid=synbal]").text(isEmpty(synbal,"暂未填写"));
	$("[sid=cxDesc]").text(isEmpty(cxDesc,"暂未填写"));
	$("[sid=jd]").text(isEmpty(jd,"暂未填写"));
	$("[sid=apply]").text(isEmpty(apply,"暂未填写"));
	$("[sid=evaluate]").text(isEmpty(evaluate,"暂未填写"));
	$("[sid=patent]").text(isEmpty(patent,"暂未填写"));

}

// 价值体现
Page.initValueEmbodiment = function(invest, social, hardware, gy) {


	if(isNotBlank(invest) && isNotBlank(social) && isNotBlank(hardware) && isNotBlank(gy)) {

		if(invest == null || invest.trim() == "") {

			$("[sid=invest]").parent().hide();
		}else{

			$("[sid=invest]").text(isEmpty(invest,"暂未填写"));//项目投资效益分析
		}

		if(social == null || social.trim() == "") {

			$("[sid=social]").parent().hide();
		}else{

			$("[sid=social]").text(isEmpty(social,"暂未填写"));//经济社会价值
		}

		if(social == null || social.trim() == "") {

			$("[sid=social]").parent().hide();
		}else{

			$("[sid=social]").text(isEmpty(social,"暂未填写"));//经济社会价值
		}

		if(hardware == null || hardware.trim() == "") {

			$("[sid=hardware]").parent().hide();
		}else{

			$("[sid=hardware]").text(isEmpty(hardware,"暂未填写"));//技术成果产业化所需硬件条件
		}

		if(gy == null || gy.trim() == "") {

			$("[sid=gy]").parent().hide();
		}else{

			$("[sid=gy]").text(isEmpty(gy,"暂未填写"));//技术工艺流程
		}
	}else {

		$("#jiazhi").hide();
	}
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


Page.showHintDialog = function(message,result,type){

    if(result) {

        if(type == 1) {

           $("#favorite").attr("favoriteStatus",0);
           $("#favorite img").attr("src","../img/coll.png");
           $("#favorite-text").text("收藏");
        }else {

           $("#favorite img").attr("src","../img/coll_filled.png");
           $("#favorite").attr("favoriteStatus",1);
			$("#favorite-text").text("已收藏");
        }
    }

     new Dialog(message,true,$("body"),1000);

}
Page.closePage = function() {

	setTimeout(function() {

		SDK.closePage();

	}, 1000);
}

// 下载图片
function loadIcon() {

	fileLoader.downloadFile(new DownloadFile(null, local, fileName, logo));
}
// 图片下载完成
function onFileFinishLoad() {

	var src = local + fileName;
	if (FILE.isFileExist(src) == true) {
		img = "<img src='" + src + "'/>";

		var logoEle = $("[sid=logo]");
		if (logoEle != null) {

			logoEle.attr("src", src);
		} else {

			$("[sid=img-panel]").append(img);
		}
	}
}
/** 检测字符串是否为空, 空 自动填充为指定的字符串* */
function isEmpty(source, defaultStr) {

	if (source == null || source.trim() == "") {

		source = defaultStr;

	}
	return source;
}

//非空返回 true
function isNotBlank(text) {

	return text!= null && text.trim()!="";
}
function OnBtnClick(id,e) {

	if(id == "owner") {

		//点击成果页面的拥有者
		var ownerId = $("#ownerName").attr("ownerId");
		var type = $("#ownerName").attr("type");
		//SDK.findOwner(parseInt(ownerId),parseInt(type),$("#" + id).text());
		SDK.findOwner(parseInt(ownerId),parseInt(type),$("#ownerName").text());
	}else if(id="favorite") {

		var achvId = $("#"+id).attr("achvId");
		var favoriteStatus = $("#"+id).attr("favoriteStatus");
		favorite(achvId+","+favoriteStatus);
		//点击关注
//		if(favoriteStatus == 1) {
//
//			 new HintDialog("确认取消收藏该成果吗?","确认","取消", $("body"), favorite, achvId+","+favoriteStatus);
//		}else {
//
//			 new HintDialog("确认收藏该成果吗?","确认","取消", $("body"), favorite, achvId+","+favoriteStatus);
//		}
	}
}

function favorite(param){

    //参数是专家id 和 关注状态
	SDK.favorite(parseInt(param.split(",")[0]),parseInt(param.split(",")[1]));
}

//成果没有图片的时候调用,隐藏 图片区域
Page.noImg = function() {

	$("#img-container").parent().parent().hide();
}

//$(function() {
//
//    Page.initImg(4,200);
//	Page.initSwiper();
//
//});