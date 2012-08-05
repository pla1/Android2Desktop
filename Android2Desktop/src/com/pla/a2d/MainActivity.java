package com.pla.a2d;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final static String TAG = "MainActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		Uri data = intent.getData();
		Log.i(TAG, "URI: " + data);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String ipAddress = preferences.getString("ipAddress", null);
		String portString = preferences.getString("port", "0");
		int port = Utils.parse(portString);
		if (data == null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				String s = extras.getString(Intent.EXTRA_TEXT);
				Log.i(TAG, "Data is null trying extra text: " + s);
				data = Uri.parse(s);
			}
		}
		if (data != null) {
			if (Utils.isBlank(ipAddress) || port == 0) {
				launchSettings();
				Toast.makeText(getBaseContext(), "IP Address and port name required.", Toast.LENGTH_SHORT).show();
				Intent settingIntent = new Intent(getBaseContext(), SettingsActivity.class);
				startActivity(settingIntent);
			}
			NetworkTask networkTask = new NetworkTask(ipAddress, port, data);
			networkTask.execute();
			finish();
		}
		final Button settingsButton = (Button) findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				launchSettings();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			launchSettings();
			return true;
		}
		return false;
	}

	private void launchSettings() {
		Intent settingsIntent = new Intent(getBaseContext(), SettingsActivity.class);
		startActivity(settingsIntent);
	}
}
