package com.silverhead.cde.control.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.os.Environment;

import com.silverhead.cde.model.database.CacheBrowser;
import com.silverhead.cde.model.database.CacheDownload;
import com.silverhead.cde.model.entity.FileMetadata;
import com.silverhead.cde.plugin.cloud.DropBox;

public class SynCache {
	
	private CacheBrowser cachebrowser;
	private CacheDownload cachedownload;
	private DropBox dropbox;
	
	public SynCache(Context context){
		cachebrowser = new CacheBrowser(context);
		cachedownload = new CacheDownload(context);
		dropbox = DropBox.getInstance(context);
	}
	
	/**
	 * 
	 * @param file
	 * @return true: exist
	 * 		   false: no exist
	 */
	public boolean checkLocal(FileMetadata file){
		File dir = new File("/storage/sdcard1/");
		List<File> files = Arrays.asList(dir.listFiles());
		for (File filetmp : files) {
			if(filetmp.getName().equals(file.getFilename())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param file
	 * @return true: exist
	 * 		   false: no exist
	 */
	public boolean checkCacheDownload(FileMetadata file){
		return cachedownload.checkFile(file);
	}
	
	/**
	 * 
	 * @param file
	 * @return true: same
	 * 		   false: no same
	 */
	public boolean checkServer(FileMetadata file){
		return dropbox.checkFile(file);
	}
	
	/**
	 * downloader will call it before download
	 * @param file
	 * @return true: needn't download
	 * 		   false: need download
	 */
	public boolean checkDownload(FileMetadata file){
		if(!checkLocal(file)){
			return false;
		}
		else if(!checkCacheDownload(file)){
			return false;
		}
		return true;
	}
	
	public FileMetadata synFile(FileMetadata file){
		if(!checkServer(file)){
			FileMetadata filetmp = dropbox.getNewFile(file);
			cachebrowser.updateFile(filetmp);
			return filetmp;
		}
		return file;
	}
	
	/**
	 * BrowserFile call it before show list file in fragment
	 * @param files
	 */
	public ArrayList<FileMetadata> synFolder(ArrayList<FileMetadata> files){
		for (FileMetadata filetmp : files) {
			filetmp = synFile(filetmp);
		}
		return files;
	}
}
