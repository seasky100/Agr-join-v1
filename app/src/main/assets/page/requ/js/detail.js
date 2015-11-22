var SDK = window.sdk;
var FILE = window.file;

var Page = function() {};

function call(tel) {

	SDK.call(tel);
}

// 项目初始
Page.init = function(id,title, price, isFavorite, ownerName,ownerId, hzType) {

	$("[sid=name]").text(title);
	if(price == 0) {

		price = "面议";
	}else if(price > 0) {

		price = "￥" + price + "万";
	}
	$("[sid=price]").text(price);
	$("[sid=ownerName]").text(ownerName);
	$("[sid=ownerName]").attr("ownerId",ownerId);//拥有者标识
	$("[sid=hzType]").text(hzType);

	$("#favorite").attr("requId",id);
	$("#favorite").attr("favoriteStatus",isFavorite);//关注状态

	//收藏
	if(isFavorite == 1) {

		$("#favorite img").attr("src","../img/coll_filled.png");
		$("#favorite-text").text("已收藏");
	}else {

		$("#favorite img").attr("src","../img/coll.png");
	}


	Button.loadAllButton({
    		onClick: onBtnClick
    	});
}

Page.initBasic = function(material, materialType, prod, prodType, hj, problem, symbal, cxDesc, other) {

	$("[sid=material]").text(isEmpty(material,"暂未填写"));//适用原料
	$("[sid=materialType]").text(isEmpty(materialType,"暂未填写"));//适用原料类型
	$("[sid=prod]").text(isEmpty(prod,"暂未填写"));//适用产品
	$("[sid=prodType]").text(isEmpty(prodType,"暂未填写"));
	$("[sid=hj]").text(isEmpty(hj,"暂未填写"));
	$("[sid=problem]").text(isEmpty(problem,"暂未填写"));
	$("[sid=symbal]").text(isEmpty(symbal,"暂未填写"));
	$("[sid=cxDesc]").text(isEmpty(cxDesc,"暂未填写"));
	$("[sid=other]").text(isEmpty(other,"暂未填写"));
}

Page.showLoading = function() {

	Loading.show();
	$("body div:not(.loading)").hide();
}

Page.hideLoading = function() {

	Loading.hide();
	$("body div:not(.loading)").show();
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


/** 检测字符串是否为空, 空 自动填充为指定的字符串* */
function isEmpty(source, defaultStr) {

	if (source == null || source.trim() == "") {

		source = defaultStr;

	}
	return source;
}

function onBtnClick(id,e) {

	if(id == "owner") {

		//点击成果页面的拥有者
		var ownerId = e.view.find("#ownerName").attr("ownerId");
		SDK.findOwner(parseInt(ownerId), e.view.find("#ownerName").text());
	}else if(id="favorite") {

     		var requId = $("#"+id).attr("requId");
     		var favoriteStatus = $("#"+id).attr("favoriteStatus");

     		favorite(requId+","+favoriteStatus);
     		//点击关注
//     		if(favoriteStatus == 1) {
//
//     			 new HintDialog("确认取消收藏该需求吗?","确认","取消", $("body"), favorite, requId+","+favoriteStatus);
//     		}else {
//
//     			 new HintDialog("确认收藏该需求吗?","确认","取消", $("body"), favorite, requId+","+favoriteStatus);
//     		}
     	}
     }

     function favorite(param){

         //参数是专家id 和 关注状态
     	SDK.favorite(parseInt(param.split(",")[0]),parseInt(param.split(",")[1]));
     }