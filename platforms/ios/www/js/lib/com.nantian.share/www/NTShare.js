cordova.define("com.nantian.share", function(require, exports, module) {
	var exec = require('cordova/exec');

	module.exports = {
			
		share: function(successCallback, errorCallback, text) {
			exec(successCallback, errorCallback, "NTShare", "share", [text]);
		}

	};
});