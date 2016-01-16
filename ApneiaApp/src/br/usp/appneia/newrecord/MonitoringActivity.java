/**
 * New Record Monitor Activity
 */
package br.usp.appneia.newrecord;

import java.io.IOException;

import com.buihha.audiorecorder.Mp3Recorder;

import br.usp.appneia.AppneiaActivity;
import br.usp.appneia.R;
import br.usp.sensorrecorder.SensorRecorder;
import br.usp.utils.DeviceUtils;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

/**
 * @author dj
 *
 */
public class MonitoringActivity extends Activity implements SensorEventListener {

	Context context = this;
	String recordFolderPath = null;
	Mp3Recorder mp3Recorder = null;
	SensorRecorder sensorRecorder = null;

	
	boolean created = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_record_monitoring);
		
		if (!created) {

			recordFolderPath = getIntent().getExtras().getString("recordFolderPath");		
			setupButtons();
			
			// load audio preferences
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    		String audioRecordingFormat = sharedPreferences.getString(DeviceUtils.PREF_AUDIO_RECORDING_FORMAT, "MP3");
    		int audioMP3kbps = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_MP3_KBPS, "96"));
    		int audioSource = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_SOURCE, 
    				Integer.toString(MediaRecorder.AudioSource.DEFAULT)));
    		int audioSampleRate = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_SAMPLE_RATE, "44100"));
    		int audioChannels = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_CHANNELS, 
    				Integer.toString(AudioFormat.CHANNEL_IN_MONO)));
    		int audioEncoding = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_ENCODING, 
    				Integer.toString(AudioFormat.ENCODING_PCM_16BIT)));
    		
    		if( audioRecordingFormat.equals("MP3")) {
				try {
					
					mp3Recorder = new Mp3Recorder(recordFolderPath, audioMP3kbps, audioSource, audioSampleRate, audioChannels, audioEncoding);
					mp3Recorder.startRecording();
					created = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    		}
    		// TODO: WAVE and 3GP
    		
    		sensorRecorder = new SensorRecorder(context, recordFolderPath);

		}
	}
	
	@Override
	public void onResume() {
		
		if (sensorRecorder != null) {
			
			sensorRecorder.onResume();
		}
		super.onResume();
	}
	
	@Override
	public void onStop() {
		
		if (sensorRecorder != null) {
			
			sensorRecorder.onStop();
		}
		super.onStop();
	}
	
	@Override
	public void onBackPressed() {
		
		new AlertDialog.Builder(this)
        .setTitle(R.string.alert_finish_monitoring_title)
        .setMessage(R.string.alert_finish_monitoring_message)
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

        	@Override
        	public void onClick(DialogInterface arg0, int arg1) {
        		try {
        		
        			Intent intentSave = new Intent(context, AppneiaActivity.class);
        			startActivity(intentSave);
        			finalize();
        		} catch (Throwable e) {
        			e.printStackTrace();
        		}
        		MonitoringActivity.super.onBackPressed();
            }

        }).create().show();
	}
	
	/**
	 * Configure actions for each button
	 */
	public void setupButtons() {
		
		Button button_monitoring_finalization = (Button) findViewById(R.id.buttonMonitoringFinalizeMonitoring);
		button_monitoring_finalization.setOnClickListener(onClickListenerButtonMonitoringFinalization());
	}
	
	/**
	 * Define the action of button Monitoring Finalization
	 * @return the listener action that starts MonitoringActivity
	 */
	private View.OnClickListener onClickListenerButtonMonitoringFinalization() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try {
					if (mp3Recorder != null) {
						
						mp3Recorder.stopRecording();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Intent intentMonitoringFinalization = new Intent(context, AfterSleepFormActivity.class);
				intentMonitoringFinalization.putExtra("recordFolderPath", recordFolderPath);
				startActivity(intentMonitoringFinalization);
				finish();
			}
		};
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (event) {
			
			sensorRecorder.saveEvent(event);		
		}
	}
}
