//手机号码验证
function checkTelephone(obj){  
    var reg= /^[1][358]\d{9}$/; 
    var phone=jQuery('#telephone').val();  
    if(!reg.test(phone)){  
        alert("电话号码格式错误!");  
        return false;  
    }else{ 
        return true;  
    }  
} 

//身份证号码验证
function isCardNo(card)  
{  
   // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
   var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
   var card=jQuery("#idcard").val();
   if(!reg.test(card))  
   {  
       alert("身份证输入不合法");  
       return  false;  
   }else{
       return true;   
   } 
} 

