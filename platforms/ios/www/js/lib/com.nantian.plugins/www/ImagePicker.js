cordova.define("com.nantian.plugins.ImagePicker", function(require, exports, module) {
var exec = require('cordova/exec');

module.exports = {
    
    /**
     *  消失通讯框
     *
     */
    show: function (success, fail) {
    	var options = {};

    	  options.minImages = options.minImages || 0
    	  options.maxImages = options.maxImages || 10
    	  options.mediaType = options.mediaType || 'any'
    	  options.width = options.width || 0
    	  options.height = options.height || 0
    	  options.quality = options.quality || 50
    	  console.log("nimabi");
    	exec(success, fail, 'ImagePicker', 'getPictures', [options])
        },
       
       
               
   

};
});