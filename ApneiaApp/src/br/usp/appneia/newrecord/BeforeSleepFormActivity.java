/**
 * New Record Before Sleep Form Activity
 */
package br.usp.appneia.newrecord;

import br.usp.appneia.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author dj
 *
 */
public class BeforeSleepFormActivity extends Activity {

	Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_record_before_sleep_form);
		
		setupButtons();
	}
	
	@Override
	public void onBackPressed() {
		
		finish();
		super.onBackPressed();
	}
	
	/**
	 * Configure actions for each button
	 */
	public void setupButtons() {
		
		Button buttonStartMonitoring = (Button) findViewById(R.id.buttonBeforeSleepFormStartMonitoring);
		buttonStartMonitoring.setOnClickListener(onClickListenerButtonStartMonitoring());
		
		Button buttonBack = (Button) findViewById(R.id.buttonBeforeSleepFormBack);
		buttonBack.setOnClickListener(onClickListenerButtonBack());
	}
	
	/**
	 * Define the action of button Start Monitoring
	 * @return the listener action that starts MonitorActivity
	 */
	private View.OnClickListener onClickListenerButtonStartMonitoring() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intentStartMonitoring = new Intent(context, MonitorActivity.class);
				startActivity(intentStartMonitoring);
			}
		};
	}
	
	/**
	 * Define the action of button Back
	 * @return the listener action that finishes this activity and goes back
	 */
	private View.OnClickListener onClickListenerButtonBack() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		};
	}
	
}
