package timr.model.timetable;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import timr.xml.XmlParser;

public class TableItem {

    public String startTime;
    public String endTime;
    public String className;
    public String _id;
    public String teacherName;
    public String roomNumber;
    public int classType;
    public int frequency;
    public int optionalPackage;
    public Group group;

    @Override
    public String toString() {
        return (startTime + " " + endTime + " " + className + " " + teacherName
                + " " + roomNumber + " " + className + " _id: " + _id + " " + frequency + " "
                + optionalPackage + " " + group);
    }

    public TableItem(Node n) {
        Element e = (Element) n;
        _id = e.getAttribute("id");
        startTime = XmlParser.retreive(e, "startTime");
        endTime = XmlParser.retreive(e, "endTime");
        className = XmlParser.retreive(e, "className");
        teacherName = XmlParser.retreive(e, "teacherName");
        roomNumber = XmlParser.retreive(e, "roomNumber");
        classType = Integer.parseInt(XmlParser.retreive(e, "classType"));
        frequency = Integer.parseInt(XmlParser.retreive(e, "frequency"));
        optionalPackage = Integer.parseInt(XmlParser.retreive(e, "optionalPackage"));
        group = new Group((Element) e.getElementsByTagName("group").item(0));

    }

    public TableItem() {
    }

    public static TableItem creatTableItem(Node node) {
        TableItem ti = new TableItem();

        return ti;
    }
}
