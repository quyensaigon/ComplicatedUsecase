<html>
<head>
<title>Profile</title>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.10/css/jquery.dataTables.min.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<LINK REL="stylesheet" HREF="style/main.css" TYPE="text/css" MEDIA="SCREEN">
<script type = "text/javascript"  src = "jquery/jquery-1.12.0.min.js"></script>
<script type = "text/javascript"  src = "js/util.js"></script>
<script type = "text/javascript" src = "https://cdn.datatables.net/1.10.10/js/jquery.dataTables.min.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<style>
table {
        
    font-size: 12px;
    font-family: Arial;
    
}
table.border, th, td.border {
    border: 1px solid black;
    border-collapse: collapse;
    padding: 5px;
}


</style>

</head>
<body topmargin="0" leftmargin="0" marginHeight="0" marginWidth="0">
<script>
alert("hello");
var username = '<%= request.getParameter("username") %>';
var token = '<%= request.getParameter("token") %>';
alert(username);
alert(token);
(function($)
{
    var oldHtml = $.fn.html;
    $.fn.html = function()
    {
        var ret = oldHtml.apply(this, arguments);

        //trigger your event.
        this.trigger("change");

        return ret;
    };
})(jQuery);
</script>

<div id="maincontent">
Hello
</div>
<script>
/*
function showProfile()
{
   alert("show profile");
   //setup_info = document.getElementById("lst_setup_info").value;
   //alert("Setup Info is " + setup_info);
   if (window.XMLHttpRequest) {                                                                   
    // code for IE7+, Firefox, Chrome, Opera, Safari                                             
    xmlhttp=new XMLHttpRequest();                                                                
  } else { // code for IE6, IE5                                                                  
    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");                                              
  }                                                                                              
  xmlhttp.onreadystatechange=function() {                                                        
    if (xmlhttp.readyState==4 && xmlhttp.status==200) { 
      //alert("quyen oi hoi");	
      document.getElementById("maincontent").innerHTML=xmlhttp.responseText;
    }                                                                                            
  }                                                                                              
  //xmlhttp.open("GET","temp_getuser.php?str="+str,true);                                        
  xmlhttp.open("GET","webapi/showprofile?username="+username,true);                              
  xmlhttp.send();
  
}
*/

function showProfile()
{
    var headerTemp= new Object();
    headerTemp.key = "Authorization";
    headerTemp.value = "Bearer "+token;
    //headerTemp.value = "Bearer xyz";
	xhrGetWithHeader('webapi/showprofile?username='+username,headerTemp,function(data){
		
		
		
		var receivedItems = data.body || [];
		var items = [];
		var i;
		// Make sure the received items have correct format
		for(i = 0; i < receivedItems.length; ++i){
			var item = receivedItems[i];
			alert(item);
			if(item && 'id' in item){
				items.push(item);
			}
		}
		
		document.getElementById("maincontent").innerHTML = data.firstname;
		
	}, function(err){
		console.log(err);
		
		
	});
}

function showProfileEx()
{
	alert("show profileEx");
	jQuery.ajax( {
	    type: 'GET',
	    url: 'webapi/showprofile?username='+username,
	    //contentType: "application/json",
	    //accepts: "application/json",
        dataType: 'jsonp',
        crossDomain:true,
        cache: false,
        beforeSend : function( xhr ) {
	        alert("before send");
	        
	        //xhr.setRequestHeader( "Authorization", "X-IBM-CloudInt-ApiKey" + access_token );
	        xhr.setRequestHeader( "Bearer ", token);
	    },
	    success: function( response ) {
		    alert ("success");
		    var returnResult = JSON.stringify(response);
		    //alert ("" +returnResult);
	        // response
	        //var result = JSON.parse(returnResult);

	        //alert (result.username);
	        
	        document.getElementById("maincontent").innerHTML = response;	        
	    }
	} );
}


$(document).ready(function() {
    // menu
     $('.nav li').hover(
      function () { //appearing on hover
        $('ul', this).fadeIn();
      },
      function () { //disappearing on hover
        $('ul', this).fadeOut();
      }
    );    
    // end menu
    //showProfileEx();
    showProfile();
    	
});   

                                                                                                        
</script> 


</body>
</html>

