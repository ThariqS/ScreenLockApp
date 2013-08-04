package org.uzero.android.crope;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Date;
import java.lang.Integer;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Collections;

import android.location.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;

import android.util.Log;

import android.content.IntentSender;

import org.uzero.android.crope.activity.GeneralPreferenceActivity;
import org.uzero.android.crope.CircularSeekBar;
import org.uzero.android.crope.CircularSeekBar.OnSeekChangeListener;

import org.uzero.android.lock.ScreenActivity;

import android.app.Activity;
import android.app.KeyguardManager.KeyguardLock;
import android.app.KeyguardManager;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AnalogClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.telephony.TelephonyManager;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.provider.Settings.Secure;

import java.util.Random;

import java.io.BufferedWriter;
import android.os.Environment;
import java.io.File;
import java.io.FileWriter;

import android.util.Log;

public class CropeActivity extends ScreenActivity implements ConnectionCallbacks, OnConnectionFailedListener{

	//count options

	private enum Question { LUNCH, SLEEP, MOOD,COFFEE,ALERT,WATER,MATH,CONVERSATIONS,BUSY,NONE,WINDOWS,EXCERCISE}
	private Question curr_question;
	private int correct_option = -1;
	ArrayList questionList = null;
	
	public static final String COUNT_FILE = "CountFile";
	public static final String COUNT_CHANCE = "Count_chance";

	public static final String COUNT_DOY = "Count_doy";
	public static final String COUNT_SLEEP = "Count_sleep";
	public static final String COUNT_LUNCH = "Count_lunch";
	public static final String COUNT_MOOD = "Count_mood";
	public static final String COUNT_LAST_CHECK = "Count_last_check";

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private SharedPreferences settings;

	private LocationClient mLocationClient;
	
	private Location mLocation;

	private GestureDetector mGestureDetector;

	private SharedPreferences mPrefs = null;

	private List<Intent> mIntentList = new ArrayList<Intent>();

	private boolean mPreviewMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		mLocationClient = new LocationClient(this, this, this);
		
		//disable keyguard
		KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
		KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
		lock.disableKeyguard();
		createFileOnDevice();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
        // Connect the client.
        mLocationClient.connect();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}
	
	protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
		super.onStop();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CONNECTION_FAILURE_RESOLUTION_REQUEST:
				switch (resultCode) {
                case Activity.RESULT_OK :
                	// If the result is OK try to connect again
                	break;
				}
		}
	}
	
	private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Get the error code
        	return false;
        }
    }

	public void onConnected(Bundle dataBundle) {
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		initContentView();
	}
	
	public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
	}
	
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
        }
    }
	
	@Override
	public void finish() {
		super.finish(mPreviewMode);
		//findViewById(R.id.crope_lock_background).setOnTouchListener(null);
		overridePendingTransition(0, 0);
	}

	public void writeLocation(Boolean ping) {
		Date time_of_action = new Date();
		String phone_id = getIMEI();
		LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(crit, true);
		Location loc = lm.getLastKnownLocation(provider);
		
	    LocationDataSource db = new LocationDataSource(this);
	    db.open();
	    db.createLocation(loc.getLatitude(),loc.getLongitude());
	    db.close();
	}


	Date time_of_start;
	private void initContentView() {

		time_of_start = new Date();
		int numOfQuestions = mPrefs.getInt("questionNumber",0);
		
		int randQuestion = (int)( Math.random() * numOfQuestions);

		initLayout(randQuestion);

		CircularSeekBar circularSeekbar = (CircularSeekBar)findViewById(R.id.circle_bar_1);
		circularSeekbar.setMaxProgress(100);
		circularSeekbar.setProgress(100);
		circularSeekbar.invalidate();

		final CropeActivity ca = this;

        circularSeekbar.setSeekBarChangeListener(new OnSeekChangeListener() {
            @Override
            public void onProgressChange(CircularSeekBar view, int newProgress) {
        	    UnlockDataSource unlockDb = new UnlockDataSource(getApplicationContext());
        	    AnswerDataSource answerDb = new AnswerDataSource(getApplicationContext());
        	    unlockDb.open();
        	    answerDb.open();
        	    TextView question = (TextView) findViewById(R.id.questionText);
                double percent = ((double) newProgress) / view.getMaxProgress();
                Log.d("Percent: ","" + percent);
                if (percent < 0.333) {
                	TextView answer = (TextView) findViewById(R.id.answer1);
                	answerDb.createAnswer(question.getText().toString(), answer.getText().toString());
                } else if (percent < 0.666) {
                	TextView answer = (TextView) findViewById(R.id.answer2);
                	answerDb.createAnswer(question.getText().toString(), answer.getText().toString());
                } else {
                	TextView answer = (TextView) findViewById(R.id.answer3);
                	answerDb.createAnswer(question.getText().toString(), answer.getText().toString());
                }
        	    unlockDb.insertUnlockTime();
        	    unlockDb.close();
        	    answerDb.close();
            	ca.finish();
            }
        });

		String phone_id = getIMEI();

		//refresh counts if it's a new day
		Calendar calendar1 = Calendar.getInstance();
    	int doy = calendar1.get(Calendar.DAY_OF_YEAR);
    	int hod = calendar1.get(Calendar.HOUR_OF_DAY);

    	settings = getSharedPreferences(COUNT_FILE, 0);

    	int old_doy = settings.getInt(COUNT_DOY,-1);
    	
    	mLocation = mLocationClient.getLastLocation();
    	
        LocationDataSource db = new LocationDataSource(this);
        db.open();
        db.createLocation(mLocation.getLatitude(),mLocation.getLongitude());
        db.close();

    	if (old_doy != doy || questionList == null) {
    		SharedPreferences.Editor editor = settings.edit();
    		editor.commit();
    	}

    	if (questionList == null || questionList.size() == 0 ) {
 			questionList = new ArrayList();
 			questionList.add(Question.MATH);
 			questionList.add(Question.LUNCH);
 			questionList.add(Question.MOOD);
 			//questionList.add(Question.COFFEE);
 			questionList.add(Question.ALERT);
 			questionList.add(Question.WATER);
 			questionList.add(Question.CONVERSATIONS);
 			questionList.add(Question.BUSY);
 			questionList.add(Question.WINDOWS);
 			questionList.add(Question.EXCERCISE);

    	}

    	Collections.shuffle(questionList);

    	/*
    	long temp_time = settings.getLong(COUNT_LAST_CHECK, 0);
    	long long_time = d.getTime();
    	long time_diff = long_time - temp_time;


    	int lunch = settings.getInt(COUNT_LUNCH,-1); 
    	int mood = settings.getInt(COUNT_MOOD,-1); 
    	int sleep = settings.getInt(COUNT_SLEEP,-1); 

    	if (time_diff < 1000*60*15) {
			curr_question = Question.NONE;
    	} else if (sleep == 0) {
    		curr_question = Question.SLEEP;
    	} else if (lunch == 0 && hod > 11 && hod < 15) {
    		curr_question = Question.LUNCH;
    	} else {
    		curr_question = Question.MOOD;
    	}*/

	}

	public void onSlide(View view) {
		finish();
	}

	private void initLayout(int questionNumber) {
		String showMode = mPrefs.getString(
				getString(R.string.prefs_key_dock_show_mode),
				getString(R.string.dock_show_mode_default_value));
		
		setContentView(R.layout.crope_lock);
		
		RelativeLayout frameLayout = (RelativeLayout) findViewById(R.id.crope_lock);
		RelativeLayout.LayoutParams params;
		
		TextView question = (TextView) findViewById(R.id.questionText);
		question.setText(mPrefs.getString("q_" + questionNumber + "_text","Question Not Found"));
		
		int answerCount = mPrefs.getInt("q_" + questionNumber + "_answerCount",0);
		
		TextView answer = (TextView) findViewById(R.id.answer1);
		answer.setText(mPrefs.getString("q_" + questionNumber + "_a_0","Answer Not Found"));
		TextView answer2 = (TextView) findViewById(R.id.answer2);
		answer2.setText(mPrefs.getString("q_" + questionNumber + "_a_1","Answer Not Found"));
		TextView answer3 = (TextView) findViewById(R.id.answer3);
		answer3.setText(mPrefs.getString("q_" + questionNumber + "_a_2","Answer Not Found"));
		
	}

	String getIMEI() {
		return Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID); 
	}

	private static BufferedWriter out;
	private void createFileOnDevice() {
	    /*
	     * Function to initially create the log file and it also writes the time of creation to file.
	     */
	    try {
		    Boolean append = true;
		    File r = Environment.getExternalStorageDirectory();
		    if(r.canWrite()){
		         File  LogFile = new File(r, "Log.txt");
		         FileWriter LogWriter = new FileWriter(LogFile, append);
		         out = new BufferedWriter(LogWriter);
		         Date date = new Date();
		         out.write("Logged at" + String.valueOf(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "\n"));
		    }
	    } catch (Exception e) {
		}
	}
}
