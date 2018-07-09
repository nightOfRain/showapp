cordova.define("com.nantian.camera", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
    *  弹出录像框
    *
    *  @param successCallback 成功的回调
    *  @param failCallback    失败的回调
    *  @param filename    	录像保存时的名称
    *  @param duration       录像的最大时长，如果无限制传入""即可
    *  @param isDataBox       是否保存在沙箱中
    *  @param flag    调用摄像头的标识  "0"(后置摄像头)/"1"(前置摄像头) 默认为后置摄像头
    *
    */
    showVideo : function(successCallback,failCallback,filename, duration, isDataBox,flag) {
        exec(successCallback, failCallback, "NTVideo", "showVideo", [filename,duration,isDataBox,flag]);
    },
    
    /**
     *  调用视频播放器
     *
     *  @param successCallback 成功的回调
     *  @param failCallback    失败的回调
     *  @param filename    	   录像的全路径，如果录像保存在安全沙箱中，这传入获取文件的key
     *  @param isDataBox       是否保存在沙箱中
     *
     */
    playVideo : function(successCallback, failCallback, filepath, isDataBox) {
    	exec(successCallback, failCallback, "NTVideoPlayer", "playVideo", [filepath,isDataBox]);
    },
               
    /**
    *  弹出扫描二维码
    *
    *  @param successCallback 成功的回调
    *  @param failCallback    失败的回调
    *
    */
    showQR : function(successCallback,failCallback){
        exec(successCallback, failCallback, "NTQRScanner", "showQR", []);
    },
               
    /**
    *  弹出拍照框
    *
    *  @param successCallback 成功回调，里面包含图片的二进制数据
    *  @param failCallback    失败回调，json格式的字符串，里面content字段是报错信息
    *  @param picname    图片保存的名称
    *  @param flag    调用摄像头的标识  "0"(后置摄像头)/"1"(前置摄像头) 默认为后置摄像头
    *  @param isDataBox       是否保存在沙箱中
    *  @param needEdit        是否需要裁剪图片，默认为否（ “0”：不裁剪 ，”1“ ：“裁剪”）；
    *
    */
    showPictureTaker : function(successCallback,failCallback,picname,flag,isDataBox,needEdit){
        exec(successCallback, failCallback, "NTPictureTaker", "showPictureTaker", [picname,flag,isDataBox,needEdit]);
    },
    
    /**
     *  弹出图片选择器
     *
     *  @param successCallback 成功回调，里面包含图片的二进制数据
     *  @param failCallback    失败回调，json格式的字符串，里面content字段是报错信息
     *  @param isDataBox       是否保存在沙箱中
     *  @param needEdit        是否需要裁剪图片，默认为否（ “0”：不裁剪 ，”1“ ：“裁剪”）；
     *  @param padX            ios 平板视图中弹出选择器的x坐标；
     *  @param padY            ios 平板视图中弹出选择器的y坐标；
     */
    showImageSwitcher : function(successCallback,failCallback,picname,isDataBox,needEdit,padX,padY){
         exec(successCallback, failCallback, "NTPictureTaker", "showImageSwitcher", [picname,isDataBox,needEdit,padX,padY]);
     },
     
     /**
      *  获取自己应用的图片和视频文件列表
      *
      *  @param successCallback 成功的回调
      *  @param failCallback    失败的回调
      *
      */
      getFileList : function(successCallback,failCallback){
          exec(successCallback, failCallback, "NTPictureTaker", "getFileList", []);
      },
               
};
});
