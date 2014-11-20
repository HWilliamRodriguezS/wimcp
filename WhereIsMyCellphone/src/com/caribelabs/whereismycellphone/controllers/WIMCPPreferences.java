package com.caribelabs.whereismycellphone.controllers;


import com.caribelabs.whereismycellphone.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class WIMCPPreferences extends PreferenceActivity{
	
	//private 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main_preferences);
		
	}
	
	

}
