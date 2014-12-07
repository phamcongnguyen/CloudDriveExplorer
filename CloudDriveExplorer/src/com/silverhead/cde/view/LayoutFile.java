package com.silverhead.cde.view;

import com.silverhead.cde.model.entity.FileMetadata;
import com.silverhead.clouddriveexplorer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LayoutFile extends LinearLayout implements android.view.View.OnClickListener{

	private ImageView imgitem;
	private TextView txtName;
	private TextView txtAttribute;
	public ProgressBar progress;
	private Context context;

	public LayoutFile(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.file, this);
		
		imgitem = (ImageView) findViewById(R.id.imgitem);
		txtName = (TextView) findViewById(R.id.name);
		txtAttribute = (TextView) findViewById(R.id.attribute);
		progress = (ProgressBar) findViewById(R.id.progress);
		//progress.setVisibility(INVISIBLE);
	}

	public void setFile(FileMetadata file) {
		imgitem.setImageResource(getExtension(file.getExtension()));
		txtName.setText(file.getFilename());
		
		if(!file.getExtension().equals("")){
			String day = file.getDate().substring(0, file.getDate().lastIndexOf("+"));
			txtAttribute.setText(file.getSize() + ";" + day);
		}
		else{
			//imgitem.setPadding(2, 8, 2, 6);
			//txtName.setPadding(2, 14, 2, 0);
			txtName.setPadding(0, 14, 0, 0);
		}
		
	}

	public ProgressBar getProgress() {
		progress.setVisibility(VISIBLE);
		return progress;
	}

	public void setProgress(ProgressBar progress) {
		this.progress = progress;
	}

	public int getExtension(String extension) {
		if (extension.equals("")) {
			return R.drawable.dir;
		}
		if (extension.equalsIgnoreCase(".pdf")) {
			return R.drawable.pdf;
		} else if (extension.equalsIgnoreCase(".doc")
				|| extension.equalsIgnoreCase(".docx")
				|| extension.equalsIgnoreCase(".odt")) {
			return R.drawable.text;
		} else if (extension.equalsIgnoreCase(".xls")
				|| extension.equalsIgnoreCase(".xlsx")
				|| extension.equalsIgnoreCase(".ods")) {
			return R.drawable.calc;
		} else if (extension.equalsIgnoreCase(".ppt")
				|| extension.equalsIgnoreCase(".pptx")
				|| extension.equalsIgnoreCase(".odp")) {
			return R.drawable.presentation;
		} else if (extension.equalsIgnoreCase(".jpeg")
				|| extension.equalsIgnoreCase(".jpg")
				|| extension.equalsIgnoreCase(".png")
				|| extension.equalsIgnoreCase(".ico")) {
			return R.drawable.picture;
		} else if (extension.equalsIgnoreCase(".avi")
				|| extension.equalsIgnoreCase(".wmv")
				|| extension.equalsIgnoreCase(".mkv")
				|| extension.equalsIgnoreCase(".rmvb")) {
			return R.drawable.movie;
		} else if (extension.equalsIgnoreCase(".zip")
				|| extension.equalsIgnoreCase(".rar")
				|| extension.equalsIgnoreCase(".tar.gz")) {
			return R.drawable.archive;
		} else if (extension.equalsIgnoreCase(".exe")) {
			return R.drawable.exe;
		} else if (extension.equalsIgnoreCase(".txt")) {
			return R.drawable.txt;
		}

		return R.drawable.others;
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==this){
			Toast.makeText(context, "alolalala", Toast.LENGTH_LONG).show();
		}
		
	}

}
