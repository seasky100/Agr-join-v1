
var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();
var Page = function() {

};

// 机构初始
Page.init = function(id, logo, name, auth, expoNo, tel, addr) {

	// 显示logo
	var driver = FILE.getSysPath();
	local = driver + orgLogoPath;// logo的本地存储路径
	var fileName = "";
	if (logo != null && logo.trim() != "") {

		fileName = logo.split("/").pop();// logo 的文件名
	}
	var src = local + fileName;
	if (logo != null && logo.trim() != "") {

		if (FILE.isFileExist(src) == true) {

			$("[sid=logo]").attr("src", src);
		} else {

			$("[sid=logo]").attr("src", orgDefaultLogo);
			SDK.loadLogo(logo);
		}
	}else{

		$("[sid=logo]").attr("src", orgDefaultLogo);
	}

	$("[sid=name]").text(isEmpty(name, "暂未填写"));

	if(auth == 1) {

		$("[sid=auth]").show();
	}else {

		$("[sid=auth]").remove();
	}

	$("[sid=expoNo]").text(expoNo);
	$("[sid=tel]").text(isEmpty(tel, "暂未填写"));
	$("[sid=addr]").text(isEmpty(addr, "暂未填写"));
};

Page.showLoading = function() {

	$("#content-panel").hide();
	Loading.show();
};

Page.hideLoading = function() {

	Loading.hide();
	$("#content-panel").show();
};

// 机构服务范围
Page.addService = function(info) {

	if (info == null || info.trim() == "") {

		$("[sid=service]").parent().hide();
	} else {

		$("[sid=service]").html(info);
	}
};

// 机构介绍
Page.addInfo = function(info, isMore, orgId, name, updateDate) {

	if (info == null || info.trim() == "") {

			$("[sid=intro]").parent().hide();
	} else {

		if(isMore) {

			$("#intro-more").show();
			$("#intro-more").attr("oid",orgId);
			$("#intro-more").attr("updateDate",updateDate);
			$("#intro-more").attr("name",name);
			$("[sid=intro]").prepend(info+"...");

		}else{

			$("[sid=intro]").prepend(info);
		}

		Button.loadAllButton({
			onClick : onBtnClick
		});

	}

};


// 加载数据超时提醒
Page.loadFailed = function() {

	Loading.hide();
	$("#content-panel").hide();
	$("[sid=hint]").show();
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

//异步下载机构logo 的回调
Page.loadhead = function(logo) {

	var driver = FILE.getSysPath();
	local = driver + orgLogoPath;// logo的本地存储路径
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

Page.noExpert = function() {

    $("#expert-panel").remove();
}

Page.addExpertImg = function(id, name, lastModify,img) {

	new ExpertImgItem(id, name, lastModify,img,$("#expert-img-container"));
}

function onBtnClick(id,e) {
    if(id == "tel") {
        var tel = e.view.text();
        if(parseInt(tel) > 0) {

            new HintDialog("是否直接电话联系","拨号","取消",$("body"),call,tel);
        }
    }else if(id == "intro-more") {

		//新页面显示机构介绍
		SDK.showMore(parseInt(e.view.attr("oid")), e.view.attr("name"), e.view.attr("updateDate"));
	}
}

function call(tel){

	SDK.call(tel);
}

function attent(param){

    //参数是专家id 和 关注状态
	SDK.attent(parseInt(param.split(",")[0]),parseInt(param.split(",")[1]));
}


/** 设置标题栏 **/
Page.setTitlebar = function(title) {

	SDK.setTitle(title);
}