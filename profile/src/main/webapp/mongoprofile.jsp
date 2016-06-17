<!DOCTYPE html>
<html lang="en">
<head>
<title>Global | Contacts</title>
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" media="screen" href="css/reset.css">
<link rel="stylesheet" type="text/css" media="screen" href="css/style.css">
<link rel="stylesheet" type="text/css" media="screen" href="css/grid_12.css">
<style type="text/css">.thumb-image{width:200px;position:relative;padding:3px;}</style>
<link href='http://fonts.googleapis.com/css?family=Condiment' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oxygen' rel='stylesheet' type='text/css'>
<script src="js/jquery-1.7.min.js"></script>
<script src="js/jquery.easing.1.3.js"></script>
<script type = "text/javascript"  src = "js/util.js"></script>
<!--[if lt IE 9]>
<script src="js/html5.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="css/ie.css">
<![endif]-->
<script>
//alert("hello");
var username = '<%= request.getParameter("username") %>';
var token = '<%= request.getParameter("token") %>';
//alert(username);
//alert(token);
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
	xhrGetWithHeader('webapi/mongoshowprofile?username='+username,headerTemp,function(data){
		
		
		
		var receivedItems = data.body || [];
		var items = [];
		var i;
		// Make sure the received items have correct format
		for(i = 0; i < receivedItems.length; ++i){
			var item = receivedItems[i];
//			alert(item);
			if(item && 'id' in item){
				items.push(item);
			}
		}
		
		document.getElementById("maincontent").innerHTML = ""+
		"<div class=\"block-5\">"+
        "    <h3 class=\"p5\">"+ data.firstname + " " + data.lastname + "</h3>"+
        "    <div class=\"box-shadow\">"+
        "    <img src=\"http://customeraccountservice.mybluemix.net/mongoattach?id="+data.id+"&key="+data.filename+"\" class= \"thumb-image\"></div>"+
        
        ""+   "<dl>"+
        "      <dt>" + data.address +","+"<br>"+
        ""+        data.city+ "," + data.state +" "+data.zip+"."+"</dt>"+
        ""+      "<dd><span>Cell phone: </span>" +data.mobilephone+"</dd>"+
        ""+      "<dd><span>Work phone: </span>"+data.workphone+"</dd>"+
        ""+      "<dd><span>E-mail: </span><a href=\"#\" class=\"link\">"+data.email+"</a></dd>"+
        ""+    "</dl>"+
        "  </div>"+
        "  <div class=\"block-6\">"+
        "    <h3> </h3>"+
        "    <table style=\"border-collapse: separate; border-spacing: 15px;\">"+
        "      <tr>"+
        "      <td>First Name:</td><td>"+data.firstname+"</td>"+
        "      </tr>"+
        "      <tr>"+
        "      <td>Last Name:</td><td>"+data.lastname+"</td>"+
        "      </tr>"+
        "      <tr>"+
        "      <td>Address:</td><td>"+data.address+"</td>"+
        "      </tr>"+  
        "      <tr>"+
        "      <td>City:</td><td>"+data.city+"</td>"+
        "      </tr>"+
        "      <tr>"+
        "      <td>State:</td><td>"+data.state+"</td>"+
        "      </tr>"+
        "      <tr>"+
        "      <td>Zip:</td><td>"+data.zip+"</td>"+
        "      </tr>"+
        "      <tr>"+
        "      <td>Gender:</td><td>"+data.gender+"</td>"+
        "      </tr>"+  
        "      <tr>"+
        "      <td>Birth Date:</td><td>"+data.birthdate+"</td>"+
        "      </tr>"+
        
        "      </tr>"+  
        "      <tr>"+
        "      <td>Cell Phone:</td><td>"+data.mobilephone+"</td>"+
        "      </tr>"+
        
        "      </tr>"+  
        "      <tr>"+
        "      <td>Home Phone:</td><td>"+data.homephone+"</td>"+
        "      </tr>"+
        
        "      </tr>"+  
        "      <tr>"+
        "      <td>Work Phone:</td><td>"+data.workphone+"</td>"+
        "      </tr>"+
        
        "      </tr>"+  
        "      <tr>"+
        "      <td>Email:</td><td>"+data.email+"</td>"+
        "      </tr>"+
        
        "    </table>"+
        "  </div>";
		
	}, function(err){
		console.log(err);
		
		
	});
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
</head>

<body>
<div class="main">
  <!--==============================header=================================-->
  <header>
    <h1><a href="mongoindex.html"><img src="images/logo.png" alt=""></a></h1>
    <div class="form-search">
      <form id="form-search" method="post" action="#">
        <input type="text" value="Type here..." onBlur="if(this.value=='') this.value='Type here...'" onFocus="if(this.value =='Type here...' ) this.value=''"  />
        <a href="#" class="search_button"></a>
      </form>
    </div>
    <div class="clear"></div>
    <nav class="box-shadow">
      <div>
        <ul class="menu">
          <li class="home-page"><a href="mongoindex.html"><span></span></a></li>
          <li><a href="about.html">About</a></li>
          <li><a href="services.html">Services</a></li>
          <li><a href="projects.html">Projects</a></li>
          <li><a href="clients.html">Clients</a></li>
          <li><a href="contacts.html">Contacts</a></li>
        </ul>
        <div class="social-icons"> <span>Follow us:</span> <a href="#" class="icon-3"></a> <a href="#" class="icon-2"></a> <a href="#" class="icon-1"></a> </div>
        <div class="clear"></div>
      </div>
    </nav>
  </header>
  <!--==============================content================================-->
  <section id="content">
    <div class="container_12">
      <div class="grid_12">
        <div class="wrap pad-3" id="maincontent">
          
        </div>
      </div>
      <div class="clear"></div>
    </div>
  </section>
</div>
<!--==============================footer=================================-->
<footer>
  <p>© 2016 IBM Usecase team 4400 North First street, San Jose, CA 95134 </p>
  <p>Designed by <a target="_blank" href="http://www.google.com/" class="link">Quyen Tran</a></p>
</footer>
</body>
</html>
