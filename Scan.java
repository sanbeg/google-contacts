import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scan 
{

    public static class Entry 
    {
	public long uid=0;
	public String username="";
	public String name=null;
	public String first_name="";
	public String last_name="";
	public String picture="";

	public String toString() 
	{
	    //return username;
	    String rv = "";
	    rv += username + "\t" + uid + "\n";
	    rv += "  " + first_name + " " + last_name + " (" + name + ")\n";
	    rv += "  " + picture + "\n";
	    
	    return rv;
	}
	

    };
    
    
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
			cur.uid = Long.parseLong(val);
		    else if (key.equals("name"))
			cur.name = val;
		    else if (key.equals("first_name"))
			cur.first_name = val;
		    else if (key.equals("last_name"))
			cur.last_name = val;
		    else if (key.equals("username"))
			cur.username = val;
		    else if (key.equals("pic_big"))
			cur.picture = val;
		    //System.err.println("EOL");
		    
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
	
	if (other_list != null) {
	    for (Entry fb_ent : list) {
		if (fb_ent.name == null) continue;
		for (Entry g_ent : other_list) {
		    if (g_ent.name == null) continue;
		    if (g_ent.name.equals(fb_ent.name)) {
			//linkedin may have first last (LinkedIn...)
			//probably nothing like that in fb friends, though
			System.out.println(fb_ent.name);
		    
			/*
			 *gmail may have combined them, so copy fb names
			 * to gmail.
			 */
			if (fb_ent.first_name.equals(g_ent.first_name) &&
			    fb_ent.last_name.equals(g_ent.last_name))
			    //System.out.println("sn:"+fb_ent.name);
			    ;
			else {
			    System.out.println(g_ent.first_name + " - " + g_ent.last_name);
			}

			if ("".equals(g_ent.picture))
			    System.out.println("  need pic");
			

		    }
		    
		}
	    }
	}
	
    }
}
