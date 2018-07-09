cordova.define("com.nantian.plugins.callphone", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
     *  消失通讯框
     *
     */
    show: function(msg) {
        exec(null, null, "CallPhone","show", [msg]);
        },
               
   

};
});