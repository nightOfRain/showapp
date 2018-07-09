cordova.define("com.nantian.secret", function(require, exports, module) {
	var exec = require('cordova/exec');

	module.exports = {
        /**
         *  加密
         *
         *  @param successCallback 成功的回调，返回加密后的密文
         *  @param failCallback    失败的回调，返回失败原因
         *	@param type 加密的类型（rsa/3des）
         *	@param text 需要加密的明文
         */
		encrypt: function(successCallback, errorCallback, type, text) {
			exec(successCallback, errorCallback, "NTSecret", "encrypt", [type,text]);
		},
		
		 /**
         *  解密
         *
         *  @param successCallback 成功的回调，返回解密后的明文
         *  @param failCallback    失败的回调，返回失败原因
         *	@param type 解密的类型（rsa/3des）
         *	@param text 需要解密的密文
         */
		decrypt: function(successCallback, errorCallback, type, text) {

			exec(successCallback, errorCallback, "NTSecret", "decrypt", [type,text]);
		}

	};
});