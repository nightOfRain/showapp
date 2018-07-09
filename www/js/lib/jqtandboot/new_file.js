

jQuery(function(){

   jQuery('body').delegate('.boor2select', 'change', function () {
    
    var temp = $('.button-glow')

    if (jQuery(this).val()== '0') {

	jQuery(temp).prop('class','button button-glow button-rounded button-raised button-primary');
		
    }
    
    else if(jQuery(this).val()== '1') {
    	
   
	jQuery(temp).prop('class','button button-glow button-border button-rounded button-primary');
    
    }
    
    else if(jQuery(this).val() == '2') {
	jQuery(temp).prop('class','button button-glow button-rounded button-highlight');
    }
    
    else if(jQuery(this).val() == '3') {
	jQuery(temp).prop('class','button button-glow button-rounded button-caution');
    }
     else if(jQuery('.boor2select').val() == '4') {
	jQuery(temp).prop('class','button button-glow button-rounded button-royal');
    }
     
  
   });
 });
 

		jQuery(function(){
$("[message='tips']").click(function(){
	Messenger.options = {
extraClasses: 'messenger-fixed messenger-on-bottom messenger-on-right',
theme: 'ice'
};
var msg;
msg = Messenger().post({
  message: "I'm sorry Hal, I just can't do that.",
  actions: {
    retry: {
      label: 'retry now',
      phrase: 'Retrying TIME',
      auto: true,
      delay: 5,
      action: function() {}
    },
    cancel: {
      action: function() {
        return msg.cancel();
      }
    }
  }
 });
});	
});
