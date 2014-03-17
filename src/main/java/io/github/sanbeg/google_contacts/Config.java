package io.github.sanbeg.google_contacts;

import java.util.Properties;
import java.io.FileInputStream;

class Config extends Properties 
{
    public Config(String filename) 
	throws java.io.IOException, java.io.FileNotFoundException
    {
	FileInputStream propfile = new FileInputStream(filename);
	try {
	    this.load(propfile);
	}
	finally {
	    propfile.close();
	}
    }
    
    public String string(String key) 
    {
	return this.getProperty(key);
    }
    

    public int integer(String key)
    {
	return Integer.parseInt(this.getProperty(key));
    }
}
