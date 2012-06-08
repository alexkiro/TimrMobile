/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timr.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import timr.model.messages.Message;
import timr.model.timetable.Timetable;
import timr.model.user.Faculty;
import timr.model.user.Prof;
import timr.model.user.Stud;
import timr.model.user.UserType;
import timr.service.Services;

/**
 *
 * @author kiro
 */
public class MainModel {
    
	public String tempU;
	public String tempP;
	
    public UserType userType;
    public Stud stud = null;
    public Prof prof = null;
    public Timetable timetable = null;
    public List<Message> messages = new LinkedList<Message>();
    public boolean unsolved = true;
    public Date lastUpdate;
    public int updateDelay = 10;
    
    public boolean updateAndCompare(){
        List<Message> msgs = Services.getStudentNotifications(stud.user, 10, unsolved);
        List<Message> newMessages = new LinkedList<Message>();
        for (Message message : msgs) {
            if (message.date.compareTo(lastUpdate) > 0){
                //TODO: add notification here
                
                newMessages.add(message);
            } else {
                break;
            }
        }
        
        if (newMessages.isEmpty()){
            return false;
        } else {
            lastUpdate = newMessages.get(0).date;
            messages.addAll(0, newMessages);
            return true;
        }
        
    }
    
    public void loadStudInfo(String username, String pass){
         
         userType = UserType.Student;
         stud = Services.getStudentXML(username);
         stud.password = pass;
         stud.subjects = Services.getSubjects(username);
         timetable = Services.getTimetableForUser(username,stud.faculties.get(0));
         
    }
    
    public void loadProfInfo(String username, String pass){
        userType = UserType.Teacher;
        prof = Services.getTeacherXML(username);
        prof.password = pass;
        prof.faculties = Services.getTeacherFaculties(username);
        for (Faculty faculty : prof.faculties){
            faculty.groups = Services.getGroups(faculty.name);
        }
        
    }
    
    public void loadUserNotificatons(){
        messages = Services.getStudentNotifications(stud.user, 10, unsolved);
        lastUpdate = messages.get(0).date;
    }
    
    private MainModel() {
    }
    
    public static MainModel getInstance() {
        return MainModelHolder.INSTANCE;
    }
    
    private static class MainModelHolder {

        private static final MainModel INSTANCE = new MainModel();
    }
}
