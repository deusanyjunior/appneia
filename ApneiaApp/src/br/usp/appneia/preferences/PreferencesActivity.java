/*
 * Maps from resources and available configurations using id(Integer) and name(String)
 * FUTURETODO: Check updated values for new devices from the API at:
 * - http://developer.android.com/reference/android/media/MediaRecorder.AudioSource.html
 * - http://developer.android.com/reference/android/media/AudioFormat.html
 */

package br.usp.appneia.preferences;

import java.util.LinkedList;
import java.util.List;

import br.usp.appneia.R;
import br.usp.utils.DeviceUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

@SuppressWarnings("deprecation")
public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    
	Context context;
	private AppCompatDelegate mDelegate;
	
	// A list with valid audio settings values retrieved from DeviceUtils
	private LinkedList<int[]> validAudioRecordSettings;
	
	private SparseArray<String> sparseArrayValidAudioSource = new SparseArray<String>();
	private SparseArray<String> sparseArrayValidAudioSampleRate = new SparseArray<String>();
	private SparseArray<String> sparseArrayValidAudioChannels = new SparseArray<String>();
	private SparseArray<String> sparseArrayValidAudioEncoding = new SparseArray<String>();
	
	private SparseArray<String> sparseArrayAudioSources = new SparseArray<String>();
	private SparseArray<String> sparseArrayAudioSampleRates = new SparseArray<String>();
	private SparseArray<String> sparseArrayAudioChannels = new SparseArray<String>();
	private SparseArray<String> sparseArrayAudioEncoding = new SparseArray<String>();

	private SparseArray<String> sparseArraySensors = new SparseArray<String>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        
        context = this;
        addPreferencesFromResource(R.xml.preferences);
        new LoadPreferences().execute();
    }
	
	//	
	// Begin getDelegate special methods
	// from: http://stackoverflow.com/questions/17849193/how-to-add-action-bar-from-support-library-into-preferenceactivity
	//
	
	private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
	
   @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }
   
   public ActionBar getSupportActionBar() {
       return getDelegate().getSupportActionBar();
   }

   public void setSupportActionBar(@Nullable Toolbar toolbar) {
       getDelegate().setSupportActionBar(toolbar);
   }
   
   @Override
   public MenuInflater getMenuInflater() {
       return getDelegate().getMenuInflater();
   }

   @Override
   public void setContentView(@LayoutRes int layoutResID) {
       getDelegate().setContentView(layoutResID);
   }
   @Override
   public void setContentView(View view) {
       getDelegate().setContentView(view);
   }

   @Override
   public void setContentView(View view, ViewGroup.LayoutParams params) {
       getDelegate().setContentView(view, params);
   }

   @Override
   public void addContentView(View view, ViewGroup.LayoutParams params) {
       getDelegate().addContentView(view, params);
   }

   @Override
   protected void onPostResume() {
       super.onPostResume();
       getDelegate().onPostResume();
   }

   @Override
   protected void onTitleChanged(CharSequence title, int color) {
       super.onTitleChanged(title, color);
       getDelegate().setTitle(title);
   }

   @Override
   public void onConfigurationChanged(Configuration newConfig) {
       super.onConfigurationChanged(newConfig);
       getDelegate().onConfigurationChanged(newConfig);
   }
   
   @Override
   protected void onStop() {
       super.onStop();
       getDelegate().onStop();
   }

   @Override
   protected void onDestroy() {
       super.onDestroy();
       getDelegate().onDestroy();
   }

   public void invalidateOptionsMenu() {
       getDelegate().invalidateOptionsMenu();
   }
   //
   // END getDelegate special methods
   //
   
	@Override
    protected void onResume() {
        super.onResume();
                
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Update the Preferences Summary when some preference changes
     */
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        
    	if (key.equals(DeviceUtils.PREF_AUDIO_SOURCE)) {

    		loadAudioSampleRates();
    		loadAudioChannels();
    		loadAudioEncoding();
    	} else if (key.equals(DeviceUtils.PREF_AUDIO_SAMPLE_RATE)) {

    		loadAudioChannels();
    		loadAudioEncoding();
    	} else if (key.equals(DeviceUtils.PREF_AUDIO_CHANNELS)) {

    		loadAudioEncoding();
    	}   
    	
    	updatePrefSummary(findPreference(key));
    }
    
    /**
     * This class loads the preferences in background
     * The progress dialog represents the loading time to finish the process.
     * @author dj
     *
     */
    private class LoadPreferences extends AsyncTask<Void, Integer, Void>{

    	ProgressDialog progressBar;
    	
    	@Override
    	protected void onPreExecute() {
    		
    		progressBar = new ProgressDialog(context);
            progressBar.setCancelable(true);
            progressBar.setMessage(context.getResources().getString(R.string.loading));
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			
			validAudioRecordSettings = DeviceUtils.getValidAudioRecordSettings();
			publishProgress(40);
			
	        loadArrayValues();
	        publishProgress(50);
	        
	        loadAudioSources();
	        publishProgress(60);
	        
	        loadAudioSampleRates();
	        publishProgress(70);
	        
	        loadAudioChannels();
	        publishProgress(80);
	        
	        loadAudioEncoding();
	        publishProgress(90);
	        
	        loadSensors();
	        publishProgress(100);
	        	        
	        return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			
			progressBar.setProgress(progress[0]);
		}
		
		@Override
		protected void onPostExecute(Void param) {
			
	        PreferenceManager.setDefaultValues(PreferencesActivity.this, R.xml.preferences,
	                false);
	        initSummary(getPreferenceScreen());
	        progressBar.dismiss();
		}
    }
    
    
    
    /**
     * Initializes the summary
     * @param p Preference
     */
    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    /**
     * Updates the summary of a preference
     * @param p the Preference to be updated
     */
    private void updatePrefSummary(Preference p) {
    	
    	if (p instanceof ListPreference) {

            ListPreference listPref = (ListPreference) p;
            if (listPref.getEntry() != null) {

                p.setSummary(listPref.getEntry());	
            }
        }
        
    	if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            if (p.getTitle().toString().contains("assword")) {
            	
                p.setSummary("******");
            } else {
                
            	p.setSummary(editTextPref.getText());
            }
        }
    }
    
    /**
     * Load arrays with all values
     */
    private void loadArrayValues() {
    	
    	// this one does not depend on Android API
    	String audioSampleRates[] = context.getResources().getStringArray(R.array.audioSampleRates);
    	for(int i = 0; i < audioSampleRates.length; i++ ) {
    		
    		sparseArrayAudioSampleRates.append(Integer.parseInt(audioSampleRates[i]), audioSampleRates[i]);
    	}
 
    	// depend on Android API
    	String audioSources_id[] = context.getResources().getStringArray(R.array.audioSources_id);
    	String audioSources[] = context.getResources().getStringArray(R.array.audioSources);
    	for(int i = 0; i < audioSources_id.length; i++ ) {
    	
    		sparseArrayAudioSources.append(Integer.parseInt(audioSources_id[i]), audioSources[i]);
    	}

    	// depend on Android API
    	String audioChannels_id[] = context.getResources().getStringArray(R.array.audioChannels_id);
    	String audioChannels[] = context.getResources().getStringArray(R.array.audioChannels);
    	for(int i = 0; i < audioChannels_id.length; i++ ) {
    	
    		sparseArrayAudioChannels.append(Integer.parseInt(audioChannels_id[i]), audioChannels[i]);
    	}
    	
    	// depend on Android API
    	String audioEncodingFormats_id[] = context.getResources().getStringArray(R.array.audioEncoding_id);
    	String audioEncodingFormats[] = context.getResources().getStringArray(R.array.audioEncoding);
    	for(int i = 0; i < audioEncodingFormats_id.length; i++ ) {
    	
    		sparseArrayAudioEncoding.append(Integer.parseInt(audioEncodingFormats_id[i]), audioEncodingFormats[i]);
    	}
    	
    	// depend on Android API
    	String sensors_id[] = context.getResources().getStringArray(R.array.sensors_id);
    	String sensors[] = context.getResources().getStringArray(R.array.sensors);
    	for(int i = 0; i < sensors_id.length; i++ ) {
    	
    		sparseArraySensors.append(Integer.parseInt(sensors_id[i]), sensors[i]);
    	}
    }
    
    
    /**
     * Fill Audio Sources preferences with available options only
     * @param context
     */
    private void loadAudioSources() {
    	
    	ListPreference listPreferenceCategory = (ListPreference) findPreference(DeviceUtils.PREF_AUDIO_SOURCE);
    	if ( listPreferenceCategory != null ) {
    		
    		String oldPref = listPreferenceCategory.getValue();
    		if( oldPref == null) {
    			
    			oldPref = "";
    		}

    		int lastValue = Integer.MIN_VALUE;
    		sparseArrayValidAudioSource = new SparseArray<String>();
    		for(int[] validSetting: validAudioRecordSettings) {
    			
    			if( validSetting[0] != lastValue ) {
    				
    				sparseArrayValidAudioSource.put(validSetting[0], 
    						sparseArrayAudioSources.get(validSetting[0]));
    				lastValue = validSetting[0];
    			}
    		}
    		    		
    		CharSequence entries[] = new String[sparseArrayValidAudioSource.size()];
    		CharSequence entriesValue[] = new String[sparseArrayValidAudioSource.size()];
    	
    		boolean oldPrefValid = false;
    		for(int i = 0; i < sparseArrayValidAudioSource.size(); i++) {
    			
    			entries[i] = sparseArrayValidAudioSource.valueAt(i);
    			entriesValue[i] = Integer.toString(sparseArrayValidAudioSource.keyAt(i));
    			if( oldPref.equals(entriesValue[i]) ) {
    				
    				oldPrefValid = true;
    			}
    		}
    		
    		listPreferenceCategory.setEntries(entries);
    		listPreferenceCategory.setEntryValues(entriesValue);
    		
    		if( oldPrefValid != true ) {

        		listPreferenceCategory.setValue(entriesValue[0].toString());
    		}
    	}
    }
    
    /**
     * Fill Audio Sample Rates preferences with valid options only
     * @param context
     */
    private void loadAudioSampleRates() {
    	
    	ListPreference listPreferenceCategory = (ListPreference) findPreference(DeviceUtils.PREF_AUDIO_SAMPLE_RATE);
    	if (listPreferenceCategory != null) {

    		String oldPref = listPreferenceCategory.getValue();
    		if( oldPref == null) {
    			
    			oldPref = "";
    		}

    		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    		int audioSource = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_SOURCE, 
    				Integer.toString(MediaRecorder.AudioSource.DEFAULT)));
    		
    		int lastValue = Integer.MIN_VALUE;
    		sparseArrayValidAudioSampleRate = new SparseArray<String>();
    		for(int[] validSetting: validAudioRecordSettings) {
    			
    			if(validSetting[0] == audioSource) {
    				
    				if(validSetting[1] != lastValue) {
    					
    					sparseArrayValidAudioSampleRate.put(validSetting[1], 
        						sparseArrayAudioSampleRates.get(validSetting[1]));
    					lastValue = validSetting[1];
    				}
    			}
    		}
    		    		
    		CharSequence entries[] = new String[sparseArrayValidAudioSampleRate.size()];
    		CharSequence entriesValue[] = new String[sparseArrayValidAudioSampleRate.size()];
    		
    		boolean oldPrefValid = false;
    		for(int i = 0; i < sparseArrayValidAudioSampleRate.size(); i++) {
    			
    			entries[i] = sparseArrayValidAudioSampleRate.valueAt(i);
    			entriesValue[i] = Integer.toString(sparseArrayValidAudioSampleRate.keyAt(i));
    			if( oldPref.equals(entriesValue[i]) ) {
    				
    				oldPrefValid = true;
    			}
    		}

    		listPreferenceCategory.setEntries(entries);
    		listPreferenceCategory.setEntryValues(entriesValue);
    		
    		if( oldPrefValid != true ) {

        		listPreferenceCategory.setValue(entriesValue[0].toString());
    		}
    	}
    }
    
    /**
     * Fill Audio Channels preferences with valid options only
     * @param context
     */
    private void loadAudioChannels() {
    	
    	ListPreference listPreferenceCategory = (ListPreference) findPreference(DeviceUtils.PREF_AUDIO_CHANNELS);
    	if (listPreferenceCategory != null) {

    		String oldPref = listPreferenceCategory.getValue();
    		if( oldPref == null) {
    			
    			oldPref = "";
    		}
    		
    		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    		int audioSource = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_SOURCE, 
    				Integer.toString(MediaRecorder.AudioSource.DEFAULT)));
    		int audioSampleRate = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_SAMPLE_RATE, "44100"));
    	
    		int lastValue = Integer.MIN_VALUE;
    		sparseArrayValidAudioChannels = new SparseArray<String>();
    		for(int[] validSetting: validAudioRecordSettings) {
    			
    			if(validSetting[0] == audioSource && validSetting[1] == audioSampleRate) {
    				
    				if(validSetting[2] != lastValue) {
    					
    					sparseArrayValidAudioChannels.put(validSetting[2], 
        						sparseArrayAudioChannels.get(validSetting[2]));
    					lastValue = validSetting[2];
    				}
    			}
    		}
    		    		
    		CharSequence entries[] = new String[sparseArrayValidAudioChannels.size()];
    		CharSequence entriesValue[] = new String[sparseArrayValidAudioChannels.size()];
    		
    		boolean oldPrefValid = false;
    		for(int i = 0; i < sparseArrayValidAudioChannels.size(); i++) {
    			
    			entries[i] = sparseArrayValidAudioChannels.valueAt(i);
    			entriesValue[i] = Integer.toString(sparseArrayValidAudioChannels.keyAt(i));
    			if( oldPref.equals(entriesValue[i]) ) {
    				
    				oldPrefValid = true;
    			}
    		}

    		listPreferenceCategory.setEntries(entries);
    		listPreferenceCategory.setEntryValues(entriesValue);
    		
    		if (oldPrefValid != true) {

        		listPreferenceCategory.setValue(entriesValue[0].toString());
    		}
    	}
    }
    
    /**
     * Fill Audio Encoding preferences with valid options only
     * @param context
     */
    private void loadAudioEncoding() {
    	
    	ListPreference listPreferenceCategory = (ListPreference) findPreference(DeviceUtils.PREF_AUDIO_ENCODING);
    	if (listPreferenceCategory != null) {

    		String oldPref = listPreferenceCategory.getValue();
    		if( oldPref == null) {
    			
    			oldPref = "";
    		}
  		
    		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    		int audioSource = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_SOURCE, 
    				Integer.toString(MediaRecorder.AudioSource.DEFAULT)));
    		int audioSampleRate = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_SAMPLE_RATE, "44100"));
    		int audioChannels = Integer.parseInt(sharedPreferences.getString(DeviceUtils.PREF_AUDIO_CHANNELS, 
    				Integer.toString(AudioFormat.CHANNEL_IN_MONO)));
    		
    		int lastValue = Integer.MIN_VALUE;
    		sparseArrayValidAudioEncoding = new SparseArray<String>();
    		for(int[] validSetting: validAudioRecordSettings) {
    			
    			if(validSetting[0] == audioSource && validSetting[1] == audioSampleRate && validSetting[2] == audioChannels) {
    				
    				if(validSetting[3] != lastValue) {
    					
    					sparseArrayValidAudioEncoding.put(validSetting[3], 
        						sparseArrayAudioEncoding.get(validSetting[3]));
    					lastValue = validSetting[3];
    				}
    			}
    		}
    		    		
    		CharSequence entries[] = new String[sparseArrayValidAudioEncoding.size()];
    		CharSequence entriesValue[] = new String[sparseArrayValidAudioEncoding.size()];
    		
    		boolean oldPrefValid = false;
    		for(int i = 0; i < sparseArrayValidAudioEncoding.size(); i++) {
    			
    			entries[i] = sparseArrayValidAudioEncoding.valueAt(i);
    			entriesValue[i] = Integer.toString(sparseArrayValidAudioEncoding.keyAt(i));
    			if( oldPref.equals(entriesValue[i]) ) {
    				
    				oldPrefValid = true;
    			}
    		}

    		listPreferenceCategory.setEntries(entries);
    		listPreferenceCategory.setEntryValues(entriesValue);
    		
    		if (oldPrefValid != true) {

        		listPreferenceCategory.setValue(entriesValue[0].toString());
    		}
    	}
    }
    
    

	/**
	 *  available sensors
	 */
	public void loadSensors() {
		
		SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference("pref_sensors");
		SparseBooleanArray loadedSensors = new SparseBooleanArray();
		
		for(Sensor sensor: sensorList) {
			
			if( loadedSensors.get(sensor.getType(), false) == false) {
		
				boolean sensorValue = sharedPreferences.getBoolean("pref_sensor_"+sensor.getType(), false);
				CheckBoxPreference checkBox = new CheckBoxPreference(context);
				checkBox.setKey("pref_sensor_"+sensor.getType());
				checkBox.setDefaultValue(sensorValue);
				checkBox.setTitle(sparseArraySensors.get(sensor.getType(),sensor.getName()));				
				preferenceScreen.addPreference(checkBox);
				loadedSensors.append(sensor.getType(), true);
			}
		}
	}
    
}
