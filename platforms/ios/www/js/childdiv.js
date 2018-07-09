/**
 * 
 */



function set_child(cptp,cpmc,cpnr){
	
	child_dev = '<table width="100%" border="0">'
				+'<tr><th width="30%" rowspan="2" scope="col"><img name="" src="'+cptp+'"  style="height:50px; width:50px;border-radius: 100%;" alt="" /></th>'
				+'<th width="55%" height="40" scope="col"><div  style="margin-left:10px;font-size:15px;absolute:bottom:0px;margin-top:10px;">'+cpmc+'</div></th>'
				+'<th width="15%" rowspan="2" scope="col"><img name="" src="img/show.png" style="margin-bottom:10px;" alt="" name="cpzs" width="10"  alt="" /></th>'
				+'</tr><tr><td height="40"><div style="margin-left:10px;font-size:13px;vertical-align:50%;margin-top:2px;width:200px;display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">'+cpnr+'</div></td></tr></table>';
}