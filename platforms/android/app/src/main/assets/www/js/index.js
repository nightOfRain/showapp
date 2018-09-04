/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function() {
        this.receivedEvent('deviceready');
        document.addEventListener("backbutton", onBackKeyDown, false); //返回键
        document.addEventListener("resume", onResume, false); //监听返回前台事件
        document.addEventListener("showkeyboard", onKeyBoardShow, false); //软键盘弹出操作
        document.addEventListener("hidekeyboard", onKeyBoardHide, false); //软键盘缩回操作
    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
        console.log('Received Event: ' + id);
    }
};

function onResume(){
	console.log("onResume");
}
function onKeyBoardShow(bottom) {
    console.log("onKeyBoardShow");
}
function onKeyBoardHide(bottom) {
    console.log("onKeyBoardHide");
}
function onBackKeyDown() {
    console.log("onBackKeyDown");
    back_history();
}

function back_history(){
    var len=top.pagearr.length;
    if(len>0){
        top.pagearr[len-1]["win"].hideFrame(top.pagearr[len-1]["method"]);
        top.pagearr[len-1]["win"]=null;
        top.pagearr[len-1]["method"]=null;
        top.pagearr.pop();
    }
}
app.initialize();