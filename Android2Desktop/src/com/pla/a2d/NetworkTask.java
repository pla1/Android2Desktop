package com.pla.a2d;

import android.net.Uri;
import android.os.AsyncTask;

public class NetworkTask extends AsyncTask<String, Void, String> {
	private String ipAddress;
	private int port;
	private Uri uri;

	public NetworkTask(String ipAddress, int port, Uri uri) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.uri = uri;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			Utils.sendUri(ipAddress, port, uri);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
		return null;
	}
}
