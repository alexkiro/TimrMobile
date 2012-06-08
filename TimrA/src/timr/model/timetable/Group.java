package timr.model.timetable;

import org.w3c.dom.Element;
import timr.xml.XmlParser;

public class Group {
	public String year;
	public String halfYear;
	public int groupNumber;

	public Group(String year, String halfYear, int groupNumber) {
		this.year = year;
		this.halfYear = halfYear;
		this.groupNumber = groupNumber;
	}

	public Group(Element e) {

		year = XmlParser.retreive(e, "yearOfStudy");
		halfYear = XmlParser.retreive(e, "halfYearOfStudy");
		try {
			groupNumber = Integer
					.parseInt(XmlParser.retreive(e, "groupNumber"));
		} catch (NumberFormatException ex) {

		}
	}

	public Group() {

	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		} else {
			if (obj instanceof Group) {
				Group g = (Group) obj;
				return ((year == g.year) && (halfYear == g.halfYear) && (groupNumber == g.groupNumber));
			} else {
				return false;
			}
		}

	}

	@Override
	public String toString() {
                String str = "";
                if (!year.equals("0")){
                    str+=year;
                } 
                if (!halfYear.equals("None") && !halfYear.equals("0")){
                    str+=halfYear;
                }
                if (groupNumber!=0){
                    str+=groupNumber;
                }
                return str;
	}
}
