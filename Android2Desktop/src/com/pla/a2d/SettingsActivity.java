package com.pla.a2d;

import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {
	private static final String TAG = "SettingsActivity";
	private static Context context;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		context = getApplicationContext();
		getActionBar().setTitle(R.string.settings);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
	}

	private static String getBuildDateDisplay() {
		if (context == null) {
			return "Unknown";
		}
		try {
			ApplicationInfo ai = context.getApplicationInfo();
			ZipFile zipFile = new ZipFile(ai.sourceDir);
			ZipEntry zipEntry = zipFile.getEntry("classes.dex");
			long time = zipEntry.getTime();
			return SimpleDateFormat.getInstance().format(new java.util.Date(time));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Unknown";
	}

	public static class SettingsFragment extends PreferenceFragment {
		private final static String TAG = "SettingsFragment";
		private SharedPreferences preferences;

		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			preferences = PreferenceManager.getDefaultSharedPreferences(context);
			preferences.registerOnSharedPreferenceChangeListener(preferenceListener);
			Log.i(TAG, "fragment onCreate");
			addPreferencesFromResource(R.xml.settings);
			Preference buildDatePreference = findPreference("buildDate");
			PackageInfo pi = new PackageInfo();
			long lastUpdateTime = pi.lastUpdateTime;
			Log.i(TAG, "Last update time: " + lastUpdateTime);
			buildDatePreference.setSummary(getBuildDateDisplay());
			String ipAddress = preferences.getString("ipAddress", null);
			if (!Utils.isBlank(ipAddress)) {
				updateIpAddressField();
			}
			String port = preferences.getString("port", null);
			if (!Utils.isBlank(port)) {
				updatePortField();
			}
		}

		private void updatePortField() {
			EditTextPreference editTextPref = (EditTextPreference) findPreference("port");
			editTextPref.setSummary(preferences.getString("port", null));
		}

		public void updateIpAddressField() {
			EditTextPreference editTextPref = (EditTextPreference) findPreference("ipAddress");
			editTextPref.setSummary(preferences.getString("ipAddress", null));
		}

		public OnSharedPreferenceChangeListener preferenceListener = new OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				Log.i(TAG, "A preference has been changed. Key is: " + key);
				if ("ipAddress".equals(key)) {
					updateIpAddressField();
				}
				if ("port".equals(key)) {
					updatePortField();
				}
			}

		};

		public void onDestroy() {
			super.onDestroy();
			preferences.unregisterOnSharedPreferenceChangeListener(preferenceListener);
		}
	}

}
