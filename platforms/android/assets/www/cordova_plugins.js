cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "js/lib/org.apache.cordova.camera/www/CameraConstants.js",
        "id": "org.apache.cordova.camera.Camera",
        "clobbers": [
            "Camera"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.camera/www/CameraPopoverOptions.js",
        "id": "org.apache.cordova.camera.CameraPopoverOptions",
        "clobbers": [
            "CameraPopoverOptions"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.camera/www/Camera.js",
        "id": "org.apache.cordova.camera.camera",
        "clobbers": [
            "navigator.camera"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.camera/www/ios/CameraPopoverHandle.js",
        "id": "org.apache.cordova.camera.CameraPopoverHandle",
        "clobbers": [
            "CameraPopoverHandle"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.console/www/console-via-logger.js",
        "id": "org.apache.cordova.console.console",
        "clobbers": [
            "console"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.console/www/logger.js",
        "id": "org.apache.cordova.console.logger",
        "clobbers": [
            "cordova.logger"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.device/www/device.js",
        "id": "org.apache.cordova.device.device",
        "clobbers": [
            "device"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.dialogs/www/notification.js",
        "id": "org.apache.cordova.dialogs.notification",
        "merges": [
            "navigator.notification"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/DirectoryEntry.js",
        "id": "org.apache.cordova.file.DirectoryEntry",
        "clobbers": [
            "window.DirectoryEntry"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/DirectoryReader.js",
        "id": "org.apache.cordova.file.DirectoryReader",
        "clobbers": [
            "window.DirectoryReader"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/Entry.js",
        "id": "org.apache.cordova.file.Entry",
        "clobbers": [
            "window.Entry"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/File.js",
        "id": "org.apache.cordova.file.File",
        "clobbers": [
            "window.File"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/FileEntry.js",
        "id": "org.apache.cordova.file.FileEntry",
        "clobbers": [
            "window.FileEntry"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/FileError.js",
        "id": "org.apache.cordova.file.FileError",
        "clobbers": [
            "window.FileError"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/FileReader.js",
        "id": "org.apache.cordova.file.FileReader",
        "clobbers": [
            "window.FileReader"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/FileSystem.js",
        "id": "org.apache.cordova.file.FileSystem",
        "clobbers": [
            "window.FileSystem"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/FileUploadOptions.js",
        "id": "org.apache.cordova.file.FileUploadOptions",
        "clobbers": [
            "window.FileUploadOptions"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/FileUploadResult.js",
        "id": "org.apache.cordova.file.FileUploadResult",
        "clobbers": [
            "window.FileUploadResult"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/FileWriter.js",
        "id": "org.apache.cordova.file.FileWriter",
        "clobbers": [
            "window.FileWriter"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/Flags.js",
        "id": "org.apache.cordova.file.Flags",
        "clobbers": [
            "window.Flags"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/LocalFileSystem.js",
        "id": "org.apache.cordova.file.LocalFileSystem",
        "clobbers": [
            "window.LocalFileSystem"
        ],
        "merges": [
            "window"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/Metadata.js",
        "id": "org.apache.cordova.file.Metadata",
        "clobbers": [
            "window.Metadata"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/ProgressEvent.js",
        "id": "org.apache.cordova.file.ProgressEvent",
        "clobbers": [
            "window.ProgressEvent"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/fileSystems.js",
        "id": "org.apache.cordova.file.fileSystems"
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/requestFileSystem.js",
        "id": "org.apache.cordova.file.requestFileSystem",
        "clobbers": [
            "window.requestFileSystem"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/resolveLocalFileSystemURI.js",
        "id": "org.apache.cordova.file.resolveLocalFileSystemURI",
        "merges": [
            "window"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/ios/FileSystem.js",
        "id": "org.apache.cordova.file.iosFileSystem",
        "merges": [
            "FileSystem"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/fileSystems-roots.js",
        "id": "org.apache.cordova.file.fileSystems-roots",
        "runs": true
    },
    {
        "file": "js/lib/org.apache.cordova.file/www/fileSystemPaths.js",
        "id": "org.apache.cordova.file.fileSystemPaths",
        "merges": [
            "cordova"
        ],
        "runs": true
    },
    {
        "file": "js/lib/org.apache.cordova.file-transfer/www/FileTransferError.js",
        "id": "org.apache.cordova.file-transfer.FileTransferError",
        "clobbers": [
            "window.FileTransferError"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.file-transfer/www/FileTransfer.js",
        "id": "org.apache.cordova.file-transfer.FileTransfer",
        "clobbers": [
            "window.FileTransfer"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.geolocation/www/Coordinates.js",
        "id": "org.apache.cordova.geolocation.Coordinates",
        "clobbers": [
            "Coordinates"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.geolocation/www/PositionError.js",
        "id": "org.apache.cordova.geolocation.PositionError",
        "clobbers": [
            "PositionError"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.geolocation/www/Position.js",
        "id": "org.apache.cordova.geolocation.Position",
        "clobbers": [
            "Position"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.geolocation/www/geolocation.js",
        "id": "org.apache.cordova.geolocation.geolocation",
        "clobbers": [
            "navigator.geolocation"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.globalization/www/GlobalizationError.js",
        "id": "org.apache.cordova.globalization.GlobalizationError",
        "clobbers": [
            "window.GlobalizationError"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.globalization/www/globalization.js",
        "id": "org.apache.cordova.globalization.globalization",
        "clobbers": [
            "navigator.globalization"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.inappbrowser/www/inappbrowser.js",
        "id": "org.apache.cordova.inappbrowser.inappbrowser",
        "clobbers": [
            "window.open"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.network-information/www/network.js",
        "id": "org.apache.cordova.network-information.network",
        "clobbers": [
            "navigator.connection",
            "navigator.network.connection"
        ]
    },
    {
        "file": "js/lib/org.apache.cordova.network-information/www/Connection.js",
        "id": "org.apache.cordova.network-information.Connection",
        "clobbers": [
            "Connection"
        ]
    },
    {
        "file": "js/lib/com.nantian.HttpRequest/www/HttpRequest.js",
        "id": "com.nantian.HttpRequest.httpReq",
        "clobbers": [
            "NTHttReq"
        ]
    },
    {
        "file": "js/lib/com.nantian.parameter/www/NTParameter.js",
        "id": "com.nantian.parameter.ntparameter",
        "clobbers": [
            "NTParameter"
        ]
    },
    {
        "file": "js/lib/com.nantian.ui/www/NTUIControl.js",
        "id": "com.nantian.ui",
        "clobbers": [
            "NTUI"
        ]
    },
    {
        "file": "js/lib/com.nantian.pickerview/www/NTPickerView.js",
        "id": "com.nantian.pickerview",
        "clobbers": [
            "NTPickerview"
        ]
    },
    {
        "file": "js/lib/com.nantian.calendar/www/NTCalendar.js",
        "id": "com.nantian.calendar",
        "clobbers": [
            "NTCalendar"
        ]
    },
    {
        "file": "js/lib/com.nantian.camera/www/camera.js",
        "id": "com.nantian.camera",
        "clobbers": [
            "NTCamera"
        ]
    },{
        "file": "js/lib/com.nantian.file/www/Ntfile.js",
        "id": "com.nt.Ntfile",
        "clobbers": [
            "Ntfile"
        ]
    },
    {
        "file": "js/lib/com.nantian.update/www/NTUpdate.js",
        "id": "com.nantian.update",
        "clobbers": [
            "NTUpdate"
        ]
    },
    {
        "file": "js/lib/com.nantian.keyboard/www/NTKeyboard.js",
        "id": "com.nantian.keyboard",
        "clobbers": [
            "NTKeyboard"
        ]
    },
    {
        "file": "js/lib/com.nantian.cache/www/NTCache.js",
        "id": "com.nantian.cache",
        "clobbers": [
            "NTCache"
        ]
    },
    {
        "file": "js/lib/com.nantian.secret/www/NTSecret.js",
        "id": "com.nantian.secret",
        "clobbers": [
                     "NTSecret"
        ]
    },
    {
        "file": "js/lib/com.nantian.sqlite/www/NTSqlite.js",
        "id": "com.nantian.sqlite",
        "clobbers": [
                    "NTSqlite"
        ]
    },
    {
        "file": "js/lib/com.nantian.callbrowser/www/NTCallBrowser.js",
        "id": "com.nantian.callbrowser",
        "clobbers": [
                    "NTCallBrowser"
        ]
    },
    {
        "file": "js/lib/com.nantian.share/www/NTShare.js",
        "id": "com.nantian.share",
        "clobbers": [
            "NTShare"
        ]
    },
    {
        "file": "js/lib/com.nantian.ntMap/www/NTMap.js",
        "id": "com.nantian.ntMap",
        "clobbers": [
            "NTMap"
        ]
    },
	{
		"file": "js/lib/com.nantian.ntLocation/www/NTLocation.js",
		"id": "com.nantian.ntLocation",
		"clobbers": [
		             "NTLocation"
		             ]
	},
    {
        "file": "js/lib/com.nantian.contacts/www/NTContacts.js",
        "id": "com.nantian.contacts",
        "clobbers": [
            "NTContacts"
        ]
    },
    {
        "file": "js/lib/com.nantian.plugins/www/myphone.js",
        "id": "com.nantian.plugins.callphone",
        "clobbers": [
            "CallPhone"
        ]
    },
    {
        "file": "js/lib/com.xiyu.plugin/www/appupdate.js",
        "id": "com.xiyu.plugin.appupdate",
        "clobbers": [
            "AppUpdate"
        ]
    },
    {
        "file": "js/lib/com.xiyu.plugin/www/myprocess.js",
        "id": "com.xiyu.plugin.myprocess",
        "clobbers": [
            "MyProcess"
        ]
    },
    {
        "file": "js/lib/com.nantian.plugins/www/ImagePicker.js",
        "id": "com.nantian.plugins.ImagePicker",
        "clobbers": [
            "ImagePicker"
        ]
    },
    {
        "file": "js/lib/com.xiyu.plugin/www/JPushPlugin.js",
        "id": "com.xiyu.plugin.jpush",
        "clobbers": [
            "JPushPlugin"
        ]
    },
    {
        "file": "js/lib/com.nantian.plugins/www/baiduLocation.js",
        "id": "com.nantian.plugins.baidu",
        "clobbers": [
            "BaiduLocation"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "org.apache.cordova.camera": "0.3.4",
    "org.apache.cordova.console": "0.2.12",
    "org.apache.cordova.device": "0.2.13",
    "org.apache.cordova.dialogs": "0.2.11",
    "org.apache.cordova.file": "1.3.2",
    "org.apache.cordova.file-transfer": "0.4.8",
    "org.apache.cordova.geolocation": "0.3.11",
    "org.apache.cordova.globalization": "0.3.3",
    "org.apache.cordova.inappbrowser": "0.5.4",
    "org.apache.cordova.network-information": "0.2.14",
    "com.nantian.HttpRequest":"0.0.1",   
    "com.nantian.parameter":"0.0.1",
    "com.nantian.ui":"0.01",
    "com.nantian.calendar":"0.01",
    "com.nantian.camera":"0.0.1",
    "com.nantian.pickerview":"0.01",
	"com.nantian.keyboard":"0.0.1",
    "com.nantian.sqlite":"0.0.1",
    "com.nantian.plugins.callphone":"0.0.1",
    "com.xiyu.plugin.appupdate":"0.0.1",
    "com.xiyu.plugin.myprocess":"0.0.1",
    "com.nantian.plugins.ImagePicker":"0.0.1",
    "com.xiyu.plugin.jpush":"0.0.1",
    "com.nantian.plugins.baidu":"0.0.1",
	"com.nantian.callbrowser":"0.0.1"

}
// BOTTOM OF METADATA
});