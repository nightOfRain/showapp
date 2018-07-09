cordova.define("com.nantian.map", function(require, exports, module) {
	var exec = require('cordova/exec');

	module.exports = {

		/**
		 *  给定地点导航
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
		},
		/**
		 *  关键词周边搜索
		 *
		 *  @param successCallback 成功的回调
		 *  @param errorCallback   失败的回调
		 *  @param name   关键词
		 *  @param range   周边搜索半径 默认为1000米
		 */
		poiSearch: function(successCallback, errorCallback, name, range) {

			exec(successCallback, errorCallback, "NTMap", "poiSearch", [name, range]);
		},
		
		/**
		 *  给定多地点显示在地图上
		 *
		 *  @param successCallback 成功的回调
		 *  @param errorCallback   失败的回调
		 *  @param json   多个地点的经纬度和名称 
	     *  	格式为：[{'lat':'','lon': '', 'name': ''},{'lat':'','lon': '', 'name': '' },{'lat':'','lon': '', 'name': ''}]
		 */
		multiSearch: function(successCallback, errorCallback, json) {
			
			exec(successCallback, errorCallback, "NTMap", "multiSearch", [json]);
		}
	
	};
});