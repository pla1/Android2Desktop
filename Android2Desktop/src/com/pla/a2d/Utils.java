package com.pla.a2d;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import android.net.Uri;

public class Utils {
	public static void sendUri(String address, int port, Uri uri) throws UnknownHostException, IOException {
		Socket clientSocket = null;
		clientSocket = new Socket(address, port);
		PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
		String urlString = uri.toString();
		int urlLength = urlString.length();
		ByteBuffer byteBuffer = ByteBuffer.allocate(urlLength + 4);
		byteBuffer.putInt(urlLength);
		byteBuffer.put(urlString.getBytes());
		pw.write(new String(byteBuffer.array()));
		pw.flush();
		pw.close();
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
