cordova.define("com.nantian.httprequest", function(require, exports, module) {
    var exec = require('cordova/exec');
               
    module.exports = {
               
               /**
                *  js提交请求
                *
                *  @param callback       回调函数，成功/失败 都是这一个
                *  @param method         get/post
                *  @param url            服务器地址，不包括ip和port，如xxx/xxx
                *  @param silent         是否需要通讯框，0--不需要 1--需要,注意：当需要通讯框时，通过发送“showLoadingView”通知原生启动通讯框，通过“dismissLoadingView”来通知原生消失通讯框
                *  @param address        选择服务器地址，例如 1、2、3 注意：必须是已经在配置文件添加上了的地址
                *  @param jsonStr        要传入的json字符串
                *  @param isEncrypted    是否需要全文加解密 0(false)/1(true)
                *
                */
               commit : function(callback,errorcallback, method, url, silent, address, jsonStr, isEncrypted) {
            	   exec(callback, errorcallback, "NTNewHttpCommunication", "commit", [ method, url, silent,address,jsonStr,isEncrypted]);
               }
               
};
});
