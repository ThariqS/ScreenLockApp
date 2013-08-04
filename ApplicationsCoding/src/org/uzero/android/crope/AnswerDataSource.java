package org.uzero.android.crope;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AnswerDataSource {
	  // Database fields
	  private SQLiteDatabase database;
	  private AnswerSQLiteHelper dbHelper;
	  private String[] allColumns = { AnswerSQLiteHelper.COLUMN_ID,
			  AnswerSQLiteHelper.COLUMN_QUESTION, AnswerSQLiteHelper.COLUMN_ANSWER };
	  
	  public AnswerDataSource(Context context) {
		  dbHelper = new AnswerSQLiteHelper(context);
	  }
	  
	  public void open () throws SQLException {
		  database = dbHelper.getWritableDatabase();
	  }
	  
	  public void close() {
		  dbHelper.close();
	  }
	  
	  public long createAnswer(String question, String answer) {
		  ContentValues values = new ContentValues();
		  values.put(AnswerSQLiteHelper.COLUMN_QUESTION, question);
		  values.put(AnswerSQLiteHelper.COLUMN_ANSWER, answer);
		  return database.insert(AnswerSQLiteHelper.TABLE_ANSWERS, null, values);
	  }
	  
	  public List<String> getAllAnswers(String question) {
		  List<String> answers = new ArrayList<String>();
		  Cursor cursor = database.query(AnswerSQLiteHelper.TABLE_ANSWERS,
			            allColumns, null, null, null, null, null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  answers.add(cursor.getString(2));
			  cursor.moveToNext();
		  }
		  // Make sure to close the cursor
		  cursor.close();
		  return answers;
	  }
}
