cordova.define("com.nantian.ntMap", function(require, exports, module) {
	var exec = require('cordova/exec');

	module.exports = {

		/**
		 *  导航
		 *
		 *  @param successCallback 成功的回调
		 *  @param errorCallback   失败的回调
		 *  @param latitude        纬度
		 *  @param longitude       经度
		 *  @param name            显示地面
		 *
		 */
		ntMapNav: function(successCallback, errorCallback, latitude, longitude, name) {

			exec(successCallback, errorCallback, "NTMap", "ntMapNav", [latitude, longitude, name]);
		}
	};
});