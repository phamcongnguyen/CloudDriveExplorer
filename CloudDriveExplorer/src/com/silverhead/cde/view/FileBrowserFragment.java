package com.silverhead.cde.view;

import java.util.ArrayList;

import com.silverhead.cde.control.file.DownloadListener;
import com.silverhead.cde.control.file.Downloader;
import com.silverhead.cde.control.file.FileBrowser;
import com.silverhead.cde.model.entity.FileMetadata;
import com.silverhead.clouddriveexplorer.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileBrowserFragment extends Fragment{

	private FileBrowser filebrowser;
	private Downloader downloader;
	
	private Context context;
	private ArrayList<FileMetadata> files;
	private FileAdapter fileadapter;
	private ArrayList<FileMetadata> folder;
	
	private ListView lvFileBrowser;
	private TextView txtRoot;
	private ImageButton imgbBack;
	
	private String account;
	private String cloud;
	private ArrayList<View> views;
	
	public FileBrowserFragment(Context context, String account, String cloud) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.account = account;
		this.cloud = cloud;
		filebrowser = new FileBrowser(context);
		files = new ArrayList<FileMetadata>();
		folder = new ArrayList<FileMetadata>();
		downloader = new Downloader(context);
		views = new ArrayList<View>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.filebrowser, container, false);
		lvFileBrowser = (ListView)view.findViewById(R.id.filebrowser);
		txtRoot = (TextView)view.findViewById(R.id.root);
		imgbBack = (ImageButton)view.findViewById(R.id.back);
		
		getBrowser();
		onClick();
		
		return view;
	}
	
	public void getBrowser(){
		files = filebrowser.browser(null, account, cloud);
		fileadapter = new FileAdapter(context, 0, files);
		lvFileBrowser.setAdapter(fileadapter);
		
		txtRoot.setText("/root");
		imgbBack.setVisibility(TRIM_MEMORY_BACKGROUND);
		views = fileadapter.getListView();
	}
	
	public void onClick(){
		lvFileBrowser.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(files.get(position).getExtension().equals("")){
					folder.add(files.get(position));
					files = filebrowser.browser(files.get(position), account, cloud);
					fileadapter = new FileAdapter(context, 0, files);
					lvFileBrowser.setAdapter(fileadapter);
					
					String txt = "/root";
					for (FileMetadata filetmp : folder) {
						txt +=  "/" + filetmp.getFilename();
					}
					txtRoot.setText(txt);
					
					imgbBack.setVisibility(TRIM_MEMORY_COMPLETE);
				}
				else{
					FileMetadata filetmp = files.get(position);
					int count = folder.size();
					if(count == 0){
						files = filebrowser.browser(null, account, cloud);
						fileadapter = new FileAdapter(context, 0, files);
						lvFileBrowser.setAdapter(fileadapter);
					}
					else{
						files = filebrowser.browser(folder.get(count - 1), account, cloud);
						fileadapter = new FileAdapter(context, 0, files);
						lvFileBrowser.setAdapter(fileadapter);
						
					}
					for (FileMetadata fileMetadata : files) {
						if(fileMetadata.getFilename().equals(filetmp.getFilename())){
							DownloadListener listener = new DownloadListener(context);
							//fileadapter.progress(position);
							((LayoutFile) views.get(position)).getProgress().setVisibility(TRIM_MEMORY_COMPLETE);
							downloader.download(files.get(position), listener, ((LayoutFile) views.get(position)).progress);
							break;
						}
					}
					
				}
			}
			
		});
		
		imgbBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int count = folder.size();
				if(count <= 1){
					files = filebrowser.browser(null, account, cloud);
					fileadapter = new FileAdapter(context, 0, files);
					lvFileBrowser.setAdapter(fileadapter);
					
					folder.remove(count - 1);
					imgbBack.setVisibility(TRIM_MEMORY_BACKGROUND);
					String txt = "/root";
					for (FileMetadata filetmp : folder) {
						txt +=  "/" + filetmp.getFilename();
					}
					txtRoot.setText(txt);
				}
				else{
					files = filebrowser.browser(folder.get(count - 2), account, cloud);
					fileadapter = new FileAdapter(context, 0, files);
					lvFileBrowser.setAdapter(fileadapter);
					
					folder.remove(count - 1);
					String txt = "/root";
					for (FileMetadata filetmp : folder) {
						txt +=  "/" + filetmp.getFilename();
					}
					txtRoot.setText(txt);
				}
			}
		});
	}
	
}
