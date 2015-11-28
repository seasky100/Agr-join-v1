var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();
var dialog;

var Page = function(){};

function call(tel){
	
	SDK.call(tel);
}

//项目初始
Page.initBasic = function(id, name, auth, logo, theUnit, department, professionalTitle, trade, tel, addr, area, attentNum, isAttented){
	
	// 显示logo 不需要异步加载
	var driver = FILE.getSysPath();
	local = driver + expertlogoPath;// logo的本地存储路径
	var fileName="";
	if(logo != null && logo.trim() != ""){
		
		fileName = logo.split("/").pop();// logo 的文件名
	}
	var src = local + fileName;
	if (logo != null && logo.trim() != "") {
		if (FILE.isFileExist(src) == true) {
			
			$("[sid=logo]").attr("src",src);
		} else {

			$("[sid=logo]").attr("src",expertDefaultLogo);
			//异步加载
			SDK.loadheadIcon(logo);
		}
	}else {

		$("[sid=logo]").attr("src",expertDefaultLogo);
	}
	if(auth == "true") {
		$("[sid=auth]").show();
	}else {
		$("[sid=auth]").hide();
	}

	$("[sid=name]").text(isEmpty(name,"暂未填写"));
	
	$("[sid=area]").text(isEmpty(area,"暂未填写"));
	$("[sid=dept]").text(isEmpty(department,"暂未填写"));

	$("[sid=unit]").text(isEmpty(theUnit,"暂未填写"));

	$("[sid=professional-title]").text(isEmpty(professionalTitle,"暂未填写"));

    if(isAttented == 1) {
        $("#attent").attr("display","button");
        $("#attent").removeAttr("onclick");
		$("#attent img").attr("src","../img/guanzhu2.png");
		$("#attent-text").text("已关注");
	 }else if(isAttented == 0){

        $("#attent").attr("display","button");
        $("#attent").removeAttr("onclick");
	 	$("#attent img").attr("src","../img/guanzhu1.png");
	 	$("#attent-text").text("关注");
	 }else if(isAttented == -1){

	 	//点击登录
	 	$("#attent").removeAttr("display");
	 	$("#attent").attr("onclick","login();");
	 }

    $("#attent").attr("attent",isAttented);//认证状态
    $("#attent").attr("expertId",id);//专家id
	$("[sid=attentNum]").text(attentNum == null?0:attentNum);
	$("[sid=trade]").text(isEmpty(trade,"暂未填写"));
	$("[sid=tel]").text(isEmpty(tel,"暂未填写"));
	$("[sid=addr]").text(isEmpty(addr,"暂未填写"));
};

// 研究领域
Page.addSpecialty = function(specialty, isMore, expertId, name, updateDate) {

	if (specialty == null || specialty.trim() == "") {

		$("#research-field").parent().hide();
	} else {

		if(isMore) {

			$("#specialty-more").show();
			$("#specialty-more").attr("eid",expertId);
			$("#specialty-more").attr("updateDate",updateDate);
			$("#specialty-more").attr("name",name);
			$("#specialty").prepend(specialty+"...");
		}else{

			$("#specialty").prepend(specialty);
		}
	}

};


//设置专家详细
Page.addIntro = function(intro, isMore, expertId, name, updateDate) {

	if(intro == null || intro.trim() == "") {
		
		$("#intro").parent().hide();
	}else {

		if(isMore) {

			$("#intro-more").show();
			$("#intro-more").attr("eid",expertId);
			$("#intro-more").attr("updateDate",updateDate);
			$("#intro-more").attr("name",name);
			$("#intro").prepend(intro+"...");
		}else{

			$("#intro").prepend(intro);
		}
    }
	Button.loadAllButton({
		onClick : onBtnClick
	});
};


Page.loadhead = function(logo) {

	var driver = FILE.getSysPath();
	local = driver + expertlogoPath;// logo的本地存储路径
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

/** 检测字符串是否为空, 空 自动填充为指定的字符串* */
function isEmpty(source, defaultStr) {

	if (source == null || (source+"").trim() == "") {

		source = defaultStr;

	}
	return source;
}


Page.showLoading = function(){

	Loading.show();
	//$("body div:not(.loading)").hide();
}

Page.hideLoading = function(){

	Loading.hide();
	//$("body div:not(.loading)").show();
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
//加载数据超时提醒
Page.loadFailed = function(){

	$("[sid=content-panel]").hide();
	$("[sid=hint]").show();
}

Page.showHintDialog = function(message,result,type){

    if(result) {

        if(type == 1) {

           $("#attent").attr("attent",0);
           $("#attent img").attr("src","../img/guanzhu1.png");
           $("#attent-text").text("关注");
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

Page.closePage = function(){

	setTimeout(function(){
	
		SDK.closePage();

	},1000);
}



//成果轮播
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

Page.addImg = function(id, name, lastModify,img) {

	new ImgItem(id, name, lastModify,img,$("#img-container"));
}


function onBtnClick(id,e) {

    if(id == "tel") {
          var tel = e.view.text();
          if(parseInt(tel) > 0) {

              dialog = new HintDialog("是否直接电话联系","拨号","取消",$("body"),call,tel);
          }
  	}else if(id == "attent") {

  		var expertId = e.view.attr("expertId");
  		var attentStatus = e.view.attr("attent");

  		attent(expertId+","+attentStatus);
  		//点击关注
//  		if(attentStatus == 1) {
//
//
//  			dialog = new HintDialog("确认取消关注该专家","确认","取消", $("body"), attent, expertId+","+attentStatus);
//  		}else {
//
//  			dialog = new HintDialog("确认关注该专家","确认","取消", $("body"), attent, expertId+","+attentStatus);
//  		}
  	}else if(id == "specialty-more"){

		SDK.showMore("specialty", parseInt(e.view.attr("eid")), e.view.attr("name"),e.view.attr("updateDate"));

  	}else if(id == "intro-more") {

  		SDK.showMore("intro", parseInt(e.view.attr("eid")), e.view.attr("name"),e.view.attr("updateDate"));


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

    SDK.openLoginPage(1);
}

//专家没哟成果
Page.noAchv = function() {

	$("#achv-panel").hide();
}

/** 设置标题栏 **/
Page.setTitlebar = function(title) {

	SDK.setTitle(title);
}