var SDK = window.sdk;

var Page = function(){};
var loadCount = 0;//页面加载次数
Page.initPage = function(icon, name, username, area, areaIds, addr, tel, qrCode, linkMan, type, auth, professionalTitle, infor, compType){

	if(null != icon)
		$(".head").attr("src",icon+"?"+new Date().getTime());
	
	if(null != name){

        $("#profile-name .value").text(name);
	}else{

	    $("#profile-name .value").text("暂未填写");
	}
    if(auth) {

        //认证后不允许修改
        $("#profile-name").attr("editable","false");
    }
	
	if(null != username){

		$("#profile-account .value").text(username);
	}
	if(null != area && area.trim() != ""){

		$("#profile-area .value").text(area);
	}else {

        $("#profile-area .value").text("暂未填写");
	}

    if(areaIds != null && areaIds.trim() != ""){

        var areaIdArray = areaIds.split(",");
        $("#profile-area").attr("provinceId", areaIdArray[0]);
        if(areaIdArray.length >= 2)
            $("#profile-area").attr("cityId", areaIdArray[1]);
        if(areaIdArray.length >= 3)
            $("#profile-area").attr("countyId", areaIdArray[2]);
    }


//	if(auth) {
//
//        //认证后不允许修改
//         $("#profile-area").removeAttr("display");
//    }

	if(null != addr){
		$("#profile-addr .value").text(addr);
	}


	if(null != tel && tel != ""){

		$("#profile-tel .value").text(tel);
	}else{

		$("#profile-tel .value").text("暂未填写");
	}

	if(auth) {

        //认证后不允许修改
        $("#profile-tel").attr("editable","false");
    }
	if(null != qrCode && qrCode != ""){

		$("#profile-qrcode").find("img").attr("src",qrCode);
	}else {

		$("#profile-qrcode").hide();
	}



    if(type ==1) {

        //企业
        $("#profile-linkMan").show();
        $("#profile-linkMan .value").text(linkMan == null?'暂未填写':linkMan);
        if(auth) {

            //认证后不允许修改
            $("#profile-linkMan").attr("editable","false");
        }

        if(infor == null || infor.trim() == '') {

            $("#profile-infor .value").text("暂未填写");
        }else {

            $("#profile-infor .value").text(infor);
        }
		$("#profile-comp-type").show();
		$("#profile-comp-type .value").text(compTypes[compType-1]);
		$("#profile-comp-type").attr("compType", compType);
		$("#profile-comp-type").attr("auth", auth);
    }else if(type == 2){

        $("#profile-professional-title").show();
        $("#profile-professional-title .value").text(professionalTitle == null?"暂未填写":professionalTitle);
//        if(auth) {
//
//            //认证后不允许修改
//            $("#profile-professional-title").attr("editable","false");
//        }

        $("#profile-infor .label").text("研究领域");
        if(infor == null || infor.trim() == '') {

            $("#profile-infor .value").text("暂未填写");
        }else {

            $("#profile-infor .value").text(infor);
        }

//        if(auth) {
//
//            //认证后不允许修改
//            $("#profile-infor").attr("editable","false");
//        }
    }else if(type == 6) {

        $("#profile-infor .label").text("从事行业");
        if(infor == null || infor.trim() == '') {

            $("#profile-infor .value").text("暂未填写");
        }else {

            $("#profile-infor .value").text(infor);
        }

//        if(auth) {
//
//            //认证后不允许修改
//            $("#profile-infor").attr("editable","false");
//        }
    }
    if(loadCount == 0) {
        Button.loadAllButton({
            onClick: onBtnClick
        });
    }

    loadCount ++ ;
}

Page.setHeadIcon = function(icon){
	
	if(null != icon){

		var driver = FILE.getSysPath();
		var local = driver + logoPath;// logo的本地存储路径
		var fileName = logo.split("/").pop();// logo 的文件名
	}
		$(".head").attr("src",icon+"?"+new Date().getTime());
};

Page.setName = function(name){

	if(null != name)
		$("#profile-name .value").text(name);
};

Page.setAccount = function(account){

	if(null != account){
		$("#account").text(account);
		$("#account").attr("account",account);
	}
};

Page.setCompType = function(result, compType){

	if(result == 1 || result == "1") {

		$("#profile-comp-type .value").text(compTypes[compType - 1]);
		$("#profile-comp-type").attr("compType", compType);
	}

};


Page.setArea = function(area){

	if(null != area){
		if(area.indexOf(",")){
			$("#profile-area .value").text(area.replace(","," "));
		}else{
			$("#profile-area .value").text(area);
		}	
	}
};

Page.setAddr = function(addr){

	if(null != addr)
		$("#profile-addr .value").text(addr);
};

Page.setTel = function(tel){

	if(null != tel)
		$("#profile-tel .value").text(tel);
};


Page.showPage = function(){

	$("#mainDiV").show();
};

Page.hidePage = function(){

	$("#mainDiV").hide();
};
Page.showLoading = function(){

	Loading.show();
};

Page.hideLoading = function(){

	Loading.hide();
};

function onBtnClick(id, e){

	var view = $(e.view);
	if(view.hasClass("cancel")){
		
		$(".dialog").hide();
	}else if(id == "logout"){

		SDK.logout();
	}else if(id == "profile-comp-type"){

		//修改企业类型
//		var auth = view.attr("auth");
//		if(auth == 'false')
		SDK.showCompType(parseInt(view.attr("compType")))
	}else if(id == "profile-area") {

        var provinceId = parseInt(view.attr("provinceId"));
        var cityId = parseInt(view.attr("cityId"));
        var countyId = parseInt(view.attr("countyId"));
	    SDK.openAreaList(provinceId, cityId, countyId );
	}else{

		var editable = view.attr("editable")=="true";
		SDK.openPage(id,editable);
	}
}