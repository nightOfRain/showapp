cordova.define("com.xiyu.plugin.appupdate", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
     *  消失通讯框
     *
     */
		CheckUpdate: function(successback, faileback) {
        exec(successback, faileback, "AppUpdate","Check_Update", []);
        },
               
   

};
});