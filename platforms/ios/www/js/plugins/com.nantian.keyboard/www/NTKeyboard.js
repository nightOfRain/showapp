cordova.define("com.nantian.keyboard", function(require, exports, module) { var exec = require('cordova/exec');
               
               module.exports={
               
               /**
                *  显示键盘
                *
                *  @param successCallback 成功的回调
                *  @param failCallback    失败的回调
                *  @param flag    标识调出时首次显示的类型 0字母键盘/1数字键盘
                *
                */
               showKeyboard : function(successCallback, failCallback, flag, isPhone) {
               
            	   exec(successCallback, failCallback, "NTKeyboard", "showKeyboard", [flag, isPhone]);
               },
               /**
                *  隐藏键盘
                *
                *  @param successCallback 成功的回调
                *  @param failCallback    失败的回调
                *
                */
               hideKeyboard : function(successCallback,failCallback) {
               
            	   exec(successCallback, failCallback, "NTKeyboard", "hideKeyboard", []);
               }
               
             };
               
               
          });
