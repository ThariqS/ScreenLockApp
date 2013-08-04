package org.uzero.android.crope;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.location.*;
import android.content.Context;
import android.provider.Settings.Secure;
import android.os.Bundle;

/**
 * Simple demo service that schedules a timer task to write something to 
 * the log at regular intervals.
 * @author BMB
 *
 */
public class BootService extends Service {
   /**
    * Delay until first execution of the Log task.
    */
   private final long mDelay = 0;
   /**
    * Period of the Log task.
    */
   private final long mPeriod = 1000*60*20;
   /**
    * Log tag for this service.
    */ 
   private final String LOGTAG = "BootDemoService";
   /**
    * Timer to schedule the service.
    */
   private Timer mTimer;
   
   /**
    * Implementation of the timer task.
    */
   private class LogTask extends TimerTask {
    public void run() {
      Log.e("myApp", "SCHEDULED RUN");
      Date time_of_action = new Date();
      long long_time = time_of_action.getTime();
      LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
      Criteria crit = new Criteria();
      crit.setAccuracy(Criteria.ACCURACY_FINE);
      String provider = lm.getBestProvider(crit, true);
      Location loc = lm.getLastKnownLocation(provider);
      
      LocationDataSource db = new LocationDataSource(getApplicationContext());
      db.open();
      db.createLocation(loc.getLatitude(),loc.getLongitude());
      
      String phone_id = getIMEI();

      Log.e("myApp", "saved object");
      db.close();

    }
   }
   private LogTask mLogTask; 
   
   @Override
   public IBinder onBind(final Intent intent) {
	   return null;
   }
   
   @Override
   public void onCreate() {
    Log.e("myApp", "LAUNCHED TASK");

    super.onCreate();
    mTimer = new Timer();
    mLogTask = new LogTask();

   }

   String getIMEI() {
	   return Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID); 
   }
  
   @Override
   public void onStart(final Intent intent, final int startId) {
    Log.e("myApp", "LAUNCHED TASK");

    // Acquire a reference to the system Location Manager
    LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
          // Called when a new location is found by the network location provider.
          //makeUseOfNewLocation(location);
        }

      public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}

      };

    // Register the listener with the Location Manager to receive location updates
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


    super.onStart(intent, startId);
    mTimer.schedule(mLogTask, mDelay, mPeriod);
   }
}