/**
 * Appneia Activity
 */
package br.usp.appneia;

import br.usp.appneia.newrecord.BeforeSleepFormActivity;
import br.usp.appneia.settings.DeviceUtils;
import br.usp.appneia.settings.SettingsActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author dj
 *
 */
public class AppneiaActivity extends Activity {

	Context context = this;
	
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appneia);
		
		DeviceUtils.loadPreferences(context);
		setupButtons();
	}
	
	@Override
	public void onBackPressed() {
		
		//TODO: ask user or
		finish(); // 'him!
		super.onBackPressed();
	}
		
	/**
	 * Configure the actions for each button
	 */
	private void setupButtons() {
		
		Button buttonNewRecord = (Button) findViewById(R.id.buttonNewRecord);
		buttonNewRecord.setOnClickListener(onClickListenerButtonNewRecord());
		
		Button buttonPastRecords = (Button) findViewById(R.id.buttonStoredRecords);
		buttonPastRecords.setOnClickListener(onClickListenerButtonPastRecords());
		
		Button buttonSettings = (Button) findViewById(R.id.buttonSettings);
		buttonSettings.setOnClickListener(onClickListenerButtonSettings());
	}
	
	/**
	 * Define the action of button New Record
	 * @return the listener action that starts BeforeSleepFormActivity
	 */
	private View.OnClickListener onClickListenerButtonNewRecord() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent_NewRecordBeforeSleepForm = 
						new Intent(context, BeforeSleepFormActivity.class);
				startActivity(intent_NewRecordBeforeSleepForm);
				finish();
			}
		};
	}
	
	/**
	 * Define the action of button Past Records
	 * @return the listener action that starts PastRecordsActivity
	 */
	private View.OnClickListener onClickListenerButtonPastRecords() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intentPastRecords = new Intent(context, PastRecordsActivity.class);
				startActivity(intentPastRecords);
//				finish(); // in this case it isn't necessary
			}
		};
	}
	
	/**
	 * Define the action of button Settings
	 * @return the listener action that starts SettingsActivity
	 */
	private View.OnClickListener onClickListenerButtonSettings() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intentSettings = new Intent(context, SettingsActivity.class);
				startActivity(intentSettings);
//				finish(); // in this case it isn't necessary
			}
		};
	}
}
