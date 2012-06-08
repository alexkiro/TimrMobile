package timr.activities;

import timr.android.R;
import timr.model.MainModel;
import timr.service.Services;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends Activity implements Runnable{
	
	private boolean valid;
	private ProgressDialog progress;
	private MainActivity instance;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		instance = this;
		progress = new ProgressDialog(this);
		
		
	}

	public void onLoginClick(View v) {
		progress = ProgressDialog.show(this, "Working..", "Validating user", true,
                false);
		Thread thread = new Thread(this);
        thread.start();
	}
	
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
                progress.dismiss();
                if (valid){
	                RadioButton srb = (RadioButton) findViewById(R.id.studRadio);
	        		RadioButton prb = (RadioButton) findViewById(R.id.profRadio);
	        		Intent intentSend;
	        		if (srb.isChecked()) {
	        			intentSend = new Intent(instance, TimrAActivity.class);
	    			} else {
	    				intentSend = new Intent(instance,SendActivity.class);
	    			}
	        		instance.startActivity(intentSend);
                } else {
                	Toast.makeText(instance,"Wrong username or password",Toast.LENGTH_SHORT).show();
                	((EditText) findViewById(R.id.passText)).setText("");
                }
        }
	};

	@Override
	public void run() {
		EditText userText = (EditText) findViewById(R.id.userText);
		EditText passText = (EditText) findViewById(R.id.passText);		
		
		String user = userText.getText().toString();
		String pass = passText.getText().toString();
		
		MainModel.getInstance().tempU = user;
		MainModel.getInstance().tempP = pass;
		
		valid = Services.validateUser(user, pass);
		handler.sendEmptyMessage(0);
			
	}
}
