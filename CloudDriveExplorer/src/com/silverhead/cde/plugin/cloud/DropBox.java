package com.silverhead.cde.plugin.cloud;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.widget.ProgressBar;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.silverhead.cde.control.file.ParseEntry;
import com.silverhead.cde.model.entity.FileMetadata;
import com.silverhead.cde.model.entity.User;
import com.silverhead.cde.plugin.cloudinterface.onCloudService;

public class DropBox implements onCloudService {

	private Context context;
	private List<Entry> entryfiles;
	private ArrayList<FileMetadata> datafiles;
	private String account;

	final static private String APP_KEY = "n5nqrjyv4vk3xtd";
	final static private String APP_SECRET = "tujptyjzeibq2t3";

	public DropboxAPI<AndroidAuthSession> mApi;
	private static DropBox instance;
	private Handler mHandler = new Handler();

	private DropBox(Context context) {
		this.context = context;
	}

	public static DropBox getInstance(Context context) {
		if (instance == null) {
			instance = new DropBox(context);
		}
		return instance;
	}

	@Override
	public void login() {
		// TODO Auto-generated method stub

		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);
		mApi.getSession().startOAuth2Authentication(context);
	}

	@Override
	public User getKey() {
		// TODO Auto-generated method stub
		AndroidAuthSession session = mApi.getSession();
		String token = null;

		if (session.authenticationSuccessful()) {
			session.finishAuthentication();
			token = session.getOAuth2AccessToken();
		}

		try {
			account = mApi.accountInfo().displayName;
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		User user = new User();
		user.setUsername(account);
		user.setCloud("DropBox");
		user.setKey(token);

		return user;
	}

	@Override
	public ArrayList<FileMetadata> FileBrowser(final FileMetadata file) {
		// TODO Auto-generated method stub

		ParseEntry parse = new ParseEntry();

		if (file == null) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						entryfiles = mApi.metadata("/", 0, null, true, null).contents;
					} catch (DropboxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			thread.start();
			while (thread.isAlive()) {
			}

			datafiles = parse.parse(entryfiles, account, "DropBox", null);
		} 
		else {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						entryfiles = mApi.metadata(file.getPath(), 0, null, true, null).contents;
					} catch (DropboxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			thread.start();
			while (thread.isAlive()) {
			}

			datafiles = parse.parse(entryfiles, account, "DropBox", file.getFilename());
		}

		return datafiles;
	}

	@Override
	public void download(final FileMetadata file, final ProgressBar progress) {
		// TODO Auto-generated method stub
		//Thread downloadFile = new Thread(new Runnable() {							
		//	@Override
		//	public void run() {
				try {
					//File output = new File("/storage/sdcard1/" + file.getFilename());
					//FileOutputStream outputStream = new FileOutputStream(output);
					//mApi.getFile(file.getPath(), null, outputStream, null);
					//mApi.getFileStream(file.getPath(), null);
					InputStream input = new BufferedInputStream(mApi.getFileStream(file.getPath(), null), 8192);
					OutputStream output = new FileOutputStream("/storage/sdcard1/" + file.getFilename());
					int count;
					byte data[] = new byte[1024];

		            int total = 0;
		            
		            while ((count = input.read(data)) != -1) {
		                total += count;
		                // publishing the progress....
		                // After this onProgressUpdate will be called
		                progress.setProgress((int)((int)(total*100)/file.getBytes()));
		                // writing data to file
		                output.write(data, 0, count);
		            }

		            // flushing output
		            output.flush();
		            
		            // closing streams
		            output.close();
		            input.close();
					
					//outputStream.close();
				} catch (DropboxException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
		//	}
		//});
    	//downloadFile.start();
    	//while(downloadFile.isAlive());
	}

	@Override
	public boolean checkFile(FileMetadata file) {
		// TODO Auto-generated method stub
		boolean check = false;
		
		try {
			check = file.getDate().equals(mApi.metadata(file.getPath(), 1, null, false, null).modified);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return check;
	}

	@Override
	public FileMetadata getNewFile(FileMetadata file) {
		// TODO Auto-generated method stub
		Entry entry = null;
		try {
			entry = mApi.metadata(file.getPath(), 1, null, false, null);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileMetadata filetmp = new FileMetadata();
		
		filetmp.setAccount(account);
		filetmp.setCloud("DropBox");
		
		String fileName = entry.fileName();
		int extIndex = fileName.lastIndexOf(".");
        if(extIndex != -1){
        	filetmp.setExtension(fileName.substring(fileName.lastIndexOf(".") + 1));
        }
        else{
        	filetmp.setExtension("");
        }
        
		filetmp.setFilename(entry.fileName());
		filetmp.setParent(file.getParent());
		filetmp.setParentpath(entry.parentPath());
		filetmp.setPath(entry.path);
		filetmp.setSize(entry.size);
		filetmp.setDate(entry.modified);
		
		return filetmp;
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		mApi.getSession().unlink();
	}

	private AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
		return session;
	}

}
