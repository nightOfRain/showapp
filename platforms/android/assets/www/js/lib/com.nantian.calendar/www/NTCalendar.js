cordova.define("com.nantian.calendar", function(require, exports, module) {
               var exec = require('cordova/exec');
               
               module.exports = {
               
               /**
                *  选取日期
                *
                *  @param successCallback 成功的回调
                *  @param failCallback    失败的回调
                *  @param dateFormat      日期格式，默认为：yyyy/MM/dd
                *
                */
               getDateValue : function(successCallback,failCallback,dateFormat) {
               
               if(dateFormat == undefined){
               dateFormat = "yyyy/MM/dd";
               }
               exec(successCallback, failCallback, "NTCalendar","getDateValue", [dateFormat]);
               }
               
               };
               });
