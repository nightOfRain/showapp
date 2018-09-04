$(function() {
	$('body').append('<div id="newWin"  class="newwin" ><iframe id="mainFrame5" class="newshowframe"></iframe></div>');
});


/*
 * 打开新的页面
 * url 跳转地址
 * method 过渡方式   从右往左进入：slfade   由下至上进入:smask
 */
//var animating = 0;
function showNewFrame(url, method) {
	var acount = localStorage.getItem("acount");
	acount++;
	localStorage.setItem("acount", acount);
	var method2;
	if(method=="slfade") 
		method2="srfade";
	else if(method=="smask")
		method2="hmask";
	//pagearr是index变量，showNewFrame（）每次堆个页面
	top.pagearr.push({"win":window,"method":method2});
	var delay = 100;
	if ($("#mainFrame5").attr("src") != url) {
		$("#mainFrame5").attr("src", url);
	} else {
		delay = 0;
	}
	var shownewframeback=function(){
		$('#newWin')[0].removeEventListener("webkitAnimationEnd",shownewframeback, false);
		$('#newWin').css('opacity', '1');
		$('#newWin').css('top', '0px');
		$('#newWin').css('left', '0px');
		$('#newWin').removeClass(method);
		$(".container-fluid").hide();
	};
	setTimeout(function() {
		$('#newWin')[0].addEventListener("webkitAnimationEnd",shownewframeback, false);
		animating=1;
		$('#newWin').show();
		$('#newWin').addClass(method);
		var len=top.pagearr.length;
	}, delay);
	
}

/*
 * 返回页面
 * method 过渡方式   由上至下消失：hmask   从左往右消失:srfade
 */
function hideFrame(method) {
	var acount = localStorage.getItem("acount");
	acount--;
	localStorage.setItem("acount", acount);
	$(".container-fluid").show();
	var hideframeback=function(){
		$('#newWin')[0].removeEventListener("webkitAnimationEnd", hideframeback, false);
		$('#newWin').hide();
		$('#newWin').removeClass(method);
		$("#mainFrame5").attr("src", "about:blank");
	};
	$('#newWin')[0].addEventListener("webkitAnimationEnd", hideframeback, false);
	$('#newWin').addClass(method);
}

//清除默认事件
function cleanEvent(event) {
				if (typeof(event) === "undefined")
					return;
				event.preventDefault();
				event.stopPropagation();
				event.stopImmediatePropagation();
}