package resource;

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
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/profile")
public class ProfileResource {

	public ProfileResource() {
		// TODO Auto-generated constructor stub
	}
	
	private Database getDB() {
		return CloudantClientMgr.getDB();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("id") String id) throws Exception {
		
		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			re.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		JsonObject resultObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		
		if (id == null) {
			try 
			{
				// get all the document present in database
				List<HashMap> allDocs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse()
						.getDocsAs(HashMap.class);

				

				for (HashMap doc : allDocs) 
				{
					HashMap<String, Object> obj = db.find(HashMap.class, doc.get("_id") + "");
					JsonObject jsonObject = toJsonObject(obj);
					jsonArray.add(jsonObject);
				}
			} 
			catch (Exception dnfe) 
			{
				System.out.println("Exception thrown : " + dnfe.getMessage());
			}

			//resultObject.addProperty("id", "all");
			//resultObject.add("body", jsonArray);

			//return Response.ok(resultObject.toString()).build();
			return Response.ok(jsonArray.toString()).build();
		}
		
		// check if document exists
		HashMap<String, Object> obj = db.find(HashMap.class, id + "");
		if (obj != null) 
		{
			JsonObject jsonObject = toJsonObject(obj);
			return Response.ok(jsonObject.toString()).build();
		} 
		else 
		{
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
	}
	
	private JsonObject toJsonObject(HashMap<String, Object> obj) 
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", obj.get("_id") + "");
		jsonObject.addProperty("firstname", obj.get("firstname") + "");
		jsonObject.addProperty("lastname", obj.get("lastname") + "");
		jsonObject.addProperty("address", obj.get("address") + "");
		jsonObject.addProperty("city", obj.get("city") + "");
		jsonObject.addProperty("state", obj.get("state") + "");
		jsonObject.addProperty("zip", obj.get("zip") + "");
		jsonObject.addProperty("homephone", obj.get("homephone") + "");
		jsonObject.addProperty("workphone", obj.get("workphone") + "");
		jsonObject.addProperty("mobilephone", obj.get("mobilephone") + "");
		jsonObject.addProperty("gender", obj.get("gender") + "");
		jsonObject.addProperty("birthdate", obj.get("birthdate") + "");
		jsonObject.addProperty("email", obj.get("email") + "");
		jsonObject.addProperty("filename", obj.get("filename") + "");
		return jsonObject;
	}

}
