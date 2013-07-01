package org.uzero.android.crope.activity;

import org.uzero.android.crope.R;

import android.os.Bundle;

public class AboutPreferenceActivity extends ArrayPreferenceActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs_about);
    }

}
