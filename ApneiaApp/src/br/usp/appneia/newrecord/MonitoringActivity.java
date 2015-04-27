/**
 * New Record Monitor Activity
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
public class MonitoringActivity extends Activity {

	Context context = this;
	String recordFolderPath = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_record_monitoring);
		
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
	public void setupButtons() {
		
		Button button_monitoring_finalization = (Button) findViewById(R.id.buttonMonitoringFinalizeMonitoring);
		button_monitoring_finalization.setOnClickListener(onClickListenerButtonMonitoringFinalization());
	}
	
	/**
	 * Define the action of button Monitoring Finalization
	 * @return the listener action that starts MonitoringActivity
	 */
	private View.OnClickListener onClickListenerButtonMonitoringFinalization() {
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intentMonitoringFinalization = new Intent(context, AfterSleepFormActivity.class);
				intentMonitoringFinalization.putExtra("recordFolderPath", recordFolderPath);
				startActivity(intentMonitoringFinalization);
				finish();
			}
		};
	}
}
