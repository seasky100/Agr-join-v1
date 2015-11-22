//解析xml文件返回解析后的数据
var loadXml = function(xmlUrl){
	
	if(xmlUrl==null||xmlUrl.trim()==""){
		
		//alert("xml文件路径为空");
		Loading.hide();
		return  false;
	}else{		

		$.ajax({
			url: xmlUrl,
			dataType: 'xml',
			type: 'get',
			timeout: 2000,
			error: function(data)
			{   
				Loading.hide();
				new Dialog("加载项目详细信息失败,请稍后再试",false,$("body"),2000);
				return ;
				//alert("加载XML 文件出错 删除文件，显示提示超找网络 或者刷新按钮");
			},
			success: function(data){

				if(data!=null){
					
					var project = readProjXML(data);
					if(project!=null){
						initProjectPage(project);	
					}
				}
			}
				
		});

	}
}
//解析文件夹,返回一个项目企业对象
function  loadProjCompsXml(xmlUrl){
if(xmlUrl==null||xmlUrl.trim()==""){
		
		//alert("xml文件路径为空");
		return  false;
	}else{
		$.ajax({
			url: xmlUrl,
			dataType: 'xml',
			type: 'get',
			timeout: 2000,
			error: function(data)
			{  
				//new Dialog("种源数据加载失败,请稍后再试",false,$("body"),1000);
				//alert("1111加载XML 文件出错 删除文件，显示提示超找网络 或者刷新按钮");
			},
			success: function(data){

				if(data!=null){

					var project = readProjCompsXML(data);
					if(project!=null){
						initProjectCompsPage(project);	
					}
				}
			}

				
		});

	}
}


//加载xml后返回的数据,读取xml文件
function readProjXML(data){
	
	//开始解析数据
	var project = new Object;

	var proj = $(data).find("proj");
	var driver = sGlobal.getSysPath();
	project.local = driver+"/findView/data/image/";//logo的本地存储路径
	
	project.id =  $(proj).children("id").text();
	project.logo =  $(proj).children("logo").text();
	project.title = $(proj).children("title").text();
	project.analyse = $(proj).children("analyse").text();
	project.comments = $(proj).children("comments").text();//点评为简短的简介
	project.complex_idx = $(proj).children("complex_idx").text()||0;
	project.capital_idx = $(proj).children("capital_idx").text();
	project.tech_idx = $(proj).children("tech_idx").text();
	project.area_idx = $(proj).children("area_idx").text();
	
	project.rate_return_idx = $(proj).children("rate_return_idx").text();
	project.rate_return_type = $(proj).children("rate_return_type").text();
	//project.rate_return_type_str = $(proj).children("rate_return_type_str").text();
	
	project.attention = $(proj).children("attention").text();
	
	project.default_area_id = $(proj).children("default_area_id").text();
	project.default_area = $(proj).children("default_area").text();
	
	project.default_input_type = $(proj).children("default_input_type").text();
	project.default_input_type_str = $(proj).children("default_input_type_str").text();
	project.default_input = $(proj).children("default_input").text();
	
	//是否有计算器 0 为没有 1为有计算器
	project.calculate_exists = $(proj).children("calculate_exists").text();
	
	project.category = $(proj).children("category").text();
	project.qrcode = $(proj).children("qrcode").text();
	var imgs = $(proj).children(imgs).children("img");

	//遍历图片元素,将信息存入数组中
	project.projImgs = new Array();
	for(var i=0;i<imgs.length;i++){

		var img = imgs[i];
		var imgObj = new  Object;
		imgObj.id = $(img).attr("id");
		imgObj.urlPath = $(img).children("url").text();
		//imgObj.fileName = imgObj.urlPath.split("/").pop();//图片 的文件名
		//imgObj.src = .project.local+imgObj.fileName;
		imgObj.name = $(img).children("name").text();
		project.projImgs.push(imgObj);
	}
	projectData.proj =  project;
	return  project;
}

//加载xml后返回的数据,读取xml文件
function readProjCompsXML(data){
	
	//开始解析数据
	var project = new Object;

	var companys = $(data).find("companys");
	var companys = $(companys).children("company");


	//遍历种源企业 
	project.companys = new Array();
	for(var i = 0;i<companys.length;i++){
		
		var company = companys[i];

		var companyObj = new  Object;

		companyObj.companyId = $(company).attr("id");
		companyObj.accountid = $(company).attr("accountid");
		companyObj.longitude = $(company).children("longitude").text();
		companyObj.latitude = $(company).children("latitude").text();
		companyObj.address = $(company).children("address").text();
		companyObj.telephone = $(company).children("telephone").text();
		companyObj.mobile = $(company).children("mobile").text();
		companyObj.linkman = $(company).children("name").text();

		project.companys.push(companyObj);
	}
	projectData.comps = project;
	return  project;
}

var seedsTemplate = null;
var coms = new  Array();

//init project page
function  initProjectPage(project){

	//加载页面前清空页面数据
	$("[sid=panel]").empty();
	var imgs = project.projImgs;
	for(var i=0;i<imgs.length;i++){
		var img = imgs[i];
		new Image(img.id,$("[sid=panel]"),img.urlPath,img.name);
		$("<p></p>").appendTo("[sid=count]");
	}
	//精确到小数点后一位
	var value = project.complex_idx/10;
	var values = value.toString().split(".");
	if(values.length==1){
		var hot = values[0]+".0";
	}else{
		hot = values[0]+"."+values[1].charAt(0);
	}
   
	$("[sid=hot-icon]").addClass("hot-"+Math.round(hot));
	$("[sid=mark]").text(hot);
	$("[sid=capital_idx]").addClass("level_"+Math.round(project.capital_idx/16));
	$("[sid=area_idx]").addClass("level_"+Math.round(project.area_idx/16));
	$("[sid=tech_idx]").addClass("level_"+Math.round(project.tech_idx/16));
	
	$("[sid=attent]").text(project.attention);

	//$("[sid=rate_return_idx]").html(project.rate_return_type_str+"：<span class='profit'>"+project.rate_return_idx+"</span>");
	if(project.rate_return_type=="1"){
		if(project.rate_return_idx != null && project.rate_return_idx != 'null' && project.rate_return_idx.trim()  !="" ){
			//投资回报率
			$("[sid=rate_return_idx]").html("投资回报率<span class='profit'>"+project.rate_return_idx+"</span>");
		}else{
			$("[sid=rate_return_idx]").html("行情良好");
		}
	}else{
		if(project.rate_return_idx != null && project.rate_return_idx != 'null' && project.rate_return_idx.trim() !="" ){
			//年收益		
			$("[sid=rate_return_idx]").html("每亩年收益<span class='profit'>"+project.rate_return_idx+"</span>");
		}else{
			$("[sid=rate_return_idx]").html("行情良好");
		}
	}
	//是否显示计算器	
	if(project.calculate_exists == '1'){
		 
		 $(".reckon").show();
		//设置地区
		if(project.default_area != null && project.default_area != ''){
			$("[sid=addrs]").text(project.default_area);
			$("[sid=addrs]").attr("areaid",project.default_area_id);
		}
		
		//默认的种植面积和投资
		if(project.default_input_type == '1'){
			$("[sid=inputTitle]").text("投入资金");
		}else{
			$("[sid=inputTitle]").text("种植面积");
		}
		$("[sid=defaultinput]").val(project.default_input);
		$("[sid=unit]").text(project.default_input_type_str);
		$("[sid=defaultinput]").attr("inputType",project.default_input_type);
	}
	
	$("[sid=comments]").html(project.comments);

	//设置TitleBar
	sTitleBar.setTitle(project.title);
	//显示界面
	$("#mainDiv").show();
	Loading.hide();
	
}
// 加载项目企业数据
function  initProjectCompsPage(project){
	//种源企业
	if(project.companys.length == 0){
		$(".seeds").hide();
		return;
	}
	$("[sid=company]").html("<span class='green'>1</span>/"+project.companys.length);

	//清空页面数据
	$("[sid=coms]").empty();
	
	for(var i=0; i<project.companys.length;i++){

		var company = project.companys[i];	
		var tel;
		/*if(seedsTemplate==null){
	
			seedsTemplate = $("#seedsTemplate").html();
		}
	
		var view = $("[sid=coms]").append(seedsTemplate).children().last();
		view.attr("id",company.companyId);
		view.attr("lat",company.latitude);
		view.attr("logt",company.longitude);
		view.find(".name").text(company.linkman);
		view.find(".middle").text(company.address);*/
		if(company.telephone!=null&&company.telephone.trim()!=""){
		
			tel=company.telephone.split(",")[0];	
		}else if(company.mobile !=null&&company.mobile.trim()!=""){
			
			tel=company.mobile.split(",")[0];
		}else{
			tel="";
		}
		//var distance = getPosition(106,78,company.latitude,company.longitude);
		//new Seed(company.companyId,i,company.linkman,distance,company.Addres,tel,$("[sid=coms]"));
		new Seed(company.companyId,i,company.linkman,company.address,tel,$("[sid=coms]"));
	}
	
	//显示种源企业
	$(".seeds").show();
	slide($("#coms"));
}

//解析文件夹,返回一个对象
function  loadAreaXml(xmlUrl,provinceId){
	
	if(xmlUrl==null||xmlUrl.trim()==""){
		
		//alert("地区xml文件路径为空");
		return  false;
	}else{		

		$.ajax({
			url: xmlUrl,
			dataType: 'xml',
			type: 'get',
			timeout: 2000,
			error: function(data)
			{  
				new Dialog("加载地区信息失败,请稍后再试",false,$("body"),2000);
			},
			success: function(data){

				if(data!=null){

					var areas = readAreaXML(data,provinceId);
					if(areas!=null){
						onRtnAreaData(areas,provinceId);
					}
				}
			}
				
		});

	}
}
function readAreaXML(data,provinceId){
	
	//开始解析数据
	var RECORDS = $(data).find("RECORDS");
	var RECORDS = $(RECORDS).children("RECORD");

	//遍历种源企业 
	var provinces = new Array();
	for(var i = 0;i<RECORDS.length;i++){
		
		var RECORD = RECORDS[i];

		var province = new  Object;
		if( $(RECORD).children("parent_id").text() == provinceId){	
			province.id = $(RECORD).children("id").text();
			province.provinceId = provinceId;
			province.name = $(RECORD).children("name").text();
			provinces.push(province)
		}
	}
	//alert(provinces.length);
	return  provinces;
}
