package com.silverhead.cde.control.file;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ProgressBar;

import com.silverhead.cde.model.database.CacheDownload;
import com.silverhead.cde.model.entity.FileMetadata;
import com.silverhead.cde.plugin.cloud.DropBox;

public class Downloader {
	
	private Context context; 
	private SynCache syncache;
	private ArrayList<threadDownload> arraythread;
	private CacheDownload cachedownload;
	
	public Downloader(Context context){
		this.context = context;
		syncache = new SynCache(context);
		arraythread = new ArrayList<threadDownload>();
		cachedownload = new CacheDownload(context);
	}
	
	public void download(FileMetadata file, DownloadListener listener, ProgressBar progress){
		if(!syncache.checkDownload(file)){
			threadDownload thread = new threadDownload(file, context, listener, progress);
			arraythread.add(thread);
			thread.execute();
		}
		else{
			listener.onDownloaded(file.getFilename());
		}
	}
	
	public void cancel(FileMetadata file){
		for (threadDownload threadDownload : arraythread) {
			if(threadDownload.getThreadname().equals(file.getFilename())){
				threadDownload.cancel(true);
				if(threadDownload.isCancelled()){
					File filetmp = new File("/storage/sdcard1" + "/" + file.getFilename());
					filetmp.delete();
				}
			}
		}
	}
	
	public class threadDownload extends AsyncTask<Void, Void, Void>{
		
		private FileMetadata file;
		private String threadname;
		private ProgressBar progress;
		
		private DropBox dropbox;
		private DownloadListener listener;
		
		public threadDownload(FileMetadata file, Context context, DownloadListener listener, ProgressBar progress){
			this.file = file;
			this.listener = listener;
			this.progress = progress;
			dropbox = DropBox.getInstance(context);
			setThreadname(this.file.getFilename());
		}
		
		public String getThreadname() {
			return threadname;
		}

		public void setThreadname(String threadname) {
			this.threadname = threadname;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			dropbox.download(file, progress);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			cachedownload.addFile(file);
			listener.onSuccess(threadname);
		}
		
	}
	
}
