cordova.define("com.nantian.callbrowser", function(require, exports, module) { var exec = require('cordova/exec');
               
               module.exports={
               
               /**
                *  传入一个url调用浏览器显示
                *
                *  @param successCallback 成功的回调
                *  @param failCallback    失败的回调
                *
                */
               callBrowser : function(successCallback,failCallback,url) {
            	   exec(successCallback, failCallback, "NTCallBrowser", "callBrowser", [ url ]);
               }
               
             };
               
               
          });
