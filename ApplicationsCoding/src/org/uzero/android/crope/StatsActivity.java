package org.uzero.android.crope;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.view.Gravity;

import java.sql.Timestamp;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.PieSlice;
import com.echo.holographlibrary.PieGraph;

public class StatsActivity extends FragmentActivity {
	
	private static String[] colorList = {"#99CC00","#FFBB33","#AA66CC","#FFBB33","#AA66CC","#99CC00"};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.stats);
        ViewGroup layout = (ViewGroup) findViewById(R.id.scrollcontainer);
        
        TextView tv = new TextView(this);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        tv.setLayoutParams(params);
        tv.setText("Daily Unlocks");
        layout.addView(tv);
        
	    UnlockDataSource unlockDb = new UnlockDataSource(getApplicationContext());
	    unlockDb.open();
        ArrayList<Bar> points = new ArrayList<Bar>();
        for (int i = 0; i < 24; i++) {
        	Bar d = new Bar();
        	d.setValue(0);
        	d.setColor(Color.parseColor("#99CC00"));
        	d.setName("" + i);
        	points.add(d);
        }
        List<Timestamp> times = unlockDb.getAllUnlockTimes();
        int numOfUnlocks = times.size();
        for (int i = 0; i < numOfUnlocks; i++) {
        	Bar b = points.get(times.get(i).getHours());
        	b.setValue(b.getValue() + 1);
        }
        unlockDb.close();
        
        BarGraph g = new BarGraph(this);
        g.setShowBarText(true);
        g.setBars(points);
        g.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 200));
        layout.addView(g);
        
	    AnswerDataSource answerDb = new AnswerDataSource(getApplicationContext());
	    answerDb.open();
	    
	    List<String> questions = answerDb.getAllQuestions();
	    
	    for (String q: questions) {
	    	
	        TextView questionView = new TextView(this);
	        questionView.setLayoutParams(params);
	        questionView.setText(q);
	        layout.addView(questionView);
	        Log.d("Question",q);
	    	
	    	HashMap<String,Integer> answers = answerDb.getAllAnswers(q);
	        PieGraph pg = new PieGraph(this);
	        int colorCount = 0;
		    for (Map.Entry<String, Integer> entry : answers.entrySet()) {
		        String key = entry.getKey();
		        int value = entry.getValue();
		        Log.d("Debug",key + " : " + Integer.toString(value));
		        PieSlice slice = new PieSlice();
		        slice.setColor(Color.parseColor(colorList[colorCount]));
		        slice.setValue(value);
		        slice.setTitle(key);
		        pg.addSlice(slice);
		        colorCount++;
		    }
	        pg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 200));
	        layout.addView(pg);
	    }
        
        answerDb.close();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

}
