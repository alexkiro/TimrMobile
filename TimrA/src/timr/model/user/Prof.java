package timr.model.user;

import java.util.LinkedList;
import java.util.List;

public class Prof extends User{
	public List<Faculty> faculties = new LinkedList<Faculty>();
       
	public Prof(){
		userType = UserType.Teacher;
	}
        
        public Faculty getFacultyByName(String name){
            for (Faculty faculty : faculties){
                if (faculty.name.equals(name)){
                    return faculty;
                }
            }
            return null;
        }
	
}
