var  ErrorDialog = function(title,info){

	this.title = title;
	this.info = info;
	this.init();
}
ErrorDialog.prototype = {
	
	init:function(){
		var dialog = $("#errorTemplate").html();
		$(dialog).appendTo("body");
		$(".container .title").text(this.title);;
		$(".container .info").text(this.info);;console.log($(dialog).children(".info"));
		
	}
}