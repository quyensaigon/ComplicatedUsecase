package quyen.healthcare.demo.profile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/")
public class Login {

	@POST
	@Path("login_process")
	@Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
	public Response login(@FormParam("username") String username, @FormParam("password") String password) throws Exception {
        
		// Call webservice from connectprovider to authenticate
		try 
		{

			authenticate(username,password);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("status", "OK");
			jsonObject.addProperty("token", username + "Qllygd_1");
			return Response.ok(jsonObject.toString()).build();

        } 
		catch (Exception e) 
		{
			//return Response.status(Response.Status.UNAUTHORIZED).build();
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("status", "failed");
			jsonObject.addProperty("token", "invalidtoken");
			return Response.ok(jsonObject.toString()).build();
        }
		
		
    }
	
	// Call webservice from connectprovider to authenticate
	private void authenticate(String username, String password) throws Exception {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
		//http://connectionprovider.mybluemix.net/webapi/verifypassword/169.50.102.93/cn=Manager,dc=srv,dc=world/abc123/ou=People,dc=srv,dc=world/qvtran/abc123
		String endPoint = "http://connectionprovider.mybluemix.net/webapi/verifypassword/169.50.102.93/cn=Manager,dc=srv,dc=world/abc123/ou=People,dc=srv,dc=world";
         
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(endPoint+"/"+username+"/"+password);
		Response response;
		try
		{
			response = target.request(MediaType.APPLICATION_JSON).get();
			
			if (response.getStatus() == 200)
			{
				String entity = response.readEntity(String.class);
				if (entity.indexOf("OK")==-1)
				{
					throw new Exception("Authentication failed");
				}
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		
		//return response;
	
	}

}
