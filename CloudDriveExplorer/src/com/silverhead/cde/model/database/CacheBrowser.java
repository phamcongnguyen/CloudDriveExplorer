package com.silverhead.cde.model.database;

import java.util.ArrayList;

import com.silverhead.cde.model.entity.FileMetadata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CacheBrowser extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "FileBrowserDB";

	private static final String TABLE_NAME = "Browser";

	private static final String FILE_NAME = "filename";
	private static final String CLOUD = "cloud";
	private static final String ACCOUNT = "account";
	private static final String PARENT = "parent";
	private static final String SIZE = "size";
	private static final String PATH = "path";
	private static final String PARENT_PATH = "parentpath";
	private static final String EXTENSION = "extension";
	private static final String DATE = "date";

	public CacheBrowser(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
				+ FILE_NAME + " TEXT," + CLOUD + " TEXT," + ACCOUNT + " TEXT,"
				+ PARENT + " TEXT," + SIZE + " TEXT," + PATH + " TEXT,"
				+ PARENT_PATH + " TEXT," + EXTENSION + " TEXT," + DATE
				+ " TEXT)";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		this.onCreate(db);
	}

	public void addFile(FileMetadata file) {
		// get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();

		values.put(FILE_NAME, file.getFilename());
		values.put(CLOUD, file.getCloud());
		values.put(ACCOUNT, file.getAccount());
		values.put(PARENT, file.getParent());
		values.put(SIZE, file.getSize());
		values.put(PATH, file.getPath());
		values.put(PARENT_PATH, file.getParentpath());
		values.put(EXTENSION, file.getExtension());
		values.put(DATE, file.getDate());

		// insert
		db.insert(TABLE_NAME, null, values);

		// close
		db.close();
	}

	/**
	 * get all file with parent
	 * 
	 * @param file
	 *            : parent
	 * @return arraylist of file
	 */
	public ArrayList<FileMetadata> getAllFiles(FileMetadata file,
			String account, String cloud) {
		ArrayList<FileMetadata> files = new ArrayList<FileMetadata>();

		// build the query
		
		String query;
		SQLiteDatabase db;
		Cursor cursor = null;
		
		if(file == null){
			query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CLOUD
					+ " =?" + " AND " + ACCOUNT + " =?";

			db = this.getWritableDatabase();
			cursor = db.rawQuery(
					query,
					new String[] {cloud, account});
		}
		else{
			query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CLOUD
					+ " =?" + " AND " + ACCOUNT + " =?" + " AND "
					+ FILE_NAME + " =?";

			db = this.getWritableDatabase();
			cursor = db.rawQuery(
					query,
					new String[] { String.valueOf(file.getCloud()),
							String.valueOf(file.getAccount()),
							String.valueOf(file.getFilename())});
		}
		

		FileMetadata filetmp = null;
		if (cursor.moveToFirst()) {
			do {
				filetmp = new FileMetadata();

				filetmp.setFilename(cursor.getString(0));
				filetmp.setCloud(cursor.getString(1));
				filetmp.setAccount(cursor.getString(2));
				filetmp.setParent(cursor.getString(3));
				filetmp.setSize(cursor.getString(4));
				filetmp.setPath(cursor.getString(5));
				filetmp.setParentpath(cursor.getString(6));
				filetmp.setExtension(cursor.getString(7));
				filetmp.setDate(cursor.getString(8));

				files.add(filetmp);
			} while (cursor.moveToNext());
		}
		db.close();
		return files;
	}

	public void updateFile(FileMetadata file) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(FILE_NAME, file.getFilename());
		values.put(CLOUD, file.getCloud());
		values.put(ACCOUNT, file.getAccount());
		values.put(PARENT, file.getParent());
		values.put(SIZE, file.getSize());
		values.put(PATH, file.getPath());
		values.put(PARENT_PATH, file.getParentpath());
		values.put(EXTENSION, file.getExtension());
		values.put(DATE, file.getDate());

		db.update(
				TABLE_NAME,
				values,
				FILE_NAME + " =?" + " AND " + CLOUD + " =?" + " AND " + ACCOUNT
						+ " =?",
				new String[] { String.valueOf(file.getFilename()),
						String.valueOf(file.getCloud()),
						String.valueOf(file.getAccount()) });
		db.close();
	}

	public void deleteFile(String account, String cloud) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, ACCOUNT + " =?" + " AND " + CLOUD + " =?",
				new String[] { account, cloud });
		db.close();
	}
}
