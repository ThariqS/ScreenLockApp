package org.uzero.android.crope;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;


import org.uzero.android.crope.theme.ThemeResources;
import org.uzero.android.lock.ScreenService;
import org.uzero.android.lock.ScreenService.ScreenServiceBinder;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	private ScreenService mScreenService = null;
	private ServiceConnection  mScreenServiceConn = null;
	private BroadcastReceiver mReceiver = null;

	@Override
	public void onReceive(final Context context, final Intent bootintent) {
	
		Log.e("myApp", "GOT BOOT");
		
		Intent intents = new Intent(context,CropeSettingActivity.class);
		intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intents);
		
		Intent startServiceIntent = new Intent(context, BootService.class);
		context.startService(startServiceIntent);
	
	}
}