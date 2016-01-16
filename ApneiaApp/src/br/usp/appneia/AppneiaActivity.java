/**
 * Appneia Activity
 */
package br.usp.appneia;

import br.usp.appneia.R;
import br.usp.appneia.newrecord.BeforeSleepFormActivity;
import br.usp.appneia.preferences.PreferencesActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * @author dj
 *
 */
public class AppneiaActivity extends AppCompatActivity {

	Context context = this;
	
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appneia);
		
//		DeviceUtils.loadPreferences(context);
		setupButtons();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.menu_item_preferences:
			
			Intent intentPreferences = new Intent(AppneiaActivity.this, PreferencesActivity.class);
			startActivity(intentPreferences);
			break;

		default:
			break;
		}
		return true;
	}
		
	/**
	 * Configure the actions for each button
	 */
	private void setupButtons() {
		
		Button buttonNewRecord = (Button) findViewById(R.id.buttonNewRecord);
		buttonNewRecord.setOnClickListener(onClickListenerButtonNewRecord());
		
//		Button buttonPastRecords = (Button) findViewById(R.id.buttonStoredRecords);
//		buttonPastRecords.setOnClickListener(onClickListenerButtonPastRecords());
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
//	private View.OnClickListener onClickListenerButtonPastRecords() {
//		
//		return new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				Intent intentPastRecords = new Intent(context, PastRecordsActivity.class);
//				startActivity(intentPastRecords);
////				finish(); // in this case it isn't necessary
//			}
//		};
//	}
	
}
