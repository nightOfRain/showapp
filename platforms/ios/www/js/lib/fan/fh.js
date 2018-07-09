jQuery(function() {
	/*日期选择、业绩时间范围选择控件*/

	/*日期选择*/
	//  jQuery('body').delegate('.fhdate', 'focus', function () {
	//       jQuery(this).mobiscroll().date({
	//      theme: 'ios7',
	//      accent: ' ',
	//      lang: 'zh',
	//      monthText: "月",
	//      dayText: "日",
	//      yearText: "年",
	//      display: 'modal',
	//      dateOrder: 'yymmdd',
	//      dateFormat: 'yy-mm-dd',
	//      endYear: (new Date()).getFullYear() + 10,
	//      mode: 'scroller',
	//      onSelect: function () {}
	//    });
	//  })


	/*业绩日期范围选择*/
	//  jQuery('.drstart').mobiscroll().date({
	//    theme: 'ios7',
	//    accent: ' ',
	//    lang: 'zh',
	//    display: 'inline',
	//    dateOrder: 'yymdd',
	//    dateFormat: 'yy-m-dd',
	//    mode: 'scroller',
	//    startYear: (new Date()).getFullYear() - 10, //开始年份
	//    endYear: (new Date()).getFullYear(), //结束年份
	//    onSelect: function () {}
	//  });
	//  jQuery('.drend').mobiscroll().date({
	//    theme: 'ios7',
	//    accent: ' ',
	//    lang: 'zh',
	//    display: 'inline',
	//    dateOrder: 'yymmdd',
	//    dateFormat: 'yy-mm-dd',
	//    mode: 'scroller',
	//    startYear: (new Date()).getFullYear(), //开始年份
	//    endYear: (new Date()).getFullYear() + 10, //结束年份
	//    onSelect: function () {}
	//  });

	init();
});

function init() {
try{

$("[ntctype='fhdate']").mobiscroll().date({
		theme: 'ios7',
		accent: ' ',
		lang: 'zh',
		monthText: "月",
		dayText: "日",
		yearText: "年",
		display: 'modal',
		dateOrder: 'yymmdd',
		dateFormat: 'yy-mm-dd',
		endYear: (new Date()).getFullYear() + 10,
		mode: 'scroller',
		onSelect: function() {}
	});
}catch(e){
}

	


	/*日期选择*/
	if (jQuery('.fhdate').length > 0) {
		jQuery('.fhdate').each(function() {
			jQuery(this).removeAttr("id");
		});
		//    for(var i=0;i<jQuery('.fhdate').length;i++){
		//      var temp=jQuery('.fhdate').eq(i);
		////    alert(jQuery('.fhdate').eq(i).attr('id'));
		//    }
		//    jQuery('.fhdate').each(function(){
		jQuery('.fhdate').mobiscroll().date({
			theme: 'ios7',
			accent: ' ',
			lang: 'zh',
			monthText: "月",
			dayText: "日",
			yearText: "年",
			display: 'modal',
			dateOrder: 'yymmdd',
			dateFormat: 'yy-mm-dd',
			endYear: (new Date()).getFullYear() + 10,
			mode: 'scroller',
			onSelect: function() {}
		});
		//    });
	};
	/*日历*/
	if (jQuery('.datefield').length > 0) {
		jQuery('.datefield').each(function() {
			jQuery(".datefield").datepicker({
				//    onSelect: function (dateText) {},
				//    minDate: 0,
			});
		})
	};
	/*业绩日期范围*/
	if (jQuery('.drstart').length > 0) {
		/*业绩日期范围选择*/
		jQuery('.drstart').mobiscroll().date({
			theme: 'ios7',
			accent: ' ',
			lang: 'zh',
			display: 'inline',
			dateOrder: 'yymdd',
			dateFormat: 'yy-m-dd',
			mode: 'scroller',
			startYear: (new Date()).getFullYear() - 10, //开始年份
			endYear: (new Date()).getFullYear(), //结束年份
			onSelect: function() {}
		});
		jQuery('.drend').mobiscroll().date({
			theme: 'ios7',
			accent: ' ',
			lang: 'zh',
			display: 'inline',
			dateOrder: 'yymmdd',
			dateFormat: 'yy-mm-dd',
			mode: 'scroller',
			startYear: (new Date()).getFullYear(), //开始年份
			endYear: (new Date()).getFullYear() + 10, //结束年份
			onSelect: function() {}
		});
	};
	/*柱形图*/
	if (jQuery('.barframe').length > 0) {
		initbar();
	};
	/*饼图*/
	if (jQuery('.pieframe').length > 0) {
		initpie();
	};
	/*曲线图*/
	if (jQuery('.lineframe').length > 0) {
		initline();
	};
	/*曲线图*/
	if (jQuery('.radarframe').length > 0) {
		initradar();
	};

}

function initbar() {
	var temp = new Barset();
	createBarChart(temp);
}

function initpie() {
	var temp = new Pieset();
	createPieChart(temp);
}

function initline() {
	var temp = new Lineset();
	createLineChart(temp);
}

function initradar() {
		var temp = new Radarset();
		createRadarChart(temp);
	}
	/*柱状图*/

function Barset() {
	var obj = {};
	obj.labels = ["January", "February", "March", "April", "May"];
	obj.datasets = [];
	var tem = {};
	tem.fillColor = "#fff";
	tem.strokeColor = "rgba(200,220,220,0.8)";
	tem.highlightFill = "#fff";
	tem.highlightStroke = "rgba(255,255,255,1)";
	tem.data = [100, 10, 80, 81, 56];
	obj.datasets.push(tem);
	return obj;
}

function createBarChart(bardata) {
	jQuery('.barframe').each(function() {
		var str = '<canvas ntType="bar"></canvas>';
		jQuery(this).append(str);
		var bar = jQuery(this).find('[ntType=bar]');
		var ct = jQuery(bar)[0];
		var ctx = ct.getContext("2d");
		myBarChart = new Chart(ctx).Bar(bardata, {
			responsive: true
		});
		setBarColor(myBarChart);
	})
}

function setBarColor(myBarChart) {
		var colorArray1 = ['rgba(255,102,153,1)', 'rgba(204,102,204,1)', 'rgba(153,204,0,1)', 'rgba(255,204,0,1)', 'rgba(0,153,204,1)', 'rgba(0,204,163,1)'];
		var colorArray2 = ['rgba(255,102,153,.8)', 'rgba(204,102,204,.8)', 'rgba(153,204,0,.8)', 'rgba(255,204,0,.8)', 'rgba(0,153,204,.8)', 'rgba(0,204,163,.8)'];
		var strokeArray1 = ['rgba(255,0,102,1)', 'rgba(153,0,153,1)', 'rgba(102,153,0,1)', 'rgba(204,153,0,1)', 'rgba(0,55,102,1)', 'rgba(0,101,81,1)'];
		var strokeArray1 = ['rgba(255,0,102,.8)', 'rgba(153,0,153,.8)', 'rgba(102,153,0,.8)', 'rgba(204,153,0,.8)', 'rgba(0,55,102,.8)', 'rgba(0,101,81,.8)'];
		var bar = myBarChart.datasets[0].bars;
		for (var i = 0; i < bar.length; i++) {
			bar[i].fillColor = colorArray1[i > 9 ? i % 9 : i];
			bar[i].highlightFill = colorArray2[i > 9 ? i % 9 : i];
			bar[i].strokeColor = colorArray1[i > 9 ? i % 9 : i];
			bar[i].highlightStroke = colorArray2[i > 9 ? i % 9 : i];
		}
		myBarChart.update();
	}
	/*饼图*/

function Pieset() {
	var pie = [{
		value: 30,
		color: "#F38630"
	}, {
		value: 50,
		color: "#E0E4CC"
	}, {
		value: 100,
		color: "#69D2E7"
	}];
	return pie;
}

function createPieChart(piedata) {
		jQuery('.pieframe').each(function() {
			var str = '<canvas ntType="pie"></canvas>';
			jQuery(this).append(str);
			var pie = jQuery(this).find('[ntType=pie]');
			var ct = jQuery(pie)[0];
			var ctx = ct.getContext("2d");
			myPieChart = new Chart(ctx).Pie(piedata, {
				responsive: true
			});
		})
	}
	/*曲线图*/

function Lineset() {
	var data = {
		labels: ["January", "February", "March", "April", "May", "June", "July"],
		datasets: [{
			fillColor: "rgba(220,220,220,0.5)",
			strokeColor: "rgba(220,220,220,1)",
			pointColor: "rgba(220,220,220,1)",
			pointStrokeColor: "#fff",
			data: [65, 59, 90, 81, 56, 55, 40]
		}, {
			fillColor: "rgba(151,187,205,0.5)",
			strokeColor: "rgba(151,187,205,1)",
			pointColor: "rgba(151,187,205,1)",
			pointStrokeColor: "#fff",
			data: [28, 48, 40, 19, 96, 27, 100]
		}]
	};
	return data;
}

function createLineChart(linedata) {
		jQuery('.lineframe').each(function() {
			var str = '<canvas ntType="line"></canvas>';
			jQuery(this).append(str);
			var line = jQuery(this).find('[ntType=line]');
			var ct = jQuery(line)[0];
			var ctx = ct.getContext("2d");
			myLineChart = new Chart(ctx).Line(linedata, {
				responsive: true
			});
		})
	}
	/*雷达图*/

function Radarset() {
	var data = {
		labels: ["Eating", "Drinking", "Sleeping", "Designing", "Coding", "Partying", "Running"],
		datasets: [{
			fillColor: "rgba(220,220,220,0.5)",
			strokeColor: "rgba(220,220,220,1)",
			pointColor: "rgba(220,220,220,1)",
			pointStrokeColor: "#fff",
			data: [65, 59, 90, 81, 56, 55, 40]
		}, {
			fillColor: "rgba(151,187,205,0.5)",
			strokeColor: "rgba(151,187,205,1)",
			pointColor: "rgba(151,187,205,1)",
			pointStrokeColor: "#fff",
			data: [28, 48, 40, 19, 96, 27, 100]
		}]
	};
	return data;
}

function createRadarChart(radardata) {
	jQuery('.radarframe').each(function() {
		var str = '<canvas ntType="radar"></canvas>';
		jQuery(this).append(str);
		var radar = jQuery(this).find('[ntType=radar]');
		var ct = jQuery(radar)[0];
		var ctx = ct.getContext("2d");
		myRadarChart = new Chart(ctx).Radar(radardata, {
			responsive: true
		});
	})
}