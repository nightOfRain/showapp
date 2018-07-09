cordova.define("com.xiyu.plugin.jpush", function(require, exports, module) {

	var exec = require('cordova/exec');

	module.exports = {
	    
	    /**
	     *  消失通讯框
	     *
	     */
			setAlias: function( alias) {
	        exec(null, null, "JPushPlugin","setAlias", [alias]);
	        },
	               
	   

	};
});