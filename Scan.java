import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.Entry;

public class Scan 
{

    private static ArrayList<Entry> scan_file(String file)
	throws java.io.IOException, java.io.FileNotFoundException
    {
	BufferedReader reader = null;
	ArrayList<Entry> list = new ArrayList<Entry>();
	Entry cur = null;
	
	Pattern pattern = Pattern.compile("^([0-9]*)\\s+(\\w+)\\s+(.+)");
	
	try {
	    reader = new BufferedReader(new FileReader(file));
	    
	    String s;
	    reader.readLine(); //first line is useless heading.
	    
	    while ((s=reader.readLine()) != null){
		Matcher matcher = pattern.matcher(s);
		if (matcher.matches()) {
		    //System.err.println("got line");
		    
		    if (! matcher.group(1).equals("")){
			cur = new Entry();
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
		    //System.err.println("EOL");
		    //add some fake google entries
		    else if (key.equals("g_id"))
			cur.g_id = val.substring(val.lastIndexOf("/")+1);
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
	ArrayList<Entry> list = null, other_list=null;
	try {
	    list = scan_file(args[0]);
	    if (args.length > 1)
		other_list = scan_file(args[1]);
	    
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
	    for (Entry fb_ent : list) {
		if (fb_ent.name == null) continue;
		boolean have_match = false;
		
		//first check profile matches
		for (Entry g_ent : other_list) {
		    if (fb_ent.matches_profile(g_ent)){
			have_match=true;
			break;
		    }
		}
		if (have_match)
		    continue;
		
		for (Entry g_ent : other_list) {
		    if (g_ent.name == null) continue;
		    //if (g_ent.name.equals(fb_ent.name)) {
		    if (g_ent.matches_name(fb_ent)){
			
			//linkedin may have first last (LinkedIn...)
			//probably nothing like that in fb friends, though
			System.out.println(fb_ent.name);
		    
			/*
			 *gmail may have combined them, so copy fb names
			 * to gmail.
			 */
			if (fb_ent.name.equals(g_ent.last_name)) {
			    System.out.println
				("   " +
				 g_ent.first_name + "->" + fb_ent.first_name +
				 " ; " +
				 g_ent.last_name + "->" + fb_ent.last_name
				 );
			}
			
			    
			if (g_ent.picture == null)
			    System.out.println("  need pic: " + fb_ent.picture);
			

		    }
		    
		}
	    }
	}
	
    }
}
