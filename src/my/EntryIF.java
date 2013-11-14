package my;

public interface EntryIF
{
    public String toString();
    public String name();
    public String first_name();
    public String last_name();
    
    public void set_first_name(String s);
    public void set_last_name(String s);

    public String fb_profile();
    public boolean has_fb_profile();
    public boolean has_picture();
    public void set_picture(String s);
    
    public String picture();
    public boolean matches_name(final EntryIF other);
    public boolean matches_profile( final EntryIF other);
}


	