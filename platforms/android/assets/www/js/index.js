/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

//document.write("<script language=javascript src='cordova.js'></script>");
//if(top.location!=self.location) top.location=self.location;
/*公司服务器*/
var IP="59.172.63.26";
var PORT="8091";
//var PORT="7080";
//生产
//var IP="43.254.150.91";
//var PORT="9088";

var opt_datatime = {
	       preset: 'date', //日期格式 date（日期）|datetime（日期加时间）
	       theme: 'android-ics light', //皮肤样式
	    display: 'modal', //显示方式
	    mode: 'clickpick', //日期选择模式
	    dateFormat: 'yy-mm-dd', // 日期格式
	       timeWheels:'HHii', //时间格式24小时
	       timeFormat:'HH:ii', //时间格式24小时
	    setText: '确定', //确认按钮名称
	    cancelText: '取消',//取消按钮名籍我
	    dateOrder: 'yymmdd', //面板中日期排列格式
	    dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
	    yearText: '年', monthText: '月',  dayText: '日',  //面板中年月日文字
	    endYear:2030, //结束年份
	    showNow:false,
	    nowText:'今天',
	    hourText:'小时',
	    minuteText:'分'
	}
var errmsg=[{
		"200":"交易成功完成",
		"487":"",
		"488":"",
		"401":"用户名密码指纹等验证不通过",
		"470":"服务繁忙,请稍后再试.",
		"500":"服务器发生未知异常,请联系维护人员.",
		"471":"用户您好,请先登录.",
		"471":"用户您好,会话已过期,请重新登录.",
		"473":"服务端要求上送位置信息",
		"474":"您当前的地理位置超出了业务办理所允许的范围了,请回到既定区域内再办理业务",
		"475":"服务器已经接受您的请求,请您放心!",
		"476":"系统繁忙,请稍后再试."
}];

function show_error(msg){
	
	var json=eval("("+msg+")");	
	var info=json.tips.message;
	var map=errmsg[0];
	
	if( json.tips.statusCode == "487" || json.tips.statusCode == "488")
		alert(json.tops.message);
	else
		alert(map(json.tips.statusCode));

}

function chance_date(name){
		var currYear = (new Date()).getFullYear();	
		var opt={};
		opt.date = {preset : 'date'};
		//opt.datetime = { preset : 'datetime', minDate: new Date(2012,3,10,9,22), maxDate: new Date(2014,7,30,15,44), stepMinute: 5  };
		opt.datetime = {preset : 'datetime'};
		opt.time = {preset : 'time'};
		opt.default = {
			theme: 'android-ics light', //皮肤样式
	        display: 'modal', //显示方式 
	        mode: 'scroller', //日期选择模式
			lang:'zh',
	        startYear:currYear - 10, //开始年份
	        endYear:currYear + 10 //结束年份
		};
		
		$("#"+name).val('').scroller('destroy').scroller($.extend(opt['date'], opt['default']));
	}

function back_home(){
	window.location.href="index.html";
}

//判断身份证号是否合法
function IdentityCodeValid(code) { 
    var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
    var tip = "";
    var pass= true;
    
 //   if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
  //      tip = "身份证号格式错误";
  //      pass = false;
  //  }
    
//   else if(!city[code.substr(0,2)]){
    
    if(code.length == 0 )
    	{
    	tip = "身份证号不能为空";
        pass = false;
    	}
    if(!city[code.substr(0,2)]){
        tip = "身份证地址编码错误";
        pass = false;
    }
    else{
        //18位身份证需要验证最后一位校验位
        if(code.length == 18){
            code = code.split('');
            //∑(ai×Wi)(mod 11)
            //加权因子
            var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
            //校验位
            var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
            var sum = 0;
            var ai = 0;
            var wi = 0;
            for (var i = 0; i < 17; i++)
            {
                ai = code[i];
                wi = factor[i];
                sum += ai * wi;
            }
            var last = parity[sum % 11];
            if(parity[sum % 11] != code[17]){
                tip = "身份证校验位错误";
                pass =false;
            }
        }
    }
    if(!pass) alert(tip);
    return pass;
}

function chance_date(name){
		var currYear = (new Date()).getFullYear();	
		var opt={};
		opt.date = {preset : 'date'};
		//opt.datetime = { preset : 'datetime', minDate: new Date(2012,3,10,9,22), maxDate: new Date(2014,7,30,15,44), stepMinute: 5  };
		opt.datetime = {preset : 'datetime'};
		opt.time = {preset : 'time'};
		opt.default = {
			theme: 'android-ics light', //皮肤样式
	        display: 'modal', //显示方式 
	        mode: 'scroller', //日期选择模式
			lang:'zh',
	        startYear:currYear - 10, //开始年份
	        endYear:currYear + 10 //结束年份
		};
		
		$("#"+name).val('').scroller('destroy').scroller($.extend(opt['date'], opt['default']));
	}

var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
        
    },
    
    onDeviceReady: function() {
    	
        app.receivedEvent('deviceready');
       // navigator.splashscreen.hide();
       // document.addEventListener("backbutton", eventBackButton, false); //返回键     
        document.addEventListener('deviceready', initial, false);
        document.addEventListener("backbutton", onBackKeyDown, false); //返回键   
        document.addEventListener("resume", onResume, false); //监听返回前台事件
  //      document.addEventListener("backbutton", onKeyBoardHide, false); //软键盘缩回操作
    },

    receivedEvent: function(id) {
        console.log('Received Event: ' + id);
    }
   
};

function onResume(){
	setTimeout(function(){
		top.writelog("我返回前台了");
	},1000);
}
function onKeyBoardShow(bottom) {
	 var diff = ($('input[type=text]:focus').offset().top - bottom) + 50;
	 alert("KeyBoardShow diff="+diff);
	 if(diff > 0) {
	  $('body').css("top", (diff * -1) + "px");
	 }
	};
	
function onKeyBoardHide() {
	alert("KeyBoardHide");
	 $('body').css("top", "0px");
};


function onBackKeyDown() {
	
	var acount=localStorage.getItem("acount");
	//20170220 add by lwj	
	/*判断图片浏览是否打开*/
	var thisURL = localStorage.getItem("thisURL");
	
	var open_flag = localStorage.getItem("open_flag");
	if(open_flag == 1){
		localStorage.setItem("open_flag", '0');
		//alert("点右上角的'X'关闭图片浏览");
		if(thisURL.match('fileno_info')){
			document.getElementById('mainFrame5').contentWindow.close_back();
			
		}else{
			document.getElementById('mainFrame5').contentWindow.document.getElementById('mainFrame5').contentWindow.close_back();
		}
		
		return;
	}
	
	if( acount == 0 || acount < 0){		
		if( window.confirm("确认退出货后检查APP吗?")){
			localStorage.setItem("login_stat", "0");
			navigator.app.exitApp(); 
		//	clearInterval(intervalid);
		}						
	}else{
		
		if(top.edit_flag == 1){
			top.confirmInfo("您的页面正在编辑中，是否确定退出?", function(msg){
				
				if(msg == 1){//确认
					top.edit_flag = 0;
					var len=top.pagearr.length;
					
					if(len>0){
						top.pagearr[len-1]["win"].hideFrame(top.pagearr[len-1]["method"]);
						top.pagearr[len-1]["win"]=null;
						top.pagearr[len-1]["method"]=null;
						top.pagearr.pop();
					}
				}
			})
		}else{
			var len=top.pagearr.length;
			
			if(len>0){
				top.pagearr[len-1]["win"].hideFrame(top.pagearr[len-1]["method"]);
				top.pagearr[len-1]["win"]=null;
				top.pagearr[len-1]["method"]=null;
				top.pagearr.pop();
			}
		}
		
	}
						
}
function jquery_chance(page){
			alert(jQuery.mobile);
			alert(jQuery.mobile.changePage);
	  jQuery.mobile.changePage('#'+page, {transition : 'slide'});
}  
function up_page(){
	localStorage.setItem("pagenum", 0);
	localStorage.setItem("pageimgnum",0);
}
function back_history(){
	//	alert("account=" + localStorage.getItem("acount"));
		var len=top.pagearr.length;
		if(len>0){
			top.pagearr[len-1]["win"].hideFrame(top.pagearr[len-1]["method"]);
			top.pagearr[len-1]["win"]=null;
			top.pagearr[len-1]["method"]=null;
			top.pagearr.pop();
		}

}

function login_again(){
    //localStorage.clear();
	top.alertInfo('通信失败,请重新登录');
	top.start_process();
	 $.ajax({
         type : "post",
         timeout: 5000,
         url: "http://"+IP+":"+PORT+"/app/6026",
         datatype : "json",
         success : function(){
        	 top.stop_process();
        	 top.location.href = "login_new.html";
         },
         error:function(msg){
      	   top.stop_process();
         top.alertInfo('通讯失败');
         top.location.href = "login_new.html";
         }
         });
    
}
function get_fileno(){
	date = new Date();  
	
	var year = date.getFullYear();  
	var month = date.getMonth()+1;  
	var day = date.getDate();
	if(month<10) month = "0"+month;  
	if(day<10) day = "0"+day;
	
	var hours = date.getHours();  
	var mins = date.getMinutes();  
	var secs = date.getSeconds();  
	var msecs = date.getMilliseconds();  
	if(hours<10) hours = "0"+hours;  
	if(mins<10) mins = "0"+mins;  
	if(secs<10) secs = "0"+secs;  
	if(msecs<10) secs = "0"+msecs; 
	return (year+month+day+hours+mins+secs+msecs);
}
function get_trandate(){
	date = new Date();  
	
	var year = date.getFullYear();  
	var month = date.getMonth()+1;  
	var day = date.getDate();
	if(month<10) month = "0"+month;  
	if(day<10) day = "0"+day;
	
	var hours = date.getHours();  
	var mins = date.getMinutes();  
	var secs = date.getSeconds();  
	var msecs = date.getMilliseconds();  
	if(hours<10) hours = "0"+hours;  
	if(mins<10) mins = "0"+mins;  
	if(secs<10) secs = "0"+secs;  
	
	return (year+'-'+month+'-'+day+' '+hours+':'+mins+':'+secs);
}
function get_date(){
	date = new Date();  
	
	var year = date.getFullYear();  
	var month = date.getMonth()+1;  
	var day = date.getDate();
	if(month<10) month = "0"+month;  
	if(day<10) day = "0"+day;
	

	return (year+'-'+month+'-'+day);
}
function Http_ajax(callback, trancode, data){
	top.start_process();
	top.writelog(trancode+"send :"+JSON.stringify(data));
	$.ajax({
        type : "post",
        timeout: 10000,
        url: "http://"+IP+":"+PORT+"/"+trancode,        
        data : data,
        datatype : "application/json",
        success : function(msg){
            
            top.stop_process();
            var msg = msg.replace(/null/g, '\'\'');
            top.writelog(trancode+" recv: "+msg);
            callback(msg);
            },
        error:function(msg){
     	   top.stop_process();
     	   top.alertInfo('交易失败:'+JSON.stringify(msg));
        }
        });
}
function exitApp() {
	localStorage.setItem("login_stat", "0");
		navigator.app.exitApp(); 
}
function scroll_auto(){
	
	
		if($("body").css("overflow") == "auto"){
			$("body").attr("style", "overflow:scroll");
		}else{
			$("body").attr("style", "overflow:auto");
		}
	}
function get_time_str(date1){
	 
    var date2 = new Date();    //结束时间  
    var date3 = date2.getTime() - new Date(date1).getTime();   //时间差的毫秒数        
  
    //------------------------------  
  
    //计算出相差天数  
    var days=Math.floor(date3/(24*3600*1000));  
  
    //计算出小时数  
  
    var leave1=date3%(24*3600*1000);    //计算天数后剩余的毫秒数  
    var hours=Math.floor(leave1/(3600*1000));  
    //计算相差分钟数  
    var leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数  
    var minutes=Math.floor(leave2/(60*1000));  
    //计算相差秒数  
    var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数  
    var seconds=Math.round(leave3/1000);  
//    alert(" 相差 "+days+"天 "+hours+"小时 "+minutes+" 分钟"+seconds+" 秒")  
    /*
    if(days > 365 ){
    	return "一年前";
    }else if(days > 31 ){
    	return days/30+"月前";
    }else*/ 
    if(days > 0 ){
    	return date1.substr(0,10);
    }else{
    	if(hours > 0){
    		return hours + "小时前";
    	}else{
    		if(minutes > 0){
    			return minutes + "分钟前";
    		}else{
    			return "刚才";
    		}
    	}
    }
}
function checkPicurl(url){

	var img = new Image();
	img.src = url;
	img.onerror = function(){
		top.writelog(url+" 图片加载失败，请检查url是否正确");
		return false;
	};

	if(img.complete){
		//	console.log(img.width+" "+img.height);
			localStorage.setItem(url, img.width+","+img.height)
	}else{
			img.onload = function(){
		//	console.log(img.width+" "+img.height);
			localStorage.setItem(url, img.width+","+img.height)
			img.onload=null;//避免重复加载
		}
	}

}

function setLocal(key){
	var msg =  localStorage.getItem("localKey");
	var myArray;
	if(isEmpty(msg)){
		myArray = new Array();
		myArray.push(key);
	}else{
		myArray = JSON.parse(msg);
			
		for(var i = 0; i > myArray.length; i++){
			if(myArray[i] == key){
				return false;
			}
		}
		
		myArray.push(key);
	}
	
	localStorage.setItem("localKey", JSON.stringify(myArray));
	
	return true;
}


//判断对象是否为空
function isEmpty(obj) {

	if(!obj && obj != 0 && obj != '') {　　　　　　　　　
	  return true;
	}
	
	if(Array.prototype.isPrototypeOf(obj) && obj.length == 0) {
		　　　return true;
	}
	
	if(Object.prototype.isPrototypeOf(obj) && Object.keys(obj).length == 0) { 
		
		　return true; 　　　
	}
	return false; 
}
var Base64 = {

	    // private property
	    _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=:",

	    // public method for encoding
	    encode: function(input) {
	        var output = "";
	        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
	        var i = 0;

	        input = Base64._utf8_encode(input);

	        while (i < input.length) {

	            chr1 = input.charCodeAt(i++);
	            chr2 = input.charCodeAt(i++);
	            chr3 = input.charCodeAt(i++);

	            enc1 = chr1 >> 2;
	            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
	            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
	            enc4 = chr3 & 63;

	            if (isNaN(chr2)) {
	                enc3 = enc4 = 64;
	            } else if (isNaN(chr3)) {
	                enc4 = 64;
	            }

	            output = output + this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) + this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);

	        }

	        return output;
	    },

	    // public method for decoding
	    decode: function(input) {
	        var output = "";
	        var chr1, chr2, chr3;
	        var enc1, enc2, enc3, enc4;
	        var i = 0;

	        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

	        while (i < input.length) {

	            enc1 = this._keyStr.indexOf(input.charAt(i++));
	            enc2 = this._keyStr.indexOf(input.charAt(i++));
	            enc3 = this._keyStr.indexOf(input.charAt(i++));
	            enc4 = this._keyStr.indexOf(input.charAt(i++));

	            chr1 = (enc1 << 2) | (enc2 >> 4);
	            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
	            chr3 = ((enc3 & 3) << 6) | enc4;

	            output = output + String.fromCharCode(chr1);

	            if (enc3 != 64) {
	                output = output + String.fromCharCode(chr2);
	            }
	            if (enc4 != 64) {
	                output = output + String.fromCharCode(chr3);
	            }

	        }

	        output = Base64._utf8_decode(output);

	        return output;

	    },

	    // private method for UTF-8 encoding
	    _utf8_encode: function(string) {
	        string = string.replace(/\r\n/g, "\n");
	        var utftext = "";

	        for (var n = 0; n < string.length; n++) {

	            var c = string.charCodeAt(n);

	            if (c < 128) {
	                utftext += String.fromCharCode(c);
	            } else if ((c > 127) && (c < 2048)) {
	                utftext += String.fromCharCode((c >> 6) | 192);
	                utftext += String.fromCharCode((c & 63) | 128);
	            } else {
	                utftext += String.fromCharCode((c >> 12) | 224);
	                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
	                utftext += String.fromCharCode((c & 63) | 128);
	            }

	        }

	        return utftext;
	    },

	    // private method for UTF-8 decoding
	    _utf8_decode: function(utftext) {
	        var string = "";
	        var i = 0;
	        var c = c1 = c2 = 0;

	        while (i < utftext.length) {

	            c = utftext.charCodeAt(i);

	            if (c < 128) {
	                string += String.fromCharCode(c);
	                i++;
	            } else if ((c > 191) && (c < 224)) {
	                c2 = utftext.charCodeAt(i + 1);
	                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
	                i += 2;
	            } else {
	                c2 = utftext.charCodeAt(i + 1);
	                c3 = utftext.charCodeAt(i + 2);
	                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
	                i += 3;
	            }

	        }

	        return string;
	    }

	}
app.initialize();