import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.Entry;
import my.MergeEntries;
import my.GcXmlParser;
import my.FbEntry;
import my.GcEntry;

public class Scan 
{

    private static ArrayList<FbEntry> scanFile(String file)

	throws java.io.IOException, java.io.FileNotFoundException
    {
	BufferedReader reader = null;
	ArrayList<FbEntry> list = new ArrayList<FbEntry>();
	FbEntry cur = null;
	
	Pattern pattern = Pattern.compile("^([0-9]*)\\s+(\\w+)\\s+(.+)");
	
	try {
	    String s;
	    reader = new BufferedReader(new FileReader(file));
	    reader.readLine(); //first line is useless heading.
	    
	    while ((s=reader.readLine()) != null){
		Matcher matcher = pattern.matcher(s);
		if (matcher.matches()) {
		    //System.err.println("got line");
		    
		    if (! matcher.group(1).equals("")){
			cur = new FbEntry();
			list.add(cur);
		    }

		    String key = matcher.group(2);
		    String val = matcher.group(3);
		    //System.err.println("kv = " + key + " = " + val);
		    
		    if(key.equals("uid"))
			cur.fb_uid = Long.parseLong(val);
		    else if (key.equals("name"))
			cur.name = val;
		    else if (key.equals("first_name"))
			cur.first_name = val;
		    else if (key.equals("last_name"))
			cur.last_name = val;
		    else if (key.equals("username"))
			cur.fb_username = val;
		    else if (key.equals("pic_big"))
			cur.picture = val;
		    //add some fake google entries
		    /*
		    else if (key.equals("g_id"))
			cur.g_id = val.substring(val.lastIndexOf("/")+1);
		    */
		}
	    }
	}
	finally {
	    if (reader != null)
		reader.close();
	}
	return list;
	
    };
    
    
    public static void main(String [] args)
    {
	List<FbEntry> list = null;
	List<GcEntry> other_list=null;

	if (args.length == 0) {
	    System.err.println("Usage: Scan FB.TXT [ GOOGLE.XML|GOOGLE.TXT ]");
	    System.exit(1);
	}
	

	try {
	    list = scanFile(args[0]);
	    if ( ! list.get(0).has_fb_profile() ) {
		System.err.println("Entry is missing Facebook UID");
		System.exit(1);
	    }
	    
	    if (args.length > 1) {
		String of = args[1];
		
		if (of.matches(".*.xml"))
		    other_list = GcXmlParser.scanFile(args[1]);
		else
		    //other_list = scanFile(args[1]);
		    System.err.println("Google data must be xml");
		
		System.out.println("Found " + other_list.size()+ " gc entries");
		
		if ( other_list.get(0).g_id == null ) {
		    System.err.println("Entry is missing Google ID");
		    System.exit(1);
		}
	    }
	}
	
	catch (Exception e) {
	    System.err.println( e.getMessage() );
	}
	
	if (other_list == null) {
	    for (Entry ent : list ) {
		if (ent.name != null)
		    System.out.println(ent);
	    }
	}
	
	//TODO - check for dup name
	if (other_list != null) {
	    for (FbEntry fb_ent : list) {
		if (fb_ent.name == null) continue;
		boolean have_match = false;
		
		//first check profile matches
		for (GcEntry g_ent : other_list) {
		    if (fb_ent.matches_profile(g_ent)){
			have_match=true;
			break;
		    }
		}
		if (have_match) {
		    System.out.println("Match: " + fb_ent.name);
		    continue;
		}
		
		for (GcEntry g_ent : other_list) {
		    if (g_ent.name == null) continue;
		    //if (g_ent.name.equals(fb_ent.name)) {
		    if (g_ent.matches_name(fb_ent)){
			
			//linkedin may have first last (LinkedIn...)
			//probably nothing like that in fb friends, though
			System.out.println(fb_ent.name);
		    
			Entry merged = MergeEntries.make_entry(fb_ent,g_ent);
			if (! merged.last_name.equals("")) {
			    System.out.println
				("   " +
				 g_ent.first_name + "->" + merged.first_name +
				 " ; " +
				 g_ent.last_name + "->" + merged.last_name
				 );
			}
			
			if (merged.picture != null)
			    System.out.println("  need pic: " + merged.picture);

			Properties edit = new Properties();

			//if ( ! merged.first_name.equals(g_ent.first_name) )
			if ( ! "".equals(merged.first_name) )
			    edit.setProperty("first-name", merged.first_name);
			//if ( ! merged.last_name.equals(g_ent.last_name) )
			if ( ! "".equals(merged.last_name) )
			    edit.setProperty("last-name", merged.last_name);
			    
			if ( fb_ent.has_fb_profile() && ! g_ent.has_fb_profile() ) {
			    edit.setProperty("facebook", fb_ent.fb_profile());
			    //edit.setProperty("fb-uid", fb_ent.uid);
			}
			
			if (edit.size() > 0 ) {
			    
			    edit.setProperty("edit", g_ent.g_id);
			    //edit.setProperty("old_name", g_ent.name);
			    try {
				edit.store(
					   new FileOutputStream("edit-" + fb_ent.last_name + ".txt"),
					   "Edit for " + fb_ent.name()
					   );
			    }
			    catch (Exception e) {
				System.err.println( "Failed to write edit:" + e.getMessage());
			    }
			}
			
				   

		    }
		    //exact match on first/last

		    else {
			String match_rule = MergeEntries.loose_name_match(fb_ent,g_ent);
			if (match_rule != null) {
			    
			    System.out.println(match_rule + " match: " + g_ent.name + "=" + fb_ent.name);
			    Properties edit = new Properties();
			    
			    if ( ! fb_ent.first_name.equals( g_ent.first_name ) )
				edit.setProperty( "first-name", fb_ent.first_name );
			    if ( ! fb_ent.last_name.equals( g_ent.last_name ) )
				edit.setProperty( "last-name", fb_ent.last_name );
			    if ( ! fb_ent.name.equals( g_ent.name ) )
				edit.setProperty( "full-name", fb_ent.name );
			    if ( ! fb_ent.fb_profile().equals( g_ent.fb_profile() ) )
				edit.setProperty( "facebook", fb_ent.fb_profile() );
			    

			    if (edit.size() > 0 ) {
				edit.setProperty("edit", g_ent.g_id);
				try {
				    edit.store(
					       new FileOutputStream("edit-" + fb_ent.name.toLowerCase().replaceAll(" ", "-") + ".txt"),
					       "Edit for " + fb_ent.name()
					       );
				}
				catch (Exception e) {
				    System.err.println( "Failed to write edit:" + e.getMessage());
				}
			    }
			    
			}

		    }

		    
		}
	    }
	}
    }
}
