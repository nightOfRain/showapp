cordova.define("com.nantian.location", function(require, exports, module) {
	var exec = require('cordova/exec');

	module.exports = {

		/**
		 *  定位
		 *
		 *  @param successCallback 成功的回调，返回json格式的字符串
		 *  @param errorCallback   失败的回调
		 *  @param type   返回坐标类型 0表示返回百度坐标，1表示返回原始GPS坐标
		 */
		getLocation: function(successCallback, errorCallback, type) {
			exec(successCallback, errorCallback, "NTLocation", "getLocation", [type]);
		}

	};
});