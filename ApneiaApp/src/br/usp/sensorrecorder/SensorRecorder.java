package br.usp.sensorrecorder;

import java.util.LinkedList;
import java.util.List;

import br.usp.appneia.R;
import br.usp.utils.DeviceUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;

public class SensorRecorder {
	
	Context context;
	
	private SensorManager sensorManager;
	private String recordFolderPath;
	private LinkedList<Sensor> selectedSensors = new LinkedList<Sensor>();
	private int sensorSampleRate;
	
	String filenameRecordSensors;
	
	public SensorRecorder(Context context, String recordFolderPath) {
		
		this.context = context;
		this.recordFolderPath = recordFolderPath;
		this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
		SparseBooleanArray sensorTypeList = new SparseBooleanArray();
		for(Sensor sensor: sensorList) {
			
			if( sensorTypeList.get(sensor.getType(), false) == false) {
		
				boolean sensorMonitored = sharedPreferences.getBoolean("pref_sensor_"+sensor.getType(), false);
				
				if( sensorMonitored ) {
		
					selectedSensors.add(sensor);
				}
				sensorTypeList.append(sensor.getType(), true);
			}
		}
		
		this.sensorSampleRate = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_SENSOR_SAMPLE_RATE, "3"));
		
		this.createRecorderFile();
	}
	
	public void onResume() {
			
		for (Sensor sensor : selectedSensors) {
			
			sensorManager.registerListener((SensorEventListener) context,
					sensor,
					sensorSampleRate);
		}
	}
	
	public void onStop() {
		
		for (Sensor sensor : selectedSensors) {
				
			sensorManager.unregisterListener((SensorEventListener) context,
					sensor);
		}
	}
	
	@SuppressLint("NewApi")
	private void createRecorderFile() {
		if (recordFolderPath != null) {
			filenameRecordSensors = context.getResources().getString(R.string.file_record_sensors);
			DeviceUtils.writeDataOnFile(recordFolderPath, filenameRecordSensors, DeviceUtils.getBuildInfo());
			
			StringBuilder data = new StringBuilder();
			data.append("\n");
			data.append("Type, Name, MaximumRange, Power, Resolution, Vendor, Version");
			if( DeviceUtils.getBuild() >= 9 ) {
				
				data.append(", MinDelay");
			}
			if( DeviceUtils.getBuild() >=21 ) {
				
				data.append(", MaxDelay");
			}
			data.append("\n");
			DeviceUtils.writeDataOnFile(recordFolderPath, filenameRecordSensors, data.toString());
			
			for(Sensor sensor : selectedSensors) {
				data = new StringBuilder();
				data.append(sensor.getType());
				data.append(", ");
				data.append(sensor.getName());
				data.append(", ");
				data.append(sensor.getMaximumRange());
				data.append(", ");
				data.append(sensor.getPower());
				data.append(", ");
				data.append(sensor.getResolution());
				data.append(", ");
				data.append(sensor.getVendor());
				data.append(", ");
				data.append(sensor.getVersion());
				if( DeviceUtils.getBuild() >= 9 ) {

					data.append(", ");
					data.append(sensor.getMinDelay());
				}
				if( DeviceUtils.getBuild() >=21 ) {
					
					data.append(", ");
					data.append(sensor.getMaxDelay());
				}
				data.append("\n");
				DeviceUtils.writeDataOnFile(recordFolderPath, filenameRecordSensors, data.toString());			
			}
			
			DeviceUtils.writeDataOnFile(recordFolderPath, filenameRecordSensors, "\n");
		}
	}
	
	public void saveEvent(SensorEvent event) {
		
		//TODO: create a ring buffer and write only after some seconds
		StringBuilder data = new StringBuilder();
		data.append(event.timestamp);
		data.append(' ');
		data.append(event.sensor.getType());
		data.append(' ');
		for (float eventData: event.values) {
			
			data.append(eventData);
			data.append(' ');
		}
		data.append("\n");
		DeviceUtils.writeDataOnFile(recordFolderPath, filenameRecordSensors, data.toString());
	}
}