/**
 * Test New Record Before Sleep Form Activity
 */
package br.usp.appneia.test;

import br.usp.appneia.R;
import br.usp.appneia.newrecord.BeforeSleepFormActivity;
import br.usp.appneia.newrecord.MonitorActivity;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * @author dj
 *
 */
public class BeforeSleepFormActivityTest extends ActivityInstrumentationTestCase2<BeforeSleepFormActivity> {

	private final int TIMEOUT_IN_MS = 1000;
	
	private BeforeSleepFormActivity mBeforeSleepFormActivity;
	private Button mButtonStartMonitoring;
	private Button mButtonBack;
	
	private RadioGroup mRadioGroupBeforeSleepFormFirstQuestion;
	private RadioButton mRadioBeforeSleepFormFirstQuestionYes;
	private RadioButton mRadioBeforeSleepFormFirstQuestionNo;
	
	private RadioGroup mRadioGroupBeforeSleepFormSecondQuestion;
	private RadioButton mRadioBeforeSleepFormSecondQuestionYes;
	private RadioButton mRadioBeforeSleepFormSecondQuestionNo;
	
	private RadioGroup mRadioGroupBeforeSleepFormThirdQuestion;
	private RadioButton mRadioBeforeSleepFormThirdQuestionYes;
	private RadioButton mRadioBeforeSleepFormThirdQuestionNo;
	
	public BeforeSleepFormActivityTest() {
		
		super(BeforeSleepFormActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
		setActivityInitialTouchMode(true);
		
		mBeforeSleepFormActivity = getActivity();
		mButtonStartMonitoring = (Button) mBeforeSleepFormActivity
				.findViewById(R.id.buttonBeforeSleepFormStartMonitoring);
		mButtonBack = (Button) mBeforeSleepFormActivity
				.findViewById(R.id.buttonBeforeSleepFormBack);
		
		mRadioGroupBeforeSleepFormFirstQuestion = 
				(RadioGroup) mBeforeSleepFormActivity
				.findViewById(R.id.radioGroupBeforeSleepFormFirstQuestion);
		mRadioBeforeSleepFormFirstQuestionYes = 
				(RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormFirstQuestionYes);
		mRadioBeforeSleepFormFirstQuestionNo = 
				(RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormFirstQuestionNo);
		
		mRadioGroupBeforeSleepFormSecondQuestion = 
				(RadioGroup) mBeforeSleepFormActivity
				.findViewById(R.id.radioGroupBeforeSleepFormSecondQuestion);
		mRadioBeforeSleepFormSecondQuestionYes = 
				(RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormSecondQuestionYes);
		mRadioBeforeSleepFormSecondQuestionNo = 
				(RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormSecondQuestionNo);
		
		mRadioGroupBeforeSleepFormThirdQuestion = 
				(RadioGroup) mBeforeSleepFormActivity
				.findViewById(R.id.radioGroupBeforeSleepFormThirdQuestion);
		mRadioBeforeSleepFormThirdQuestionYes = 
				(RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormThirdQuestionYes);
		mRadioBeforeSleepFormThirdQuestionNo = 
				(RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormThirdQuestionNo);
	}

	/**
	 * Verify test fixture
	 */
	public void testAAPreconditions() {
		
		assertNotNull("mBeforeSleepFormActivity is null", mBeforeSleepFormActivity);
		assertNotNull("mButtonStartMonitoring is null", mButtonStartMonitoring);
		assertNotNull("mButtonBack is null", mButtonBack);
		
		assertNotNull("mRadioGroupBeforeSleepFormFirstQuestion is null", mRadioGroupBeforeSleepFormFirstQuestion);
		assertNotNull("mRadioBeforeSleepFormFirstQuestionYes is null", mRadioBeforeSleepFormFirstQuestionYes);
		assertNotNull("mRadioBeforeSleepFormFirstQuestionNo is null", mRadioBeforeSleepFormFirstQuestionNo);
		
		assertNotNull("mRadioGroupBeforeSleepFormSecondQuestion is null", mRadioGroupBeforeSleepFormSecondQuestion);
		assertNotNull("mRadioBeforeSleepFormSecondQuestionYes is null", mRadioBeforeSleepFormSecondQuestionYes);
		assertNotNull("mRadioBeforeSleepFormSecondQuestionNo is null", mRadioBeforeSleepFormSecondQuestionNo);
		
		assertNotNull("mRadioGroupBeforeSleepFormThirdQuestion is null", mRadioGroupBeforeSleepFormThirdQuestion);
		assertNotNull("mRadioBeforeSleepFormThirdQuestionYes is null", mRadioBeforeSleepFormThirdQuestionYes);
		assertNotNull("mRadioBeforeSleepFormThirdQuestionNo is null", mRadioBeforeSleepFormThirdQuestionNo);
	}
	
	/**
	 * Test button StartMonitoring
	 */
	public void testBeforeSleepFormActivity_ButtonStartMonitoring() {
		
		final String expectedLabel = mBeforeSleepFormActivity.getString(R.string.before_sleep_start_monitoring);
		final String actualLabel = mButtonStartMonitoring.getText().toString();
		assertEquals(expectedLabel, actualLabel);
		
		ActivityMonitor mMonitorActivityMonitor = getInstrumentation()
				.addMonitor(MonitorActivity.class.getName(), null, false);
		
		TouchUtils.clickView(this, mButtonStartMonitoring);
		MonitorActivity mMonitorActivity = (MonitorActivity)
				mMonitorActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
		
		assertNotNull("MonitorActivity is null", mMonitorActivity);
		assertEquals("Monitor for MonitorActivity has not been called", 
				1, mMonitorActivityMonitor.getHits());
		assertEquals("MonitorActivity is of wrong type", 
				MonitorActivity.class, mMonitorActivity.getClass());
				
		getInstrumentation().removeMonitor(mMonitorActivityMonitor);
		
		// TODO: verify a better solution
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
	
	public void testBeforeSleepFormActivity_ButtonBack() {
		
		final String expectedLabel = mBeforeSleepFormActivity.getString(R.string.back);
		final String actualLabel = mButtonBack.getText().toString();
		assertEquals(expectedLabel, actualLabel);
		
	}

}
