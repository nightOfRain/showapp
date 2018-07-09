cordova.define("com.nantian.ntLocation", function(require, exports, module) {
	var exec = require('cordova/exec');

	module.exports = {

		/**
		 *  定位
		 *
		 *  @param successCallback 成功的回调，返回json格式的字符串
		 *  @param errorCallback   失败的回调
		 *
		 */
		getLocation: function(successCallback, errorCallback) {
			exec(successCallback, errorCallback, "NTLocation", "getLocation", []);
		}

	};
});