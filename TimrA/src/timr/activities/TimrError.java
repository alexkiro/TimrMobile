package timr.activities;

import android.widget.Toast;

public class TimrError {
	public static void show(String message){
		//Toast.makeText(TimrAActivity.instance,message,Toast.LENGTH_SHORT).show();
		System.out.println(message);
	}
}
