cordova.define("com.nantian.parameter", function(require, exports,
		module) {
	var exec = require('cordova/exec');

	module.exports = {

		/**
		 * 存储数据到原生共享域
		 * 
		 * @param successCallback
		 *            存储成功的回调
		 * @param failCallback
		 *            存储失败的回调
		 * @param name
		 *            参数名
		 * @param value
		 *            参数值
		 * 
		 */
		addValue : function(successCallback, failCallback, name, value) {

			exec(successCallback, failCallback, "NTDataDictionary", "addValue",
					[ name,value ]);
		},

		/**
		 * 从原生共享域删除数据
		 * 
		 * @param successCallback
		 *            删除成功的回调
		 * @param failCallback
		 *            删除失败的回调
		 * @param key
		 *            要删除的参数名
		 * 
		 */
		delValue : function(successCallback, failCallback, keyVal) {
			exec(successCallback, failCallback, "NTDataDictionary", "delValue",
					[ keyVal ]);
		},

		/**
		 * 根据参数名从原生共享域获取参数值
		 * 
		 * @param name
		 *            参数名
		 * @param successCallback
		 *            成功的回调
		 * @param failCallback
		 *            失败的回调
		 * 
		 */
		getValue : function(successCallback, failCallback, name) {

			exec(successCallback, failCallback, "NTDataDictionary", "getValue",
					[ name ]);
		},

		/**
		 * 删除所有存在原生共享域的值，请慎用
		 * 
		 * @param successCallback
		 *            成功的回调
		 * @param failCallback
		 *            失败的回调
		 * 
		 */
		deleteAll : function(successCallback, failCallback) {
			exec(successCallback, failCallback, "NTDataDictionary",
					"deleteAll", []);
		}

	};

});
