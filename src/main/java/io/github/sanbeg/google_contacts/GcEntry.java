package io.github.sanbeg.google_contacts;

public class GcEntry extends Entry 
{
    //for google, just set profile directly
    public String fb_profile=null;
    
    @Override
    public String fb_profile() 
    {
	return fb_profile;
    }
  
    @Override
    public boolean has_fb_profile() 
    {
	return fb_profile != null;
    }
  
}
