/**
 * New Record Monitor Activity
 */
package br.usp.appneia.newrecord;

import java.io.IOException;

import com.buihha.audiorecorder.Mp3Recorder;

import br.usp.appneia.R;
import br.usp.appneia.settings.DeviceUtils;
import br.usp.sensorrecorder.SensorRecorder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
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
			int[] p = DeviceUtils.getSharedPreferences(context);
			
			if (p[0] == 0) { // 0 = MP3
				
				try {
					
					mp3Recorder = new Mp3Recorder(recordFolderPath, p[1], p[2], p[3], p[4], p[5]);
					mp3Recorder.startRecording();
					created = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (p[7] != 0) {
				
				sensorRecorder = new SensorRecorder(context, recordFolderPath, p[6], p[7]);
			}
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
		
		//TODO: ask user

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
