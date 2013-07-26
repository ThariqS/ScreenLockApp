package org.uzero.android.crope;

import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LocationDataSource {
	  // Database fields
	  private SQLiteDatabase database;
	  private LocationSQLiteHelper dbHelper;
	  private String[] allColumns = { LocationSQLiteHelper.COLUMN_ID,
			  LocationSQLiteHelper.COLUMN_LATITUDE, LocationSQLiteHelper.COLUMN_LONGITUDE };
	  
	  public LocationDataSource(Context context) {
		  dbHelper = new LocationSQLiteHelper(context);
	  }
	  
	  public void open () throws SQLException {
		  database = dbHelper.getWritableDatabase();
	  }
	  
	  public void close() {
		  dbHelper.close();
	  }
	  
	  public long createLocation(LatLng loc) {
		  ContentValues values = new ContentValues();
		  values.put(LocationSQLiteHelper.COLUMN_LATITUDE, loc.latitude);
		  values.put(LocationSQLiteHelper.COLUMN_LONGITUDE, loc.longitude);
		  return database.insert(LocationSQLiteHelper.TABLE_LOCATIONS, null, values);
	  }
	  
	  public long createLocation(double latitude, double longitude) {
		  ContentValues values = new ContentValues();
		  values.put(LocationSQLiteHelper.COLUMN_LATITUDE, latitude);
		  values.put(LocationSQLiteHelper.COLUMN_LONGITUDE, longitude);
		  return database.insert(LocationSQLiteHelper.TABLE_LOCATIONS, null, values);
	  }
	  
	  public void deleteLocation(long id) {
		  database.delete(LocationSQLiteHelper.TABLE_LOCATIONS, LocationSQLiteHelper.COLUMN_ID
			        + " = " + id, null);
	  }
	  
	  public List<LatLng> getAllLocations() {
		  List<LatLng> locations = new ArrayList<LatLng>();
		  Cursor cursor = database.query(LocationSQLiteHelper.TABLE_LOCATIONS,
			            allColumns, null, null, null, null, null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  LatLng loc = new LatLng(cursor.getDouble(1),cursor.getDouble(2));
			  locations.add(loc);
			  cursor.moveToNext();
		  }
		  // Make sure to close the cursor
		  cursor.close();
		  return locations;
	  }
}
