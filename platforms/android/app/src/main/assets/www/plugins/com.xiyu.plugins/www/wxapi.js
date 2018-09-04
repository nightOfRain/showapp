cordova.define("xiyu-plugin-wxapi", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
     *  消失通讯框
     *
     */
    share: function(succp, failp, options) {
        var args = [options.type, options.text];
        exec(succp, failp, "myWxapi","get", args);
        },
               
   

};
});