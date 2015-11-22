/**
define MyDialog
*/
var expertlogoPath = "/fundview/data/images/expert/logo/";
var compLogoPath = "/fundview/data/images/comp/logo/";
var achvLogoPath = "/fundview/data/images/achv/logo/";
var requLogoPath = "/fundview/data/images/requ/logo/";
var orgLogoPath = "/fundview/data/images/org/logo/";
var productLogoPath = "/fundview/data/images/product/logo/";

var fundviewPath = "/fundview/data/images/fundview/logo/";//宣传图片
var fundviewImgPath = "/fundview/data/images/fundview/logo/";//宣传图片

var fundviewInfoPath = "/fundview/data/images/fundview/infor/img/";//丰景咨询图片


var expertDefaultLogo = "../img/expert-logo.png";
var achvDefaultLogo = "../img/achv-default.jpg";
var requDefaultLogo = "../img/company-logo.png";
var compDefaultLogo = "../img/company-logo.png";
var orgDefaultLogo = "../img/org-logo.png";
var productDefaultLogo = "../img/product-default.jpg";
var fundviewDefaultLogo = "../img/banner2.jpg";
var fundviewInforDefaultLogo = "../img/zx_moren.jpg";


var compTypes=["加工企业", "农资企业", "生产企业", "流通企业", "其他企业"];

var  MyDialog = function(){};

MyDialog.container = null;

MyDialog.open = function(url){
	
	MyDialog.container = $('<div id="myDialog"></div>')
					.css("position","fixed")
					.css("width","100%")
					.css("height","100%")
					.css("left","0")
					.css("top","0")
					.css("-webkit-tap-highlight-color","rgba(0, 0, 0, 0)")
					.css("z-index","500")/*.css("overflow","scroll").css("background", "#ddd")*/
					.css("-webkit-transition", "top 0.5s linear");
	
	MyDialog.container.appendTo("body");
	MyDialog.container.load(url /*+ new Date().getTime()*/);

	MyDialog.container.bind("touchend", null, function(e){
		
		MyDialog.hide();
	}).bind("touchmove", null, function(e){
		
		e.preventDefault();
		
		MyDialog.hide();
	});
};

MyDialog.hide = function(){
	
	//$("#myDialogPanel").css("top", "-500px");
	setTimeout(function(){			
		MyDialog.container.remove();
	}, 300);
};

MyDialog.close = function(){
	
	MyDialog.container.remove();
};

$(function(){ 
	
	$("body").append("<div id='activePage' style='position:fixed; width:100%; height:1px; background-color:#666; opacity:0.5; bottom:0;'></div>");
});

/**
 * 判断obj 是否是空,为空 就返回 defaultValue, 否则返回obj
 * @param obj
 * @param defaultValue
 */
function isEmpty(obj, defaultValue) {
	
	if(obj == null || obj.trim() == "") {
		
		return defaultValue;
	}else {
		
		return obj;
	}
}
/**
 * 全局的SDK回调的接口方法
 */
var Global = function(){};

// 当页面处于活动状态
Global.onPageActive = function(){$('#activePage').show();};

// 当页面处于休眠状态
Global.onPageInActive = function(){$('#activePage').hide();};