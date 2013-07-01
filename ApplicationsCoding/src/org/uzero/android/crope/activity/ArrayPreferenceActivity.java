package org.uzero.android.crope.activity;

import org.uzero.android.crope.CropeSettingActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.view.MenuItem;

import com.example.android.actionbarcompat.ActionBarPreferenceActivity;

public abstract class ArrayPreferenceActivity extends ActionBarPreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

	protected Preference.OnPreferenceChangeListener buildOnPreferenceChangeListener(final ListPreference prefs) {
		return new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if(newValue != null) {
					prefs.setSummary(getEntryLabel(prefs, (String)newValue).toString());
				}
				return true;
			}
		};
	}

	protected CharSequence getEntryLabel(ListPreference prefs, String value) {
		int index = prefs.findIndexOfValue(value);
		CharSequence[] entries = prefs.getEntries();
		return entries[index];
	}

}
