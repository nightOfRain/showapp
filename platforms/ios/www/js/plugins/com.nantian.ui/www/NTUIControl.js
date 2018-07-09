cordova.define("com.nantian.ui", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
     *  消失通讯框
     *
     */
    onLoaded: function() {
        exec(null, null, "NTUICtrl","onLoaded", []);
    },
               
    /**
     *  显示通讯框
     *
     */
    beginLoaded: function() {
        exec(null, null, "NTUICtrl","beginLoaded", []);
    }
};
});
