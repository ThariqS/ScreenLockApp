package org.uzero.android.crope;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_LOCATIONS = "locations";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";

	private static final String LOC_DATABASE_NAME = "locations.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation SQL statement
	private static final String LOC_DATABASE_CREATE = "create table "
	      + TABLE_LOCATIONS + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_LATITUDE
	      + " DECIMAL(10, 8) NOT NULL, "+  COLUMN_LONGITUDE
	      + " DECIMAL(11, 8) NOT NULL,"
	      + " ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

	public LocationSQLiteHelper(Context context) {
		    super(context, LOC_DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(LOC_DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer) {
		// TODO Auto-generated method stub
	    Log.w(LocationSQLiteHelper.class.getName(),
	            "Upgrading database from version " + oldVer + " to "
	                + newVer + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
	    onCreate(database);
	}
	
}
