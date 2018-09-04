cordova.define("xiyu-plugin-baiduai", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
     *  消失通讯框
     *
     */
    getResult: function(succp, failp, options) {
        var args = [options.type, options.imgurl];
        exec(succp, failp, "BaiduAi","get", args);
        },
               
   

};
});