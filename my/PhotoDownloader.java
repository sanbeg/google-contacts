package my;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.data.Link;


import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

import java.util.Properties;

public class PhotoDownloader 
{
    private ContactsService service_;
    private String prefix_;
    private boolean do_replace_;
    
    public PhotoDownloader(ContactsService service, Properties conf) 
    {
	service_ = service;
	prefix_ = conf.getProperty("photo-prefix");
	do_replace_ = ("yes".equals(conf.getProperty("photo-replace")));
    }

    public boolean download(Link photoLink)
    //throws ServiceException, IOException {
    {
	String url = photoLink.getHref();
	String image=prefix_ + url.substring( url.lastIndexOf('/')+1  ) + ".jpg";
	
	byte[] buffer = new byte[4096];

	if ( !do_replace_ && new File(image).exists())
	    return true;
	
	try {
	    GDataRequest request = service_.createLinkQueryRequest(photoLink);
	    try {
		request.execute();
		InputStream in = request.getResponseStream();
		//InputStream in = service.getStreamFromLink(photoLink);
		FileOutputStream file = new FileOutputStream(image);
		try {
		    for (;;) {
			int n = in.read(buffer);
			if (n < 0) break;
			file.write(buffer, 0, n);
		    }
		    
		    file.close();
		}
		catch (Exception e) {
		    new File(image).delete();
		    throw(e);
		}
	    }
	    finally {
		request.end();
	    }
	}
	catch (Exception e) {
	    System.err.println( "Image download failed: " + e.getMessage() );
	    return false;
	}
	    
	return true;
    }
}
