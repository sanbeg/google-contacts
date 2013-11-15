package my;

import java.util.ArrayList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import my.Entry;

public class GcXmlParser {

    public static void print_node(Entry ent, Node n){
	Pattern facebook_re = Pattern.compile("http://www\\.facebook\\.com/");
	
	NodeList children = n.getChildNodes();
	for (int i=0; i<children.getLength(); ++i){
	    Node cn = children.item(i);
	    String name = cn.getNodeName();
	    NamedNodeMap a = cn.getAttributes();

	    if (name.equals("fn"))
		ent.name = cn.getTextContent();
	    
	    else if (name.equals("name")||name.equals("section"))
		print_node(ent, cn);
	    else if (name.equals("given"))
		ent.first_name=cn.getTextContent();
	    else if (name.equals("family"))
		ent.last_name = cn.getTextContent();
	    else if (name.equals("photo"))
		ent.picture = a.getNamedItem("href").getNodeValue();
	    
	    else if (name.equals("website")) {
		
		String profile = a.getNamedItem("href").getNodeValue();
		Matcher match = facebook_re.matcher(profile);
		if (match.lookingAt())
		    ent.fb_profile = profile;
		
	    }
	    
	    else if (
		     name.equals("email")
		     || name.equals("phone")
		     ){
		if (true) continue;
		
		System.out.println("\t"+name+"\t"+cn.getTextContent());				
		if (a.getNamedItem("primary") != null)
		    System.out.println("\t\tprimary");
		Node rel = a.getNamedItem("rel");
		if (rel != null) {
		    String rel_s = rel.getNodeValue();
		    System.out.println("\t\t" + 
				       rel_s.substring(rel_s.indexOf('#')+1,rel_s.length()-1));
		}
	    }
	}
    }
    
    
    public static ArrayList<Entry> scan_file(String file)
	throws java.io.IOException, 
	       java.io.FileNotFoundException,
	       ParserConfigurationException,
	       SAXException
    {
	DocumentBuilderFactory docFactory 
	    = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder=docFactory.newDocumentBuilder();
	InputStream is=new FileInputStream(file);
	Document doc = builder.parse(is);
	
	NodeList nl = doc.getElementsByTagName("contact");
	ArrayList<Entry> list = new ArrayList<Entry>();
	
	for (int i=0; i<nl.getLength(); ++i) {
	    Entry e = new Entry();
	    
	    Node n = nl.item(i);
	    NamedNodeMap a = n.getAttributes();
	    e.g_id = a.getNamedItem("id").getNodeValue();
	    print_node(e,n);
	    list.add(e);
	}

	return list;
    }
    
    

    public static void main(String [] args){
	try {
	    scan_file(args[0]);
	    
	} catch (ParserConfigurationException e) {
	// TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }
}




