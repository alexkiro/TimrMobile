package timr.activities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import timr.android.R;
import timr.model.MainModel;
import timr.model.timetable.TableItem;
import timr.service.Services;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TimeListAdapter {

	public static void addAdapter(final Context context, final ListView list,
			final List<TableItem> timetable) {
		if (list == null) {
			return;
		}

		list.setClickable(true);

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				TableItem t = timetable.get(arg2);
				
				if (t._id != null && (!t._id.isEmpty())) {
					String site = MainModel.getInstance().stud.subjects
							.get(t._id);
					if (site != null && (!site.isEmpty())) {
						promptUpdateSite(context, t._id, site,
								"Update site for: " + t.className);
					} else {
						promptInsertSite(context, t._id, "Add Site to Subject"
								+ t.className);
					}
				} else {
					System.err.println("No _id for subject: " + t.className);
				}
				return true;
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TableItem t = timetable.get(arg2);
				String[] freq = { "Every Week", "Odd Weeks", "Even Weeks" };

				String title = t.className;

				String info = "Time:\t " + t.startTime + " - " + t.endTime
						+ "\n" + "Prof:\t" + t.teacherName + "\n" + "Room:\t"
						+ t.roomNumber + "\n" + "Group:\t" + t.group.toString()
						+ "\n" + "Freq:\t" + freq[t.frequency] + "\n"
						+ "Site:\t"
						+ MainModel.getInstance().stud.subjects.get(t._id);
				if (t.optionalPackage != -1) {
					info += "\n" + "Package:\t" + t.optionalPackage;
				}

				new AlertDialog.Builder(context).setPositiveButton("Ok", null)
						.setMessage(info).setTitle(title).create().show();

			}
		});

		String[] from = new String[] { "time", "course", "place" };
		int[] to = new int[] { R.id.time, R.id.course, R.id.place };

		List<Map<String, String>> data = new LinkedList<Map<String, String>>();

		for (TableItem tableItem : timetable) {
			Map<String, String> ti = new HashMap<String, String>();
			ti.put("time", tableItem.startTime + " - " + tableItem.endTime);
			ti.put("course", tableItem.className);
			ti.put("place", tableItem.roomNumber);
			data.add(ti);
		}

		SimpleAdapter adapter = new SimpleAdapter(context, data,
				R.layout.time_list_item, from, to) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				ImageButton img = (ImageButton) v.findViewById(R.id.image);

				switch (timetable.get(position).classType) {
				case 0:
					img.setImageDrawable(context.getResources().getDrawable(
							R.drawable.course));
					break;
				case 1:
					img.setImageDrawable(context.getResources().getDrawable(
							R.drawable.laboratory));
					break;
				case 2:
					//$FALL-THROUGH$
				default:
					img.setImageDrawable(context.getResources().getDrawable(
							R.drawable.seminar));
				}
				img.setFocusable(false);

				img.setOnClickListener(goToSite(context,
						timetable.get(position)));

				return v;
			}
		};
		list.setAdapter(adapter);
	}

	

	private static OnClickListener goToSite(final Context context,
			final TableItem t) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (t._id != null && (!t._id.isEmpty())) {
					String site = MainModel.getInstance().stud.subjects
							.get(t._id);
					if (site != null && (!site.isEmpty())) {
						try {
							startBrowser(context, site);
						} catch (Exception ex) {
							TimrError.show(ex.getMessage());
						}
					} else {
						promptInsertSite(context, t._id, "Add Site to Subject"
								+ t.className);
					}
				} else {
					System.err.println("No _id for subject: " + t.className);
				}

			}
		};
	}

	private static void startBrowser(Context context, String uri) {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_BROWSABLE);
		i.setData(Uri.parse(uri));
		context.startActivity(i);
	}

	private static void promptInsertSite(Context context, final String _id,
			String message) {
		final EditText input = new EditText(context);
		input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
		input.setText("http://");

		new AlertDialog.Builder(context)
				.setTitle("Set Website")
				.setMessage(message)
				.setView(input)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String result = input.getText().toString();
						if (result != null && (!result.isEmpty())) {
							MainModel.getInstance().stud.subjects.put(_id,
									result);
							Services.insertSite(
									MainModel.getInstance().stud.user, result,
									_id);
						}
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
	}

	private static void promptUpdateSite(Context context, final String _id,
			final String oldsite, String message) {
		final EditText input = new EditText(context);
		input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
		input.setText(oldsite);

		new AlertDialog.Builder(context)
				.setTitle("Set Website")
				.setMessage(message)
				.setView(input)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String result = input.getText().toString();
						if (result != null && (!result.isEmpty())) {
							MainModel.getInstance().stud.subjects.put(_id,
									result);
							Services.updateSite(
									MainModel.getInstance().stud.user, oldsite,
									result);
						}
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();

	}

}
