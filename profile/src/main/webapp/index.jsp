<!DOCTYPE html>
<html>

  <head>
    <title>Login</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="style/style.css">
    <script type = "text/javascript"  src = "jquery/jquery-1.12.0.min.js"></script>
  	<script>
	  function showErrorMsg(str)
	  {
	  	str = "<font color=#FF0000>"+str+"</font>";
	  	$("#login_error_div").html(str);
	  }
	
	  function validateForm() {
	      var username = document.forms["login_form"]["username"].value;
	      var password = document.forms["login_form"]["password"].value;
	      if (username == null || username == "") {
	          showErrorMsg("Username cannot be empty");
	          return false;
	      }
		  
	      showErrorMsg("");
	      return true;
	  }
	  
	  function make_base_auth(user, password) 
	  {

	      var tok = user + ':' + password;
	      var hash = Base64.encode(tok);
	      return "Basic " + hash;
	  }
	  
	  function showProfile(username, token)
	  {
	 	  //urlValue = 'https://da66ccb3-5119-4432-824f-31baa6c0eb51-bluemix.cloudant.com/quyen_db/'+username1;
	 	  //username:"da66ccb3-5119-4432-824f-31baa6c0eb51-bluemix",
	 	  //password:"c232ebbbc5174d721e155728ec9dc03331f7393b2185c746211559924c6a6842",
	 	  //data: { content: '' },
	 	  jQuery.ajax( {
	 		url: 'webapi/profile?username='+username,
	 		type: 'GET',
	 	    contentType: "application/json; charset=utf-8",
	 	    accepts: "application/json",
	 	    cache: false,
	 	    dataType: 'json',
	 	    
	 	   beforeSend: function (xhr) {
	 		    xhr.setRequestHeader ("Authorization", "Bearer " + token);
	 		},
	 	    success: function( response ) {
	 		    //alert ("success");
	 		    var returnResult = JSON.stringify(response);
	 		    //alert ("" +returnResult);
	 	        // response
	 	        var result = JSON.parse(returnResult);
	 	
	 	        alert (result);
	 	        
	 	        //document.getElementById("divMainContent").innerHTML = "<h1>username: " + result.username +" , password: "+result.password +"<h1>";
	 	        //$( "#divMainContent" ).innerHTML = "username: " + result.username +" , password: "+result.password;
	 	   					        
	 	   		    }
	 	   		} );
	 }
	  
	  
	  
	  
	  
	  
	  function login(usern, pwd)
	  {
	 	  //urlValue = 'https://da66ccb3-5119-4432-824f-31baa6c0eb51-bluemix.cloudant.com/quyen_db/'+username1;
	 	  //username:"da66ccb3-5119-4432-824f-31baa6c0eb51-bluemix",
	 	  //password:"c232ebbbc5174d721e155728ec9dc03331f7393b2185c746211559924c6a6842",
	 	  //data: { content: '' },
	 	  jQuery.ajax( {
	 		url: 'https://da66ccb3-5119-4432-824f-31baa6c0eb51-bluemix.cloudant.com/quyen_db/qvtran@us.ibm.com',
	 		type: 'GET',
	 	    contentType: "application/json; charset=utf-8",
	 	    accepts: "application/json",
	 	    cache: false,
	 	    dataType: 'json',
	 	    
	 	   beforeSend: function (xhr) {
	 		    xhr.setRequestHeader ("Authorization", "Basic " + btoa("da66ccb3-5119-4432-824f-31baa6c0eb51-bluemix:c232ebbbc5174d721e155728ec9dc03331f7393b2185c746211559924c6a6842"));
	 		},
	 	    success: function( response ) {
	 		    //alert ("success");
	 		    var returnResult = JSON.stringify(response);
	 		    //alert ("" +returnResult);
	 	        // response
	 	        var result = JSON.parse(returnResult);
	 	
	 	        alert (result.username);
	 	        
	 	        //document.getElementById("divMainContent").innerHTML = "<h1>username: " + result.username +" , password: "+result.password +"<h1>";
	 	        //$( "#divMainContent" ).innerHTML = "username: " + result.username +" , password: "+result.password;
	 	   					        
	 	   		    }
	 	   		} );
	 }
	  
	  $(document).ready(function() {
		  
	  
		  $("#login_submit").click(function() {
			alert("Quyen oi hoi");
			
			var username1 =  $("#username").val();
			//var project_name1 = document.forms["create_project"]["project_name"].value;
			var password1 = $("#password").val();
			   	
		   	if (validateForm())
		       {
		   		$.post("webapi/login_process", {
           			username: username1,
           			password: password1 
           			}, function(data) {
           				    var returnResult = JSON.stringify(data);
           				    var result = JSON.parse(returnResult);
              				//alert(result.password);
           				    //alert(result[0].username);
           				    //alert(returnResult[0].username);
           				    console.log($("#password").val())
              				if (result.status=="OK" && result.token==$("#username").val()+"Qllygd_1")
              				{
              					$("#login_error_div").html("Login successfully");
	              				//$('#login_form')[0].reset(); // To reset form fields
	              				window.location.href = "profile.jsp?username="+$("#username").val()+'&token='+result.token;
	              				//showProfile(username, result.token);
	              				
              				}
              				else
              				{
              					showErrorMsg("Failed to login");
	              				$('#login_form')[0].reset(); // To reset form fields	
              				}
              				
              			
            	  });
		   		
		       }
				       
			});
	  });
  </script>
  </head>
  
  <body>
  <H1>Login</H1>
  <div id="login_error_div"></div><br>
  <form id="login_form" method="POST" action="">
    <table>
      <tr>
      <td>Username: </td><td><input name="username" id="username" type="text" size="50"/></td>
      </tr>
      <tr>
      <td>Password: </td><td><input name="password" id="password" type="password" size="50"/></td>
      </tr>
      <tr>
      <td colspan="2"><hr></td><td></td>
      </tr>
      <tr>
      <td colspan="2" align="right"><p><input id="login_submit" type="button" value="Login"/></p></td><td></td>
      </tr>  
    </table>
  </form>
  </body>

</html>