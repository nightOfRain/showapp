

document.addEventListener("DOMContentLoaded",initial);

var option = {
    "menu": [
        {
            "name": "首页",
            "url": "",
            "icon": "img/home.png",
            "subItem": [
                {
                    "name": "首页子项1",
                    "url": ""
                },
                {
                    "name": "首页子项2",
                    "url": ""
                },
                {
                    "name": "首页子项3",
                    "url": ""
                }
            ]
        },
        {
            "name": "功能",
            "url": "",
            "icon": "img/trading.png",
            "subItem": []
        },
        {
            "name": "功能",
            "url": "",
            "icon": "img/trading.png",
            "subItem": []
        },
        {
            "name": "功能",
            "url": "",
            "icon": "img/trading.png",
            "subItem": []
        },
        {
            "name": "功能",
            "url": "",
            "icon": "img/trading.png",
            "subItem": []
        },
        {
            "name": "功能",
            "url": "",
            "icon": "img/trading.png",
            "subItem": [
                {
                    "name": "首页子项4",
                    "url": ""
                },
                {
                    "name": "首页子项5",
                    "url": ""
                }
            ]
        }
    ]
};

function initial(){
	document.getElementsByClassName("ui-loader")[0].remove();
	drawMenu("XXX","XXX_SUB","contentFrame",option);
}

/**
 * 
 * @param {Object} MenuContainer 1级菜单容器ID
 * @param {Object} subMenuContainer 2级菜单容器ID
 * @param {Object} contentFrame 正文iframe的ID
 * @param {Object} option 配置项
 */
function drawMenu(MenuContainer,subMenuContainer,contentFrame,option){
	var MenuContainer= document.getElementById(MenuContainer);
	MenuContainer.style.width = 60+"px";
	var subMenuContainer = document.getElementById(subMenuContainer);
	subMenuContainer.style.width = 232+"px";
//	var parentHeight = MenuContainer.offsetHeight;
	var parentHeight =  document.body.scrollHeight;
	
	//构造父容器
	var menuBar1 = document.createElement("div");
	menuBar1.setAttribute("class","menuBar1");
	menuBar1.style.height = parentHeight - 20 +"px";
	var menuBar2 = document.createElement("ul");	
	menuBar2.setAttribute("class","menuBar2");
	menuBar2.style.height = parentHeight +"px";
	
	
	//构造一级菜单菜单项
	for(var i = 0 ; i < option.menu.length ; i++){
		//构造菜单项
		var itemOption = option.menu[i];
		var item = document.createElement("div");
		item.setAttribute("class","item");
		item.setAttribute("menuIndex",i);
		var img = document.createElement("img");
		img.src = itemOption.icon;
		img.style.height = 27+"px";
		img.style.width = 30+"px"
		var label = document.createElement("div");
		label.innerHTML = itemOption.name;
		
		//绑定菜单事件
		if(itemOption.subItem.length > 0){
			$(item).bind("tap",function(){
				//存在二级菜单,渲染二级菜单
				drawSubItem(menuBar2,this,contentFrame,option);
				subMenuContainer.style.display = '';
			});
		}else{
			$(item).bind("tap",function(){
				//不存在二级菜单，直接跳转
				subMenuContainer.style.display = 'none';
				document.getElementById("contentFrame").src = option.menu[this.getAttribute("menuIndex")].url;
			})
		}
		
		
		item.appendChild(img);
		item.appendChild(label);
		menuBar1.appendChild(item);
	}
	
	MenuContainer.appendChild(menuBar1);
	subMenuContainer.appendChild(menuBar2);
	
//	alert($(MenuContainer.childNodes[0]));
//	$(MenuContainer.childNodes[0]).trigger("tap");
}

function drawSubItem(container,scrEl,contentFrame,option){
	container.innerHTML="";
	var subItem = option.menu[scrEl.getAttribute("menuIndex")].subItem;
	for(var i = 0 ; i < subItem.length ; i++){
		var li = document.createElement("li");
		li.setAttribute("subMenuIndex",i);
		li.setAttribute("menuIndex",scrEl.getAttribute("menuIndex"));
		var label = document.createElement("div");
		label.setAttribute("class","label");
		var arrow = document.createElement("div");
		arrow.setAttribute("class","arrow");
		label.innerHTML = subItem[i].name;
		
		var line = document.createElement("div");
		line.setAttribute("class","line");
		
		$(li).bind("tap",function(){
			changePage(this,contentFrame,option);
		});
		
		li.appendChild(label);
		li.appendChild(arrow);
		container.appendChild(li);
		container.appendChild(line);
	}
}


function changePage(srcEl,contentFrame,option){
	var menuIndex = srcEl.getAttribute("menuIndex");
	var subMenuIndex = srcEl.getAttribute("subMenuIndex");
	var url = option.menu[menuIndex].subItem[subMenuIndex].url;
	document.getElementById("contentFrame").src = url;
}
