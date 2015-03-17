/**
 * Test AppneiaActivity
 */
package br.usp.appneia.test;

import br.usp.appneia.AppneiaActivity;
import br.usp.appneia.NewRecordFirstFormActivity;
import br.usp.appneia.PastRecordsActivity;
import br.usp.appneia.R;
import br.usp.appneia.SettingsActivity;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;

/**
 * @author dj
 *
 */
public class AppneiaActivityTest extends ActivityInstrumentationTestCase2<AppneiaActivity>{

	private final int TIMEOUT_IN_MS = 1000;
	
	private AppneiaActivity mAppneiaActivity;
	private Button mButtonNewRecord;
	private Button mButtonPastRecords;
	private Button mButtonSettings;
	
	public AppneiaActivityTest() {
		super(AppneiaActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
		setActivityInitialTouchMode(true);
		
		mAppneiaActivity = getActivity();
		mButtonNewRecord = (Button) mAppneiaActivity.findViewById(R.id.buttonNewRecord);
		mButtonPastRecords = (Button) mAppneiaActivity.findViewById(R.id.buttonPastRecords);
		mButtonSettings = (Button) mAppneiaActivity.findViewById(R.id.buttonSettings);
	}

	/**
	 * Verify test fixture
	 */
	public void testAAPreconditions() {
		
		assertNotNull("mAppneiaActivity is null", mAppneiaActivity);
		assertNotNull("mButtonNewRecord is null", mButtonNewRecord);
		assertNotNull("mButtonPastRecords is null", mButtonPastRecords);
		assertNotNull("mButtonSettings is null", mButtonSettings);
	}
	
	/**
	 * Test button NewRecord
	 */
	public void testAppneiaActivityButtonNewRecord() {
		
		final String expectedLabel = mAppneiaActivity.getString(R.string.new_record);
		final String actualLabel = mButtonNewRecord.getText().toString();
		assertEquals(expectedLabel, actualLabel);
		
		ActivityMonitor mNewRecordFirstFormActivityMonitor = getInstrumentation()
				.addMonitor(NewRecordFirstFormActivity.class.getName(), null, false);
		
		TouchUtils.clickView(this, mButtonNewRecord);
		NewRecordFirstFormActivity mNewRecordFirstFormActivity = (NewRecordFirstFormActivity)
				mNewRecordFirstFormActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
		
		assertNotNull("NewRecordFirstFormActivity is null", mNewRecordFirstFormActivity);
		assertEquals("Monitor for NewRecordFirstFormActivity has not been called", 
				1, mNewRecordFirstFormActivityMonitor.getHits());
		assertEquals("NewRecordFirstFormActivity is of wrong type", 
				NewRecordFirstFormActivity.class, mNewRecordFirstFormActivity.getClass());
				
		getInstrumentation().removeMonitor(mNewRecordFirstFormActivityMonitor);
		
		// TODO: verify a better solution
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	
	/**
	 * Test button PastRecords
	 */
	public void testAppneiaActivityButtonPastRecords() {
		
		final String expectedLabel = mAppneiaActivity.getString(R.string.past_records);
		final String actualLabel = mButtonPastRecords.getText().toString();
		assertEquals(expectedLabel, actualLabel);
		
		ActivityMonitor mPastRecordsActivityMonitor = getInstrumentation()
				.addMonitor(PastRecordsActivity.class.getName(), null, false);
		
		TouchUtils.clickView(this, mButtonPastRecords);
		PastRecordsActivity mPastRecordsActivity = (PastRecordsActivity)
				mPastRecordsActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
		
		assertNotNull("PastRecordsActivity is null", mPastRecordsActivity);
		assertEquals("Monitor for PastRecordsActivity has not been called",
				1, mPastRecordsActivityMonitor.getHits());
		assertEquals("PastRecordsActivity is of wrong type",
				PastRecordsActivity.class, mPastRecordsActivity.getClass());
		
		getInstrumentation().removeMonitor(mPastRecordsActivityMonitor);
		
		// TODO: verify a better solution
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	
	/**
	 * Test button Settings
	 */
	public void testAppneiaActivityButtonSettings() {
		
		final String expectedLabel = mAppneiaActivity.getString(R.string.settings);
		final String actualLabel = mButtonSettings.getText().toString();
		assertEquals(expectedLabel, actualLabel);
		
		ActivityMonitor mSettingsActivityMonitor = getInstrumentation()
				.addMonitor(SettingsActivity.class.getName(), null, false);
		
		TouchUtils.clickView(this, mButtonSettings);
		SettingsActivity mSettingsActivity = (SettingsActivity)
				mSettingsActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
		
		assertNotNull("SettingsActivity is null", mSettingsActivity);
		assertEquals("Monitor for SettingsActivity has not been called",
				1, mSettingsActivityMonitor.getHits());
		assertEquals("SettingsActivity is of wrong type",
				SettingsActivity.class, mSettingsActivity.getClass());
		
		getInstrumentation().removeMonitor(mSettingsActivityMonitor);
		
		// TODO: verify a better solution
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	
}
