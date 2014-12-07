package com.silverhead.cde.control.onlistener;

public interface onDownloadListener {
	public void onSuccess(String filename);
	public void onDownloaded(String filename);
	public void onFail(String filename);
	public void onCancel(String filename);
}
