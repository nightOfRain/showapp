cordova.define("com.nantian.file", function(require, exports, module) {
	/*
	 *
	 * Licensed to the Apache Software Foundation (ASF) under one
	 * or more contributor license agreements.  See the NOTICE file
	 * distributed with this work for additional information
	 * regarding copyright ownership.  The ASF licenses this file
	 * to you under the Apache License, Version 2.0 (the
	 * "License"); you may not use this file except in compliance
	 * with the License.  You may obtain a copy of the License at
	 *
	 *   http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing,
	 * software distributed under the License is distributed on an
	 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
	 * KIND, either express or implied.  See the License for the
	 * specific language governing permissions and limitations
	 * under the License.
	 *
	 */
	var exec = require('cordova/exec');

	var demoExport = {};
	demoExport.fileUpload = function(successCallback, errorCallback, filePathStr, url, isDataBox) {
		exec(successCallback, errorCallback, "NTFile", "fileUpload", [filePathStr, url, isDataBox]);
	};

	/**
	 * 查询
	 * 0表示上传中，1表示上传失败，2表示未上传，3表示文件不存在
	 */
	demoExport.findFileInfo = function(successCallback, errorCallback) {
		exec(successCallback, errorCallback, "NTFile", "findFileInfo", []);
	};
	
	/**
	 * 删除文件
	 * filePathStr：要删除的文件路径集合（jsonArray格式的字符串）
	 */
	demoExport.deleteFile = function(successCallback, errorCallback, filePathStr) {
		exec(successCallback, errorCallback, "NTFile", "deleteFile", [filePathStr]);
	};

	module.exports = demoExport;

});