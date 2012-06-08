/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timr.model.timetable;

/**
 *
 * @author kiro
 */
public class Subject {

    public String _id;
    public String name;

    public Subject(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public Subject(String name) {
        this(null, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Subject) {
            Subject other = (Subject) obj;
            if (this._id.equals(other._id) && this._id != null && other._id != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
