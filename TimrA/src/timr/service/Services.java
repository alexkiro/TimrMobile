package timr.service;

import java.util.List;
import java.util.Map;

import timr.activities.TimrError;
import timr.model.messages.Message;
import timr.model.timetable.Timetable;
import timr.model.user.Faculty;
import timr.model.user.Prof;
import timr.model.user.Stud;
import timr.model.user.UserType;
import timr.xml.XmlParser;

/**
 * Class that provides implemented interfaces for server's web services
 * 
 * @author Kiro
 */
public class Services {
	static final String NAMESPACE = "http://tempuri.org/";
	static final String URL = "http://ms.info.uaic.ro/webservice/TimrService.asmx";
	static final String XmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
	static final String ValidateUser = "ValidateUser";
	static final String InsertUser = "InsertUser";
	static final String TimetableBachelor = "GetTimetableForBachelorYear";
	static final String TimetableMasters = "GetTimetableForMastersYear";
	static final String TimetableUser = "GetTimetableForUser";
	static final String StudentXML = "GetStudentAsXml";
	static final String SubjectsXML = "GetAllMonitoredWebsitesAsXml";

	static final String StudentNotifications = "GetStudentNotificationsAsXml";
	static final String GetAllGroups = "GetAllGroups";
	static final String TeacherFaculties = "GetFaculties";
	static final String TeacherXML = "GetTeacherAsXml";
	static final String InsertFeed = "InsertFeed";
	static final String InsertSite = "InsertWebsite";
	static final String UpdateSite = "SaveWebsite";
	static final String SolveNotification = "SolveUserNotification";

	static public void solveNotification(String user, String _id) {
		try {
			new SOAPClient(URL, SolveNotification, NAMESPACE)
					.addParameter("username", user)
					.addParameter("notificationId", _id).call();
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
	}

	static public void updateSite(String student, String oldLink, String newLink) {
		try {
			new SOAPClient(URL, UpdateSite, NAMESPACE)
					.addParameter("username", student)
					.addParameter("oldLink", oldLink)
					.addParameter("newLink", newLink).call();
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
	}

	static public void insertFeed(String faculty, String teacher,
			List<String> groups, String message) {
		try {
			new SOAPClient(URL, InsertFeed, NAMESPACE)
					.addParameter("username", "")
					// TODO: remove when necessary
					.addParameter("faculty", faculty)
					.addParameter("feed",
							XmlParser.createFeed(teacher, groups, message))
					.call();
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
	}

	static public Prof getTeacherXML(String teacher) {
		try {
			return XmlParser.parseProf(XmlHeader
					+ new SOAPClient(URL, TeacherXML, NAMESPACE).addParameter(
							"teacherId", teacher).call());
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
		return null;
	}

	static public List<Faculty> getTeacherFaculties(String teacher) {
		try {
			return XmlParser.parseFaculties(XmlHeader
					+ new SOAPClient(URL, TeacherFaculties, NAMESPACE)
							.addParameter("teacherId", teacher).call());
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
		return null;
	}

	static public Map<String, String> getGroups(String faculty) {
		try {
			return XmlParser.parseGroups(XmlHeader
					+ new SOAPClient(URL, GetAllGroups, NAMESPACE)
							.addParameter("facultyId", faculty).call());
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
		return null;
	}

	static public void insertSite(String user, String website, String subjectId) {
		try {

			new SOAPClient(URL, InsertSite, NAMESPACE)
					.addParameter("studentId", user)
					.addParameter("websiteLink", website)
					.addParameter("subjectId", subjectId).call();
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
	}

	static public List<Message> getStudentNotifications(String user, int count,
			boolean unsolved) {
		try {
			return XmlParser.parseStudentNotifications(XmlHeader
					+ new SOAPClient(URL, StudentNotifications, NAMESPACE)
							.addParameter("username", user)
							.addParameter("count", String.valueOf(count))
							.addParameter("getOnlyUnsolvedNotifications",
									String.valueOf(unsolved)).call());
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
		return null;
	}

	static public boolean validateUser(String user, String pass) {
		try {
			String str = new SOAPClient(URL, ValidateUser, NAMESPACE)
					.addParameter("username", user)
					.addParameter("password", pass).call();
			//System.out.println(str);
			//System.out.println(Boolean.parseBoolean(str));
			return Boolean.parseBoolean(str);

		} catch (Exception e) {
			System.out.println("EROAROROAR");
			e.printStackTrace();
			TimrError.show(e.getMessage());
		}
		return false;
	}

	static public Stud getStudentXML(String student) {
		try {

			return XmlParser.parseStudent(XmlHeader
					+ new SOAPClient(URL, StudentXML, NAMESPACE).addParameter(
							"username", student).call());
		} catch (Exception e) {
			// e.printStackTrace();
			TimrError.show(e.getMessage());
		}
		return null;
	}

	static public Map<String, String> getSubjects(String student) {
		try {

			return XmlParser.parseSubjects(XmlHeader
					+ new SOAPClient(URL, SubjectsXML, NAMESPACE).addParameter(
							"username", student).call());
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
		return null;
	}

	static public boolean insertUser(String username, String pass,
			String email, int type) {
		try {
			return Boolean
					.parseBoolean(new SOAPClient(URL, InsertUser, NAMESPACE)
							.addParameter("username", username)
							.addParameter("password", pass)
							.addParameter("email", email)
							.addParameter("userType",
									UserType.fromInt(type).toString()).call());
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
		return false;
	}

	static public Timetable getTimetableForUser(String user, String faculty) {
		try {
			return XmlParser.parseTimetable(XmlHeader
					+ new SOAPClient(URL, TimetableUser, NAMESPACE)
							.addParameter("username", user)
							.addParameter("faculty", faculty).call());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static public Timetable getTimetableForBachelorYear(String year,
			String halfYear) {
		try {
			return XmlParser.parseTimetable(XmlHeader
					+ new SOAPClient(URL, TimetableBachelor, NAMESPACE)
							.addParameter("year", year)
							.addParameter("halfYear", halfYear).call());
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
		return null;
	}

	static public Timetable getTimetableForMastersYear(String year) {
		try {
			return XmlParser.parseTimetable(XmlHeader
					+ new SOAPClient(URL, TimetableMasters, NAMESPACE)
							.addParameter("year", year).call());
		} catch (Exception e) {
			TimrError.show(e.getMessage());
		}
		return null;
	}
}
