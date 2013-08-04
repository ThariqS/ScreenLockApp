package org.uzero.android.crope;

import java.io.IOException;
import java.io.InputStream;

import org.uzero.android.crope.theme.ThemeResources;
import org.uzero.android.lock.ScreenService;
import org.uzero.android.lock.ScreenService.ScreenServiceBinder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
        
        parseQuestionList();
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
		
		else if (getString(R.string.prefs_key_show_stats).equals(preference.getKey())) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setClass(this,StatsActivity.class);
			intent.addCategory(Intent.CATEGORY_TEST);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
	
	private void parseQuestionList() {
		
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		try {
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
			
			InputStream is = getAssets().open("default_question_list.xml");
			
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();

			parser.setInput(is, null);

			int eventType = parser.getEventType();
			int questionNumber = 0;
			int answerCount = 0;
			String text = "";
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagname = parser.getName();
				switch (eventType) {
					case XmlPullParser.START_TAG:
						if (tagname.equalsIgnoreCase("question")) {
							// create a new instance of employee
							
						} else if (tagname.equalsIgnoreCase("answers")) {
							answerCount = 0;
						}
						break;
	
					case XmlPullParser.TEXT:
						text = parser.getText();
						break;
	
					case XmlPullParser.END_TAG:
						if (tagname.equalsIgnoreCase("question")) {
							questionNumber++;
						} else if (tagname.equalsIgnoreCase("questions")) {
							editor.putInt("questionNumber", questionNumber+1);
						} else if (tagname.equalsIgnoreCase("question_type")) {
							editor.putString("q_" + questionNumber + "_type", text);
						} else if (tagname.equalsIgnoreCase("text")) {
							editor.putString("q_" + questionNumber + "_text", text);
						} else if (tagname.equalsIgnoreCase("answer_type")) {
							editor.putString("q_" + questionNumber + "_answerType", text);
						} else if (tagname.equalsIgnoreCase("answer")) {
							editor.putString("q_" + questionNumber + "_a_" + answerCount, text);
							answerCount++;
						} else if (tagname.equalsIgnoreCase("answers")) {
							editor.putInt("q_" + questionNumber + "_answerCount", answerCount+1);
						}

						break;
	
					default:
						break;
				}
				eventType = parser.next();
			}
			editor.commit();

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
		
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