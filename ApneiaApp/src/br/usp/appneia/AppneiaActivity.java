package br.usp.appneia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AppneiaActivity extends Activity {

	Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appneia);
		
		setupButtons();
	}
	
	/**
	 * Configure the actions for each button
	 */
	private void setupButtons() {
		
		Button button_new_record = (Button) findViewById(R.id.buttonNewRecord);
		button_new_record.setOnClickListener(onClickListenerButtonNewRecord());
		
		Button button_past_records = (Button) findViewById(R.id.buttonPastRecords);
		button_past_records.setOnClickListener(onClickListenerButtonPastRecords());
		
		Button button_settings = (Button) findViewById(R.id.buttonSettings);
		button_settings.setOnClickListener(onClickListenerButtonSettings());
	}
	
	/**
	 * Define the action of button New Record
	 * @return the listener action that starts NewRecordFirstActivity
	 */
	private View.OnClickListener onClickListenerButtonNewRecord() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intentNewRecord = new Intent(context, NewRecordFirstFormActivity.class);
				startActivity(intentNewRecord);
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
			}
		};
	}
}
