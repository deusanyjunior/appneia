/**
 * 
 */
package br.usp.appneia.settings;

import java.util.LinkedList;

import br.usp.appneia.R;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @author dj
 *
 */
public class SpinnerManager {

	Context context;
	
	// A list with values retrieved from DeviceUtils
	private LinkedList<int[]> validAudioRecordSettings;
	
	/*
	 * Maps from resources and available configurations. String and Integer
	 * FUTURETODO: Update for new devices
	 */
	private SparseArray<String> sparseArrayMP3kbps;
	private SparseArray<String> sparseArrayAudioSources;
	private SparseArray<String> sparseArraySampleRatesHz;
	private SparseArray<String> sparseArrayChannelTypes;
	private SparseArray<String> sparseArrayEncodingFormats;
	private SparseArray<String> sparseArraySensorsDelay;
	
	private LinkedList<Integer> listValidMP3kbps = new LinkedList<Integer>();
	private LinkedList<Integer> listValidAudioSources = new LinkedList<Integer>();
	private LinkedList<Integer> listValidSampleRatesHz = new LinkedList<Integer>();
	private LinkedList<Integer> listValidChannelTypes = new LinkedList<Integer>();
	private LinkedList<Integer> listValidEncodingFormats = new LinkedList<Integer>();
	private LinkedList<Integer> listValidSensorsDelay = new LinkedList<Integer>();
	
	private String[] validMP3kbps;
	private String[] validAudioSources;
	private String[] validSampleRatesHz;
	private String[] validChannelTypes;
	private String[] validEncodingFormats;
	private String[] validSensorsDelay;
	
	private int mp3kbps = -1;
	private int audioSource = -1;
	private int sampleRateHz = -1;
	private int channelType = -1;
	private int encodingFormat = -1;
	private int sensorsDelay = -1;
	
	Spinner spinnerMP3kbps;
	Spinner spinnerAudioSources;
	Spinner spinnerSampleRatesHz;
	Spinner spinnerChannelTypes;
	Spinner spinnerEncodingFormats;
	Spinner spinnerSensorsDelay;
	
	ArrayAdapter<String> arrayAdapterMP3kbps;
	ArrayAdapter<String> arrayAdapterAudioSources;
	ArrayAdapter<String> arrayAdapterSampleRatesHz;
	ArrayAdapter<String> arrayAdapterChannelTypes;
	ArrayAdapter<String> arrayAdapterEncodingFormats;
	ArrayAdapter<String> arrayAdapterSensorsDelay;
	
	/**
	 * 
	 */
	public SpinnerManager(Context ctx) {
		
		this.context = ctx;
		this.validAudioRecordSettings = DeviceUtils.getValidAudioRecordSettings();
	}
	
	public void createSpinners(
			View sMP3kbps, 
			View sAudioSources, 
			View sSampleRatesHz, 
			View sChanelTypes, 
			View sEncodingFormats,
			View sSensorsDelay) {
		
		loadValidSettings(true, true, true);
		
		spinnerMP3kbps = (Spinner) sMP3kbps;
		arrayAdapterMP3kbps = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, validMP3kbps);
		arrayAdapterMP3kbps.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinnerMP3kbps.setAdapter(arrayAdapterMP3kbps);
		spinnerMP3kbps.setSelection(0);
		spinnerMP3kbps.setOnItemSelectedListener(onItemSelectedListenerMP3kbps());
		
		spinnerAudioSources = (Spinner) sAudioSources;
		arrayAdapterAudioSources = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, validAudioSources);
		arrayAdapterAudioSources.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinnerAudioSources.setAdapter(arrayAdapterAudioSources);
		spinnerAudioSources.setSelection(0);
//		spinnerAudioSources.setOnItemSelectedListener(listener);
		
		spinnerSampleRatesHz = (Spinner) sSampleRatesHz;
		arrayAdapterSampleRatesHz = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, validSampleRatesHz);
		arrayAdapterSampleRatesHz.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinnerSampleRatesHz.setAdapter(arrayAdapterSampleRatesHz);
		spinnerSampleRatesHz.setSelection(0);
//		spinnerSampleRatesHz.setOnItemSelectedListener(listener);
		
		spinnerChannelTypes = (Spinner) sChanelTypes;
		arrayAdapterChannelTypes = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, validChannelTypes);
		arrayAdapterChannelTypes.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinnerChannelTypes.setAdapter(arrayAdapterChannelTypes);
		spinnerChannelTypes.setSelection(0);
//		spinnerChannelTypes.setOnItemSelectedListener(listener);
		
		spinnerEncodingFormats = (Spinner) sEncodingFormats;
		arrayAdapterEncodingFormats = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, validEncodingFormats);
		arrayAdapterEncodingFormats.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinnerEncodingFormats.setAdapter(arrayAdapterEncodingFormats);
		spinnerEncodingFormats.setSelection(0);
//		spinnerEncodingFormats.setOnItemSelectedListener(listener);
		
		spinnerSensorsDelay = (Spinner) sSensorsDelay;
		arrayAdapterSensorsDelay = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, validSensorsDelay);
		arrayAdapterSensorsDelay.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinnerSensorsDelay.setAdapter(arrayAdapterSensorsDelay);
		spinnerSensorsDelay.setSelection(0);
//		spinnerSensorsDelay.setOnItemSelectedListener(listener);
		
//		notifyChangesToView();
	}
	
	public void notifyChangesToView() {
		
		if (arrayAdapterMP3kbps != null) {
			
			arrayAdapterMP3kbps.notifyDataSetChanged();
		}
		if (arrayAdapterAudioSources != null) {
			
			arrayAdapterAudioSources.notifyDataSetChanged();
		}
		if (arrayAdapterSampleRatesHz != null) {
			
			arrayAdapterSampleRatesHz.notifyDataSetChanged();
		}
		if (arrayAdapterChannelTypes != null) {
			
			arrayAdapterChannelTypes.notifyDataSetChanged();
		}
		if (arrayAdapterEncodingFormats != null) {
			
			arrayAdapterEncodingFormats.notifyDataSetChanged();
		}
		if (arrayAdapterSensorsDelay != null) {
			
			arrayAdapterSensorsDelay.notifyDataSetChanged();
		}
	}
	
	/**
	 * Return the settings on the spinners
	 * @return int[] : 	0 - mp3kbps,
	 * 					1 - audioSource,
	 * 					2 - sampleRateHz,
	 * 					3 - channelType, 
	 * 					4 - encodingFormat,
	 * 					5 - sensorsDelay
	 */
	public int[] getSpinnerSettings() {
		
		return new int[]{mp3kbps, audioSource, sampleRateHz, channelType, encodingFormat, sensorsDelay};
	}
		
	/**
	 * Load valid settings from validAudioRecordSettings.
	 * Always reload Encoding Formats
	 * 
	 * @param reloadAll
	 * @param reloadSampleRates
	 * @param reloadChannelTypes
	 */
	private void loadValidSettings(boolean reloadAll, 
									boolean reloadSampleRates, 
									boolean reloadChannelTypes) {
		
		if (reloadAll) {
			
			this.validAudioRecordSettings = DeviceUtils.getValidAudioRecordSettings();
		
			listValidMP3kbps = new LinkedList<Integer>();
			listValidAudioSources = new LinkedList<Integer>();
			listValidSampleRatesHz = new LinkedList<Integer>();
			listValidChannelTypes = new LinkedList<Integer>();
			listValidEncodingFormats = new LinkedList<Integer>();
			listValidSensorsDelay = new LinkedList<Integer>();
			
			
			mp3kbps = -1;
			audioSource = -1;
			sampleRateHz = -1;
			channelType = -1;
			encodingFormat = -1;
			sensorsDelay = -1;
		} else if (reloadSampleRates) {
			
			listValidSampleRatesHz = new LinkedList<Integer>();
			listValidChannelTypes = new LinkedList<Integer>();
			listValidEncodingFormats = new LinkedList<Integer>();
			
			sampleRateHz = -1;
			channelType = -1;
			encodingFormat = -1;
		} else if (reloadChannelTypes) {
			
			listValidChannelTypes = new LinkedList<Integer>();
			listValidEncodingFormats = new LinkedList<Integer>();
			
			channelType = -1;
			encodingFormat = -1;
		}

		if (mp3kbps == -1) {
			
			String[] mp3kbpsArray = context.getResources().getStringArray(R.array.mp3kbps);
			mp3kbps = Integer.parseInt(mp3kbpsArray[0]);

			for (String mp3kbpsSetting : mp3kbpsArray) {
				
				listValidMP3kbps.add(Integer.parseInt(mp3kbpsSetting));
			}
		}
		
		if (sensorsDelay == -1) {
			
			String[] sensorsDelayArray = context.getResources().getStringArray(R.array.sensorsdelay);
			if (sensorsDelayArray.length > 0) {
				
				sensorsDelay = 0;
			}
			for (int i = 0; i < sensorsDelayArray.length; i++) {
				
				listValidSensorsDelay.add(i);
			}
		}
		
		if (validAudioRecordSettings.size() > 0) {
			
			// set audio source
			if (audioSource == -1) {
				int[] validSetting = validAudioRecordSettings.getFirst();				
				audioSource = validSetting[0];
			}
			
			for (int[] setting: validAudioRecordSettings) {
				
				// add all audio sources options
				if ((reloadAll) && !listValidAudioSources.contains(setting[0])) {
				
					listValidAudioSources.add(setting[0]);
				}
				if (setting[0] == audioSource) {
					
					// add sample rates supported by selected audio source
					if ((reloadAll || reloadSampleRates) && !listValidSampleRatesHz.contains(setting[1])) {
							
						listValidSampleRatesHz.add(setting[1]);
					}
					// set sample rate 
					if (sampleRateHz == -1) {
						
						sampleRateHz = setting[1];
					}
					if (setting[1] == sampleRateHz) {
						
						// add channel types supported by selected audio source and sample rate
						if ((reloadAll || reloadSampleRates || reloadChannelTypes) && !listValidChannelTypes.contains(setting[2])) {
								
							listValidChannelTypes.add(setting[2]);
						}
						// set channel type
						if (channelType == -1) {
							
							channelType = setting[2];							
						}
						if (setting[2] == channelType) {
							
							// add encoding formats supported by selected audio source, sample rate, and channel type
							if (!listValidEncodingFormats.contains(setting[3])) {
								
								listValidEncodingFormats.add(setting[3]);
							}
							// set encoding format
							if (encodingFormat == -1) {
								
								encodingFormat = setting[3];
							}
						}
					}
				}
			}
			
			fillValidValuesWithNames();
		} else {
			// TODO: set values to error and ask user to reload all?!
		}
	}
	
	/**
	 * Fill String arrays with the valid names to be presented on spinners based on the mapped values.
	 */
	private void fillValidValuesWithNames() {
		
		if (sparseArrayMP3kbps == null || 
				sparseArrayAudioSources == null || 
				sparseArraySampleRatesHz == null || 
				sparseArrayChannelTypes == null || 
				sparseArrayEncodingFormats == null ||
				sparseArraySensorsDelay == null) {
			
			loadAndMapResourcesNames();
		}
		
		// fill mp3kbps
		if (listValidMP3kbps.size() > 0) {
			
			validMP3kbps = new String[listValidMP3kbps.size()];
			for (int i = 0; i < listValidMP3kbps.size(); i++) {
				
				validMP3kbps[i] = sparseArrayMP3kbps.get(listValidMP3kbps.get(i));
			}
		}
		// fill audio sources
		if (listValidAudioSources.size() > 0) {
			
			validAudioSources = new String[listValidAudioSources.size()];
			for (int i = 0; i < listValidAudioSources.size(); i++) {
				
				validAudioSources[i] = sparseArrayAudioSources.get(listValidAudioSources.get(i));
			}
		}
		// fill sample rates
		if (listValidSampleRatesHz.size() > 0) {
			
			validSampleRatesHz = new String[listValidSampleRatesHz.size()];
			for (int i = 0; i < listValidSampleRatesHz.size(); i++) {
				
				validSampleRatesHz[i] = sparseArraySampleRatesHz.get(listValidSampleRatesHz.get(i));
			}
		}
		// fill channel types
		if (listValidChannelTypes.size() > 0) {
			
			validChannelTypes = new String[listValidChannelTypes.size()];
			for (int i = 0; i < listValidChannelTypes.size(); i++) {
				
				validChannelTypes[i] = sparseArrayChannelTypes.get(listValidChannelTypes.get(i));
			}
		}
		// fill encoding formats
		if (listValidEncodingFormats.size() > 0) {
			
			validEncodingFormats = new String[listValidEncodingFormats.size()];
			for (int i = 0; i < listValidEncodingFormats.size(); i++) {
				
				validEncodingFormats[i] = sparseArrayEncodingFormats.get(listValidEncodingFormats.get(i));
				if (validEncodingFormats[i] == null) {
					validEncodingFormats[i] = "";
				}
			}
		}
		// fill sensors delay
		if (listValidSensorsDelay.size() > 0) {
			
			validSensorsDelay = new String[listValidSensorsDelay.size()];
			for (int i = 0; i < listValidSensorsDelay.size(); i++) {
				
				validSensorsDelay[i] = sparseArraySensorsDelay.get(listValidSensorsDelay.get(i));
			}
		}
	}
	
	/**
	 * Load the possible string values from resources 
	 * to be used with the spinners and map to corresponding integer.
	 * Normally following the API INTEGER identification when possible. 
	 */
	private void loadAndMapResourcesNames() {
		
		sparseArrayMP3kbps = new SparseArray<String>();
		sparseArrayAudioSources = new SparseArray<String>();
		sparseArraySampleRatesHz = new SparseArray<String>();
		sparseArrayChannelTypes = new SparseArray<String>();
		sparseArrayEncodingFormats = new SparseArray<String>();
		sparseArraySensorsDelay = new SparseArray<String>();
		
		String[] mp3kbpsArray = context.getResources().getStringArray(R.array.mp3kbps);
		String[] audioSourcesArray = context.getResources().getStringArray(R.array.audiosources);
		String[] sampleRatesHzArray = context.getResources().getStringArray(R.array.samplerateshz);
		String[] channelsTypesArray = context.getResources().getStringArray(R.array.channeltypes);
		String[] encodingFormatsArray = context.getResources().getStringArray(R.array.encodingformats);
		String[] sensorsDelayArray = context.getResources().getStringArray(R.array.sensorsdelay);
		
		for (int i = 0; i < mp3kbpsArray.length; i++) {
			
			// this one differ because these values are not defined by Android API
			sparseArrayMP3kbps.put(Integer.parseInt(mp3kbpsArray[i]), mp3kbpsArray[i]);
		}
		for (int i = 0; i < audioSourcesArray.length; i++) {
			
			sparseArrayAudioSources.put(i, audioSourcesArray[i]);
		}		
		for (int i = 0; i < sampleRatesHzArray.length; i++) {
			
			// this one differ because these values are not defined by Android API
			sparseArraySampleRatesHz.put(Integer.parseInt(sampleRatesHzArray[i]), sampleRatesHzArray[i]);
		}		
		for (int i = 0; i < channelsTypesArray.length; i++) {
			
			sparseArrayChannelTypes.put(i, channelsTypesArray[i]);
		}	
		for (int i = 0; i < encodingFormatsArray.length; i++) {
			
			// 16Bit = 2, 8Bit = 3 at Android API
			sparseArrayEncodingFormats.put(i+2, encodingFormatsArray[i]);
		}
		for (int i = 0; i < sensorsDelayArray.length; i++) {
			
			sparseArraySensorsDelay.put(i, sensorsDelayArray[i]);
		}
	}
	
	/**
	 * MP3 kbps listener for item selected. Update the setting.
	 * @return listener
	 */
	private AdapterView.OnItemSelectedListener onItemSelectedListenerMP3kbps() {
		
		return new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				
				mp3kbps = listValidMP3kbps.get(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		};
	}
	
	
	
	
	
	
	
}
