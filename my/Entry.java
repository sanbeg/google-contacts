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
    
};
