package org.uzero.android.crope;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnswerSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_ANSWERS = "answers";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_QUESTION = "question";
	public static final String COLUMN_ANSWER = "answer";

	private static final String DATABASE_NAME = "answers.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
	      + TABLE_ANSWERS + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_QUESTION
	      + " VARCHAR(255) NOT NULL, "+  COLUMN_ANSWER
	      + " VARCHAR(255) NOT NULL,"
	      + " ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

	public AnswerSQLiteHelper(Context context) {
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
	    Log.w(LocationSQLiteHelper.class.getName(),
	            "Upgrading database from version " + oldVer + " to "
	                + newVer + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
	    onCreate(database);
	}
}
