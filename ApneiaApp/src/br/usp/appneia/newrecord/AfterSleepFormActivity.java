/**
 * New Record After Sleep Activity
 */
package br.usp.appneia.newrecord;

import br.usp.appneia.AppneiaActivity;
import br.usp.appneia.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 
 * @author dj
 *
 */
public class AfterSleepFormActivity extends Activity {

	Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_record_after_sleep_form);
		
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
				
				Intent intentSave = new Intent(context, AppneiaActivity.class);
				startActivity(intentSave);
				finish();
			}
		};
	}
}
