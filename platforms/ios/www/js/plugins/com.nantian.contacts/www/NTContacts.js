cordova.define("com.nantian.contacts", function(require, exports, module) {

	var exec = require('cordova/exec');

	module.exports = {
	    /**
	     *  获取所有联系人信息
	     *
	     *  @param successCallback 成功的回调
	     *  @param failCallback    失败的回调
	     */
	    getAllContacts : function(successCallback,failCallback){
	        exec(successCallback, failCallback, "NTContacts", "getAllContacts", []);
	    },
	    call : function(successCallback,failCallback,num){
	    	exec(successCallback, failCallback, "NTContacts", "call", [num]);
	    }
	};
});