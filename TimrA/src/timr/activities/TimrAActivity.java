package timr.activities;

import timr.android.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TimrAActivity extends TabActivity {
	
	public static TimrAActivity instance;
	
    /** Called when the activity is first created. */	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        instance = this;
        
        TabHost tabHost = getTabHost();
        
        TabSpec tsTime = tabHost.newTabSpec("Timetable");
        tsTime.setIndicator("",getResources().getDrawable(R.drawable.timetable));
        Intent intentTime = new Intent(this,TimeActivity.class);
        tsTime.setContent(intentTime);
        
        TabSpec tsFeed = tabHost.newTabSpec("Feeds");
        tsFeed.setIndicator("",getResources().getDrawable(R.drawable.notifications));
        Intent intentFeed = new Intent(this,FeedActivity.class);
        tsFeed.setContent(intentFeed);
        
        tabHost.addTab(tsTime);
        tabHost.addTab(tsFeed);
    }
}