package com.silverhead.cde.control.file;

import java.util.ArrayList;
import java.util.List;

import com.dropbox.client2.DropboxAPI.Entry;
import com.silverhead.cde.model.entity.*;

public class ParseEntry {
	
	public ArrayList<FileMetadata> parse(List<Entry> entryfiles, String account, String cloud, String parent){
		
		ArrayList<FileMetadata> datafiles = new ArrayList<FileMetadata>();
		
		for (Entry entry : entryfiles) {
			FileMetadata file = new FileMetadata();
			file.setAccount(account);
			file.setCloud(cloud);
			
			String fileName = entry.fileName();
			int extIndex = fileName.lastIndexOf(".");
	        if(extIndex != -1){
	        	file.setExtension(fileName.substring(fileName.lastIndexOf(".")));
	        }
	        else{
	        	file.setExtension("");
	        }
	        
			file.setFilename(entry.fileName());
			file.setParent(parent);
			file.setParentpath(entry.parentPath());
			file.setPath(entry.path);
			file.setSize(entry.size);
			file.setDate(entry.modified);
			file.setBytes(entry.bytes);
			datafiles.add(file);
		}
		
		return datafiles;
	}
	
}
