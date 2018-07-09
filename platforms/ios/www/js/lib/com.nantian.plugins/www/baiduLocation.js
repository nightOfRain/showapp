cordova.define("com.nantian.plugins.baidu", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
     *  消失通讯框
     *
     */
		getCurrentPosition: function(successFn,failureFn) {
        exec(successFn, failureFn, "BaiduLocation","getCurrentPosition", []);
        },
               
   

};
});