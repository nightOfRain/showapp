var myScroll,
	pullDownEl, pullDownOffset,
	pullUpEl, pullUpOffset,
	generatedCount = 0;
var pullrowheight = pullrowheight!=0 && pullrowheight != undefined?pullrowheight:50;	
var pullUpabled = true;
var rowlimit = 10;

/**
 * 下拉刷新 （自定义实现此方法）
 * myScroll.refresh();		// 数据加载完成后，调用界面更新方法
 */
function pullDownAction () {
	setTimeout(function () {	// <-- Simulate network congestion, remove setTimeout from production!
		var el, li, i;
		el = document.getElementById('thelist');

		for (i=0; i<3; i++) {
			li = document.createElement('li');
			li.innerText = 'Generated row ' + (++generatedCount);
			el.insertBefore(li, el.childNodes[0]);
		}
		myScroll.refresh();		//数据加载完成后，调用界面更新方法   Remember to refresh when contents are loaded (ie: on ajax completion)
	}, 1000);	// <-- Simulate network congestion, remove setTimeout from production!
}

/**
 * 滚动翻页 （自定义实现此方法）
 * myScroll.refresh();		// 数据加载完成后，调用界面更新方法
 */
function pullUpAction () {
	setTimeout(function () {	// <-- Simulate network congestion, remove setTimeout from production!
		var el, li, i;
		el = document.getElementById('thelist');

		for (i=0; i<3; i++) {
			li = document.createElement('li');
			li.innerText = 'Generated row ' + (++generatedCount);
			el.appendChild(li, el.childNodes[0]);
		}
//		pullUpabled = false;
		myScroll.refresh();		// 数据加载完成后，调用界面更新方法 Remember to refresh when contents are loaded (ie: on ajax completion)
	}, 1000);	// <-- Simulate network congestion, remove setTimeout from production!
}

/**
 * 初始化iScroll控件
 */
function scrollTableInitial() {
//	pullDownEl = document.getElementById('pullDown');
	pullDownEl = document.getElementsByName("pullDown")[0];
	pullDownOffset = pullDownEl.offsetHeight;
//	pullUpEl = document.getElementById('pullUp');	
	pullUpEl = document.getElementsByName("pullUp")[0];	
	pullUpOffset = pullUpEl.offsetHeight;
	
	var offsetPoint = [];
	var offsetFlag = "judgement";
	
	myScroll = new iScroll('wrapper', {
		scrollbarClass: 'myScrollbar', /* 重要样式 */
		useTransition: false, /* 此属性不知用意，本人从true改为false */
		topOffset: pullDownOffset,
		onRefresh: function () {
			if (pullDownEl.className.match('loading')) {
				pullDownEl.className = '';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = pullUpabled?'上拉加载更多...':'已全部显示';
			} else if (pullUpEl.className.match('loading')) {
				pullUpEl.className = '';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = pullUpabled?'上拉加载更多...':'已全部显示';
				pullUpEl.getElementsByClassName("pullUpIcon")[0].style.display = pullUpabled?'':'none';
			}
		},
		onScrollMove: function () {
			offsetPoint[offsetPoint.length] = this.y;
			if(Math.abs(offsetPoint[0] - offsetPoint[offsetPoint.length-1]) > pullrowheight){
				var trace = 0 ;
				for(var i = 0 ; i < offsetPoint.length;i++){
					trace += offsetPoint[i];
				}
				if(offsetPoint[offsetPoint.length-1] > 0){
					//下拉
					pullDownEl.className = 'flip';
					pullDownEl.querySelector('.pullDownLabel').innerHTML = '松手开始更新...';
					this.minScrollY = 0;
					offsetPoint = [];
				}else if(offsetPoint[0]>trace && this.y < this.maxScrollY - pullrowheight){
					if(pullUpabled && pullDownEl.className != 'flip'){
						pullUpEl.className = 'flip';
						pullUpEl.querySelector('.pullUpLabel').innerHTML = '松手开始更新...';
						this.maxScrollY = this.maxScrollY;
						offsetPoint = [];
					}
				}
			}
		},
		onScrollEnd: function () {
			if (pullDownEl.className.match('flip')) {
				pullDownEl.className = 'loading';
				pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中...';				
				pullDownAction();	// Execute custom function (ajax call?)
			} else if (pullUpEl.className.match('flip')) {
				pullUpEl.className = 'loading';
				pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中...';				
				pullUpAction();	// Execute custom function (ajax call?)
			}
		}
	});
}

//判断是否还能继续加载
function hasMore(obj){
	if(obj.length < 10){
		pullUpabled = false;
	}else{
		pullUpabled = true;
	}
}


function resetLock(){
	pullUpabled = true;
}

//初始化绑定iScroll控件 
document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
document.addEventListener('DOMContentLoaded', scrollTableInitial, false); 