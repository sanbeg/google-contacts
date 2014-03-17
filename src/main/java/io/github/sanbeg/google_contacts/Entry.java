package io.github.sanbeg.google_contacts;

public class Entry
{
    public String name=null;
    public String first_name="";
    public String last_name="";
    public String picture=null;
    public String g_id=null;
    
    public String toString() 
    {
	//return username;
	String rv = "";
	/*
	if (fb_username != null)
	    rv += fb_username + "  (" + fb_uid + ")\n";
	*/
	if ( has_fb_profile() )
	    rv += fb_profile();
	
	if (g_id != null)
	    rv += g_id + "\n";
	rv += "  " + first_name + " " + last_name + " (" + name + ")\n";
	if (picture != null)
	    rv += "  " + picture + "\n";
	
	return rv;
    }
    
    public String fb_profile() 
    {
	return null;
    }
    public boolean has_fb_profile() 
    {
	return false;
    }
    
    public String name() 
    {
	if (name == null)
	    return first_name + " " + last_name;
	else
	    return name;
    }
    public String first_name() 
    {
	return first_name;
    }
    public String last_name() 
    {
	return last_name;
    }
    
    public void set_first_name(String name) 
    {
	first_name = name;
    }
    public void set_last_name(String name) 
    {
	last_name = name;
    }
    
    public boolean matches_name(Entry other) 
    {
	if (name == null) return false;
	if (name.equals(other.name())) return true;
	return false;
    }

    public boolean matches_profile( Entry other ) 
    {
	return false;
    }
    public boolean has_picture() 
    {
	return picture != null;
    }
    public String picture() 
    {
	return picture;
    }
    public void set_picture(String pic)
    {
	picture=pic;
    }
    
};
