cordova.define("com.nantian.lockview", function(require, exports, module) {
               var exec = require('cordova/exec');
               
               module.exports = {
               
               /**
                *  页面设置手势密码
                *
                *  @param successCallback 成功的回调
                *  @param errorCallback   失败的回调
                *  @param userID          用户id
                *
                */
               setLockPwd : function(successCallback,errorCallback,userID) {
               
               exec(successCallback, errorCallback, "NTLockView", "setLockPwd",[userID]);
               },
               
               /**
                *  此用户开启手势密码
                *
                *  @param successCallback 成功的回调
                *  @param errorCallback   失败的回调
                *  @param userID          用户id
                *
                */
               enableLockGes : function(successCallback,errorCallback,userID) {
               
               exec(successCallback, errorCallback, "NTLockView", "enableLockGes",[userID]);
               },
               
               /**
                *  此用户关闭手势密码
                *
                *  @param successCallback 成功的回调
                *  @param errorCallback   失败的回调
                *
                */
               disableLockGes : function(successCallback,errorCallback,userID) {
               
               exec(successCallback, errorCallback, "NTLockView", "disableLockGes",[userID]);
               },
               
               /**
                *  是否存在手势密码，如果存在调用成功的回调，如果不存在调用失败的回调
                *
                *  @param successCallback 成功的回调
                *  @param errorCallback   失败的回调
                *  @param userID          用户id
                *
                */
               isExistLockPwd : function(successCallback,errorCallback,userID) {
               
               exec(successCallback, errorCallback, "NTLockView", "isExistLockPwd",[userID]);
               }


               };
               });
