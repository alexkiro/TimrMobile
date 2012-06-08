package timr.activities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import timr.android.R;
import timr.model.MainModel;
import timr.model.messages.Feed;
import timr.model.messages.Message;
import timr.model.messages.SiteUpdate;
import timr.model.messages.TimeUpdate;
import timr.service.Services;
import timr.timrutil.Utils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FeedListAdapter {
	public static void addAdapter(final Context context, final ListView list,
			final List<Message> feeds) {
		if (list == null) {
			System.out.println("list is null");
			return;
		}
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
					final int arg2, long arg3) {
				new AlertDialog.Builder(context)
				.setTitle("Delete Notification ?")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Message msg = feeds.get(arg2);
						msg.solved = true;
								
		                Services.solveNotification(MainModel.getInstance().stud.user, msg._id);
		                MainModel.getInstance().messages.remove(arg2);
		                if (MainModel.getInstance().unsolved){
		                	addAdapter(context, list, feeds);
		                }
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								
							}
						}).show();
				return true;
			}
		});
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Message m = feeds.get(arg2);
				
				String title;
				String info;
				
				if (m instanceof Feed){
					title = ((Feed) m).sender;
					info = ((Feed) m).message;
				} else if (m instanceof TimeUpdate){
					TimeUpdate time = ((TimeUpdate) m);
					title = time.title;
					info = time.type + "\n"
		                    + "Subject:\t" + time.className + "\n"
		                    + "Day:\t" + time.day + "\n"
		                    + "Type:\t" + time.classType + "\n"
		                    + "Start:\t" + time.start + "\n"
		                    + "End:\t" + time.end + "";
					
					
				} else if (m instanceof SiteUpdate){
					title = m.title;
					info = "Site changed: " + "\n" + ((SiteUpdate) m).site;
				} else {
					title = "Not recognized feed";
					info = "Error";
				}
				
				new AlertDialog.Builder(context)
	        	.setPositiveButton("Ok", null)
	        	.setMessage(info)
	        	.setTitle(title)
	        	.create().show();
			}
		});

		String[] from = new String[] { "from", "when" };
		int[] to = new int[] { R.id.from, R.id.when };

		List<Map<String, String>> data = new LinkedList<Map<String, String>>();

		for (Message msg : feeds) {
			Map<String, String> ti = new HashMap<String, String>();
			if (msg instanceof Feed) {
				ti.put("from", ((Feed) msg).title);
			} else if (msg instanceof SiteUpdate) {
				ti.put("from", ((SiteUpdate) msg).title);
			} else if (msg instanceof TimeUpdate) {
				ti.put("from", ((TimeUpdate) msg).title);				
			}
			ti.put("when", Utils.compareDate(msg.date));
			data.add(ti);
		}

		SimpleAdapter adapter = new SimpleAdapter(context, data,
				R.layout.feed_list_item, from, to) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				ImageButton img = (ImageButton) v.findViewById(R.id.image);

				Message msg = feeds.get(position);

				if (msg instanceof Feed) {
					img.setImageDrawable(context.getResources().getDrawable(
							R.drawable.feed_notification));
				} else if (msg instanceof SiteUpdate) {
					img.setImageDrawable(context.getResources().getDrawable(
							R.drawable.site_notification));
				} else if (msg instanceof TimeUpdate) {
					img.setImageDrawable(context.getResources().getDrawable(
							R.drawable.time_notification));
				}
				img.setFocusable(false);
				// TODO: set on click;
				return v;
			}
		};
		list.setAdapter(adapter);
	}
}
