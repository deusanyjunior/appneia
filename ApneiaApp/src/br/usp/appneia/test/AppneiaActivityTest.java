/**
 * Test AppneiaActivity
 */
package br.usp.appneia.test;

import br.usp.appneia.AppneiaActivity;
//import br.usp.appneia.PastRecordsActivity;
import br.usp.appneia.R;
import br.usp.appneia.newrecord.BeforeSleepFormActivity;
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
//	private Button mButtonStoredRecords;
	
	public AppneiaActivityTest() {
		
		super(AppneiaActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
		setActivityInitialTouchMode(true);
		
		mAppneiaActivity = getActivity();
		mButtonNewRecord = (Button) mAppneiaActivity.findViewById(R.id.buttonNewRecord);
//		mButtonStoredRecords = (Button) mAppneiaActivity.findViewById(R.id.buttonStoredRecords);
	}

	/**
	 * Verify test fixture
	 */
	public void testAAPreconditions() {
		
		assertNotNull("mAppneiaActivity is null", mAppneiaActivity);
		assertNotNull("mButtonNewRecord is null", mButtonNewRecord);
//		assertNotNull("mButtonStoredRecords is null", mButtonStoredRecords);
	}
	
	/**
	 * Test button NewRecord
	 */
	public void testAppneiaActivityButtonNewRecord() {
		
		final String expectedLabel = mAppneiaActivity.getString(R.string.new_record);
		final String actualLabel = mButtonNewRecord.getText().toString();
		assertEquals(expectedLabel, actualLabel);
		
		ActivityMonitor mBeforeSleepFormActivityMonitor = getInstrumentation()
				.addMonitor(BeforeSleepFormActivity.class.getName(), null, false);
		
		TouchUtils.clickView(this, mButtonNewRecord);
		BeforeSleepFormActivity mBeforeSleepFormActivity = (BeforeSleepFormActivity)
				mBeforeSleepFormActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
		
		assertNotNull("BeforeSleepFormActivity is null", mBeforeSleepFormActivity);
		assertEquals("Monitor for BeforeSleepFormActivity has not been called", 
				1, mBeforeSleepFormActivityMonitor.getHits());
		assertEquals("BeforeSleepFormActivity is of wrong type", 
				BeforeSleepFormActivity.class, mBeforeSleepFormActivity.getClass());
				
		getInstrumentation().removeMonitor(mBeforeSleepFormActivityMonitor);
		
		// TODO: verify a better solution
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	
	/**
	 * Test button PastRecords
	 */
//	public void testAppneiaActivityButtonPastRecords() {
//		
//		final String expectedLabel = mAppneiaActivity.getString(R.string.stored_records);
//		final String actualLabel = mButtonStoredRecords.getText().toString();
//		assertEquals(expectedLabel, actualLabel);
//		
//		ActivityMonitor mPastRecordsActivityMonitor = getInstrumentation()
//				.addMonitor(PastRecordsActivity.class.getName(), null, false);
//		
//		TouchUtils.clickView(this, mButtonStoredRecords);
//		PastRecordsActivity mPastRecordsActivity = (PastRecordsActivity)
//				mPastRecordsActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
//		
//		assertNotNull("PastRecordsActivity is null", mPastRecordsActivity);
//		assertEquals("Monitor for PastRecordsActivity has not been called",
//				1, mPastRecordsActivityMonitor.getHits());
//		assertEquals("PastRecordsActivity is of wrong type",
//				PastRecordsActivity.class, mPastRecordsActivity.getClass());
//		
//		getInstrumentation().removeMonitor(mPastRecordsActivityMonitor);
//		
//		// TODO: verify a better solution
//		this.sendKeys(KeyEvent.KEYCODE_BACK);
//	}
	
}
