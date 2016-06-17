package quyen.healthcare.com.connectprovider;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonObject;
import com.novell.ldap.*;

import java.io.UnsupportedEncodingException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("verifypassword")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	//Sample: http://localhost:9080/connectprovider/webapi/verifypassword/169.50.102.93/cn=Manager,dc=srv,dc=world/abc123/ou=People,dc=srv,dc=world/qvtran/abc123
    @GET
    @Path("/{ldapHostParam}/{loginDNParam}/{dnPasswordParam}/{baseParam}/{userName}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyPassword(@PathParam("ldapHostParam")String ldapHostParam, @PathParam("loginDNParam")String loginDNParam,@PathParam("dnPasswordParam")String dnPasswordParam,@PathParam("baseParam")String baseParam,@PathParam("userName")String userName,@PathParam("password")String password) {
        //return "Got it!";
    	
    	System.out.println("ldapHostParam: " + ldapHostParam);
    	System.out.println("loginDNParam: " + loginDNParam);
    	System.out.println("dnPasswordParam: " + dnPasswordParam);
    	System.out.println("base: " + baseParam);
    	System.out.println("userName: " + userName);
    	System.out.println("password: " + password);
    	
    	//get objectDN from username
    	int ldapPort = LDAPConnection.DEFAULT_PORT;

        int ldapVersion = LDAPConnection.LDAP_V3;
        
    	JsonObject jsonObject = new JsonObject();
    	//if (checkPassword(ldapHostParam,loginDNParam ,dnPasswordParam,userName,password)==true)
    	//doAuthentication(String ldapHost,int ldapPort,String serviceUserDN, String serviceUserPassword,String base, String userName, String password)
    	if (doAuthentication(ldapHostParam,ldapPort,loginDNParam,dnPasswordParam,baseParam,userName,password)==true)
    	{
    		jsonObject.addProperty("status", "OK");
    	}
    	else
    	{
    		jsonObject.addProperty("status", "failed");
    	}
		
		
		return Response.ok(jsonObject.toString()).build();
    }
    
    private boolean checkPassword(String ldapHostParam,String loginDNParam ,String dnPasswordParam,String objectDNParam,String testPasswordParam )
    {
    	int ldapPort = LDAPConnection.DEFAULT_PORT;

        int ldapVersion = LDAPConnection.LDAP_V3;

        String ldapHost = ldapHostParam;

        String loginDN = loginDNParam;

        String password = dnPasswordParam;

        String objectDN = objectDNParam;

        String testPassword = testPasswordParam;
        
        

        LDAPConnection lc = new LDAPConnection();

        boolean correct = false;

        try {

           // connect to the server


            lc.connect( ldapHost, ldapPort );



           // authenticate to the server


            lc.bind( ldapVersion, loginDN, password.getBytes("UTF8") );



            LDAPAttribute attr = new LDAPAttribute(

                                            "userPassword", testPassword );

            System.out.println("Quyen oi hoi: " + objectDN);
            correct = lc.compare( objectDN, attr );



            //System.out.println( correct ? "The password is correct.":

            //                              "The password is incorrect.\n");



           // disconnect with the server


            lc.disconnect();
            return correct;

        }

        catch( LDAPException e ) {

            if ( e.getResultCode() == LDAPException.NO_SUCH_OBJECT ) {

                //System.err.println( "Error: No such entry" );
            	return correct;

            } else if ( e.getResultCode() == 

                                        LDAPException.NO_SUCH_ATTRIBUTE ) {

                //System.err.println( "Error: No such attribute" );
            	return correct;

            } else {

                //System.err.println( "Error: " + e.toString() );
            	return correct;

            }        

        }

        catch( UnsupportedEncodingException e ) {

            //System.out.println( "Error: " + e.toString() );
        	return correct;

        }
        
        //return correct;
    }
    
  
    
    
    
 	   
 	  public boolean doAuthentication(String ldapHost,int ldapPort,String serviceUserDN, String serviceUserPassword,String base, String userName, String password)
 	  {

 		  // service user
 		  //String serviceUserDN = "cn=Quyen Tran,ou=People,dc=srv,dc=world";
 		   
 		  //String serviceUserDN = "cn=Manager,dc=srv,dc=world";
 		  //String serviceUserPassword = "abc123";

 		  // user to authenticate
 		  String identifyingAttribute = "uid";
 		  //String identifier = "qvtran";
 		  //String password = "abc123";
 		  //String base = "ou=People,dc=srv,dc=world";

 		  // LDAP connection info
 		  String ldap = ldapHost;
 		  int port = ldapPort;
 		  String ldapUrl = "ldap://" + ldap + ":" + port;

 		   // first create the service context
 		   DirContext serviceCtx = null;
 		   try 
 		   {
 		       // use the service user to authenticate
 		       Properties serviceEnv = new Properties();
 		       serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
 		       serviceEnv.put(Context.PROVIDER_URL, ldapUrl);
 		       serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
 		       serviceEnv.put(Context.SECURITY_PRINCIPAL, serviceUserDN);
 		       serviceEnv.put(Context.SECURITY_CREDENTIALS, serviceUserPassword);
 		       serviceCtx = new InitialDirContext(serviceEnv);

 		       // we don't need all attributes, just let it get the identifying one
 		       String[] attributeFilter = { identifyingAttribute };
 		       SearchControls sc = new SearchControls();
 		       sc.setReturningAttributes(attributeFilter);
 		       sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

 		       // use a search filter to find only the user we want to authenticate
 		       String searchFilter = "(" + identifyingAttribute + "=" + userName + ")";
 		       NamingEnumeration<SearchResult> results = serviceCtx.search(base, searchFilter, sc);

 		       if (results.hasMore()) 
 		       {
 		           // get the users DN (distinguishedName) from the result
 		           SearchResult result = results.next();
 		           String distinguishedName = result.getNameInNamespace();

 		           // attempt another authentication, now with the user
 		           Properties authEnv = new Properties();
 		           authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
 		           authEnv.put(Context.PROVIDER_URL, ldapUrl);
 		           authEnv.put(Context.SECURITY_PRINCIPAL, distinguishedName);
 		           authEnv.put(Context.SECURITY_CREDENTIALS, password);
 		           new InitialDirContext(authEnv);

 		           System.out.println("Authentication successful");
 		           return true;
 		       }
 		       return false;
 		   } 
 		   catch (Exception e) 
 		   {
 		       e.printStackTrace();
 		       if (serviceCtx != null) 
 		       {
		           try 
		           {
		               serviceCtx.close();
		           } 
		           catch (NamingException ee) 
		           {
		               ee.printStackTrace();
		               
		           }
		           
 		       } 
 		       return false;
 		   }
 	  }
    
}
