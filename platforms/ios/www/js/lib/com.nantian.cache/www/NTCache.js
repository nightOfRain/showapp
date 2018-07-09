cordova.define("com.nantian.cache", function(require, exports, module) {
	var exec = require('cordova/exec');

	module.exports = {
		/**
		 * 保存信息
		 */
		save: function(successCallback, errorCallback, key, value) {
			exec(successCallback, errorCallback, "NTCache", "save", [key, value]);
		},
		/**
		 * 通过key获取之前保存的信息
		 */
		get: function(successCallback, errorCallback, key) {
			exec(successCallback, errorCallback, "NTCache", "get", [key]);
		}

	};
});