package com.pla.a2d;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import android.net.Uri;
import android.util.Log;

public class Utils {
	private static final String TAG = "Utils";

	public static void sendUri(String address, int port, Uri uri) throws UnknownHostException, IOException {
		Socket clientSocket = null;
		clientSocket = new Socket(address, port);
		DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
		String urlString = uri.toString();
		int urlLength = urlString.length();
		ByteBuffer byteBuffer = ByteBuffer.allocate(urlLength + 4);
		byteBuffer.putInt(urlLength);
		byteBuffer.put(urlString.getBytes());
		dataOutputStream.write(byteBuffer.array());
		Log.i(TAG,
				"Wrote ByteBuffer. URL length: " + urlLength + " url string: " + urlString + " byte buffer length: "
						+ byteBuffer.array().length);
		dataOutputStream.flush();
		dataOutputStream.close();
	}

	public static int parse(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
		}
		return i;
	}

	public static boolean isBlank(String s) {
		if (s == null || s.trim().length() == 0) {
			return true;
		}
		return false;
	}
}
