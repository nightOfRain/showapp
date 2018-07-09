jQuery(function(){
  /*颜色选择器*/
   jQuery('body').delegate('.myselect', 'change', function () {
    
	  var temp = jQuery(this).parents().eq(1).find('.ios7');
   
  
    if (jQuery(this).val()== '0') {
    	
	jQuery(temp).children().eq(0).prop('class','grayButton');
    	
    }
    
    else if(jQuery(this).val()== '1') {
   
   jQuery(temp).children().eq(0).prop('class','redButton');
    	
    }
    
    else if(jQuery(this).val() == '2') {
    	
    	 jQuery(temp).children().eq(0).prop('class','whiteButton');
    }
    
    else if(jQuery(this).val() == '3') {
    	
    	 jQuery(temp).children().eq(0).prop('class','blueButton');
    }
    
    else if(jQuery(this).val() == '4') {
    	 
    	 jQuery(temp).children().eq(0).prop('class','greenButton');
    };
 
   
   });
 
});
$(function(){
                function updateClocks(){
                    var localTime = new Date();
                    $('#clocks > div').each(function(){
                        var tz_offset = $(this).data('tz_offset') || 0;
                        var ms = localTime.getTime() 
                             + (localTime.getTimezoneOffset() )
                             + (tz_offset + 1) ;
                        var time = new Date(ms);
                        var hour = time.getHours();
                        var minute = time.getMinutes();
                        var second = time.getSeconds();
                        var $el = $(this);
                        var ampm = 'AM';
                        var nicehour = hour;
                        if (hour > 12 ) {
                            nicehour = hour - 12;
                            ampm = 'PM';
                        } else if ( hour == 0 ) {
                            nicehour = 12;
                        }
                        $('.hour', $el).css('-webkit-transform', 'rotate(' + ( hour * 30 + (minute/2) ) + 'deg)');
                        $('.min', $el).css('-webkit-transform', 'rotate(' + ( minute * 6 ) + 'deg)');
                        $('.sec', $el).css('-webkit-transform', 'rotate(' + ( second * 6 ) + 'deg)');
                        $('.time', this).html(nicehour + ':' + minute + ':' + second + ' ' + ampm);
                    });
                }
                $('#time').submit(function(){
                    addClock($('#label').val(), Number($('#timezone').val()));
                    $('input').blur();
                    $('#add .cancel').click();
                    this.reset();
                    jqt.goBack();
                    return false;
                });
                
                updateClocks();
                setInterval(updateClocks, 1000);
            });