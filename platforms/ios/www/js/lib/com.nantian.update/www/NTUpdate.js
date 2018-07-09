cordova.define("com.nantian.update", function(require, exports, module) {
	var exec = require('cordova/exec');

	module.exports = {
		/**
		 * 保存信息
		 */
		update: function(successCallback, errorCallback, key, value) {
			exec(successCallback, errorCallback, "NTUpdate", "update", [key, value]);
		},
		/**
		 * 获取zip版本号和apk版本号,返回包含zip版本和zpp版本的json串
		 */
		getVersion: function(successCallback, errorCallback) {
			exec(successCallback, errorCallback, "NTUpdate", "getVersion", []);
		}
	};
});