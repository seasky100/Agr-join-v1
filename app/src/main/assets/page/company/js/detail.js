var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();
var dialog;
var Page = function() {

};

// 项目初始
Page.init = function(id, logo, name, auth, attentNum, isAttented, expoNo, trade, tel, area, regTime, regCapital, regType, addr) {

	// 显示logo
//	var driver = FILE.getSysPath();
//	local = driver + compLogoPath;// logo的本地存储路径
//	var fileName = "";
//	if (logo != null && logo.trim() != "") {
//
//		fileName = logo.split("/").pop();// logo 的文件名
//	}
//	var src = local + fileName;
//	if (logo != null && logo.trim() != "") {
//
//		if (FILE.isFileExist(src) == true) {
//
//			$("[sid=logo]").attr("src", src);
//		} else {
//
//			$("[sid=logo]").attr("src", compDefaultLogo);
//			SDK.loadLogo(logo);
//		}
//	}else {
//
//		$("[sid=logo]").attr("src", compDefaultLogo);
//	}
	$("[sid=logo]").attr("src", logo);
	$("[sid=name]").text(isEmpty(name, "暂未填写"));

	if(auth == "true") {

		$("[sid=auth]").show();
	}else {

		$("[sid=auth]").remove();
	}

	 if(isAttented==1) {
        $("#attent").attr("display","button");
        $("#attent").removeAttr("onclick");
		$("#attent img").attr("src","../img/guanzhu2.png");
		 $("#attent-text").text("已关注");
	 }else if(isAttented == 0){

		$("#attent-text").text("关注");
        $("#attent").attr("display","button");
        $("#attent").removeAttr("onclick");
	 	$("#attent img").attr("src","../img/guanzhu1.png");
	 }else if(isAttented == -1){

	 	//点击登录
	 	$("#attent").removeAttr("display");
	 	$("#attent").attr("onclick","login();");
	 }

    $("#attent").attr("attent",isAttented);//认证状态
    $("#attent").attr("compId",id);//企业id
	$("[sid=attentNum]").text(attentNum);
	$("[sid=expoNo]").text(expoNo);
	$("[sid=trade]").text(isEmpty(trade, "暂未填写"));
	$("[sid=tel]").text(isEmpty(tel, "暂未填写"));
	$("[sid=area]").text(isEmpty(area, "暂未填写"));
	$("[sid=regTime]").text(isEmpty(regTime, "暂未填写"));
	$("[sid=regCapital]").text((regCapital == null || regCapital == "" || regCapital == 0)?"暂未填写":regCapital+"万");
	$("[sid=regType]").text(isEmpty(regType, "暂未填写"));
	$("[sid=addr]").text(isEmpty(addr, "暂未填写"));

};

Page.showLoading = function() {

	Loading.show();
	$("#content-panel").hide();
};

Page.hideLoading = function() {

	Loading.hide();
	$("#content-panel").show();
};

// 企业经营范围
Page.addService = function(info, isMore, compId, name, updateDate) {

	if (info == null || info.trim() == "") {

		$("[sid=service]").parent().hide();
	} else {

		if(isMore) {

			$("#service-more").show();
			$("#service-more").attr("cid",compId);
			$("#service-more").attr("updateDate",updateDate);
			$("#service-more").attr("name",name);
			$("[sid=service]").prepend(info+"...");
		}else{

			$("[sid=service]").prepend(info);
		}

	}


};


// 企业介绍
Page.addInfo = function(info, isMore, compId, name, updateDate) {

	if (info == null || info.trim() == "") {

		$("[sid=detail]").parent().hide();
	} else {

		if(isMore) {

			$("#info-more").show();
			$("#info-more").attr("cid",compId);
			$("#info-more").attr("updateDate",updateDate);
			$("#info-more").attr("name",name);
			$("[sid=detail]").prepend(info+"...");
		}else{

			$("[sid=detail]").prepend(info);
		}
	}
    Button.loadAllButton({
		onClick : onBtnClick
	});
};

//添加技术需求requId, logo, name, hj, price, oldLogo,ownerName,lastModify,$("#list")
Page.addRequ = function(requId, name, hj, price, ownerName,lastModify) {

	new RequItem(requId, name, hj, price, ownerName, lastModify,$("#requ-container"));
};


// 加载数据超时提醒
Page.loadFailed = function() {

	$("[sid=content-panel]").hide();
	$("[sid=hint]").show();
	setTimeout(function() {

		SDK.closePage();
	},1000);
}


Page.showHintDialog = function(message,result,type){

    if(result) {

        if(type == 1) {

           $("#attent").attr("attent",0);
           $("#attent-text").text("关注");
           $("#attent img").attr("src","../img/guanzhu1.png");
           $("#attentNum").text($("#attentNum").text()-1);
        }else {

           $("#attent img").attr("src","../img/guanzhu2.png");
           $("#attent").attr("attent",1);
           $("#attent-text").text("已关注");
           $("#attentNum").text($("#attentNum").text()-0 + 1);
        }
        if(dialog != null) {

			dialog.close();
		}
		new Dialog(message,true,$("body"),1000);
	}else {

		 if(type == 1) {

			  new Dialog("取消关注失败",true,$("body"),1000);
		 }else {

			  new Dialog("关注失败",true,$("body"),1000);
		 }
	 }
}

Page.closePage = function() {

	setTimeout(function() {

		SDK.closePage();

	}, 1000);
}
/**
 *调用action 下载完成之后调用代理对象的方法替换默认图片
*/
Page.onFinishDownload = function(){

	// 调用委托类的更新方法
	fileLoader.downloadingFile.deligate.onFileFinishLoad();

	// 继续下载文件
	fileLoader.doTask();
}

/** 检测字符串是否为空, 空 自动填充为指定的字符串* */
function isEmpty(source, defaultStr) {

	if (source == null || source.trim() == "") {

		source = defaultStr;

	}
	return source;
}

//异步下载企业logo 的回调
Page.loadhead = function(logo) {

	var driver = FILE.getSysPath();
	local = driver + compLogoPath;// logo的本地存储路径
	if(logo != null && logo.trim() != ""){

		fileName = logo.split("/").pop();// logo 的文件名
	}
	var src = local + fileName;
	if (logo != null && logo.trim() != "") {

		if (FILE.isFileExist(src) == true) {

			$("[sid=logo]").attr("src",src);
		}
	}
};

//产品轮播
Page.initSwiper= function() {
  var swiper = new Swiper('.swiper-container', {
          /* scrollbar: '.swiper-scrollbar',*/
     	  scrollbarHide: true,
     	  slidesPerView: "auto",
     	  centeredSlides: false,
     	  spaceBetween: 10,
     	  grabCursor: true,
           loop: false
     });
}

Page.addImg = function(id, name, lastModify,img, localImg) {

	if(img != null && img.trim() != "") {

		new ImgItem(id, name, lastModify, img, localImg, $("#img-container"));
	}
}


function onBtnClick(id,e) {

  	if(id == "tel") {
        var tel = e.view.text();
        if(parseInt(tel) > 0) {

            new HintDialog("是否直接电话联系","拨号","取消",$("body"),call,tel);
        }
	}else if(id == "attent") {

		var compId = e.view.attr("compId");
		var attentStatus = e.view.attr("attent");
		attent(compId+","+attentStatus);
		//点击关注
//		if(attentStatus == 1) {
//
//			 new HintDialog("确认取消关注该企业","确认","取消", $("body"), attent, compId+","+attentStatus);
//		}else {
//
//			 new HintDialog("确认关注该企业","确认","取消", $("body"), attent, compId+","+attentStatus);
//		}
	}else if(id == "service-more") {

		//新页面显示经营范围
		SDK.showMore("service", parseInt(e.view.attr("cid")), e.view.attr("name"),e.view.attr("updateDate"));
	}else if(id == "info-more") {

		//新页面显示企业介绍
		SDK.showMore("info", parseInt(e.view.attr("cid")), e.view.attr("name"), e.view.attr("updateDate"));
	}
}

function call(tel){

	SDK.call(tel);
}

function attent(param){

    //参数是专家id 和 关注状态
	SDK.attent(parseInt(param.split(",")[0]),parseInt(param.split(",")[1]));
}

function login() {

    SDK.openLoginPage(2);
}

//专家没哟成果
Page.noRequ = function() {

	$("#requ-panel").hide();
}

Page.noProduct = function() {

	$("#product-panel").hide();
}


/** 设置标题栏 **/
Page.setTitlebar = function(title) {

	SDK.setTitle(title);
}