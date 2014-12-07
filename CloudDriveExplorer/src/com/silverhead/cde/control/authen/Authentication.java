package com.silverhead.cde.control.authen;

import java.util.ArrayList;

import android.content.Context;

import com.silverhead.cde.model.database.CacheBrowser;
import com.silverhead.cde.model.database.CacheDownload;
import com.silverhead.cde.model.database.CacheUser;
import com.silverhead.cde.model.entity.*;
import com.silverhead.cde.plugin.cloud.DropBox;

public class Authentication {

	private DropBox dropbox;
	private CacheUser cacheuser;
	private CacheBrowser cachebrowser;
	private CacheDownload cachedownload;
	
	
	public Authentication(Context context) {

		dropbox = DropBox.getInstance(context);
		cacheuser = new CacheUser(context);
		cachebrowser = new CacheBrowser(context);
		cachedownload = new CacheDownload(context);
	}

	public ArrayList<User> login() {
		//if (checkUser().size() != 0) {
			//return checkUser();
		//} else {
			loginNewAccount();
		//}
		return null;
	}

	public ArrayList<User> checkUser() {
		ArrayList<User> users = new ArrayList<User>();
		users = cacheuser.getAllUsers("DropBox");
		return users;
	}

	public void loginNewAccount() {
		dropbox.login();
	}

	public void loginCurrentAccount(User user) {
		
	}

	public String storeKey() {
		User user = new User();
		user = dropbox.getKey();
		cacheuser.addUser(user);
		return user.getUsername();
	}

	public void logout(User user) {
		dropbox.logout();
		cachebrowser.deleteFile(user.getUsername(), user.getCloud());
		cachedownload.deleteFile(user.getUsername(), user.getCloud());
		cacheuser.addUser(user);
	}
}
