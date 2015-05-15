/**
 * Settings Activity
 */
package br.usp.appneia.settings;

import br.usp.appneia.AppneiaActivity;
import br.usp.appneia.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author dj
 *
 */
public class SettingsActivity extends Activity {
	
	Context context;
	boolean notCreated = true;
	SpinnerManager spinnerManager;
	
	int sensors = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		context = this;
		if (notCreated) {
			
			setRecordsFolderInfo();
			spinnerManager = new SpinnerManager(context);
			spinnerManager.createSpinners(
					findViewById(R.id.spinnerSettingsMP3kbps),
					findViewById(R.id.spinnerSettingsAudioSource), 
					findViewById(R.id.spinnerSettingsAudioSampleRate),
					findViewById(R.id.spinnerSettingsAudioChannels),
					findViewById(R.id.spinnerSettingsPCMEncoding),
					findViewById(R.id.spinnerSettingsSensorsDelay));
		}
		
		setupButtons();
		setupCheckboxes();
	}
	
	@Override
	public void onBackPressed() {
		
		int[] sS = spinnerManager.getSpinnerSettings();
		
		if (!DeviceUtils.updatePreferences(context, 0, sS[0], sS[1], sS[2], sS[3], sS[4], sS[5], sensors )) {
			
			Toast.makeText(context, getResources().getString(R.string.error_saving), Toast.LENGTH_LONG).show();
		}
		
		Intent intentSave = new Intent(context, AppneiaActivity.class);
		startActivity(intentSave);
		finish();
	}
	
	/**
	 * Show the folder where the records will be saved, and verify the free space.
	 */
	private void setRecordsFolderInfo() {
		
		TextView editTextSettingsRecordsFolder = (TextView)
				findViewById(R.id.TextViewSettingsRecordsFolderName);
		TextView textViewSettingsRecordsFolderFreeSpace = (TextView)
				findViewById(R.id.textViewSettingsRecordsFolderFreeSpace);
		
		if (editTextSettingsRecordsFolder != null) {
			
			StringBuilder storagePath = new StringBuilder(); 
			storagePath.append(DeviceUtils.getStoragePath());
			storagePath.append("/");
			storagePath.append(getResources().getString(R.string.app_name));
			editTextSettingsRecordsFolder.setText(storagePath);
		}
				
		if (textViewSettingsRecordsFolderFreeSpace != null) {
			
			StringBuilder freeSpace = new StringBuilder();
			freeSpace.append(DeviceUtils.getStorageFreeSpace());
			freeSpace.append(getResources().getString(R.string.settings_records_folder_free_space));
			textViewSettingsRecordsFolderFreeSpace.setText(freeSpace);
		}
	}
	
	
	/**
	 * Configure actions for each button
	 */
	private void setupButtons() {
		
		Button buttonSave = (Button) findViewById(R.id.buttonSettingsBack);
		buttonSave.setOnClickListener(onClickListenerButtonSettingsBack());
	}
	
	
	/**
	 * Define the action of button Back
	 * @return the listener action that goes to main screen
	 */
	private View.OnClickListener onClickListenerButtonSettingsBack() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				onBackPressed();
			}
		};
	};
	
	private void setupCheckboxes() {
		
		CheckBox checkBoxAllSensors = (CheckBox) findViewById(R.id.checkBoxSettingsSensorAll);
		checkBoxAllSensors.setOnCheckedChangeListener(onCheckedChangeListenerCheckBoxAllSensors());
	}
	
	private CompoundButton.OnCheckedChangeListener onCheckedChangeListenerCheckBoxAllSensors() {
		
		return new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if (isChecked) {
					sensors = -1;
				} else {
					sensors = 0;
				}
			}
		};
	}
	
	
}
