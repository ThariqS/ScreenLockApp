package org.uzero.android.crope;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

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
	  
	  public List<String> getAllQuestions() {
		  List<String> questions = new ArrayList<String>();
		  String[] justQuestions = {AnswerSQLiteHelper.COLUMN_QUESTION};
		  Cursor cursor = database.query(true,AnswerSQLiteHelper.TABLE_ANSWERS,
				  justQuestions, null, null, null, null, null, null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  questions.add(cursor.getString(0));
			  cursor.moveToNext();
		  }
		  // Make sure to close the cursor
		  cursor.close();
		  return questions;
	  }
	  
	  public HashMap<String,Integer> getAllAnswers(String question) {
		  HashMap<String,Integer> answers = new HashMap<String,Integer>();
		  Cursor cursor= database.rawQuery("select answer, count(*) from answers where question='" + question + "' group by answer", null);
		  cursor.moveToFirst();
		  while (!cursor.isAfterLast()) {
			  answers.put(cursor.getString(0), cursor.getInt(1));
			  cursor.moveToNext();
		  }
		  // Make sure to close the cursor
		  cursor.close();
		  return answers;
	  }
}
