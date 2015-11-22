var Page = function () {
}
var SDK = window.sdk;
var FILE = window.file;
var fileLoader = new FileLoader();
var oldHeight=0;

/*
 初始化 加载 本地所有咨讯,按照id 升序
 */
Page.addInfor = function (id, title, logo, introduction, deliveryTime, publishTime, updateDate) {

    new InforItem(id, title, logo, introduction, deliveryTime, publishTime, updateDate, $("#list"));
}

Page.init = function() {

    setTimeout(function() {
         $("html,body").animate({scrollTop:$("#list").height()},0)
         oldHeight = $("#list").height();
    },0);
}

//历史加载完成后,调整滚动条
Page.loadHistoryComplete = function() {

    var screenHeight = $(window).height();
    if(oldHeight == 0) {

         var height = $("#list").height() + screenHeight;
    }else{

         var height = $("#list").height() - oldHeight - screenHeight;
    }

     setTimeout(function() {

        $("html,body").animate({scrollTop:height},0)
        oldHeight = $("#list").height();
     },0);
}

/*
 加载历史
 */

Page.addHistoryData = function (id, title, logo, introduction, deliveryTime, publishTime, updateDate) {

    new InforItem(id, title, logo, introduction, deliveryTime, publishTime, updateDate, $("#list"));
}


/**
 *调用action 下载完成之后调用代理对象的方法替换默认图片
 */
Page.onFinishDownload = function () {

    // 调用委托类的更新方法
    fileLoader.downloadingFile.deligate.onFileFinishLoad();

    // 继续下载文件
    fileLoader.doTask();
}


Page.setId = function(id) {

    if(id == null || id== "")
        id = 0;
    SDK.setSmallestId(parseInt(id));
}
