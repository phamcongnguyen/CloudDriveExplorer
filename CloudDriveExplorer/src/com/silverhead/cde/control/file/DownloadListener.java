package com.silverhead.cde.control.file;

import android.content.Context;
import android.widget.Toast;

import com.silverhead.cde.control.onlistener.onDownloadListener;

public class DownloadListener implements onDownloadListener{

	private Context context;
	
	public DownloadListener(Context context){
		this.context = context;
	}
	
	@Override
	public void onSuccess(String filename) {
		// TODO Auto-generated method stub
		Toast.makeText(context, filename + " download success", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFail(String filename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel(String filename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDownloaded(String filename) {
		// TODO Auto-generated method stub
		Toast.makeText(context, filename + " downloaded", Toast.LENGTH_LONG).show();
	}

}
