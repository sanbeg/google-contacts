import java.util.Properties;
import java.net.URL;

import java.io.FileInputStream;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.Website;

import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.GivenName;
import com.google.gdata.data.extensions.FamilyName;
import com.google.gdata.data.extensions.FullName;

class Update 
{
    
    public static Properties get_properties(String filename) 
    {
	Properties props = new Properties();
	try {
	    FileInputStream propfile = new FileInputStream(filename);
	    props.load(propfile);
	    propfile.close();
	}
	catch (Exception e) {
	    System.err.println(e.getMessage());
	    System.exit(1);
	}
	return props;
    }

    public static ContactsService get_service() throws Exception
    {
	Properties conf = get_properties("steve.conf");
	ContactsService myService = new ContactsService("x");
	myService.setUserCredentials(conf.getProperty("gmail"), conf.getProperty("pass"));

	return myService;
    }
    

    public static void main (String args[]) throws Exception
    {
       
	ContactsService service = get_service();
	
	for (String file : args) {
	    try {
		Properties edit = new Properties();
		FileInputStream stream = new FileInputStream(file);
		edit.load( stream );
		stream.close();

		////////////////////
		
		System.out.println( edit.getProperty("edit"));
		String base = edit.getProperty("edit");
		String full = base.replace("/base/", "/full/");
		
		ContactEntry entry = service.getEntry(new URL(full), ContactEntry.class);
		System.out.println( entry.getName().getFullName().getValue() );
		
		int edit_count = 0;

		try {
		    String field;
		
		    if (!entry.hasName())
			entry.setName( new Name() );
		    Name name = entry.getName();
		    
		    field = edit.getProperty("first-name");
		    if (field != null){
			if ( !name.hasGivenName() )
			    name.setGivenName( new GivenName() );
			name.getGivenName().setValue(field);
			++edit_count;
		    }
		    
		    field = edit.getProperty("last-name");
		    if (field != null) {
			if ( ! name.hasFamilyName() )
			    name.setFamilyName( new FamilyName() );
			
			name.getFamilyName().setValue(field);
			++edit_count;
		    }
		    
		    field = edit.getProperty("full-name");
		    if (field != null) {
			if ( ! name.hasFullName() )
			    name.setFullName( new FullName() );
			
			name.getFullName().setValue(field);
			++edit_count;
		    }

		    field = edit.getProperty("facebook");
		    if (field != null) {
			Website profile = new Website();
			profile.setHref(field);
			profile.setRel(Website.Rel.PROFILE);
			entry.addWebsite(profile);
			++edit_count;
		    }
		    

		    if (edit_count > 0) {
			URL editUrl = new URL(entry.getEditLink().getHref());
			System.out.println(editUrl);
			
			try {
			    ContactEntry contactEntry = service.update(editUrl, entry);
			    System.out.println("Updated: " + contactEntry.getUpdated().toString());
			    //} catch (PreconditionFailedException e) {
			} catch (Exception e) {
			    System.err.println("Etags mismatch: " + e.getMessage());
			    
			}
		    }
		    
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
		
	    }
	    catch (Exception e) {
		System.err.println(file + ": " + e.getMessage());
		
	    }
	    
	}
    }
}
