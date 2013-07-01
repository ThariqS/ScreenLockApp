package org.uzero.android.crope.activity;

import org.uzero.android.crope.R;

import android.os.Bundle;
import android.preference.ListPreference;

public class GeneralPreferenceActivity extends ArrayPreferenceActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs_general);

		ListPreference prefs = (ListPreference)findPreference(getString(R.string.prefs_key_unlock_process));
		prefs.setOnPreferenceChangeListener(buildOnPreferenceChangeListener(prefs));
		prefs.setSummary(prefs.getEntry().toString());

		prefs = (ListPreference)findPreference(getString(R.string.prefs_key_unlock_slide_distance));
		prefs.setOnPreferenceChangeListener(buildOnPreferenceChangeListener(prefs));
		prefs.setSummary(prefs.getEntry().toString());

		prefs = (ListPreference)findPreference(getString(R.string.prefs_key_dock_show_mode));
		prefs.setOnPreferenceChangeListener(buildOnPreferenceChangeListener(prefs));
		prefs.setSummary(prefs.getEntry().toString());

	}

}
