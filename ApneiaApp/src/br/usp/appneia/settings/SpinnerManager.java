/**
 * 
 */
package br.usp.appneia.settings;

import java.util.LinkedList;

import br.usp.appneia.R;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
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
	 * Maps from resources and available configurations.
	 * FUTURETODO: Update for new devices
	 */
	private SparseArray<String> sparseArrayAudioSources;
	private SparseArray<String> sparseArraySampleRatesHz;
	private SparseArray<String> sparseArrayChannelTypes;
	private SparseArray<String> sparseArrayEncodingFormats;
	
	private String[] validAudioSources;
	private String[] validSampleRatesHz;
	private String[] validChannelTypes;
	private String[] validEncodingFormats;
	
	private int audioSource = -1;
	private int sampleRateHz = -1;
	private int channelType = -1;
	private int encodingFormat = -1;
	
	Spinner spinnerAudioSources;
	Spinner spinnerSampleRatesHz;
	Spinner spinnerChannelTypes;
	Spinner spinnerEncodingFormats;
	
	ArrayAdapter<String> arrayAdapterAudioSources;
	ArrayAdapter<String> arrayAdapterSampleRatesHz;
	ArrayAdapter<String> arrayAdapterChannelTypes;
	ArrayAdapter<String> arrayAdapterEncodingFormats;
	
	/**
	 * 
	 */
	public SpinnerManager(Context ctx) {
		
		this.context = ctx;
		this.validAudioRecordSettings = DeviceUtils.getValidAudioRecordSettings();
	}
	
	public void createSpinners(View sAudioSources, View sSampleRatesHz, View sChanelTypes, View sEncodingFormats) {
		
		loadValidSettings(true, true, true);
		
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
		
//		notifyChangesToView();
	}
	
	public void notifyChangesToView() {
		
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
		
		LinkedList<Integer> listValidAudioSources = new LinkedList<Integer>();
		LinkedList<Integer> listValidSampleRatesHz = new LinkedList<Integer>();
		LinkedList<Integer> listValidChannelTypes = new LinkedList<Integer>();
		LinkedList<Integer> listValidEncodingFormats = new LinkedList<Integer>();
		
		if (reloadAll) {
			
			this.validAudioRecordSettings = DeviceUtils.getValidAudioRecordSettings();
			audioSource = -1;
			sampleRateHz = -1;
			channelType = -1;
			encodingFormat = -1;
		} else if (reloadSampleRates) {
			
			sampleRateHz = -1;
			channelType = -1;
			encodingFormat = -1;
		} else if (reloadChannelTypes) {
			
			channelType = -1;
			encodingFormat = -1;
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
			
			fillValidValuesWithNames(listValidAudioSources,  
										listValidSampleRatesHz, 
										listValidChannelTypes, 
										listValidEncodingFormats);
		} else {
			// TODO: set values to error and ask user to reload all?!
		}
	}
	
	/**
	 * Fill String arrays with the valid names to be presented on spinners based on the mapped values.
	 * 
	 * @param listValidAudioSources
	 * @param listValidSampleRatesHz
	 * @param listValidChannelTypes
	 * @param listValidEncodingFormats
	 */
	private void fillValidValuesWithNames(LinkedList<Integer> listValidAudioSources, 
											LinkedList<Integer> listValidSampleRatesHz,
											LinkedList<Integer> listValidChannelTypes, 
											LinkedList<Integer> listValidEncodingFormats) {
		
		if (sparseArrayAudioSources == null || sparseArraySampleRatesHz == null || sparseArrayChannelTypes == null || sparseArrayEncodingFormats == null) {
			
			loadAndMapResourcesNames();
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
	}
	
	/**
	 * Load the possible string values to be used with the spinners and map to corresponding integer.
	 */
	private void loadAndMapResourcesNames() {
		
		sparseArrayAudioSources = new SparseArray<String>();
		sparseArraySampleRatesHz = new SparseArray<String>();
		sparseArrayChannelTypes = new SparseArray<String>();
		sparseArrayEncodingFormats = new SparseArray<String>();
		
		String[] audioSourcesArray = context.getResources().getStringArray(R.array.audiosources);
		String[] sampleRatesHzArray = context.getResources().getStringArray(R.array.samplerateshz);
		String[] channelsTypesArray = context.getResources().getStringArray(R.array.channeltypes);
		String[] encodingFormatsArray = context.getResources().getStringArray(R.array.encodingformats);
		
		for(int i = 0; i < audioSourcesArray.length; i++) {
			
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
	}
}
