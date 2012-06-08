package timr.activities;

import timr.android.R;
import timr.model.MainModel;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class FeedActivity extends Activity implements Runnable {

	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_list);

		progress = new ProgressDialog(this);

		startLoad();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem item1 = menu.add(Menu.NONE, 0, Menu.NONE, "Refresh");
		MenuItem item2 = menu.add(Menu.NONE, 1, Menu.NONE, "Logout");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == 1) {
			TimrAActivity.instance.finish();
			return true;
		} else if (item.getItemId() == 0) {
			startLoad();
			return true;
		}
		return false;
	}

	public void startLoad() {
		progress = ProgressDialog.show(this, "Working..",
				"Retrieving data from sever", true, false);
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

	public void setAdapter() {
		ListView list = (ListView) findViewById(R.id.feed_list);
		FeedListAdapter
				.addAdapter(this, list, MainModel.getInstance().messages);
	}
}
