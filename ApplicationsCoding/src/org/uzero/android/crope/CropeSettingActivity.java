package org.uzero.android.crope;

import org.uzero.android.crope.theme.ThemeResources;
import org.uzero.android.lock.ScreenService;
import org.uzero.android.lock.ScreenService.ScreenServiceBinder;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.example.android.actionbarcompat.ActionBarPreferenceActivity;

public class CropeSettingActivity extends ActionBarPreferenceActivity {

	public static final int REQUEST_HELP = 0x10;

	public static final int RESULT_FINISH = 0x10;

	private ScreenService mScreenService = null;

	private ServiceConnection  mScreenServiceConn = null;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_main);

		mScreenServiceConn = new ServiceConnection() {
			public void onServiceConnected(ComponentName name, IBinder service) {
				mScreenService = ((ScreenServiceBinder)service).getService();
				updateScreenSetButtonBackground();
			}

			public void onServiceDisconnected(ComponentName name) {
				mScreenService = null;
			}
		};

		Intent intent = new Intent(this, ScreenService.class);
		intent.setAction(ScreenService.ACTION_LOCK_RESTORE);
        bindService(intent, mScreenServiceConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == REQUEST_HELP && resultCode == RESULT_FINISH) {
    		finish();
    	}
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	updateScreenSetButtonBackground();
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unbindService(mScreenServiceConn);
    }

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		
		if(getString(R.string.prefs_key_lock_enabled).equals(preference.getKey())) {
			toggleScreenActivity();
		}
		
		else if(getString(R.string.prefs_key_preview_screen_lock).equals(preference.getKey())) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClass(this,CropeActivity.class);
			intent.addCategory(Intent.CATEGORY_TEST);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}
		
		else if (getString(R.string.prefs_key_show_locations).equals(preference.getKey())) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClass(this,LocationMapActivity.class);
			intent.addCategory(Intent.CATEGORY_TEST);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

    private void toggleScreenActivity() {
    	if(mScreenService == null) {
    		return;
    	}

        if(getPackageName().equals(mScreenService.getActivity())) {
            mScreenService.clearActivity();
        } else {
        	mScreenService.setActivity(CropeActivity.class);
        }
    }


    private void updateScreenSetButtonBackground() {
    	if(mScreenService == null) {
    		return;
    	}

    	CheckBoxPreference prefs = (CheckBoxPreference)findPreference(getString(R.string.prefs_key_lock_enabled));
    	if(prefs == null) {
    		return;
    	}

        if(getPackageName().equals(mScreenService.getActivity())) {
        	prefs.setChecked(true);
        } else {
        	prefs.setChecked(false);
        }
    }
}