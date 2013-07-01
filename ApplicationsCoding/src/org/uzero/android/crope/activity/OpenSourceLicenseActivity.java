package org.uzero.android.crope.activity;

import org.uzero.android.crope.CropeSettingActivity;
import org.uzero.android.crope.R;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.actionbarcompat.ActionBarActivity;

public class OpenSourceLicenseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oss_license);
		if(Build.VERSION.SDK_INT >= 14) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, CropeSettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
		}
		
        return super.onOptionsItemSelected(item);
	}

}
