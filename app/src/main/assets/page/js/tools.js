
//根据经纬度计算距离
function getDistatce( lat1,  lon1,  lat2,  lon2) {

	var R = 6371;
	var distance = 0.0;
	var dLat = (lat2 - lat1) * Math.PI / 180;
	var dLon = (lon2 - lon1) * Math.PI / 180;
	var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180)
			* Math.sin(dLon / 2) * Math.sin(dLon / 2);
	distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
	return Math.round(distance * 100) / 100;

}
function getPosition(){}
/*格式化日期*/
function formatDate_1(date, format) {   
    if (!date) return;   
    if (!format) format = "yyyy-MM-dd";   
    switch(typeof date) {   
        case "string":   
            date = new Date(date.replace(/-/, "/"));   
            break;   
        case "number":   
            date = new Date(date);   
            break;   
    }    
    if (!date instanceof Date) return;   
    var dict = {   
        "yyyy": date.getFullYear(),   
        "M": date.getMonth() + 1,   
        "d": date.getDate(),   
        "H": date.getHours(),   
        "m": date.getMinutes(),   
        "s": date.getSeconds(),   
        "MM": ("" + (date.getMonth() + 101)).substr(1),   
        "dd": ("" + (date.getDate() + 100)).substr(1),   
        "HH": ("" + (date.getHours() + 100)).substr(1),   
        "mm": ("" + (date.getMinutes() + 100)).substr(1),   
        "ss": ("" + (date.getSeconds() + 100)).substr(1)   
    };       
    return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {   
        return dict[arguments[0]];   
    });             
}   

function formatDate(time){

	if(!isNaN(time) && time != 0){

		var date = new Date();
		date.setTime(time);
		var timeStr = date.getMonth()+1+"月"+date.getDate()+"号 ";
		//alert();
		var hours = date.getHours()-12>0?"下午":"上午";
		return timeStr+hours;
	}else{
		
		return "";
	}
}

function formatDate_2(time){

	if(!isNaN(time) && time != 0){

		var date = new Date();
		date.setTime(time);
		var timeStr = date.getMonth()+1+"月"+date.getDate()+"号 ";
		var hours = date.getHours();
		var minutes = date.getMinutes();
		if(hours<=9){

			hours = "0"+hours;
		}
		if(minutes<=9){
			
			minutes = "0"+minutes;
		}
		var day = date.getHours()-12>0?"下午":"上午";
		return timeStr+" "+day +" "+hours + ":" +minutes+" ";
	}else{
		
		return "";
	}
}

function formatDate_3(time){

	if(!isNaN(time) && time != 0){

		var date = new Date();
		date.setTime(time);
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		
		if(month<=9){

			month = "0"+month;
		}
		if(day<=9){
			
			day = "0"+day;
		}
		return year+"-"+month+"-"+day+" "+(date.getHours()-12>0?"下午":"上午") ;
	}else{
		
		return "";
	}
}

