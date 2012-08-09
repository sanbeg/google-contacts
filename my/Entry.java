package my;

public class Entry 
{
    public long fb_uid=0;
    public String fb_username="";
    public String name=null;
    public String first_name="";
    public String last_name="";
    public String picture=null;
    public String g_id=null;
    
    public String toString() 
    {
	//return username;
	String rv = "";
	if (fb_username != null)
	    rv += fb_username + "  (" + fb_uid + ")\n";
	if (g_id != null)
	    rv += g_id + "\n";
	
	rv += "  " + first_name + " " + last_name + " (" + name + ")\n";
	if (picture != null)
	    rv += "  " + picture + "\n";
	
	return rv;
    }
    
    public String fb_profile() 
    {
	StringBuilder rv = new StringBuilder("http://www.facebook.com/");
	if (fb_username != null)
	    rv.append(fb_username);
	else if (fb_uid != 0) 
	    rv.append(fb_uid);
	else
	    return null;
	return rv.toString();
    }
    public boolean has_fb_profile() 
    {
	return fb_uid != 0;
    }
    

    public boolean matches_name(final Entry other) 
    {
	if (name == null) return false;
	if (name.equals(other.name)) return true;
	return false;
    }

    public boolean matches_profile( final Entry other ) 
    {
	return false;
    }
    

    public static Entry make_merge (final Entry fb_ent, final Entry g_ent) 
    {
	Entry rv = new Entry();
	
	rv.g_id = g_ent.g_id;
	
	/*
	 *gmail may have combined them, so copy fb names
	 * to gmail.
	 */
	if (fb_ent.name.equals(g_ent.last_name)) {
	    rv.first_name = fb_ent.first_name;
	    rv.last_name = fb_ent.last_name;
	}
	if (g_ent.picture == null)
	    rv.picture = fb_ent.picture;
	
	return rv;
    }
    
    

};
