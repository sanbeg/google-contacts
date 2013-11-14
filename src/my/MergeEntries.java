package my;

import my.Entry;

public class MergeEntries 
{
    
    public static Entry make_entry (final EntryIF fb_ent, final Entry g_ent) 
    {
	Entry rv = new Entry();
	
	rv.g_id = g_ent.g_id;
	three_way_merge(fb_ent,g_ent,rv);
	return rv;
    }


    public static void merge_into (final EntryIF fb_ent, EntryIF g_ent) 
    {
	three_way_merge(fb_ent,g_ent,g_ent);
    }

    static void three_way_merge (final EntryIF fb, final EntryIF gc, EntryIF rv) 
    {
	/*
	 *gmail may have combined them, so copy fb names
	 * to gmail.
	 */
	if (fb.name().equals(gc.last_name())) {
	    rv.set_first_name( fb.first_name() );
	    rv.set_last_name( fb.last_name() );
	}
	if (! gc.has_picture() )
	    rv.set_picture(fb.picture());
    }
    
    public static String loose_name_match(EntryIF fb_ent, EntryIF g_ent) 
    {

	//exact match on first/last
	if (g_ent.first_name.equals(fb_ent.first_name) && g_ent.last_name.equals(fb_ent.last_name)) {
	    return "First/last";
	}
	//try splitting fb_ent.last, matching there

	//if no last name, try first only?
	else if (g_ent.last_name.equals("") 
		 && 
		 g_ent.first_name.equals(fb_ent.first_name)) {
	    return "First only";
	}
	//also try split on "-"?
	else if (fb_ent.last_name.contains(" ") 
		 &&
		 fb_ent.first_name.equals(g_ent.first_name)) {
	    String names[] = fb_ent.last_name.split(" ");
	    if (
		names[names.length-1].equals(g_ent.last_name)
		)			
		return "Split";
	    else
		return null; //FIXME
	}
	else if (g_ent.last_name.equals(fb_ent.last_name)
		 &&
		 g_ent.first_name.charAt(0) == fb_ent.first_name.charAt(0)) {
	    return "Initial";
	}
	else if (g_ent.first_name.equals(fb_ent.first_name)) {
	    //System.out.println("First match: " + g_ent.name + "=" + fb_ent.name);
	}
	return null;
    }
    

}
