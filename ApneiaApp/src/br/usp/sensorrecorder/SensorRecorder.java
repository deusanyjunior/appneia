package br.usp.sensorrecorder;

import br.usp.appneia.R;
import br.usp.appneia.settings.DeviceUtils;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorRecorder {
	
	Context context;
	
	private SensorManager sensorManager;
	private String recordFolderPath;
	String filenameRecordSensors;
	private int sensorsDelay = -1;
	private int sensors = 0;
	
	public SensorRecorder(Context context, String recordFolderPath, int sensorsDelay, int sensors) {
		
		this.context = context;
		this.recordFolderPath = recordFolderPath;
		this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		this.sensorsDelay = sensorsDelay;
		this.sensors = sensors;
		this.createRecorderFile();
		
	}
	
	public void onResume() {
		
		if (sensors == -1) {
			
			for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
				
				sensorManager.registerListener((SensorEventListener) context,
						sensor,
						sensorsDelay);
			}
		}
	}
	
	public void onStop() {
		
		if (sensors == -1) {
			
			for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
				
				sensorManager.unregisterListener((SensorEventListener) context,
						sensor);
			}
		}
	}
	
	private void createRecorderFile() {
		if (recordFolderPath != null) {
			filenameRecordSensors = context.getResources().getString(R.string.file_record_sensors);
			DeviceUtils.writeDataOnFile(recordFolderPath, filenameRecordSensors, DeviceUtils.getBuildInfo());
			DeviceUtils.writeDataOnFile(recordFolderPath, filenameRecordSensors, "\n");
		}
	}
	
	public void saveEvent(SensorEvent event) {
		
		//TODO: create a buffer ring and write only after some seconds
		StringBuilder data = new StringBuilder();
		data.append(event.timestamp);
		data.append(' ');
		data.append(event.sensor.getName());
		data.append(' ');
		for (float eventData: event.values) {
			
			data.append(eventData);
			data.append(' ');
		}
		data.append("\n");
		DeviceUtils.writeDataOnFile(recordFolderPath, filenameRecordSensors, data.toString());		
	
	}
}