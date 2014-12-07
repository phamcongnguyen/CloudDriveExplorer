package com.silverhead.cde.view;

import java.util.ArrayList;

import com.silverhead.cde.model.entity.FileMetadata;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

public class FileAdapter extends ArrayAdapter<FileMetadata>{
	
	private Context context;
	private ArrayList<FileMetadata> files;
	private ArrayList<View> views;
	
	public FileAdapter(Context context, int resource, ArrayList<FileMetadata> files) {
		super(context, resource, files);
		// TODO Auto-generated constructor stub
		
		this.context = context;
		this.files = files;
		views = new ArrayList<View>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		//if(convertView == null){
			convertView = new LayoutFile(context);
			((LayoutFile) convertView).setFile(files.get(position));
		//}
			
			views.add(convertView);
		
		return convertView;
	}
	
	public ProgressBar progress(int position){
		return ((LayoutFile) views.get(position)).getProgress();
	}
	
	public ArrayList<View> getListView(){
		return views;
	}
	
}
