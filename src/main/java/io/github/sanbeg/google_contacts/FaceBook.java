package io.github.sanbeg.google_contacts;

import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.DefaultFacebookClient;

class FqlUser 
{
    @Facebook String uid;
    @Facebook String name;
    @Facebook String first_name;
    @Facebook String last_name;
    @Facebook String username;
    @Facebook String big_pic;
}


public class FaceBook 
{
    
    public static void main ( String [] args ) 
    {
        Config conf = null;
        try {
            conf = new Config(args[0]);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        

        FacebookClient facebookClient = new DefaultFacebookClient(conf.string("fb-token"));

        //String query = "SELECT uid,name,first_name,last_name,username,pic_big FROM user WHERE uid IN [flist]";

        String query = "SELECT uid,name,first_name,last_name,username,pic_big FROM user WHERE uid in (SELECT uid2 FROM friend WHERE uid1 = me())";
        
        java.util.List<FqlUser> users = facebookClient.executeFqlQuery(query, FqlUser.class);

        for ( FqlUser user : users ) {
            System.out.println( user.username );
        }
        
    }
}
