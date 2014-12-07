package com.silverhead.cde.control.authen;

import com.silverhead.cde.view.FileBrowserFragment;

import android.app.Fragment;
import android.content.Context;

public class FileListener {
	
	public Fragment browserFile(Context context, String account, String cloud){
		FileBrowserFragment browser = new FileBrowserFragment(context, account, cloud);
		return browser;
	}
	
}
