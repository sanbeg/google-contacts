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
    @Facebook String pic_big;

    /**Produce a String like the old fbcmd stuff */
    public String toFbcmdString() 
    {
        return String.format("0\tuid\t%s\n",uid)
            + String.format("\tname\t%s\n",name)
            + String.format("\tfirst_name\t%s\n",first_name)
            + String.format("\tlast_name\t%s\n",last_name)
            + String.format("\tpic_big\t%s\n",pic_big);
    }
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

        System.out.println("index\tfield\tvalue\n");
        for ( FqlUser user : users ) {
            System.out.print( user.toFbcmdString() );
        }
        
    }
}
