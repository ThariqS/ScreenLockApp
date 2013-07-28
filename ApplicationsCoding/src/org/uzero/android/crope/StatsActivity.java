package org.uzero.android.crope;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.sql.Timestamp;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;

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
        	Bar b = points.get(times.get(i).getHours());
        	b.setValue(b.getValue() + 1);
        }

        BarGraph g = (BarGraph)findViewById(R.id.graph);
        g.setShowBarText(false);
        g.setBars(points);
        unlockDb.close();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

}
