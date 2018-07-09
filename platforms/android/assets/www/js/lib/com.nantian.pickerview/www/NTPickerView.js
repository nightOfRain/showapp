cordova.define("com.nantian.pickerview", function(require, exports, module) {
               var exec = require('cordova/exec');
               
               module.exports = {
               
               openPickerView : function(successCallback,failCallback,titleText,contentAry) {
               
               if(titleText == undefined){
               titleText = "";
               }
               
               exec(successCallback, failCallback, "NTPickerView","openPickerView", [titleText,contentAry]);
               }
               
        };
});

