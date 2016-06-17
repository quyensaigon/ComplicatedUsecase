package resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.naming.ldap.*;
import javax.naming.directory.*;
import javax.naming.*;
import javax.net.ssl.*;
import java.io.*;

import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/attach")
@MultipartConfig()
public class AttachServlet extends HttpServlet {

	private static final int readBufferSize = 8192;
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	 
			throws ServletException, IOException {
		System.out.println("quyen 0");
		Part part = request.getPart("file");

		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstname");
		String lastName = request.getParameter("lastname");
		String address = request.getParameter("address");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		String zip = request.getParameter("zip");
		String homePhone = request.getParameter("homephone");
		String workPhone = request.getParameter("workphone");
		String mobilePhone = request.getParameter("mobilephone");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String birthDate = request.getParameter("birthdate");
		String fileName = request.getParameter("filename");
		System.out.println("quyen 1");
		

		Database db = null;
		try {
			System.out.println("quyen 2");
			db = CloudantClientMgr.getDB();
			System.out.println("quyen 3");
		} catch (Exception re) {
			re.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		System.out.println("quyen 4");
		ResourceServlet servlet = new ResourceServlet();
		System.out.println("quyen 5");
		JsonObject resultObject = servlet.createProfile(db, id, firstName, lastName, address, city, state, zip,homePhone,workPhone, mobilePhone, email,gender,birthDate, part, fileName);
		System.out.println("quyen 6");
		//create LDAP user here
		String uidNumber = getUidNumber();
		System.out.println("quyen 7 ");
		System.out.println("uidNumber is "+uidNumber);
		//uidNumber="4467";
		updateUidNumber(uidNumber);
		System.out.println("quyen 8");
		createLdapUser(firstName,lastName,email,password,uidNumber);
		
		
		System.out.println("quyen 9");
		
		System.out.println("Upload completed.");

		response.getWriter().println(resultObject.toString());
	}
	/*
	 protected void doPost(HttpServletRequest request, HttpServletResponse response)
	 
			throws ServletException, IOException {
		Part part = request.getPart("file");

		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String value = request.getParameter("value");
		String fileName = request.getParameter("filename");

		Database db = null;
		try {
			db = CloudantClientMgr.getDB();
		} catch (Exception re) {
			re.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		ResourceServlet servlet = new ResourceServlet();

		JsonObject resultObject = servlet.create(db, id, name, value, part, fileName);

		System.out.println("Upload completed.");

		response.getWriter().println(resultObject.toString());
	}
	*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String key = request.getParameter("key");

		response.setHeader("Content-Disposition", "inline; filename=\"" + key + "\"");

		InputStream dbResponse = CloudantClientMgr.getDB().find(id + "/" + key);
		OutputStream output = response.getOutputStream();

		try {
			int readBytes = 0;
			byte[] buffer = new byte[readBufferSize];
			while ((readBytes = dbResponse.read(buffer)) >= 0) {
				output.write(buffer, 0, readBytes);
			}
		} finally {
			dbResponse.close();
		}

	}
	
	public void createLdapUser(String firstName, String lastName, String email,String password, String uidNumber)
	 {
		 Hashtable env = new Hashtable();
        String adminName = "cn=Manager,dc=srv,dc=world";
        String adminPassword = "abc123";
        String userName = "cn="+email+",ou=People,dc=srv,dc=world";
        String groupName = "ou=People,dc=srv,dc=world";
        String ldapServer = "ldap://169.50.102.93:389";
        
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
   
        //set security credentials, note using simple cleartext authentication
        env.put(Context.SECURITY_AUTHENTICATION,"simple");
        env.put(Context.SECURITY_PRINCIPAL,adminName);
        env.put(Context.SECURITY_CREDENTIALS,adminPassword);
                  
        //connect to my domain controller
        env.put(Context.PROVIDER_URL, ldapServer);
                  
        try {

             // Create the initial directory context
             LdapContext ctx = new InitialLdapContext(env,null);

             // Create attributes to be associated with the new user
                 Attributes attrs = new BasicAttributes(true); 
            
             //These are the mandatory attributes for a user object
             //Note that Win2K3 will automagically create a random 
             //samAccountName if it is not present. (Win2K does not)
                 Attribute oc = new BasicAttribute("objectClass");  
                 oc.add("top");  
                 //oc.add("person");  
                 //oc.add("organizationalPerson");  
                 oc.add("inetOrgPerson"); 
                 oc.add("posixAccount");
             attrs.put(oc);
             //    attrs.put("samAccountName","AlbertE");
             attrs.put("cn",firstName+" "+ lastName);

             //These are some optional (but useful) attributes
             attrs.put("givenName",firstName);
             attrs.put("sn",lastName);
             attrs.put("displayName",firstName+" "+ lastName);
             attrs.put("description",firstName+" "+ lastName);
             
             attrs.put("mail",email);
             //attrs.put("telephoneNumber","999 123 4567");
             //Attribute userUserPassword = new BasicAttribute("userPassword","password");
             attrs.put("uid", email);
             attrs.put("userPassword",password);
             attrs.put("gidNumber", "500");
             attrs.put("uidNumber",uidNumber);
             attrs.put("homeDirectory","/home/users/"+email);
             
             //some useful constants from lmaccess.h
             int UF_ACCOUNTDISABLE = 0x0002;
             int UF_PASSWD_NOTREQD = 0x0020;
             int UF_PASSWD_CANT_CHANGE = 0x0040;
             int UF_NORMAL_ACCOUNT = 0x0200;
             int UF_DONT_EXPIRE_PASSWD = 0x10000;
             int UF_PASSWORD_EXPIRED = 0x800000;
        
             //Note that you need to create the user object before you can
             //set the password. Therefore as the user is created with no 
             //password, user AccountControl must be set to the following
             //otherwise the Win2K3 password filter will return error 53
             //unwilling to perform.

             //    attrs.put("userAccountControl",Integer.toString(UF_NORMAL_ACCOUNT + UF_PASSWD_NOTREQD + UF_PASSWORD_EXPIRED+ UF_ACCOUNTDISABLE));

        
             // Create the context
             Context result = ctx.createSubcontext(userName, attrs);
             System.out.println("Created disabled account for: " + userName);

             //now that we've created the user object, we can set the 
             //password and change the userAccountControl
             //and because password can only be set using SSL/TLS
             //lets use StartTLS
             ctx.close();
             
             /*
             StartTlsResponse tls = (StartTlsResponse)ctx.extendedOperation(new StartTlsRequest());
             tls.negotiate();
        
             
             
             //set password is a ldap modfy operation
             //and we'll update the userAccountControl
             //enabling the acount and force the user to update ther password
             //the first time they login
             ModificationItem[] mods = new ModificationItem[2];
        
             //Replace the "unicdodePwd" attribute with a new value
             //Password must be both Unicode and a quoted string
             String newQuotedPassword = "\"Password2000\"";
             byte[] newUnicodePassword = newQuotedPassword.getBytes("UTF-16LE");

             mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("unicodePwd", newUnicodePassword));
             mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl",Integer.toString(UF_NORMAL_ACCOUNT + UF_PASSWORD_EXPIRED)));
        
             // Perform the update
             ctx.modifyAttributes(userName, mods);
             System.out.println("Set password & updated userccountControl");
             

             //now add the user to a group.

                  try     {
                       ModificationItem member[] = new ModificationItem[1];
                       member[0]= new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("member", userName)); 
                  
                       ctx.modifyAttributes(groupName,member);
                       System.out.println("Added user to group: " + groupName);

                  } 
                  catch (NamingException e) {
                        System.err.println("Problem adding user to group: " + e);
                  }
             //Could have put tls.close()  prior to the group modification
             //but it seems to screw up the connection  or context ?
             tls.close();
             ctx.close();
             */
             System.out.println("Successfully created User: " + userName);
        
        } 
        catch (NamingException e) {
             System.err.println("Problem creating object: " + e);
        }
   
        catch (Exception e) {
             System.err.println("Problem creating object: " + e);               }

	 }
	
	public String getUidNumber()
	{
		String uidNumber = "";
		
		// Get uidNumber from Cloudant
		String endPoint = "http://customeraccountservice.mybluemix.net/api/profile";
         
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(endPoint+"?id=ldapautoindex@gmail.com");
		Response response;
		try
		{
			String res = target.request(MediaType.APPLICATION_JSON).get(String.class);
			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(res).getAsJsonObject();
			
			
			System.out.println("autoNumber :"+ json.get("zip"));
			String uidNumberTemp = ""+ json.get("zip");
			uidNumber = uidNumberTemp.replace("\"", "");
			return uidNumber;
		}
		catch(Exception e)
		{
			return uidNumber;
		}
		// Update uidNumber to Cloudant
		
		//return uidNumber;
	}
	
	public void updateUidNumber(String uidNumber)
	{
		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			re.printStackTrace();
			//return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		//JsonObject resultObject = new JsonObject();
		//JsonArray jsonArray = new JsonArray();
		
		
		try
		{
			// check if document exists
			//HashMap<String, Object> obj = db.find(HashMap.class, "ldapautoindex@gmail.com");
			HashMap<String, Object> obj = null;
			obj = db.find(HashMap.class, "ldapautoindex@gmail.com");
			if (obj != null) 
			{
				//Increase uidNumber to 1
				long newUid = Long.parseLong(uidNumber); 
				newUid = newUid +1;
				
				obj.put("zip", ""+newUid);
				db.update(obj);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	private Database getDB() {
		return CloudantClientMgr.getDB();
	}
	

}
