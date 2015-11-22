
/**
 * 定义 Button 对象
 */
var  Button = function(view, params){
	
	// 初始化属性
	this.timer = null;
	this.id = view.attr("id");
	this.view = view;
	this.onClick = params.onClick;
	this.onLongpress = params.onLongpress;
	// 执行初始化操作
	this.init();
	
	// 绑定事件
	this.attachEvent();
};

Button.debug = false;
Button.doing = false;
Button.timer = null;

/**
 * 装载页面中所有display=button的元素为按钮
 */
Button.loadAllButton = function(params){
	
	$("[display=button]").each(function(idx, e){
   
		new Button($(e), params);
	});
};

/**
 * 创建一个按钮
 */
Button.create = function(e, params){

	new Button(e, params);
};

/**
 * 初始化视图
 */
Button.prototype.init = function(){
	
};

/**
 * 绑定事件
 */
Button.prototype.attachEvent = function(){

	this.view.bind("click", this, this.clickHandler);
	this.view.bind("touchstart", this, this.touchStartHandler);
	this.view.bind("touchmove", this, this.touchMoveHandler);
	this.view.bind("touchend", this, this.touchEndHandler);
};

/**
 * 触摸开始
 */
Button.prototype.touchStartHandler = function(e){
	
	var t = e.data;

	clearTimeout(Button.timer);
	
	Button.timer = setTimeout(function(){

		if(Button.doing)
			return;

		Button.doing = true;
		
		if(t.onLongpress != null)
			t.onLongpress(t.id, t);
			
		Button.doing = false;
	},500);
};

/**
 * 触摸移动
 */
Button.prototype.touchMoveHandler = function(e){
	
	clearTimeout(Button.timer);
	Button.doing = false;
};

/**
 * 触摸结束
 */
Button.prototype.touchEndHandler = function(e){

	clearTimeout(Button.timer);
	Button.doing = false;
};

/**
 * 触摸取消
 */
Button.prototype.clickHandler = function(e){
	
	var t = e.data;
	
	if(t.onClick != null)
		t.onClick(t.id, t);
};
