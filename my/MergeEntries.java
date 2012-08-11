package my;

import my.Entry;

public class MergeEntries 
{
    
    public static Entry make_entry (final Entry fb_ent, final Entry g_ent) 
    {
	Entry rv = new Entry();
	
	rv.g_id = g_ent.g_id;
	three_way_merge(fb_ent,g_ent,rv);
	return rv;
    }


    public static void merge_into (final Entry fb_ent, Entry g_ent) 
    {
	three_way_merge(fb_ent,g_ent,g_ent);
    }

    static void three_way_merge (final Entry fb, final Entry gc, Entry rv) 
    {
	/*
	 *gmail may have combined them, so copy fb names
	 * to gmail.
	 */
	if (fb.name.equals(gc.last_name)) {
	    rv.first_name = fb.first_name;
	    rv.last_name = fb.last_name;
	}
	if (gc.picture == null)
	    rv.picture = fb.picture;
    }
    
}
