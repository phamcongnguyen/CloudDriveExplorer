package com.silverhead.cde.model.database;

import com.silverhead.cde.model.entity.FileMetadata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CacheDownload extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "DownloadDB";

	private static final String TABLE_NAME = "Download";

	private static final String FILE_NAME = "filename";
	private static final String CLOUD = "cloud";
	private static final String ACCOUNT = "account";
	private static final String PARENT = "parent";
	private static final String SIZE = "size";
	private static final String PATH = "path";
	private static final String PARENT_PATH = "parentpath";
	private static final String EXTENSION = "extension";
	private static final String DATE = "date";

	public CacheDownload(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
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

	public boolean checkFile(FileMetadata file) {
		// build the query
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CLOUD
				+ " =?" + " AND " + ACCOUNT + " =?" + " AND " + FILE_NAME
				+ " =?" + " AND " + DATE + " =?";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				query,
				new String[] { String.valueOf(file.getCloud()),
						String.valueOf(file.getAccount()),
						String.valueOf(file.getFilename()),
						String.valueOf(file.getDate()) });

		if (cursor.moveToFirst()) {
			db.close();
			return true;
		}

		db.close();
		return false;
	}

	public void deleteFile(String account, String cloud) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, ACCOUNT + " =?" + " AND " + CLOUD + " =?",
				new String[] { account, cloud });
		db.close();
	}

}
