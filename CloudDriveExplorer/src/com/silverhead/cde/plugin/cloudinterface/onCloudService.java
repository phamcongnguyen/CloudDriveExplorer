package com.silverhead.cde.plugin.cloudinterface;

import java.util.ArrayList;

import android.widget.ProgressBar;

import com.silverhead.cde.model.entity.*;

public interface onCloudService {
	
	public void login();
	public User getKey();
	public ArrayList<FileMetadata> FileBrowser(FileMetadata file);
	public void download(FileMetadata file, ProgressBar progress);
	public boolean checkFile(FileMetadata file);
	public FileMetadata getNewFile(FileMetadata file);
	public void logout();
	
}
