package com.caribelabs.whereismycellphone.controllers;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.caribelabs.whereismycellphone.R;

public class FindingReceiverActivity extends Activity{
	
	private String WHEREISMYCELLPHONE = this.getClass().toString();
	private MediaPlayer mediaPlayer; 
	private Vibrator vibratorPlayer;
	private Uri ringtone = Uri.parse("");
	
	private Button btnFound ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searching_receiver);
		 btnFound = (Button) findViewById(R.id.btnFound);
		 btnFound.setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopAlarm();
			}
		});
		vibratorPlayer = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		playSound(getApplicationContext(),getAlarmUri());
		//moveTaskToBack (true);
		
	}
	
    private void playSound(Context context, Uri alert) {
    	mediaPlayer = new MediaPlayer();
        try {
        	mediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            	mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            	mediaPlayer.setLooping(true);
            	mediaPlayer.prepare();
            	mediaPlayer.start();
            }
        } catch (IOException e) {
            Log.e(WHEREISMYCELLPHONE + " : ",e.getMessage());
        }
    }
    
    private void stopAlarm(){
    	mediaPlayer.stop();
        turnVibratorOn(false);
        finish();    
        moveTaskToBack(true);
    }
    
    private void turnVibratorOn(final boolean state){
		if (state) {
			long[] pattern = { 0, 300, 1000 };
			vibratorPlayer.vibrate(pattern, 0);
		}else{
			vibratorPlayer.cancel();
		}
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
    @Override
	public void onBackPressed() {
    	moveTaskToBack (true);
    }
    
    private Uri getAlarmUri() {
        if (ringtone == null  || ringtone.toString() == "" ) {
        	ringtone = Settings.System.DEFAULT_ALARM_ALERT_URI;
            if (ringtone == null  || ringtone.toString() == "") {
            	ringtone = Settings.System.DEFAULT_ALARM_ALERT_URI;
            }
        }
        
        return ringtone;
    }
}
