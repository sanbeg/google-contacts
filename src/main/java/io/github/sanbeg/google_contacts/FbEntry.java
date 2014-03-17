package io.github.sanbeg.google_contacts;


public class FbEntry extends Entry
{
    public long fb_uid=0;
    public String fb_username="";

    @Override
    public String fb_profile() 
    {
	StringBuilder rv = new StringBuilder("http://www.facebook.com/");
	if (fb_username != null && ! "".equals(fb_username) )
	    rv.append(fb_username);
	else if (fb_uid != 0) 
	    rv.append(fb_uid);
	else
	    return null;
	return rv.toString();
    }

    @Override
    public boolean has_fb_profile() 
    {
	return fb_uid > 0;
    }
    
}
