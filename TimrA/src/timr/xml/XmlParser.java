package timr.xml;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import timr.model.timetable.Days;
import timr.model.user.Stud;
import timr.model.timetable.TableItem;
import timr.model.timetable.Timetable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import timr.model.messages.Feed;
import timr.model.messages.Message;
import timr.model.messages.SiteUpdate;
import timr.model.messages.TimeUpdate;
import timr.model.user.Faculty;
import timr.model.user.Prof;

public class XmlParser {
    
    public static final String SiteUpdate = "MonitoredWebsitesNotification";
    public static final String TimeUpdate = "TimetableNotification";
    public static final String Feed = "FeedNotification";

        public static String createFeed(String teacher, List<String> groups, String message) throws ParserConfigurationException, TransformerConfigurationException, TransformerException{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            Document doc = db.newDocument();
            Element root = doc.createElement("feed");
            doc.appendChild(root);
            
            Element username = doc.createElement("username");
            username.appendChild(doc.createTextNode(teacher));
            root.appendChild(username);
            
            Element grps = doc.createElement("groups");
            root.appendChild(grps);            
            
            for (String string : groups) {
                Element group = doc.createElement("group");
                group.appendChild(doc.createTextNode(string));
                grps.appendChild(group);         
            }
            
            Element msg = doc.createElement("message");
            msg.appendChild(doc.createTextNode(message));
            root.appendChild(msg);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            
            Writer w = new StringWriter();
            StreamResult result =  new StreamResult(w);
            transformer.transform(source, result);            
            
            return w.toString();
        }
    
	public static List<Message> parseStudentNotifications(String document) throws ParserConfigurationException, SAXException, IOException, ParseException {
            List<Message> list = new LinkedList<Message>();
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(document)));

            Element root = doc.getDocumentElement();
            String nodeValue = root.getTextContent();           
            
            NodeList notifications = root.getElementsByTagName("notification");
            
            for (int i = 0; i < notifications.getLength(); i++) {
                Element not = (Element) notifications.item(i);
                Element details = (Element) not.getElementsByTagName("details").item(0);
                String type = details.getAttribute("type");
                
                //  6/7/2012 6:00:08 PM
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
                
                if (type.equals(SiteUpdate)){
                    SiteUpdate site = new SiteUpdate();
                    site.date = format.parse(retreive(not, "dateSent"));
                    site._id = retreive(not, "id");
                    site.solved = Boolean.parseBoolean(retreive(not, "solved"));
                    site.title = retreive(not, "title");
                    site.site = retreive(details, "websiteLink");
                    list.add(site);
                } else if (type.equals(TimeUpdate)) {
                    TimeUpdate time = new TimeUpdate();
                    time.date = format.parse(retreive(not, "dateSent"));
                    time._id = retreive(not, "id");
                    time.solved = Boolean.parseBoolean(retreive(not, "solved"));
                    time.title = retreive(not, "title");
                    time.className = retreive(details, "className");
                    time.classType = retreive(details, "typeOfClass");
                    time.day = retreive(details, "dayOfWeek");
                    time.start = retreive(details, "startTime");
                    time.end = retreive(details, "endTime");
                    time.type = retreive(details, "timetableNotificationType");
                    list.add(time);
                } else if (type.equals(Feed)){
                    Feed feed = new Feed();
                    feed.date = format.parse(retreive(not, "dateSent"));
                    feed._id = retreive(not, "id");
                    feed.solved = Boolean.parseBoolean(retreive(not, "solved"));
                    feed.title = retreive(not, "title");
                    feed.message = retreive(details, "message");
                    feed.sender = retreive(details, "sender");
                    list.add(feed);
                }            
            }         
            
            return list;
        }
	
        
        public static List<Faculty> parseFaculties(String document) throws ParserConfigurationException, SAXException, IOException {
            List<Faculty> list = new LinkedList<Faculty>();
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(document))); //TODO: set from string

            Element root = doc.getDocumentElement();
            
            NodeList groups = root.getElementsByTagName("Id");
            for (int i = 0; i < groups.getLength(); i++) {
                Faculty faculty = new Faculty();
                faculty.name = ((Element)groups.item(0)).getTextContent();
                list.add(faculty);
            }
            
            return list;
        }
        
        public static Map<String,String> parseGroups(String document) throws ParserConfigurationException, SAXException, IOException {
            Map<String,String> map = new HashMap<String, String>();
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(document))); //TODO: set from string

            Element root = doc.getDocumentElement();
            
            NodeList groups = root.getElementsByTagName("group");
            for (int i = 0; i < groups.getLength(); i++) {
                Element group = (Element) groups.item(i);
                String _id = retreive(group, "id");
                String name = retreive(group, "name");
                map.put(name, _id);
            }
            
            return map;
        }
        
        public static Map<String,String> parseSubjects(String document) throws ParserConfigurationException, SAXException, IOException {
            Map<String,String> map = new HashMap<String, String>();
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(document))); //TODO: set from string

            Element root = doc.getDocumentElement();
            
            NodeList websites = root.getElementsByTagName("website");
            for (int i = 0; i < websites.getLength(); i++) {
                Element site = (Element) websites.item(i);
                String _id = retreive(site, "subjectId");
                String name = retreive(site, "link");
                map.put(_id, name);
            }
            
            return map;
        }
	
	public static Stud parseStudent(String document) throws ParserConfigurationException, SAXException, IOException{
		Stud user = new Stud();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(document)));
		
		Element root = doc.getDocumentElement();		
		
		user.email = retreive(root, "email");
		user.name = retreive(root, "name");
		user.user = retreive(root, "username");
                
                Element faculties = (Element)root.getElementsByTagName("faculties").item(0);
                NodeList facultyList = faculties.getElementsByTagName("faculty");
                
                for (int i = 0; i < facultyList.getLength(); i++) {
                    user.faculties.add(facultyList.item(i).getTextContent());
                }

		return user;
	}
        
        public static Prof parseProf(String document) throws ParserConfigurationException, SAXException, IOException{
		Prof user = new Prof();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(document)));
		
		Element root = doc.getDocumentElement();		
		
		user.email = retreive(root, "email");
		user.name = retreive(root, "name");
		user.user = retreive(root, "username");
  
		return user;
	}
	
	public static Timetable parseTimetable(String document) throws ParserConfigurationException, SAXException, IOException {
		Timetable timetable = new Timetable();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(document)));
		
	
		Element root = doc.getDocumentElement();
		
		NodeList nl = root.getElementsByTagName("tableItem");
		
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			TableItem tableItem = new TableItem(n);			
			timetable.map.get(Days.fromString(n.getParentNode().getNodeName())).add(tableItem);
		}
		
		
		return timetable;
	}
	
	
	public static String retreive(Element e, String tagName, int i) {
		try {
			NodeList elementsByTagName = e.getElementsByTagName(tagName);
			Node item = elementsByTagName.item(i);
			String str;
			if (item.getNodeType() == Node.TEXT_NODE) {
				str = item.getNodeValue();
			} else if (item.getNodeType() == Node.ELEMENT_NODE) {
                            if (item.getChildNodes().getLength()!=0){
				str = ((Element) item).getFirstChild().getNodeValue();
                            } else {
                                str = "Empty message";
                            }
			} else {
				str = "";
			}

			return str;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String retreive(Element e, String tagName) {
		return retreive(e, tagName, 0);
	}
	
}
