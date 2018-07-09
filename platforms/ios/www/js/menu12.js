var option = {
  "menu": [
    {
      "name": "首页",
      "url": "content1.html",
      "icon": "img/iphone4/phone4_pic1.png",
        },
    {
      "name": "功能1",
      "url": "content2.html",
      "icon": "img/iphone4/phone4_pic2.png",
        },
    {
      "name": "功能2",
      "url": "content3.html",
      "icon": "img/iphone4/phone4_pic3.png",
        },
    {
      "name": "功能3",
      "url": "content4.html",
      "icon": "img/iphone4/phone4_pic4.png",
        },
    {
      "name": "功能4",
      "url": "content5.html",
      "icon": "img/iphone4/phone4_pic5.png",
        },
    {
      "name": "功能5",
      "url": "content6.html",
      "icon": "img/iphone4/phone4_pic6.png",
        },
    {
      "name": "功能6",
      "url": "content7.html",
      "icon": "img/iphone4/phone4_pic7.png",
        },
    {
      "name": "功能7",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic8.png",
        },
    {
      "name": "功能8",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic10.png",
        },
    {
      "name": "功能9",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic9.png",
        },
    {
      "name": "功能10",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic9.png",
        },
    {
      "name": "功能11",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic9.png",
        },
    {
      "name": "功能12",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic9.png",
        },
    {
      "name": "功能13",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic9.png",
        },
    {
      "name": "功能14",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic9.png",
        },
    {
      "name": "功能15",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic9.png",
        },
    {
      "name": "功能16",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic9.png",
        },
    {
      "name": "功能17",
      "url": "content8.html",
      "icon": "img/iphone4/phone4_pic9.png",
        },
    {
      "name": "功能18",
      "url": "content9.html",
      "icon": "img/iphone4/phone4_pic9.png",
        }
    ]
};
document.addEventListener("DOMContentLoaded", initial);
var myscroll;

function initial() {
  var contentHeight = document.body.scrollHeight;
  var flowContainer = document.getElementsByClassName("flowContainer")[0];
  flowContainer.style.height = contentHeight * 0.9 + 'px';
  flowContainer.style.marginTop = contentHeight * 0.05 + 'px';
  generateMenu();
  myScroll = new iScroll('menuWrapper', {
    vScrollbar: false,
    hScrollbar: false
  });
}

function generateMenu() {
  var menuData = option.menu;
  var left = document.getElementsByClassName("left")[0];
  var mid = document.getElementsByClassName("mid")[0];
  var right = document.getElementsByClassName("right")[0];
  for (var i = 0; i < menuData.length; i++) {
    var div = document.createElement("div");
    div.setAttribute("class", "block" + (Math.ceil(Math.random() * 10) % 3 + 1));
    var img = document.createElement("img");
    img.setAttribute("class", "phone4-icon");
    img.setAttribute("src", menuData[i].icon);
    
    var container = document.createElement("div");
    
    
    var disc=document.createElement("span");
    disc.setAttribute("class", "phone4-disc");
    disc.innerHTML = menuData[i].name;
    container.appendChild(disc);
    container.appendChild(img);
    div.appendChild(container);
    
    if ((i + 1) % 3 == 1) {
      left.appendChild(div);
    }
    if ((i + 1) % 3 == 2) {
      mid.appendChild(div);
    }
    if ((i + 1) % 3 == 0) {
      right.appendChild(div);
    }
  }
}