package com.silverhead.cde.control.file;

import java.util.ArrayList;

import android.content.Context;

import com.silverhead.cde.model.database.CacheBrowser;
import com.silverhead.cde.model.entity.*;
import com.silverhead.cde.plugin.cloud.DropBox;

public class FileBrowser {
	
	private DropBox dropbox;
	private CacheBrowser cachebrowser;
	private SynCache syncache;
	
	public FileBrowser(Context context){
		dropbox = DropBox.getInstance(context);
		cachebrowser = new CacheBrowser(context);
	}
	
	public ArrayList<FileMetadata> browser(FileMetadata file, String account, String cloud){
		ArrayList<FileMetadata> files = new ArrayList<FileMetadata>();
		files = cachebrowser.getAllFiles(file, account, cloud);
		if(files.size() == 0){
			files = dropbox.FileBrowser(file);
		}
		else{
			ArrayList<FileMetadata> filestmp = files;
			files = syncache.synFolder(filestmp);
		}
		return files;
	}
	
}
