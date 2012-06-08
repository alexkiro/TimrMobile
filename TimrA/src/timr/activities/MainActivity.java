package timr.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Intent intentSend = new Intent(this,TimrAActivity.class);
	        //Intent intentSend = new Intent(this,SendActivity.class);
	        this.startActivity(intentSend);
	 }
}
