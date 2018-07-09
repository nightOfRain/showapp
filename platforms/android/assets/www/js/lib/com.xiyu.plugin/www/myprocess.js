cordova.define("com.xiyu.plugin.myprocess", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
     *  消失通讯框
     *
     */
    start: function() {
        exec(null, null, "MyProcess","start", []);
        },
       
        stop: function() {
            exec(null, null, "MyProcess","stop", []);
            },
               
   

};
});