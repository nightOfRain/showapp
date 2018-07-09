cordova.define("com.nantian.callbrowser", function(require, exports, module) { var exec = require('cordova/exec');
               
               module.exports={
               
               /**
                *  传入一个url调用系统浏览器显示
                *
                *  @param successCallback 成功的回调
                *  @param failCallback    失败的回调
                *
                */
               callBrowser : function(successCallback,failCallback,url) {
            	   exec(successCallback, failCallback, "NTCallBrowser", "callBrowser", [ url ]);
               },
               /**
                *  @param successCallback 	成功的回调
                *  @param failCallback    	失败的回调
                *  @param url    			要显示网页的路径
                *  @param animationType   	动画类型（0为淡入淡出、1为右滑进右滑出）
                *  @param title    			标题
                *  @param backTitle    		返回按钮的文字（默认为 返回）
                */
               showWebview : function(successCallback,failCallback,url,animationType,title,backTitle) {
                   exec(successCallback, failCallback, "NTCallBrowser", "showWebview", [ url,animationType,title,backTitle ]);
               }
               
             };
               
               
          });
