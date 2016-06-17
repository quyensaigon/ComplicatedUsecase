package resource;

import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Set;

//import com.cloudant.client.api.ClientBuilder;
//import com.cloudant.client.api.CloudantClient;
//import com.cloudant.client.api.Database;
//import com.cloudant.client.org.lightcouch.CouchDbException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.UnknownHostException;


public class MongoClientMgr {

	//private static Mongo mongo = null;
	private static MongoClient mongoClient = null;
	private static MongoDatabase db = null;

	private static String databaseName = "profiledb";

	//private static String user = null;
	//private static String password = null;

	private static void initClient() {
		if (mongoClient == null) {
			synchronized (MongoClientMgr.class) {
				if (mongoClient != null) {
					return;
				}
				mongoClient = createClient();

			} // end synchronized
		}
	}

	private static MongoClient createClient() {
		/*
		// VCAP_SERVICES is a system environment variable
		// Parse it to obtain the NoSQL DB connection info
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		String serviceName = null;

		if (VCAP_SERVICES != null) {
			// parse the VCAP JSON structure
			JsonObject obj = (JsonObject) new JsonParser().parse(VCAP_SERVICES);
			Entry<String, JsonElement> dbEntry = null;
			Set<Entry<String, JsonElement>> entries = obj.entrySet();
			// Look for the VCAP key that holds the cloudant no sql db information
			for (Entry<String, JsonElement> eachEntry : entries) {
				if (eachEntry.getKey().toLowerCase().contains("cloudant")) {
					dbEntry = eachEntry;
					break;
				}
			}
			if (dbEntry == null) {
				throw new RuntimeException("Could not find cloudantNoSQLDB key in VCAP_SERVICES env variable");
			}

			obj = (JsonObject) ((JsonArray) dbEntry.getValue()).get(0);
			serviceName = (String) dbEntry.getKey();
			System.out.println("Service Name - " + serviceName);

			obj = (JsonObject) obj.get("credentials");

			user = obj.get("username").getAsString();
			password = obj.get("password").getAsString();

		} else {
			throw new RuntimeException("VCAP_SERVICES not found");
		}

		try {
			CloudantClient client = ClientBuilder.account(user)
					.username(user)
					.password(password)
					.build();
			return client;
		} catch (CouchDbException e) {
			throw new RuntimeException("Unable to connect to repository", e);
		}
		*/
		try
		{
			String password = "abc123";
			MongoCredential credential = MongoCredential.createCredential("qvtran","profiledb",password.toCharArray());
			//MongoClient mongoClient = new MongoClient("mongodb137.aws-us-east-1-portal.20.dblayer.com", 10137);
			//MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://mongodb137.aws-us-east-1-portal.20.dblayer.com:10137"));
			mongoClient = new MongoClient(new ServerAddress("mongodb137.aws-us-east-1-portal.20.dblayer.com", 10137), Arrays.asList(credential));
			db = mongoClient.getDatabase("profiledb");
		}
		catch(Exception e)
		{
			throw new RuntimeException("Unable to connect to repository", e);
		
		}
		
		return mongoClient;
		
	}

	public static MongoDatabase getDB() {
		if (mongoClient == null) {
			initClient();
		}

		
		 if (db == null) 
		 {
			 throw new RuntimeException("DB Not found");
		 }
		
		
		return db;
	}

	private MongoClientMgr() {
	}
}
