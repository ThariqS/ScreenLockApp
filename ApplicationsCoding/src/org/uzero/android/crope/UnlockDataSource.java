package org.uzero.android.crope;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.sql.Timestamp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UnlockDataSource {
	  // Database fields
	  private SQLiteDatabase database;
	  private UnlockSQLiteHelper dbHelper;
	  private String[] allColumns = { UnlockSQLiteHelper.COLUMN_ID,
			  UnlockSQLiteHelper.COLUMN_TS};

	  
	  public UnlockDataSource(Context context) {
		  dbHelper = new UnlockSQLiteHelper(context);
	  }
	  
	  public void open () throws SQLException {
		  database = dbHelper.getWritableDatabase();
	  }
	  
	  public void close() {
		  dbHelper.close();
	  }
	  
	  public long insertUnlockTime() {
		  ContentValues values = new ContentValues();
		  long currentTime = Calendar.getInstance().getTimeInMillis();
		  Timestamp ts = new Timestamp(currentTime);
		  values.put(UnlockSQLiteHelper.COLUMN_TS, ts.toString());
		  return database.insert(UnlockSQLiteHelper.TABLE_UNLOCKS, null, values);
	  }
	  
	  public List<Timestamp> getAllUnlockTimes() {
		  List<Timestamp> times = new ArrayList<Timestamp>();
		  Cursor cursor = database.query(UnlockSQLiteHelper.TABLE_UNLOCKS,
				  allColumns, null, null, null, null, null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  times.add(Timestamp.valueOf(cursor.getString(1)));
			  cursor.moveToNext();
		  }
		  return times;
	  }
}
