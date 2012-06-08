package timr.model.user;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import timr.model.timetable.Group;

public class Stud extends User{
	public LinkedList<Group> group = new LinkedList<Group>();
        public LinkedList<String> faculties = new LinkedList<String>();
        public Map<String,String> subjects = new HashMap<String, String>();
	
	public Stud(){
		userType=UserType.Student;
	}

}
