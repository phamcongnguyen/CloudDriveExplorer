package com.silverhead.multicloud;

import com.silverhead.cde.control.authen.Authentication;
import com.silverhead.cde.control.authen.FileListener;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btnLogin;
	
	private String account;
	private boolean check = false;
	
	private Authentication authen;
	private FileListener browserFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnLogin = (Button)findViewById(R.id.btnLogin);
		
		authen = new Authentication(this);
		browserFile = new FileListener();
		
		onClick();
		
	}
	
	public void onClick(){
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				authen.login();
				check = true;
				btnLogin.setVisibility(TRIM_MEMORY_BACKGROUND);
			}
		});
		
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(check){
			account = authen.storeKey();
			
			Fragment fragment = browserFile.browserFile(this, account, "DropBox");
			FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		}
		
	}
	
	
}
