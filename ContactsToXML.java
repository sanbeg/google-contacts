import com.google.gdata.client.*;
import com.google.gdata.client.contacts.*;
import com.google.gdata.data.*;
import com.google.gdata.data.contacts.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.IOException;
import java.net.URL;

import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.ContactEntry;



import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Properties;
import java.io.FileInputStream;


class ContactsToXML
{
   
    public static Properties get_properties(String filename) 
    {
	
	Properties props = new Properties();
	try {
	    FileInputStream propfile = new FileInputStream(filename);
	    props.load(propfile);
	    propfile.close();
	}
	catch (Exception e) {
	    System.err.println(e.getMessage());
	    System.exit(1);
	}
	return props;
	
    }
 
    public static void writeDoc(Document doc, String file) 
	throws javax.xml.transform.TransformerException
    {
	// write the content into xml file
	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();
	DOMSource source = new DOMSource(doc);
	StreamResult result;
	
	if (file != null && file != "")
	     result = new StreamResult(new File(file));
	else
	    // Output to console for testing
	     result = new StreamResult(System.out);
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	transformer.transform(source, result);
    }
    

    private static class TextContainer
    {
	private Document document_;
	private Element parent_;
	public TextContainer(Document doc, Element elem) 
	{
	    document_=doc;
	    parent_=elem;
	}
	public Element node(String name, 
			    com.google.gdata.data.ValueConstruct data)
	{
	    return node(name,data.getValue());
	}

	public Element node(String name, String text)
	{
	    Element elem = document_.createElement(name);
	    elem.appendChild(document_.createTextNode(text));
	    parent_.appendChild(elem);
	    return elem;
	}
    }
    
	    
    public static void main(String[] args) {

	ContactsService myService = new ContactsService("x");
	Properties conf = get_properties("steve.conf");
	
	try {

	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

	    Document doc = docBuilder.newDocument();

	    myService.setUserCredentials(conf.getProperty("gmail"), conf.getProperty("pass"));
	    printAllContacts(myService, doc);

	    writeDoc(doc,"");
	}
	catch (Exception e) {
	    System.err.println( e.getMessage() );
	}
    
    }

    public static Document printAllContacts(ContactsService myService, Document doc)
	throws ServiceException, IOException {
	// Request the feed
	URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");

	Query myQuery = new Query(feedUrl);
	myQuery.setMaxResults(1000);
	ContactFeed resultFeed = myService.query(myQuery, ContactFeed.class);

	Element rootElement = doc.createElement("contacts");
	doc.appendChild(rootElement);
	rootElement.setAttribute("title", resultFeed.getTitle().getPlainText());
	rootElement.setAttribute("id", resultFeed.getId());
	//should call setTzShift to get local time
	//rootElement.setAttribute("updated", resultFeed.getUpdated().toUiString());
	rootElement.setAttribute("updated", resultFeed.getUpdated().toStringRfc822());


	for (ContactEntry entry : resultFeed.getEntries()) {
	    Element contactElement = doc.createElement("contact");
	    rootElement.appendChild(contactElement);
	    
	    //should we strip quotes, or move this to a text node?
	    contactElement.setAttribute("etag", entry.getEtag() );
	    contactElement.setAttribute("id", entry.getId() );
	    
	    if (entry.hasName()) {
		Name name = entry.getName();
		if (name.hasFullName()) {
		    new TextContainer(doc,contactElement).
			node("fn",name.getFullName().getValue());
		}

		Element nameElem = doc.createElement("name");
		TextContainer text=new TextContainer(doc,nameElem);

		if (name.hasNamePrefix())
		    text.node("prefix",name.getNamePrefix().getValue());
		if (name.hasGivenName())
		    text.node("given", name.getGivenName().getValue());
		if (name.hasAdditionalName()) 
		    text.node("additional",name.getAdditionalName().getValue());
		if (name.hasFamilyName())
		    text.node("family",name.getFamilyName().getValue());
		if (name.hasNameSuffix())
		    text.node("suffix",name.getNameSuffix().getValue());
		contactElement.appendChild(nameElem);
	    } else {
		continue;
	    }
	    
	    if (entry.hasPhoneNumbers()) {

		Element se = doc.createElement("section");
		se.setAttribute("tag", "phone");
		contactElement.appendChild(se);


		for (PhoneNumber phone : entry.getPhoneNumbers()){
		    Element phoneElem = doc.createElement("phone");
		    se.appendChild(phoneElem);
		    
		    phoneElem.appendChild(doc.createTextNode(phone.getPhoneNumber()));

		    
		    if (phone.getRel() != null) {
			phoneElem.setAttribute("rel", phone.getRel());
		    }
		    if (phone.getLabel() != null) {
			phoneElem.setAttribute("label", phone.getLabel());
		    }
		    if (phone.getPrimary()) {
			phoneElem.setAttribute("primary", "yes");
		    }
		}
	    }
	    
	    
	    if (entry.hasEmailAddresses()) {

		Element se = doc.createElement("section");
		se.setAttribute("tag", "email");
		contactElement.appendChild(se);

		for (Email email : entry.getEmailAddresses()) {
		    Element emailElem = doc.createElement("email");
		    se.appendChild(emailElem);
		    
		    emailElem.appendChild(doc.createTextNode(email.getAddress()));
		    
		    if (email.getRel() != null) {
			emailElem.setAttribute("rel", email.getRel());
		    }
		    if (email.getLabel() != null) {
			emailElem.setAttribute("label", email.getLabel());
		    }
		    if (email.getPrimary()) {
			emailElem.setAttribute("primary", "yes");
		    }
		}
	    }
	    


	    for (PostalAddress pa : entry.getPostalAddresses() ) {
		Element paElem = doc.createElement("address");
		contactElement.appendChild(paElem);
		
		paElem.appendChild(doc.createTextNode(pa.getValue()));
		
		if (pa.getRel() != null) {
		    paElem.setAttribute("rel", pa.getRel());
		}
		if (pa.getLabel() != null) {
		    paElem.setAttribute("label", pa.getLabel());
		}
		if (pa.getPrimary()) {
		    paElem.setAttribute("primary", "yes");
		}
	    }
	    for (StructuredPostalAddress pa : entry.getStructuredPostalAddresses() ) {
		Element paElem = doc.createElement("address");
		contactElement.appendChild(paElem);
		TextContainer text = new TextContainer(doc,paElem);
		
		if (pa.hasStreet())
		    text.node("street", pa.getStreet());
		if (pa.hasPobox())
		    text.node("pobox", pa.getPobox());
		if (pa.hasNeighborhood())
		    text.node("neighborhood", pa.getNeighborhood());
		if (pa.hasCity())
		    text.node("city", pa.getCity());
		if (pa.hasRegion())
		    text.node("state",pa.getRegion());
		if (pa.hasPostcode())
		    text.node("zip",pa.getPostcode());
		if (pa.hasCountry())
		    text.node("country", pa.getCountry().getValue());
		


		if (pa.getRel() != null) {
		    paElem.setAttribute("rel", pa.getRel());
		}
		if (pa.getLabel() != null) {
		    paElem.setAttribute("label", pa.getLabel());
		}
		if (pa.getPrimary()) {
		    paElem.setAttribute("primary", "yes");
		}
	    }

	    /*
	    System.out.println("IM addresses:");
	    for (Im im : entry.getImAddresses()) {
		System.out.print(" " + im.getAddress());
		if (im.getLabel() != null) {
		    System.out.print(" label:" + im.getLabel());
		}
		if (im.getRel() != null) {
		    System.out.print(" rel:" + im.getRel());
		}
		if (im.getProtocol() != null) {
		    System.out.print(" protocol:" + im.getProtocol());
		}
		if (im.getPrimary()) {
		    System.out.print(" (primary) ");
		}
		System.out.print("\n");
	    }
	    */

	    if (entry.hasBirthday())
		new TextContainer(doc,contactElement).
		    node("birthday", entry.getBirthday().getWhen());
	    

	    for (GroupMembershipInfo group : entry.getGroupMembershipInfos()) {

		Element groupElem = doc.createElement("group");
		contactElement.appendChild(groupElem);
		groupElem.setAttribute("href", group.getHref());
	    }
	    /*
	    System.out.println("Extended Properties:");
	    for (ExtendedProperty property : entry.getExtendedProperties()) {
		if (property.getValue() != null) {
		    System.out.println("  " + property.getName() + "(value) = " +
				       property.getValue());
		} else if (property.getXmlBlob() != null) {
		    System.out.println("  " + property.getName() + "(xmlBlob)= " +
				       property.getXmlBlob().getBlob());
		}
	    }
	    */
	    Link photoLink = entry.getContactPhotoLink();
	    String photoLinkHref = photoLink.getHref();
	    //System.out.println("Photo Link: " + photoLinkHref);
	    if (photoLink.getEtag() != null) {
		//System.out.println("Contact Photo's ETag: " + photoLink.getEtag()); 
		Element photoElem = doc.createElement("photo");
		contactElement.appendChild(photoElem);
		photoElem.setAttribute("href", photoLink.getHref());
		
	    }
	}
	return doc;
    }
    


    
}
