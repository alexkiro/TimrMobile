/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timr.timrutil;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Provides several utilities used throughout the application
 * @author Kiro
 */
public final class Utils {    

    public static String compareDate(Date d1) {
        Date time = Calendar.getInstance().getTime();
        if (d1.getYear() == time.getYear()) {
            if (d1.getMonth() == time.getMonth()) {
                if (d1.getDay() == time.getDay()) {
                    if (d1.getHours() == time.getHours()) {
                        if (d1.getMinutes() == time.getMinutes()) {
                            if (d1.getSeconds() == time.getSeconds()) {
                                return "just now...";
                            } else {
                                int dif = time.getSeconds() - d1.getSeconds();
                                if (dif == 1) {
                                    return "one second ago...";
                                } else {
                                    return dif + " seconds ago...";
                                }
                            }
                        } else {
                            int dif = time.getMinutes() - d1.getMinutes();
                            if (dif == 1) {
                                return "one minute ago...";
                            } else {
                                return dif + " minutes ago...";
                            }
                        }
                    } else {
                        int dif = time.getHours() - d1.getHours();
                        if (dif == 1) {
                            return "one hour ago...";
                        } else {
                            return dif + " hours ago...";
                        }
                    }
                } else {
                    int dif = time.getDay() - d1.getDay();
                    if (dif == 1) {
                        return "one day ago...";
                    } else {
                        return dif + " days ago...";
                    }
                }
            } else {
                int dif = time.getMonth() - d1.getMonth();
                if (dif == 1) {
                    return "one month ago...";
                } else {
                    return dif + " months ago...";
                }
            }
        } else {
            int dif = time.getYear() - d1.getYear();
            if (dif == 1) {
                return "one year ago...";
            } else {
                return dif + " years ago...";
            }
        }        
    }

    public static String wrapMessage(String message, int line) {
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(message);

        int count = 0;
        while (st.hasMoreTokens()) {
            String string = st.nextToken();
            count += string.length();
            if (count > line) {
                sb.append("<br>" + string + " ");
                count = string.length() + 1;
            } else if (count == line) {
                sb.append(string + "<br>");
                count = 0;
            } else {
                sb.append(string + " ");
                count++;
            }
        }

        return sb.toString();
    }
}
