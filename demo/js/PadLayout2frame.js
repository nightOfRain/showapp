var option = {
  "listbox": [
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic1.png",
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic2.png",
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic3.png",
      
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic4.png",
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic5.png",
    
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic6.png",
    
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic7.png",
    
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic8.png",
    
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic9.png",
    
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic10.png",
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic11.png",
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad2/pad2_pic12.png",
        }
    ]
};
document.addEventListener("DOMContentLoaded", initial);
var myscroll;

function initial() {
generateMenu(option);
  myScroll = new iScroll('menuWrapper', {
    vScrollbar: false,
    hScrollbar: false
  });
}

function generateMenu(option) {
  var wrapper = document.getElementById("menuWrapper");
  var menuUl = wrapper.getElementsByTagName("ul")[0];
  menuUl.innerHTML = "";
  menuUl.setAttribute("class", "MenuUl");
  var menuData = option.listbox;
  for (var i = 0; i < menuData.length; i++) {
    var li = document.createElement("li");
    li.innerHTML = menuData[i].name;
    li.menuUrl = menuData[i].url;
    li.setAttribute("class", "sigleItem");
    li.menuIndex = i;
    menuUl.appendChild(li);
    var img = document.createElement("img");
    img.setAttribute("class", "icon");
    img.setAttribute("src", menuData[i].icon);
    li.setAttribute("class", "listbox");
    li.appendChild(img);
    if (menuData[i].url != undefined && menuData[i].url != null && menuData[i].url != "") {
      //改菜单跳转地址不为空,不存在二级菜单
      $(li).bind("tap", function () {
        //				if (this.menuUrl != undefined && this.menuUrl != null && this.menuUrl != "") {
        //					//仅有一级菜单,直接跳转
        //					alert("aaa");
        //					document.getElementById("contentFrame").src = this.menuUrl;
        //				}else{
        //					//展示二级菜单
        //					alert();
        //					var menuLi = menuUl.getElementsByTagName("li");
        //					for(var j = 0 ; j < menuLi.length ; j++){
        //						if(menuLi[j].parentIndex == this.menuIndex){
        //							alert("aaa");
        //							menuLi[j].style.display = '';
        //						}
        //					}
        //				}
        document.getElementById("contentFrame").src = this.menuUrl;
      });
    }
  }
}