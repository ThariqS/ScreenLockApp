package org.uzero.android.crope;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UnlockSQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_UNLOCKS = "unlocks";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TS = "ts";

	private static final String DATABASE_NAME = "unlocks.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
	      + TABLE_UNLOCKS + "(" + COLUMN_ID
	      + " integer primary key autoincrement, "
	      + " ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

	public UnlockSQLiteHelper(Context context) {
		    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		database.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVer, int newVer) {
		// TODO Auto-generated method stub
	    Log.w(UnlockSQLiteHelper.class.getName(),
	            "Upgrading database from version " + oldVer + " to "
	                + newVer + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_UNLOCKS);
	    onCreate(database);
	}
}
