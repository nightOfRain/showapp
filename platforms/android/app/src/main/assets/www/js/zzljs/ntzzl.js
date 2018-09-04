jQuery(function() {

	$(".takepic").each(function(index) {
		$(".takepic").click(function() {
			NTCamera.showPictureTaker(function(str) {
				// alert(str);
			}, null);
		});
	});
	
	bindMutipleChoice();
	clearAddrSelect();
	initMap();

});

function initMap(){
	var map = new BMap.Map("allmap"); // 创建Map实例
							var point = new BMap.Point(116.404, 39.915); // 创建点坐标
							map.centerAndZoom(point, 15); // 初始化地图,设置中心点坐标和地图级别。
							//var top_left_navigation = new BMap.NavigationControl(); 
							//map.addControl(top_left_navigation);  
							
							
	}

var addi = 0;
function clearAddrSelect(){
	//alert($(".CCA2_address"));
	$(".CCA2_address").each(function(){
		var sls=$(this).find("select");
		addi++;
		for(i=0;i<sls.length;i++){
			$(sls[i]).html('');
		}
		sls[0].id="prov" + addi;
		sls[1].id="city" + addi;
		sls[2].id="area" + addi;
		addressInit(sls[0].id,sls[1].id, sls[2].id);		
		
	});
}

//绑定省市区

//function bindCitys(event) {
//	addi++;
//	var obj = event.srcElement ? event.srcElement : event.target;
//	var $obj = $(obj);
//	//$obj.attr("isBind", "0");
//	if ($obj.attr("isBind") != "1") {
//		$obj.attr("isBind", "1");
//		var p = "prov" + addi;
//		var c = "city" + addi;
//		var a = "area" + addi;
//		$obj[0].id = p;
//		$obj.parent().parent().find("select")[1].id = c;
//		$obj.parent().parent().find("select")[2].id = a;
//		addressInit(p, c, a);
//	}
//
//}
//绑定多选一
function bindMutipleChoice() {

	$(".my_switch").each(function() {
		var switcLength = $(this).css("width").replace('px', '');
		// alert(switcLength);
		var count = $(this).parent().find("td").length;
		$(this).html($(this).parent().find("td").first().find("label").html());
		$(this).css("margin-left", switcLength * -count + "px");
	});

	$(".ntmutichoice td").each(function(index) {
		$(this).click(function() {
			var count = $(this).siblings().length + 1;
			// alert(count);
			var switcLength = $(this).css("width").replace('px', '');
			var my_switch = $(this).parent().parent().parent().parent().next();
			var i = $(this).attr("ind");
			my_switch.html($(this).find("label").html());
			for (j = 0; j < switcLength; j++) {
				res = switcLength * -count + (i) * j;
				my_switch.css("margin-left", res + "px");
			}

		});
	});
}

//修改导航栏样式
function ac(elem){
	var par=elem.parentNode;
	var ul=par.parentNode;
	for(var i=0;i<ul.childNodes.length;i++){
		if(ul.childNodes[i].className=="active"){
			ul.childNodes[i].className="";
		}
	}
	par.className="active";
}
