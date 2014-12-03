package com.caribelabs.whereismycellphone.controllers;


import com.caribelabs.whereismycellphone.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.provider.Settings;

public class WIMCPPreferences extends PreferenceActivity{
	
	private SharedPreferences prefs;
	
	private RingtonePreference menuRingtone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main_preferences);
		Context context = getApplicationContext();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String ringtone = RingtoneManager.getRingtone(context,
				Settings.System.DEFAULT_ALARM_ALERT_URI).getTitle(context);
		
		menuRingtone = (RingtonePreference)findPreference("ringtone");
		menuRingtone.setSummary(ringtone);
		menuRingtone.setOnPreferenceChangeListener(
				new RingtonePreference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference p,
							Object newValue) {

						String name = RingtoneManager.getRingtone(
								getApplicationContext(),
								Uri.parse((String) newValue)).getTitle(
								getApplicationContext());
						p.setSummary((String) name);
						return true;
					}
				});
		
	}
	
	

}
