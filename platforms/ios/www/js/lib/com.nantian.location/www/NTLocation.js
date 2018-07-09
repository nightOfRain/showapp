cordova.define("com.nantian.location", function(require, exports, module) {
	var exec = require('cordova/exec');

	module.exports = {

		/**
		 *  定位
		 *
		 *  @param successCallback 成功的回调，返回 字符串 ： longitude&latitude
		 *  @param errorCallback   失败的回调
		 *
		 */
		getLocation: function(successCallback, errorCallback) {

			exec(successCallback, errorCallback, "NTLocation", "getLocation", []);
		}

	};
});