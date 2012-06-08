package timr.activities;

import java.util.Calendar;

import timr.android.R;
import timr.model.MainModel;
import timr.model.timetable.Days;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ListView;
import android.widget.Toast;

public class TimeActivity extends Activity implements Runnable{
	
	private String username;
	private String pass;
	private ProgressDialog progress;
	private TimeActivity instance;
	private ViewPager pager;
	private TimrPageAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Created Time");
		instance = this;
		setContentView(R.layout.pager);

		pager = (ViewPager) findViewById(R.id.main_pager);
		adapter = new TimrPageAdapter();

		progress = new ProgressDialog(this);
		
		startLoad("Adrian2", null);
	}
	
	public void startLoad(String u, String p){
		username = u;
		pass = p;
		progress = ProgressDialog.show(this, "Working..", "Retrieving data from sever", true,
                false);
		 Thread thread = new Thread(this);
         thread.start();
	}

	@Override
	public void run() {
		MainModel.getInstance().loadStudInfo(username, pass);
		handler.sendEmptyMessage(0);
	}
	
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                progress.dismiss();
                setPager();
        }
	};
	
	private void setPager(){
		pager.setAdapter(adapter);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int[] days = {R.id.time_list_sunday, R.id.time_list_monday, 
						R.id.time_list_tuesday,
						R.id.time_list_wednesday, R.id.time_list_thursday,
						R.id.time_list_friday, R.id.time_list_saturday
						};
				TimeListAdapter.addAdapter(instance, (ListView) instance.findViewById(days[arg0]),
						MainModel.getInstance().timetable.map.get(Days.values()[arg0]));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		pager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1);

	}
}
