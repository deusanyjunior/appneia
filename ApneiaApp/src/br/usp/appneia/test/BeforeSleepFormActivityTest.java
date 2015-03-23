/**
 * Test New Record Before Sleep Form Activity
 */
package br.usp.appneia.test;

import br.usp.appneia.R;
import br.usp.appneia.newrecord.BeforeSleepFormActivity;
import br.usp.appneia.newrecord.MonitoringActivity;
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
public class BeforeSleepFormActivityTest extends
		ActivityInstrumentationTestCase2<BeforeSleepFormActivity> {

	private final int TIMEOUT_IN_MS = 1000;

	private BeforeSleepFormActivity mBeforeSleepFormActivity;
	private Button mButtonStartMonitoring;

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
		
		mRadioGroupBeforeSleepFormFirstQuestion = (RadioGroup) mBeforeSleepFormActivity
				.findViewById(R.id.radioGroupBeforeSleepFormFirstQuestion);
		mRadioBeforeSleepFormFirstQuestionYes = (RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormFirstQuestionYes);
		mRadioBeforeSleepFormFirstQuestionNo = (RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormFirstQuestionNo);

		mRadioGroupBeforeSleepFormSecondQuestion = (RadioGroup) mBeforeSleepFormActivity
				.findViewById(R.id.radioGroupBeforeSleepFormSecondQuestion);
		mRadioBeforeSleepFormSecondQuestionYes = (RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormSecondQuestionYes);
		mRadioBeforeSleepFormSecondQuestionNo = (RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormSecondQuestionNo);

		mRadioGroupBeforeSleepFormThirdQuestion = (RadioGroup) mBeforeSleepFormActivity
				.findViewById(R.id.radioGroupBeforeSleepFormThirdQuestion);
		mRadioBeforeSleepFormThirdQuestionYes = (RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormThirdQuestionYes);
		mRadioBeforeSleepFormThirdQuestionNo = (RadioButton) mBeforeSleepFormActivity
				.findViewById(R.id.radioBeforeSleepFormThirdQuestionNo);
	}

	/**
	 * Verify test fixture
	 */
	public void testAAPreconditions() {

		assertNotNull("mBeforeSleepFormActivity is null",
				mBeforeSleepFormActivity);
		assertNotNull("mButtonStartMonitoring is null", mButtonStartMonitoring);

		assertNotNull("mRadioGroupBeforeSleepFormFirstQuestion is null",
				mRadioGroupBeforeSleepFormFirstQuestion);
		assertNotNull("mRadioBeforeSleepFormFirstQuestionYes is null",
				mRadioBeforeSleepFormFirstQuestionYes);
		assertNotNull("mRadioBeforeSleepFormFirstQuestionNo is null",
				mRadioBeforeSleepFormFirstQuestionNo);

		assertNotNull("mRadioGroupBeforeSleepFormSecondQuestion is null",
				mRadioGroupBeforeSleepFormSecondQuestion);
		assertNotNull("mRadioBeforeSleepFormSecondQuestionYes is null",
				mRadioBeforeSleepFormSecondQuestionYes);
		assertNotNull("mRadioBeforeSleepFormSecondQuestionNo is null",
				mRadioBeforeSleepFormSecondQuestionNo);

		assertNotNull("mRadioGroupBeforeSleepFormThirdQuestion is null",
				mRadioGroupBeforeSleepFormThirdQuestion);
		assertNotNull("mRadioBeforeSleepFormThirdQuestionYes is null",
				mRadioBeforeSleepFormThirdQuestionYes);
		assertNotNull("mRadioBeforeSleepFormThirdQuestionNo is null",
				mRadioBeforeSleepFormThirdQuestionNo);
	}

	/**
	 * Test if the form starts with all radio not pre checked
	 */
	public void testBeforeSleepFormActivity_RadioNotPreChecked() {

		assertFalse(
				"mRadioBeforeSleepFormFirstQuestionYes should not be checked",
				mRadioBeforeSleepFormFirstQuestionYes.isChecked());
		assertFalse(
				"mRadioBeforeSleepFormFirstQuestionNo should not be checked",
				mRadioBeforeSleepFormFirstQuestionNo.isChecked());
		assertEquals(
				"mRadioGroupBeforeSleepFormFirstQuestion should not have checked radio",
				-1, mRadioGroupBeforeSleepFormFirstQuestion
						.getCheckedRadioButtonId());

		assertFalse(
				"mRadioBeforeSleepFormSecondQuestionYes should not be checked",
				mRadioBeforeSleepFormSecondQuestionYes.isChecked());
		assertFalse(
				"mRadioBeforeSleepFormSecondQuestionNo should not be checked",
				mRadioBeforeSleepFormSecondQuestionNo.isChecked());
		assertEquals(
				"mRadioGroupBeforeSleepFormSecondQuestion should not have checked radio",
				-1, mRadioGroupBeforeSleepFormSecondQuestion
						.getCheckedRadioButtonId());

		assertFalse(
				"mRadioBeforeSleepFormThirdQuestionYes should not be checked",
				mRadioBeforeSleepFormThirdQuestionYes.isChecked());
		assertFalse(
				"mRadioBeforeSleepFormThirdQuestionNo should not be checked",
				mRadioBeforeSleepFormThirdQuestionNo.isChecked());
		assertEquals(
				"mRadioGroupBeforeSleepFormThirdQuestion should not have checked radio",
				-1, mRadioGroupBeforeSleepFormThirdQuestion
						.getCheckedRadioButtonId());
	}

	/**
	 * Test the radio group from the first question
	 */
	public void testBeforeSleepFormActivity_RadioChangeCheckedFirstQuestion() {

		TouchUtils.clickView(this, mRadioBeforeSleepFormFirstQuestionYes);
		assertTrue("mRadioBeforeSleepFormFirstQuestionYes should be checked",
				mRadioBeforeSleepFormFirstQuestionYes.isChecked());
		assertFalse(
				"mRadioBeforeSleepFormFirstQuestionNo should not be checked",
				mRadioBeforeSleepFormFirstQuestionNo.isChecked());
		assertEquals(
				"The first radio button from BeforeSleepFormFirstQuestion should be checked",
				R.id.radioBeforeSleepFormFirstQuestionYes,
				mRadioGroupBeforeSleepFormFirstQuestion
						.getCheckedRadioButtonId());

		TouchUtils.clickView(this, mRadioBeforeSleepFormFirstQuestionNo);
		assertTrue("mRadioBeforeSleepFormFirstQuestionNo should be checked",
				mRadioBeforeSleepFormFirstQuestionNo.isChecked());
		assertFalse(
				"mRadioBeforeSleepFormFirstQuestionYes should not be checked",
				mRadioBeforeSleepFormFirstQuestionYes.isChecked());
		assertEquals(
				"The second radio button from BeforeSleepFormFirstQuestion should be checked",
				R.id.radioBeforeSleepFormFirstQuestionNo,
				mRadioGroupBeforeSleepFormFirstQuestion
						.getCheckedRadioButtonId());
	}

	/**
	 * Test the radio group from the second question
	 */
	public void testBeforeSleepFormActivity_RadioChangeCheckedSecondQuestion() {

		TouchUtils.clickView(this, mRadioBeforeSleepFormSecondQuestionYes);
		assertTrue("mRadioBeforeSleepFormSecondQuestionYes should be checked",
				mRadioBeforeSleepFormSecondQuestionYes.isChecked());
		assertFalse(
				"mRadioBeforeSleepFormSecondQuestionNo should not be checked",
				mRadioBeforeSleepFormSecondQuestionNo.isChecked());
		assertEquals(
				"The Second radio button from BeforeSleepFormSecondQuestion should be checked",
				R.id.radioBeforeSleepFormSecondQuestionYes,
				mRadioGroupBeforeSleepFormSecondQuestion
						.getCheckedRadioButtonId());

		TouchUtils.clickView(this, mRadioBeforeSleepFormSecondQuestionNo);
		assertTrue("mRadioBeforeSleepFormSecondQuestionNo should be checked",
				mRadioBeforeSleepFormSecondQuestionNo.isChecked());
		assertFalse(
				"mRadioBeforeSleepFormSecondQuestionYes should not be checked",
				mRadioBeforeSleepFormSecondQuestionYes.isChecked());
		assertEquals(
				"The second radio button from BeforeSleepFormSecondQuestion should be checked",
				R.id.radioBeforeSleepFormSecondQuestionNo,
				mRadioGroupBeforeSleepFormSecondQuestion
						.getCheckedRadioButtonId());
	}

	/**
	 * Test the radio group from the third question
	 */
	public void testBeforeSleepFormActivity_RadioChangeCheckedThirdQuestion() {

		TouchUtils.clickView(this, mRadioBeforeSleepFormThirdQuestionYes);
		assertTrue("mRadioBeforeSleepFormThirdQuestionYes should be checked",
				mRadioBeforeSleepFormThirdQuestionYes.isChecked());
		assertFalse(
				"mRadioBeforeSleepFormThirdQuestionNo should not be checked",
				mRadioBeforeSleepFormThirdQuestionNo.isChecked());
		assertEquals(
				"The Third radio button from BeforeSleepFormThirdQuestion should be checked",
				R.id.radioBeforeSleepFormThirdQuestionYes,
				mRadioGroupBeforeSleepFormThirdQuestion
						.getCheckedRadioButtonId());

		TouchUtils.clickView(this, mRadioBeforeSleepFormThirdQuestionNo);
		assertTrue("mRadioBeforeSleepFormThirdQuestionNo should be checked",
				mRadioBeforeSleepFormThirdQuestionNo.isChecked());
		assertFalse(
				"mRadioBeforeSleepFormThirdQuestionYes should not be checked",
				mRadioBeforeSleepFormThirdQuestionYes.isChecked());
		assertEquals(
				"The second radio button from BeforeSleepFormThirdQuestion should be checked",
				R.id.radioBeforeSleepFormThirdQuestionNo,
				mRadioGroupBeforeSleepFormThirdQuestion
						.getCheckedRadioButtonId());
	}

	/**
	 * Test button Start Monitoring
	 */
	public void testBeforeSleepFormActivity_ButtonStartMonitoring() {

		final String expectedLabel = mBeforeSleepFormActivity
				.getString(R.string.before_sleep_start_monitoring);
		final String actualLabel = mButtonStartMonitoring.getText().toString();
		assertEquals(expectedLabel, actualLabel);

		ActivityMonitor mMonitorActivityMonitor = getInstrumentation()
				.addMonitor(MonitoringActivity.class.getName(), null, false);

		TouchUtils.clickView(this, mButtonStartMonitoring);
		MonitoringActivity mMonitorActivity = (MonitoringActivity) mMonitorActivityMonitor
				.waitForActivityWithTimeout(TIMEOUT_IN_MS);

		assertNotNull("MonitoringActivity is null", mMonitorActivity);
		assertEquals("Monitor for MonitoringActivity has not been called", 1,
				mMonitorActivityMonitor.getHits());
		assertEquals("MonitoringActivity is of wrong type",
				MonitoringActivity.class, mMonitorActivity.getClass());

		getInstrumentation().removeMonitor(mMonitorActivityMonitor);

		// TODO: verify a better solution
		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
}
