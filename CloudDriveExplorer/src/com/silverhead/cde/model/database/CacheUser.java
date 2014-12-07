package com.silverhead.cde.model.database;

import java.util.ArrayList;

import com.silverhead.cde.model.entity.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CacheUser extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "UserDB";

	private static final String TABLE_NAME = "User";

	private static final String USERNAME = "username";
	private static final String CLOUD = "cloud";
	private static final String KEY = "key";

	public CacheUser(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
				+ USERNAME + " TEXT, " + CLOUD + " TEXT, " + KEY + " TEXT" + ")";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		this.onCreate(db);
	}

	public void addUser(User user) {
		// get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(USERNAME, user.getUsername());
		values.put(CLOUD, user.getCloud());
		values.put(KEY, user.getKey());

		// insert
		db.insert(TABLE_NAME, null, values);

		// close
		db.close();
	}

	public ArrayList<User> getAllUsers(String cloud) {
        ArrayList<User> users = new ArrayList<User>();
 
        // build the query
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + CLOUD + " =?";
        
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{cloud});
        
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setUsername(cursor.getString(0));
                user.setCloud(cursor.getString(1));
                user.setKey(cursor.getString(2));
                
                users.add(user);
            } while (cursor.moveToNext());
        }
        
        return users;
    }
	
	public void deleteUser(User user) {
		 
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // delete
        db.delete(TABLE_NAME,
                USERNAME + " = ?" + " AND " + CLOUD + " = ?",
                new String[] { String.valueOf(user.getUsername()),String.valueOf(user.getCloud())});
 
        // close
        db.close();
    }
}
