document.addEventListener("DOMContentLoaded", initial);
//设置上、中、下比例
var headerRate = 0,
	midRate = 0.92,
	footerRate = 0.08;
//主页初始化
function initial() {
	try{
	$('.container-fluid').prepend('<div class="top11" style="display:none;"></div>');
	var contentHeight = document.documentElement.clientHeight;
	//	var y = document.body.scrollHeight - Math.floor(contentHeight*0.35) - Math.floor(contentHeight*0.55) - Math.floor(contentHeight*0.1);
	var top = document.getElementsByClassName("top11")[0];
	top.style.height = contentHeight * headerRate + 'px';
	var bottomNav = document.getElementsByClassName("bottomNav")[0];
	bottomNav.style.height = contentHeight * footerRate + 'px';
	bottomNav_H=contentHeight * footerRate;
	var bottom = document.getElementsByClassName("bottom")[0];
//	bottom.style.height = contentHeight * midRate - 2 + 'px';
	bottom.style.height = contentHeight * midRate +  'px';
	
	jQuery(".bottomNav li").each(function() {
		this.getElementsByTagName("a")[0].style.height = contentHeight * footerRate + 'px'; //调整navBar按钮高度
		this.getElementsByTagName("a")[0].style.lineHeight = contentHeight * footerRate + 'px'; //调整navBar按钮高度
		jQuery(this).bind("click", function() {
			var flag = $(this).find('a').attr('ind');
			

			
			jQuery(".bottomNav li a").removeClass("navbar-btn-active");
			jQuery(".bottomNav li ").attr("style","background-color:#ffffff;");
			$(this).find('a').addClass("navbar-btn-active");
			changePage($(this).find('a').attr('ind'));

		});
	});
	}catch(e){
		
	}

//	var iscrollObj = iscrollAssist.newVerScrollForPull($('#sc'), pulldownAction, pullupAction);
//	iscrollObj.refresh();
}

//底部导航切换页面
function changePage(index) {
	$(".showframe").hide();

	$("#mainFrame"+index)[0].contentWindow.initPage();
	$("#mainFrame" + index).show();

}


var pulldownAction = function() {
	this.refresh();
	console.log("下拉执行逻辑");
};
var pullupAction = function() {
	this.refresh();
	console.log("上拉执行逻辑");
};