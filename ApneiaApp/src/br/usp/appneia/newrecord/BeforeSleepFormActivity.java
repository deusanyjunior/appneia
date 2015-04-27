/**
 * New Record Before Sleep Form Activity
 */
package br.usp.appneia.newrecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.usp.appneia.R;
import br.usp.appneia.settings.DeviceUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * @author dj
 *
 */
public class BeforeSleepFormActivity extends Activity {

	Activity activity = this;
	Context context = this;
	
	StringBuilder recordFolderPath;
	File recordFolder;
	boolean folderCreated;
	
	int firstAnswer = -1;
	int secondAnswer = -1;
	int thirdAnswer = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_record_before_sleep_form);
		
		folderCreated = setupRecordFolder();
		if (folderCreated) {
			String recordDetails = getResources().getString(R.string.file_record_details);
			writeDataFile(recordDetails, DeviceUtils.getBuildInfo());
		}
		setupButtons();
	}
	
	@Override
	public void onBackPressed() {
		
		//TODO: ask user
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Set the record folder based on the date and time
	 * @return true if the folder has been created successfully.
	 */
	private boolean setupRecordFolder() {
		
		if (!DeviceUtils.verifyStorageStatus()) {
			DeviceUtils.toastAllStorageStatus(context);
			return false;
		};
		
		String storagePath = DeviceUtils.getStoragePath();
		File appFolder = new File(storagePath+"/"+getResources().getString(R.string.app_name));
		if (!appFolder.exists() && !appFolder.mkdirs()) {
			return false;
		} else if (!appFolder.isDirectory()) {
			return false;
		}
		
		String date = new SimpleDateFormat("yyMMdd-HHmmss", Locale.US).format(new Date());
		recordFolderPath = new StringBuilder();
		recordFolderPath.append(appFolder);
		recordFolderPath.append("/");
		recordFolderPath.append(date);
		recordFolder = new File(recordFolderPath.toString());
		if (!recordFolder.exists() && !recordFolder.mkdirs()) {
			return false;
		} else if (!recordFolder.isDirectory()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Write data on file
	 * 
	 * @param filePath is the path to file to write the data
	 * @param data to be written
	 * @return true if the data has been written successfully
	 */
	private boolean writeDataFile(String filePath, String data) {
		
		File file = new File(recordFolder.getAbsolutePath(), filePath);
		if (file.isDirectory()) {
			return false;
		} 
		
		try {
			FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath(), true); // Append the content
			outputStream.write(data.getBytes());
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Configure actions for each button
	 */
	private void setupButtons() {
		
		Button buttonStartMonitoring = (Button) findViewById(R.id.buttonBeforeSleepFormStartMonitoring);
		buttonStartMonitoring.setOnClickListener(onClickListenerButtonStartMonitoring());
	}
	
	/**
	 * Define the action of button Start Monitoring
	 * @return the listener action that starts MonitoringActivity
	 */
	private View.OnClickListener onClickListenerButtonStartMonitoring() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (verifyCheckBoxes() && saveAnswers()) {
					
					Intent intentStartMonitoring = new Intent(context, MonitoringActivity.class);
					startActivity(intentStartMonitoring);
					finish();
				}
			}
		};
	}
	
	/**
	 * Verify if all radio groups have answer
	 * @return true if the user answered all question
	 */
	private boolean verifyCheckBoxes() {
				
		RadioGroup firstQuestion = (RadioGroup) activity.findViewById(R.id.radioGroupBeforeSleepFormFirstQuestion);
		firstAnswer = firstQuestion.getCheckedRadioButtonId();
		
		RadioGroup secondQuestion = (RadioGroup) activity.findViewById(R.id.radioGroupBeforeSleepFormSecondQuestion);
		secondAnswer = secondQuestion.getCheckedRadioButtonId();
		
		RadioGroup thirdQuestion = (RadioGroup) activity.findViewById(R.id.radioGroupBeforeSleepFormThirdQuestion);
		thirdAnswer = thirdQuestion.getCheckedRadioButtonId();
		
		if (firstAnswer == -1 || secondAnswer == 1 || thirdAnswer == -1) {
			return false;
		} 
		
		return true;
	}
	
	/**
	 * Prepare the answers and save on File
	 * @return true if all answers have been saved
	 */
	private boolean saveAnswers() {
		
		//TODO: unsafe conversions!
		try {
			 
			String recordDetails = getResources().getString(R.string.file_record_details);
			String answer1 = ((RadioButton) activity.findViewById(firstAnswer)).getText().toString();
			String answer2 = ((RadioButton) activity.findViewById(secondAnswer)).getText().toString();
			String answer3 = ((RadioButton) activity.findViewById(thirdAnswer)).getText().toString();
			
			StringBuilder answers = new StringBuilder();
			answers.append("\n");
			answers.append(this.getResources().getString(R.string.before_sleep_form));
			answers.append("\n");
			answers.append(answer1);
			answers.append(" ");
			answers.append(answer2);
			answers.append(" ");
			answers.append(answer3);
			answers.append("\n");
			return writeDataFile(recordDetails, answers.toString());
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}
}
