/**
 * Settings Activity
 */
package br.usp.appneia.settings;

import br.usp.appneia.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author dj
 *
 */
public class SettingsActivity extends Activity {
	
	Context context;
	boolean notCreated = true;
	SpinnerManager spinnerManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		context = this;
		if (notCreated) {
			
			setRecordsFolderInfo();
			spinnerManager = new SpinnerManager(context);
			spinnerManager.createSpinners(findViewById(R.id.spinnerSettingsAudioSource), 
											findViewById(R.id.spinnerSettingsAudioSampleRate),
											findViewById(R.id.spinnerSettingsAudioChannels),
											findViewById(R.id.spinnerSettingsPCMEncoding));
		}
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
	
	
	
	
	
	
	
}
