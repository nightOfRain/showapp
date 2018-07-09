cordova.define("com.nantian.keyboard", function(require, exports, module) { var exec = require('cordova/exec');
               
               module.exports={
               
               /**
                *  显示键盘
                *
                *  @param successCallback 成功的回调
                *  @param failCallback    失败的回调
                *
                */
               showKeyboard : function(successCallback,failCallback,isPhone) {
               
            	   exec(successCallback, failCallback, "NTKeyboard", "showKeyboard", [isPhone]);
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
