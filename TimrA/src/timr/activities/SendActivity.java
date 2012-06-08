package timr.activities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import timr.android.R;
import timr.model.MainModel;
import timr.model.user.Faculty;
import timr.model.user.Prof;
import timr.service.Services;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SendActivity extends Activity implements Runnable{
	private ProgressDialog progress;
	private String username;
	private String pass;
	private SendActivity instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.send_feed);
		final Button next = (Button) findViewById(R.id.addButton);
		next.setBackgroundResource(R.drawable.add);
		next.setText("");
		final Button send = (Button) findViewById(R.id.sendButton);
		send.setBackgroundResource(R.drawable.feed_notification);
		send.setText("Send");
		progress = new ProgressDialog(this);	
		
		startLoad("busaco","busaco");
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
		MainModel.getInstance().loadProfInfo(username, pass);
		handler.sendEmptyMessage(0);
	}
	
	public void loadFaculties(){
		Spinner fSpinner = (Spinner) findViewById(R.id.facultyCombo);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
		List<Faculty> faculties = MainModel.getInstance().prof.faculties;
		
		for (Faculty faculty : faculties) {
			adapter.add(faculty.name);
		}
		fSpinner.setAdapter(adapter);
	}
	
	public void loadGroups(){
		Spinner fSpinner = (Spinner) findViewById(R.id.facultyCombo);
		String name = fSpinner.getSelectedItem().toString();
		Prof prof = MainModel.getInstance().prof;
		List<String> list = new LinkedList<String>(prof.getFacultyByName(name).groups.keySet());
		Collections.sort(list);
		
		Spinner gSpinner = (Spinner) findViewById(R.id.groupCombo);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item);
		
		adapter.add("All");
		
		for (String string : list) {
			adapter.add(string);
		}
		
		gSpinner.setAdapter(adapter);		
	}
	
	public void onClickAddGroup(View view) {
		
		Spinner gSpinner = (Spinner) findViewById(R.id.groupCombo);
		if (gSpinner.getSelectedItemPosition() == 0){
			Spinner fSpinner = (Spinner) findViewById(R.id.facultyCombo);
			String name = fSpinner.getSelectedItem().toString();
			Prof prof = MainModel.getInstance().prof;
			List<String> list = new LinkedList<String>(prof.getFacultyByName(name).groups.keySet());
			Collections.sort(list);
			
			for (String string : list) {
				EditText toText = (EditText) findViewById(R.id.toText);
				toText.append(", " + string);
			}
		} else {
			String groupName = gSpinner.getSelectedItem().toString();	
			EditText toText = (EditText) findViewById(R.id.toText);
			toText.append(", " + groupName);
		}
		
	 }
	
	public void onClickSendFeed(View view){
		EditText toText = (EditText) findViewById(R.id.toText);
		EditText msgText = (EditText) findViewById(R.id.messageText);
		
		Spinner fSpinner = (Spinner) findViewById(R.id.facultyCombo);
		String name = fSpinner.getSelectedItem().toString();
		String message = msgText.getText().toString();
		Prof prof = MainModel.getInstance().prof;
		Faculty faculty = prof.getFacultyByName(name);
		
		List<String> groupIds = new LinkedList<String>();
		
		String inputText = toText.getText().toString();
        StringTokenizer st = new StringTokenizer(inputText, " \n\t\r,");
        while(st.hasMoreTokens()){
            String token = st.nextToken();
            String _id = faculty.groups.get(token);
            if (_id!=null && (!_id.isEmpty())){
                groupIds.add(_id);
            }
        }
        if (groupIds.isEmpty()){
        	Toast.makeText(this, "Error no valid group name found", Toast.LENGTH_SHORT).show();
        } else {
            if (message.isEmpty()){
            	Toast.makeText(this, "You cannot send an empty message feed", Toast.LENGTH_SHORT).show();
            } else {
                SendFeed(faculty.name, prof.user, groupIds, message);
            }
        }
	}
	
	private void SendFeed(final String faculty, final String user, final List<String> groupIds, final String message){
		progress = new ProgressDialog(this);
		progress = ProgressDialog.show(this, "Working..", "Sending data from sever", true,
                false);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Services.insertFeed(faculty, user, groupIds, message);
				sentFinishedHandle.sendEmptyMessage(0);
			}
		}).start();
	}
	
	private Handler sentFinishedHandle = new Handler(){
		@Override
        public void handleMessage(android.os.Message msg) {
                progress.dismiss();
                EditText toText = (EditText) instance.findViewById(R.id.toText);
        		EditText msgText = (EditText) instance.findViewById(R.id.messageText);
                Toast.makeText(instance, "Feed sent succesfully", Toast.LENGTH_SHORT).show();
                toText.setText("");
                msgText.setText("");     
        }
	};
	
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
                progress.dismiss();
                loadFaculties();
                loadGroups();
                
        }
	};
}
