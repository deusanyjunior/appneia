/**
 * Test New Record Monitoring Activity
 */
package br.usp.appneia.test;

import br.usp.appneia.R;
import br.usp.appneia.newrecord.AfterSleepFormActivity;
import br.usp.appneia.newrecord.MonitoringActivity;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;

/**
 * @author dj
 *
 */
public class MonitoringActivityTest extends
		ActivityInstrumentationTestCase2<MonitoringActivity> {

	private final int TIMEOUT_IN_MS = 1000;
	
	private MonitoringActivity mMonitoringActivity;
	private Button mButtonMonitoringFinishMonitoring;
	
	public MonitoringActivityTest() {
		
		super(MonitoringActivity.class);
	}

	protected void setUp() throws Exception {
		
		super.setUp();
		setActivityInitialTouchMode(true);
		
		mMonitoringActivity = getActivity();
		mButtonMonitoringFinishMonitoring = (Button) mMonitoringActivity
				.findViewById(R.id.buttonMonitoringFinishMonitoring);
	}
	
	/**
	 * Verify test fixture
	 */
	public void testAAPreconditions() {
		
		assertNotNull("mMonitoringActivity is null", mMonitoringActivity);
		assertNotNull("mButtonMonitoringFinishMonitoring is null", mButtonMonitoringFinishMonitoring);
	}

	/**
	 * Test button Finish Monitoring
	 */
	public void testMonitoringActivity_ButtonMonitoringFinishMonitoring() {
		
		final String expectedLabel = mMonitoringActivity.getString(R.string.monitoring_finish_monitoring);
		final String actualLabel = mButtonMonitoringFinishMonitoring.getText().toString();
		assertEquals(expectedLabel, actualLabel);
		
		ActivityMonitor mAfterSleepFormActivityMonitor = getInstrumentation()
				.addMonitor(AfterSleepFormActivity.class.getName(), null, false);
		
		TouchUtils.clickView(this, mButtonMonitoringFinishMonitoring);
		AfterSleepFormActivity mAfterSleepFormActivity = (AfterSleepFormActivity)
				mAfterSleepFormActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
		
		assertNotNull("AfterSleepFormActivity is null", mAfterSleepFormActivity);
		assertEquals("Monitor for AfterSleepFormActivity has not been called", 
				1, mAfterSleepFormActivityMonitor.getHits());
		assertEquals("AfterSleepFormActivity is of wrong type", 
				AfterSleepFormActivity.class, mAfterSleepFormActivity.getClass());
				
		getInstrumentation().removeMonitor(mAfterSleepFormActivityMonitor);
		
		// TODO: verify a better solution
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
}
