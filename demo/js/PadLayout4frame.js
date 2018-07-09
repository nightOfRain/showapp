var option = {
  "listbox": [
    {
      "name": "",
      "url": "",
      "icon": "img/pad4/pad4_pic1.png",
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad4/pad4_pic2.png",
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad4/pad4_pic3.png",
      
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad4/pad4_pic4.png",
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad4/pad4_pic5.png",
    
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad4/pad4_pic6.png",
    
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad4/pad4_pic7.png",
    
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad4/pad4_pic8.png",
    
        },
    {
      "name": "",
      "url": "",
      "icon": "img/pad4/pad4_pic9.png",
    
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
      var li = document.createElement("li");
    li.innerHTML = menuData[0].name;
    li.menuUrl = menuData[0].url;
    li.setAttribute("class", "sigleItem");
    li.menuIndex = 0;
    menuUl.appendChild(li);
    var img = document.createElement("img");
    img.setAttribute("class", "icon");
    img.setAttribute("src", menuData[0].icon);
    li.setAttribute("class", "listbox0");
    li.appendChild(img);
    if (menuData[0].url != undefined && menuData[0].url != null && menuData[0].url != "") {
      $(li).bind("tap", function () {
        document.getElementById("contentFrame").src = this.menuUrl;
      });
    }
  li=null;
  for (var i = 1; i < menuData.length; i++) {
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
        document.getElementById("contentFrame").src = this.menuUrl;
      });
    }
  }
}