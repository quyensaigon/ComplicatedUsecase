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
public class MongoProfile {

	@GET
	@Path("mongoshowprofile")
	@Secured
    @Produces(MediaType.APPLICATION_JSON)
	public Response getProfile(@QueryParam("username") String username) throws Exception {
        
        String endPoint = "http://customeraccountservice.mybluemix.net/api/mongoprofile";
         
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(endPoint+"?id="+username);
		Response response;
		try
		{
			response = target.request(MediaType.APPLICATION_JSON).get();
		}
		catch(Exception e)
		{
			throw e;
		}
		
		return response;
		//return Response.ok(response).build();
        
		
        //JsonObject jsonObject = new JsonObject();
		//jsonObject.addProperty("status", "OK");
		//jsonObject.addProperty("token", response.toString());
		//return Response.ok(jsonObject.toString()).build();
    }

}
