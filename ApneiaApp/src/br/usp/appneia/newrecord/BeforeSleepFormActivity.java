/**
 * New Record Before Sleep Form Activity
 */
package br.usp.appneia.newrecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.usp.appneia.AppneiaActivity;
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
import android.widget.Toast;

/**
 * @author dj
 *
 */
public class BeforeSleepFormActivity extends Activity {

	Activity activity = this;
	Context context = this;
	
	String recordFolderPath = null;
	
	int firstAnswer = -1;
	int secondAnswer = -1;
	int thirdAnswer = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_record_before_sleep_form);
		
		// folder name is based on date and time
		String recordFolder = new SimpleDateFormat("yyMMdd-HHmmss", Locale.US).format(new Date());
		recordFolderPath = DeviceUtils.setupRecordFolder(context, recordFolder);
		setupButtons();
	}
	
	@Override
	public void onBackPressed() {
		
		//TODO: ask user
		try {
			Intent intentSave = new Intent(context, AppneiaActivity.class);
			startActivity(intentSave);
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onBackPressed();
	}
	
	private boolean createRecorderFile() {
		
		if (recordFolderPath != null) {
			
			String fileNameRecordAnswers = getResources().getString(R.string.file_record_answers);
			DeviceUtils.writeDataOnFile(recordFolderPath, fileNameRecordAnswers, DeviceUtils.getBuildInfo());
			return true;
		} else {
			
			return false;
		}
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
				
				if (verifyCheckBoxes()) {
					
					if (saveAnswers()) {
						
						Intent intentStartMonitoring = new Intent(context, MonitoringActivity.class);
						intentStartMonitoring.putExtra("recordFolderPath", recordFolderPath);
						startActivity(intentStartMonitoring);
						finish();
					} else {
						
						Toast.makeText(context, activity.getResources().getString(R.string.error_saving), Toast.LENGTH_LONG).show();
					}
				} else {
					
					Toast.makeText(context, activity.getResources().getString(R.string.error_form), Toast.LENGTH_LONG).show();
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
		
		if (!createRecorderFile()) {
			
			return false;
		}
		
		//TODO: unsafe conversions!
		try {
			 
			String fileNameRecordAnswers = getResources().getString(R.string.file_record_answers);
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
			return DeviceUtils.writeDataOnFile(recordFolderPath, fileNameRecordAnswers, answers.toString());
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}
}
