  // 'load', 'deviceready', 'offline', and 'online'.
$(document).bind("mobileinit", function() {
	 
    //$.mobile.autoInitialize = false; //删除这行配置参数就会出现渲染错误

    $.support.cors = true;

    $.mobile.allowCrossDomainPages = true;

});

var pictureSource; // picture source

var destinationType; // sets the format of returned value
    document.addEventListener('deviceready', this.onDeviceReady, false);

    
    function  onDeviceReady(){
        pictureSource = navigator.camera.PictureSourceType;
        
        destinationType = navigator.camera.DestinationType;
    };


     // Cordova is ready to be used!
  
     //

  
     // Called when a photo is successfully retrieved
  
     //
  


     // A button will call this function
  
     //
  
     function capturePhoto() {
  
         // Take picture using device camera and retrieve image as base64-encoded string
  
         navigator.camera.getPicture(uploadPhoto, onFail, {
  
             quality : 50,
  
             destinationType : navigator.camera.DestinationType.FILE_URI,//这里要用FILE_URI，才会返回文件的URI地址
  
             //destinationType : Camera.DestinationType.DATA_URL,
  
             sourceType : Camera.PictureSourceType.CAMERA,
  
             allowEdit : true,
  
             encodingType : Camera.EncodingType.JPEG,
  
             popoverOptions : CameraPopoverOptions,
  
             saveToPhotoAlbum : true
  
         });
  
     }
  
     // A button will call this function
  
     //
  
     function capturePhotoEdit() {
  
         // Take picture using device camera, allow edit, and retrieve image as base64-encoded string  
  
         navigator.camera.getPicture(uploadPhoto, onFail, {
  
             quality : 50,
  
             allowEdit : true,
  
             destinationType : destinationType.DATA_URL,
  
             saveToPhotoAlbum : true
  
         });
  
     }
  
     // A button will call this function
  
     //
  
     function getPhoto(source) {
  
         // Retrieve image file location from specified source
  
         navigator.camera.getPicture(uploadPhoto, onFail, {
  
             quality : 50,
  
             destinationType : destinationType.FILE_URI,//这里要用FILE_URI，才会返回文件的URI地址
  
             sourceType : source
  
         });
  
     }
  
     // Called if something bad happens.
  
     //
  
     function onFail(message) {
  
         alert('Failed because: ' + message);
  
     }
  
     function uploadPhoto(imageURI) {
  
         var options = new FileUploadOptions();
          
         options.fileKey = "fileAddPic";//用于设置参数，对应form表单里控件的name属性，这是关键，废了一天时间，完全是因为这里，这里的参数名字，和表单提交的form对应
  
         options.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1);
         
    
  		
         options.mimeType = "image/jpeg";
  
         //这里的uri根据自己的需求设定，是一个接收上传图片的地址
  
         var uri = encodeURI("http://192.168.1.9:8080/mfpexample/fileUpload?txcode=imp001&filename="+options.fileName+"&flag="+flag+"&type="+type+"&userid="+userid+"&zjh="+zjh);

  
         options.chunkedMode = false;
          
    
  
         var ft = new FileTransfer();
  
         ft.upload(imageURI, uri, win, fail, options);
  
     }
  
     function win(r) {
  
         alert("上传成功");
  
        	alert(r.response);
         var response = r.response;
  
         //alert("response = " + response);
  
         //这里的message是自定义的返回值，具体的根据自己的需求而定
  
        // var message = eval("(" + r.response + ")").message;
  var message = eval("(" + r.response + ")")
         $("#_picFile").val(message);
  
         //alert("message = " + message);
  
         var imageURI = CONSTANT['context'] + message;
  

  
         //以下是三个默认的返回参数
  
         console.log("Code = " + r.responseCode);
  
         console.log("Response = " + r.response);
  
         console.log("Sent = " + r.bytesSent);
  
     }
  
     function fail(error) {
  
         alert("上传失败");
  

  
         console.log("upload error source " + error.source);
  
         console.log("upload error target " + error.target);
  
     }
     window.resolveLocalFileSystemURI(imageURI, onResolveSuccess, onError);
     
     function onResolveSuccess(fileEntry){
          
          alert(fileEntry.fullPath);
      }
      function onError(error) { 

          toLog("error code: "+ error.code);

      };