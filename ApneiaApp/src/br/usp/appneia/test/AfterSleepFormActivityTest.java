/**
 * Test New Record After Sleep Form Activity
 */
package br.usp.appneia.test;

import br.usp.appneia.AppneiaActivity;
import br.usp.appneia.R;
import br.usp.appneia.newrecord.AfterSleepFormActivity;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * @author dj
 *
 */
public class AfterSleepFormActivityTest extends
		ActivityInstrumentationTestCase2<AfterSleepFormActivity> {

	private final int TIMEOUT_IN_MS = 1000;

	private AfterSleepFormActivity mAfterSleepFormActivity;
	private Button mButtonSave;

	private RadioGroup mRadioGroupAfterSleepFormFirstQuestion;
	private RadioButton mRadioAfterSleepFormFirstQuestionYes;
	private RadioButton mRadioAfterSleepFormFirstQuestionNo;

	private RadioGroup mRadioGroupAfterSleepFormSecondQuestion;
	private RadioButton mRadioAfterSleepFormSecondQuestionYes;
	private RadioButton mRadioAfterSleepFormSecondQuestionNo;

	private RadioGroup mRadioGroupAfterSleepFormThirdQuestion;
	private RadioButton mRadioAfterSleepFormThirdQuestionYes;
	private RadioButton mRadioAfterSleepFormThirdQuestionNo;

	public AfterSleepFormActivityTest() {
		super(AfterSleepFormActivity.class);
	}

	protected void setUp() throws Exception {

		super.setUp();
		setActivityInitialTouchMode(true);

		mAfterSleepFormActivity = getActivity();
		mButtonSave = (Button) mAfterSleepFormActivity
				.findViewById(R.id.buttonAfterSleepFormSave);

		mRadioGroupAfterSleepFormFirstQuestion = (RadioGroup) mAfterSleepFormActivity
				.findViewById(R.id.radioGroupAfterSleepFormFirstQuestion);
		mRadioAfterSleepFormFirstQuestionYes = (RadioButton) mAfterSleepFormActivity
				.findViewById(R.id.radioAfterSleepFormFirstQuestionYes);
		mRadioAfterSleepFormFirstQuestionNo = (RadioButton) mAfterSleepFormActivity
				.findViewById(R.id.radioAfterSleepFormFirstQuestionNo);

		mRadioGroupAfterSleepFormSecondQuestion = (RadioGroup) mAfterSleepFormActivity
				.findViewById(R.id.radioGroupAfterSleepFormSecondQuestion);
		mRadioAfterSleepFormSecondQuestionYes = (RadioButton) mAfterSleepFormActivity
				.findViewById(R.id.radioAfterSleepFormSecondQuestionYes);
		mRadioAfterSleepFormSecondQuestionNo = (RadioButton) mAfterSleepFormActivity
				.findViewById(R.id.radioAfterSleepFormSecondQuestionNo);

		mRadioGroupAfterSleepFormThirdQuestion = (RadioGroup) mAfterSleepFormActivity
				.findViewById(R.id.radioGroupAfterSleepFormThirdQuestion);
		mRadioAfterSleepFormThirdQuestionYes = (RadioButton) mAfterSleepFormActivity
				.findViewById(R.id.radioAfterSleepFormThirdQuestionYes);
		mRadioAfterSleepFormThirdQuestionNo = (RadioButton) mAfterSleepFormActivity
				.findViewById(R.id.radioAfterSleepFormThirdQuestionNo);
	}

	/**
	 * Verify test fixture
	 */
	public void testAAPreconditions() {

		assertNotNull("mAfterSleepFormActivity is null",
				mAfterSleepFormActivity);
		assertNotNull("mButtonSave is null", mButtonSave);

		assertNotNull("mRadioGroupAfterSleepFormFirstQuestion is null",
				mRadioGroupAfterSleepFormFirstQuestion);
		assertNotNull("mRadioAfterSleepFormFirstQuestionYes is null",
				mRadioAfterSleepFormFirstQuestionYes);
		assertNotNull("mRadioAfterSleepFormFirstQuestionNo is null",
				mRadioAfterSleepFormFirstQuestionNo);

		assertNotNull("mRadioGroupAfterSleepFormSecondQuestion is null",
				mRadioGroupAfterSleepFormSecondQuestion);
		assertNotNull("mRadioAfterSleepFormSecondQuestionYes is null",
				mRadioAfterSleepFormSecondQuestionYes);
		assertNotNull("mRadioAfterSleepFormSecondQuestionNo is null",
				mRadioAfterSleepFormSecondQuestionNo);

		assertNotNull("mRadioGroupAfterSleepFormThirdQuestion is null",
				mRadioGroupAfterSleepFormThirdQuestion);
		assertNotNull("mRadioAfterSleepFormThirdQuestionYes is null",
				mRadioAfterSleepFormThirdQuestionYes);
		assertNotNull("mRadioAfterSleepFormThirdQuestionNo is null",
				mRadioAfterSleepFormThirdQuestionNo);
	}

	/**
	 * Test if the form starts with all radio not pre checked
	 */
	public void testAfterSleepFormActivity_RadioNotPreChecked() {

		assertFalse(
				"mRadioAfterSleepFormFirstQuestionYes should not be checked",
				mRadioAfterSleepFormFirstQuestionYes.isChecked());
		assertFalse(
				"mRadioAfterSleepFormFirstQuestionNo should not be checked",
				mRadioAfterSleepFormFirstQuestionNo.isChecked());
		assertEquals(
				"mRadioGroupAfterSleepFormFirstQuestion should not have checked radio",
				-1, mRadioGroupAfterSleepFormFirstQuestion
						.getCheckedRadioButtonId());

		assertFalse(
				"mRadioAfterSleepFormSecondQuestionYes should not be checked",
				mRadioAfterSleepFormSecondQuestionYes.isChecked());
		assertFalse(
				"mRadioAfterSleepFormSecondQuestionNo should not be checked",
				mRadioAfterSleepFormSecondQuestionNo.isChecked());
		assertEquals(
				"mRadioGroupAfterSleepFormSecondQuestion should not have checked radio",
				-1, mRadioGroupAfterSleepFormSecondQuestion
						.getCheckedRadioButtonId());

		assertFalse(
				"mRadioAfterSleepFormThirdQuestionYes should not be checked",
				mRadioAfterSleepFormThirdQuestionYes.isChecked());
		assertFalse(
				"mRadioAfterSleepFormThirdQuestionNo should not be checked",
				mRadioAfterSleepFormThirdQuestionNo.isChecked());
		assertEquals(
				"mRadioGroupAfterSleepFormThirdQuestion should not have checked radio",
				-1, mRadioGroupAfterSleepFormThirdQuestion
						.getCheckedRadioButtonId());
	}

	/**
	 * Test the radio group from the first question
	 */
	public void testAfterSleepFormActivity_RadioChangeCheckedFirstQuestion() {

		TouchUtils.clickView(this, mRadioAfterSleepFormFirstQuestionYes);
		assertTrue("mRadioAfterSleepFormFirstQuestionYes should be checked",
				mRadioAfterSleepFormFirstQuestionYes.isChecked());
		assertFalse(
				"mRadioAfterSleepFormFirstQuestionNo should not be checked",
				mRadioAfterSleepFormFirstQuestionNo.isChecked());
		assertEquals(
				"The first radio button from AfterSleepFormFirstQuestion should be checked",
				R.id.radioAfterSleepFormFirstQuestionYes,
				mRadioGroupAfterSleepFormFirstQuestion
						.getCheckedRadioButtonId());

		TouchUtils.clickView(this, mRadioAfterSleepFormFirstQuestionNo);
		assertTrue("mRadioAfterSleepFormFirstQuestionNo should be checked",
				mRadioAfterSleepFormFirstQuestionNo.isChecked());
		assertFalse(
				"mRadioAfterSleepFormFirstQuestionYes should not be checked",
				mRadioAfterSleepFormFirstQuestionYes.isChecked());
		assertEquals(
				"The second radio button from AfterSleepFormFirstQuestion should be checked",
				R.id.radioAfterSleepFormFirstQuestionNo,
				mRadioGroupAfterSleepFormFirstQuestion
						.getCheckedRadioButtonId());
	}

	/**
	 * Test the radio group from the second question
	 */
	public void testAfterSleepFormActivity_RadioChangeCheckedSecondQuestion() {

		TouchUtils.clickView(this, mRadioAfterSleepFormSecondQuestionYes);
		assertTrue("mRadioAfterSleepFormSecondQuestionYes should be checked",
				mRadioAfterSleepFormSecondQuestionYes.isChecked());
		assertFalse(
				"mRadioAfterSleepFormSecondQuestionNo should not be checked",
				mRadioAfterSleepFormSecondQuestionNo.isChecked());
		assertEquals(
				"The Second radio button from AfterSleepFormSecondQuestion should be checked",
				R.id.radioAfterSleepFormSecondQuestionYes,
				mRadioGroupAfterSleepFormSecondQuestion
						.getCheckedRadioButtonId());

		TouchUtils.clickView(this, mRadioAfterSleepFormSecondQuestionNo);
		assertTrue("mRadioAfterSleepFormSecondQuestionNo should be checked",
				mRadioAfterSleepFormSecondQuestionNo.isChecked());
		assertFalse(
				"mRadioAfterSleepFormSecondQuestionYes should not be checked",
				mRadioAfterSleepFormSecondQuestionYes.isChecked());
		assertEquals(
				"The second radio button from AfterSleepFormSecondQuestion should be checked",
				R.id.radioAfterSleepFormSecondQuestionNo,
				mRadioGroupAfterSleepFormSecondQuestion
						.getCheckedRadioButtonId());
	}

	/**
	 * Test the radio group from the third question
	 */
	public void testAfterSleepFormActivity_RadioChangeCheckedThirdQuestion() {

		TouchUtils.clickView(this, mRadioAfterSleepFormThirdQuestionYes);
		assertTrue("mRadioAfterSleepFormThirdQuestionYes should be checked",
				mRadioAfterSleepFormThirdQuestionYes.isChecked());
		assertFalse(
				"mRadioAfterSleepFormThirdQuestionNo should not be checked",
				mRadioAfterSleepFormThirdQuestionNo.isChecked());
		assertEquals(
				"The Third radio button from AfterSleepFormThirdQuestion should be checked",
				R.id.radioAfterSleepFormThirdQuestionYes,
				mRadioGroupAfterSleepFormThirdQuestion
						.getCheckedRadioButtonId());

		TouchUtils.clickView(this, mRadioAfterSleepFormThirdQuestionNo);
		assertTrue("mRadioAfterSleepFormThirdQuestionNo should be checked",
				mRadioAfterSleepFormThirdQuestionNo.isChecked());
		assertFalse(
				"mRadioAfterSleepFormThirdQuestionYes should not be checked",
				mRadioAfterSleepFormThirdQuestionYes.isChecked());
		assertEquals(
				"The second radio button from AfterSleepFormThirdQuestion should be checked",
				R.id.radioAfterSleepFormThirdQuestionNo,
				mRadioGroupAfterSleepFormThirdQuestion
						.getCheckedRadioButtonId());
	}

	/**
	 * Test button Save
	 */
	public void testZZAfterSleepFormActivity_ButtonSave() {

		final String expectedLabel = mAfterSleepFormActivity
				.getString(R.string.after_sleep_save);
		final String actualLabel = mButtonSave.getText().toString();
		assertEquals(expectedLabel, actualLabel);

		ActivityMonitor mAppneiaActivityMonitor = getInstrumentation()
				.addMonitor(AppneiaActivity.class.getName(), null, false);

		TouchUtils.clickView(this, mButtonSave);
		AppneiaActivity mAppneiaActivity = (AppneiaActivity) mAppneiaActivityMonitor
				.waitForActivityWithTimeout(TIMEOUT_IN_MS);

		assertNotNull("AppneiaActivity is null", mAppneiaActivity);
		assertEquals("Monitor for AppneiaActivity has not been called", 1,
				mAppneiaActivityMonitor.getHits());
		assertEquals("AppneiaActivity is of wrong type",
				AppneiaActivity.class, mAppneiaActivity.getClass());

		getInstrumentation().removeMonitor(mAppneiaActivityMonitor);

		// TODO: verify a better solution
//		this.sendKeys(KeyEvent.KEYCODE_BACK);
	}
}
