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

/*
    if (mReceiver == null) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new BootReceiver();
        context.registerReceiver(mReceiver, filter);
    }
*/
/*
    if (bootintent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
        //screenOff = true;
    } else if (bootintent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        //screenOff = false;
         Toast.makeText(context, "test",Toast.LENGTH_SHORT).show();
    }*/

/*
	mScreenServiceConn = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			mScreenService = ((ScreenServiceBinder)service).getService();
			mScreenService.setActivity(CropeActivity.class);		
		}

		public void onServiceDisconnected(ComponentName name) {
			mScreenService = null;
		}
	};

	Intent intent = new Intent(context, ScreenService.class);
	intent.setAction(ScreenService.ACTION_LOCK_RESTORE);
    context.bindService(intent, mScreenServiceConn, Context.BIND_AUTO_CREATE);
    Toast.makeText(context, "test",Toast.LENGTH_SHORT).show();*/


 }
}