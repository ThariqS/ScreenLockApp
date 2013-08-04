package org.uzero.android.crope;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.sql.Timestamp;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.PieSlice;
import com.echo.holographlibrary.PieGraph;

public class StatsActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);
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
        	//Log.d("Unlock Times", times.get(i).getHours()+":"+ times.get(i).getMinutes());
        	Bar b = points.get(times.get(i).getHours());
        	b.setValue(b.getValue() + 1);
        }

        BarGraph g = (BarGraph)findViewById(R.id.graph);
        g.setShowBarText(false);
        g.setBars(points);
        unlockDb.close();
        
	    AnswerDataSource answerDb = new AnswerDataSource(getApplicationContext());
	    answerDb.open();
	    
	    List<String> answers = answerDb.getAllAnswers("Have you exercised today?");
	    int numOfAnswers = answers.size();
	    int[] answerCounts = new int[3];
        for (int i = 0; i < numOfAnswers; i++) {
        	Log.d("Answers", answers.get(i));
        	if (answers.get(i).equals("Yes")) {
        		answerCounts[0]++;
        	} else if (answers.get(i).equals("No")) {
        		answerCounts[1]++;
        	} else if (answers.get(i).equals("Yes, but later")) {
        		answerCounts[2]++;
        	}
        }
        Log.d("Counts", answerCounts[0]+":"+ answerCounts[1]+":"+answerCounts[2]);
        PieGraph pg = (PieGraph)findViewById(R.id.pieChart);
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#99CC00"));
        slice.setValue(answerCounts[0]);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFBB33"));
        slice.setValue(answerCounts[1]);
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#AA66CC"));
        slice.setValue(answerCounts[2]);
        pg.addSlice(slice);
        
        answerDb.close();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

}
