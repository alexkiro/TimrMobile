package timr.activities;

import timr.android.R;
import timr.model.MainModel;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

public class FeedActivity extends Activity implements Runnable{
	
	private ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_list);		
		
		progress = new ProgressDialog(this);
		
		startLoad();		
	}
	
	public void startLoad(){
		progress = ProgressDialog.show(this, "Working..", "Retrieving data from sever", true,
                false);
		 Thread thread = new Thread(this);
         thread.start();
	}

	@Override
	public void run() {
		MainModel.getInstance().loadUserNotificatons();
		handler.sendEmptyMessage(0);
	}
	
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
                progress.dismiss();
                setAdapter();
        }
	};
	
	public void setAdapter(){
		ListView list = (ListView)findViewById(R.id.feed_list);
		FeedListAdapter.addAdapter(this, list, MainModel.getInstance().messages);
	}
}
