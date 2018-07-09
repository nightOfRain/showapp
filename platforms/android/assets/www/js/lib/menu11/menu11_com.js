$(function() {
	if(!$("#newWin")[0])$('body').append('<div id="newWin"  class="newwin" ><iframe id="mainFrame5" class="newshowframe"></iframe></div>');
});

var animating=0;
/*
 * 打开新的页面
 * url 跳转地址
 * method 过渡方式   从右往左进入：slfade   由下至上进入:smask
 */
function showNewFrame(url, method) {
	if(animating==0){
		var delay = 400;
		if ($("#mainFrame5").attr("src") != url) {
			$("#mainFrame5").attr("src", url);
		} else {
			delay = 0;
		}
		var showNewFrameBack=function(){
			$('#newWin')[0].removeEventListener("webkitAnimationEnd",showNewFrameBack, false);
			$('#newWin').css('opacity', '1').css('top', '0px').css('left', '0px').removeClass(method);
			$(".container-fluid").hide();
			animating=0;
		};
		setTimeout(function() {
			$('#newWin')[0].addEventListener("webkitAnimationEnd",showNewFrameBack, false);
			$('#newWin').show();
			animating=1;
			$('#newWin').addClass(method);
		}, delay);
	}
}

/*
 * 返回页面
 * method 过渡方式   由上至下消失：hmask   从左往右消失:srfade
 */
function hideFrame(method) {
	if(animating==0){
		var hideFrameBack=function(){
			$('#newWin')[0].removeEventListener("webkitAnimationEnd", hideFrameBack, false);
			$('#newWin').hide();
			$('#newWin').removeClass(method);
			animating=0;
		};
		$(".container-fluid").show();
		$('#newWin')[0].addEventListener("webkitAnimationEnd", hideFrameBack, false);
		$('#newWin').addClass(method);
		animating=1;
	}
}

//清除默认事件
function cleanEvent(event) {
				if (typeof(event) === "undefined")
					return;
				event.preventDefault();
				event.stopPropagation();
				event.stopImmediatePropagation();
}