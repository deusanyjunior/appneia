/**
 * New Record After Sleep Activity
 */
package br.usp.appneia.newrecord;

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
 * 
 * @author dj
 *
 */
public class AfterSleepFormActivity extends Activity {

	Activity activity = this;
	Context context = this;
	
	String recordFolderPath;
	
	int firstAnswer = -1;
	int secondAnswer = -1;
	int thirdAnswer = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_record_after_sleep_form);
		
		recordFolderPath = getIntent().getExtras().getString("recordFolderPath");
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
	 * Configure actions for each button
	 */
	private void setupButtons() {
		
		Button buttonSave = (Button) findViewById(R.id.buttonAfterSleepFormSave);
		buttonSave.setOnClickListener(onClickListenerButtonSave());
	}
	
	/**
	 * Define the action of button Save
	 * @return the listener action that saves everything and goes to main screen
	 */
	private View.OnClickListener onClickListenerButtonSave() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (verifyCheckBoxes()) {
					
					if (saveAnswers()) {
						
						Intent intentSave = new Intent(context, AppneiaActivity.class);
						startActivity(intentSave);
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
				
		RadioGroup firstQuestion = (RadioGroup) activity.findViewById(R.id.radioGroupAfterSleepFormFirstQuestion);
		firstAnswer = firstQuestion.getCheckedRadioButtonId();
		
		RadioGroup secondQuestion = (RadioGroup) activity.findViewById(R.id.radioGroupAfterSleepFormSecondQuestion);
		secondAnswer = secondQuestion.getCheckedRadioButtonId();
		
		RadioGroup thirdQuestion = (RadioGroup) activity.findViewById(R.id.radioGroupAfterSleepFormThirdQuestion);
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
			 
			String fileNameRecordDetails = getResources().getString(R.string.file_record_details);
			String answer1 = ((RadioButton) activity.findViewById(firstAnswer)).getText().toString();
			String answer2 = ((RadioButton) activity.findViewById(secondAnswer)).getText().toString();
			String answer3 = ((RadioButton) activity.findViewById(thirdAnswer)).getText().toString();
			
			StringBuilder answers = new StringBuilder();
			answers.append("\n");
			answers.append(this.getResources().getString(R.string.after_sleep_form));
			answers.append("\n");
			answers.append(answer1);
			answers.append(" ");
			answers.append(answer2);
			answers.append(" ");
			answers.append(answer3);
			answers.append("\n");
			return DeviceUtils.writeDataOnFile(recordFolderPath, fileNameRecordDetails, answers.toString());
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
}
