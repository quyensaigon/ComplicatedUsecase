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

import org.bson.Document;
import org.bson.types.Binary;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.BasicDBObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

@Path("/favorites")
/**
 * CRUD service of todo list table. It uses REST style.
 */
public class MongoResourceServlet {

	public MongoResourceServlet() {
	}
/*
	@POST
	public Response create(@QueryParam("id") Long id, @FormParam("name") String name, @FormParam("value") String value)
			throws Exception {

		DB db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			re.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		String idString = id == null ? null : id.toString();
		JsonObject resultObject = create(db, idString, name, value, null, null);

		System.out.println("Create Successful.");

		return Response.ok(resultObject.toString()).build();
	}
	
	protected JsonObject create(DB db, String id, String name, String value, Part part, String fileName)
			throws IOException {

		// check if document exist
		HashMap<String, Object> obj = (id == null) ? null : db.find(HashMap.class, id);

		if (obj == null) {
			// if new document

			id = String.valueOf(System.currentTimeMillis());

			// create a new document
			System.out.println("Creating new document with id : " + id);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("name", name);
			data.put("_id", id);
			data.put("value", value);
			data.put("creation_date", new Date().toString());
			db.save(data);

			// attach the attachment object
			obj = db.find(HashMap.class, id);
			saveAttachment(db, id, part, fileName, obj);
		} else {
			// if existing document
			// attach the attachment object
			saveAttachment(db, id, part, fileName, obj);

			// update other fields in the document
			obj = db.find(HashMap.class, id);
			obj.put("name", name);
			obj.put("value", value);
			db.update(obj);
		}

		obj = db.find(HashMap.class, id);

		JsonObject resultObject = toJsonObject(obj);

		return resultObject;
	}
*/	
	protected JsonObject createProfile(MongoDatabase db, String id,String  firstName,String  lastName,String  address,String  city,String  state,String  zip,String homePhone,String workPhone,String  mobilePhone,String  email,String gender,String  birthDate,Part part, String fileName)
	//(Database db, String id, String password, String firstName, String lastName, String address,String city, String state, String zip, String homePhone, String workPhone, String mobilePhone, String gender, String birthDate, String email,Part part, String fileName)
			throws IOException {
		Document obj = null;
		MongoCollection<Document> collection = db.getCollection("profiles");
		System.out.println("quyen oi 0");
		// check if document exist
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("_id", id);
		FindIterable<Document> cursor = collection.find(whereQuery);
		obj = cursor.first();
		
		// attach the attachment object
		
		
		System.out.println("quyen oi 1");
		if (obj == null) {
			// if new document
			System.out.println("quyen oi 2");

			//id = String.valueOf(System.currentTimeMillis());

			// create a new document
			System.out.println("Creating new document with id : " + id);
			//obj = new BasicDBObject("_id", id);
			System.out.println("quyen oi 3");
			// Read photo
			InputStream inputStream = part.getInputStream();
			byte b[] = new byte[inputStream.available()];
			inputStream.read(b);
 
            Binary data = new Binary(b);
 
            
			
			obj = new Document()
					.append("_id", id)
					.append("firstname", firstName)
					.append("lastname", lastName)
					.append("address", address)
					.append("city", city)
					.append("state", state)
					.append("zip", zip)
					.append("homephone", homePhone)
					.append("workphone", workPhone)
					.append("mobilephone", mobilePhone)
					.append("gender", gender)
					.append("birthdate", birthDate)
					.append("filename", fileName)
					.append("email", email)
					.append("creationdate", new Date().toString())
					.append("photo", data);
			
			collection.insertOne(obj);
			System.out.println("quyen oi 4");
			
			//whereQuery = new BasicDBObject();
			//whereQuery.put("_id", id);
			/*
			 cursor = collection.find(whereQuery);
			 
			
			// attach the attachment object
			if (cursor.hasNext())
			{
				obj = cursor.next();
				saveAttachment(db, id, part, fileName, obj);
				System.out.println("quyen oi 5");
			}
			else
			{
				System.out.println("quyen oi 6");
			}*/
		} else {
			// if existing document
			// attach the attachment object
			//saveAttachment(db, id, part, fileName, obj);

			// update other fields in the document
			//DBCollection collection = db.getCollection("profiles");
			
			
			//obj = db.find(HashMap.class, id);
			//BasicDBObject o = new BasicDBObject();
			
			
			BasicDBObject newDocument = new BasicDBObject();
			newDocument.put("firstname", firstName);
			newDocument.put("lastname", lastName);
			newDocument.put("address", address);
			newDocument.put("city", city);
			newDocument.put("state", state);
			newDocument.put("zip", zip);
			newDocument.put("homephone", homePhone);
			newDocument.put("workphone", workPhone);
			newDocument.put("mobilephone", mobilePhone);
			newDocument.put("gender", gender);
			newDocument.put("birthdate", birthDate);

			BasicDBObject searchQuery = new BasicDBObject().append("_id", id);

			collection.findOneAndUpdate(searchQuery,newDocument);
				
				
			
		}

		
		//HashMap<String, Object> objTemp = obj.toJson();

		//JsonObject resultObject = toJsonObject(objTemp);
		
		JsonObject resultObject = new JsonObject();
		resultObject.addProperty("_id",obj.get("_id").toString());
		

		return resultObject;
	}
/*
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("id") Long id, @QueryParam("cmd") String cmd) throws Exception {

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
			try {
				// get all the document present in database
				List<HashMap> allDocs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse()
						.getDocsAs(HashMap.class);

				if (allDocs.size() == 0) {
					allDocs = initializeSampleData(db);
				}

				for (HashMap doc : allDocs) {
					HashMap<String, Object> obj = db.find(HashMap.class, doc.get("_id") + "");
					JsonObject jsonObject = new JsonObject();
					LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) obj.get("_attachments");

					if (attachments != null && attachments.size() > 0) {
						JsonArray attachmentList = getAttachmentList(attachments, obj.get("_id") + "");
						jsonObject.addProperty("id", obj.get("_id") + "");
						jsonObject.addProperty("name", obj.get("name") + "");
						jsonObject.addProperty("value", obj.get("value") + "");
						jsonObject.add("attachements", attachmentList);

					} else {
						jsonObject.addProperty("id", obj.get("_id") + "");
						jsonObject.addProperty("name", obj.get("name") + "");
						jsonObject.addProperty("value", obj.get("value") + "");
					}

					jsonArray.add(jsonObject);
				}
			} catch (Exception dnfe) {
				System.out.println("Exception thrown : " + dnfe.getMessage());
			}

			resultObject.addProperty("id", "all");
			resultObject.add("body", jsonArray);

			return Response.ok(resultObject.toString()).build();
		}

		// check if document exists
		HashMap<String, Object> obj = db.find(HashMap.class, id + "");
		if (obj != null) {
			JsonObject jsonObject = toJsonObject(obj);
			return Response.ok(jsonObject.toString()).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
*/
	
	

	private JsonArray getAttachmentList(LinkedTreeMap<String, Object> attachmentList, String docID) {
		JsonArray attachmentArray = new JsonArray();

		for (Object key : attachmentList.keySet()) {
			LinkedTreeMap<String, Object> attach = (LinkedTreeMap<String, Object>) attachmentList.get(key);

			JsonObject attachedObject = new JsonObject();
			// set the content type of the attachment
			attachedObject.addProperty("content_type", attach.get("content_type").toString());
			// append the document id and attachment key to the URL
			attachedObject.addProperty("url", "attach?id=" + docID + "&key=" + key);
			// set the key of the attachment
			attachedObject.addProperty("key", key + "");

			// add the attachment object to the array
			attachmentArray.add(attachedObject);
		}

		return attachmentArray;
	}

	private JsonObject toJsonObject(HashMap<String, Object> obj) {
		JsonObject jsonObject = new JsonObject();
		LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) obj.get("_attachments");
		if (attachments != null && attachments.size() > 0) {
			JsonArray attachmentList = getAttachmentList(attachments, obj.get("_id") + "");
			jsonObject.add("attachements", attachmentList);
		}
		jsonObject.addProperty("id", obj.get("_id") + "");
		jsonObject.addProperty("name", obj.get("name") + "");
		jsonObject.addProperty("value", obj.get("value") + "");
		return jsonObject;
	}

	private void saveAttachment(DB db, String id, Part part, String fileName, BasicDBObject obj)
			throws IOException {
		if (part != null) {
			InputStream inputStream = part.getInputStream();
			try {
				//db.saveAttachment(inputStream, fileName, part.getContentType(), id, (String) obj.get("_rev"));
				byte b[] = new byte[inputStream.available()];
				inputStream.read(b);
	 
	            Binary data = new Binary(b);
	            //BasicDBObject o = new BasicDBObject();
	            obj.append("photo", data);
	            
			
			} finally {
				inputStream.close();
			}
		}
	}


	private MongoDatabase getDB() {
		return MongoClientMgr.getDB();
	}

}
