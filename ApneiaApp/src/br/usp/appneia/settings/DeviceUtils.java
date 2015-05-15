package br.usp.appneia.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import br.usp.appneia.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

public class DeviceUtils {
	
	/*
	 * Time interval to save samples
	 */
	private static final int TIME_INTERVAL = 120;
	
	/*
	 * List of last storage status 
	 */
	private static ArrayList<Integer> storageStatus;
	
	/*
	 * List of valid audio recording settings
	 */
	private static LinkedList<int[]> validAudioRecordingSettings;
		
	public static final String SETTINGS_PREFERENCES_NAME = "settingsPreferences";
	
	private static final String recordingFormatPreference_name = "recordingFormat";
	private static final String mp3kbpsPreference_name = "mp3kbps";
	private static final String audioSourcePreference_name = "audioSource";
	private static final String sampleRateHzPreference_name = "sampleRateHz";
	private static final String channelTypePreference_name = "channelType";
	private static final String encodingFormatPreference_name = "encodingFormat";
	private static final String sensorsPreferences_name = "sensors";
	private static final String sensorsDelayPreference_name = "sensorsSampleRate";
	
	/**
	 * Returns the storage directory used to save files.
	 * 
	 * @return String path to the storage directory
	 */
	public static String getStoragePath() {
		
		return Environment.getExternalStorageDirectory().getPath(); 
	}
	
	/**
	 * Returns the free space on external storage. The deprecated methods
	 * are used to support old APIs with small storage.
	 * 
	 * @return long representing the free space in megabytes.
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static long getStorageFreeSpace() {
		
		long freeSpace = 0;
		File externalStorageDir = null;
		int build = 0;
		
		try {
			if (Integer.parseInt(android.os.Build.VERSION.SDK) < android.os.Build.VERSION_CODES.DONUT) {
				build = Integer.parseInt(android.os.Build.VERSION.SDK);
			} else {
				build = android.os.Build.VERSION.SDK_INT;
			}
		} catch (NumberFormatException e) {
			build = 0;
		}
		
		if (build < 9) {
			
			externalStorageDir = Environment.getExternalStorageDirectory();
			StatFs statFs = new StatFs(externalStorageDir.getAbsolutePath());  
			long blocks = statFs.getAvailableBlocks();
			freeSpace = (blocks * statFs.getBlockSize()) / 1024 / 1024;
		} else {
			
			externalStorageDir = Environment.getExternalStorageDirectory();
			freeSpace = externalStorageDir.getFreeSpace() / 1024 / 1024;
		}
		
		return freeSpace;
	}
	
		
	/**
	 * Verify storage status and add the last status to a list in order to be retrieved afterward.
	 * 
	 * Reference:
	 * http://stackoverflow.com/questions/4580683/writing-text-file-to-sd-card-fails
	 * 
	 * @param none
	 * @return boolean Return true if the external storage can be used	
	 * 
	 */
	public static Boolean verifyStorageStatus() {

		
	    String auxSDCardStatus = Environment.getExternalStorageState();
		
		if (storageStatus == null) {
			storageStatus = new ArrayList<Integer>();
		}

	    if ( auxSDCardStatus.equals(Environment.MEDIA_MOUNTED) ) {
	    	storageStatus.add(R.string.toast_device_utils_media_mounted);
	    	return true;
	    } else if ( auxSDCardStatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY) ) {
	        storageStatus.add(R.string.toast_device_utils_media_mounted_read_only);
	        return true;
	    } else if ( auxSDCardStatus.equals(Environment.MEDIA_NOFS) ) {
	        storageStatus.add(R.string.toast_device_utils_media_nofs);
	        return false;
	    } else if ( auxSDCardStatus.equals(Environment.MEDIA_REMOVED) ) {
	        storageStatus.add(R.string.toast_device_utils_media_removed);
	        return false;
	    } else if ( auxSDCardStatus.equals(Environment.MEDIA_SHARED) ) {
	        storageStatus.add(R.string.toast_device_utils_media_shared);
	        return false;
	    } else if ( auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTABLE) ) {
	        storageStatus.add(R.string.toast_device_utils_media_unmountable);
	        return false;
	    } else if ( auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTED) ) {
	        storageStatus.add(R.string.toast_device_utils_media_unmounted);
	        return false;
	    }

	    return true;
	}
	
	/**
	 * Toast all status and reset the array of status.
	 * 
	 * @param mContext the application context
	 */
	public static void toastAllStorageStatus(Context mContext) {
		
		ArrayList<Integer> tempStatus = new ArrayList<Integer>();
		
		if (storageStatus != null) {
			
			tempStatus.addAll(storageStatus);
			storageStatus = new ArrayList<Integer>();
			
			for (Integer statusId : tempStatus) {
				//TODO: what can we do if the application crashes or finishes?!
				Toast.makeText(mContext, statusId, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/**
	 * Retrieve all information available about the system build and return a string
	 * formated with this information depending on the API.
	 * 
	 * Reference:
	 * https://github.com/andrejb/DspBenchmarking
	 * 
	 * @return String containing all the information about the system build 
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static String getBuildInfo() {
		StringBuffer info = new StringBuffer();
		int build = 0;
		
		try {
			if (Integer.parseInt(android.os.Build.VERSION.SDK) < android.os.Build.VERSION_CODES.DONUT) {
				build = Integer.parseInt(android.os.Build.VERSION.SDK);
			} else {
				build = android.os.Build.VERSION.SDK_INT;
			}
		} catch (NumberFormatException e) {
			build = 0;
		}
		
			info.append("# board: " + android.os.Build.BOARD + "\n");									// API Level 1
		if (build >= android.os.Build.VERSION_CODES.FROYO)
			info.append("# bootloader: "+android.os.Build.BOOTLOADER+"\n"); 							// API Level 8
			info.append("# brand: " + android.os.Build.BRAND + "\n");									// API Level 1
		if (build < android.os.Build.VERSION_CODES.LOLLIPOP)
			info.append("# cpu_abi: " + android.os.Build.CPU_ABI + "\n");  								// API Level 4 - 21
		if (build >= android.os.Build.VERSION_CODES.FROYO && 
				build < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			info.append("# cpu_abi2: "+android.os.Build.CPU_ABI2+"\n");  								// API Level 8 - 21		
			info.append("# device: " + android.os.Build.DEVICE + "\n");									// API Level 1
		if (build >= android.os.Build.VERSION_CODES.CUPCAKE)
			info.append("# display: " + android.os.Build.DISPLAY + "\n");  								// API Level 3
			info.append("# fingerprint: " + android.os.Build.FINGERPRINT + "\n"); 						// API Level 1
		if (build >= android.os.Build.VERSION_CODES.FROYO)
			info.append("# hardware: "+android.os.Build.HARDWARE+"\n");  								// API Level 8
			info.append("# host: " + android.os.Build.HOST + "\n");										// API Level 1
			info.append("# id: " + android.os.Build.ID + "\n");											// API Level 1
		if (build >= android.os.Build.VERSION_CODES.DONUT)
			info.append("# manufacturer: " + android.os.Build.MANUFACTURER + "\n"); 					// API Level 4
			info.append("# model: " + android.os.Build.MODEL + "\n");									// API Level 1
			info.append("# product: " + android.os.Build.PRODUCT + "\n");								// API Level 1
		if (build >= android.os.Build.VERSION_CODES.FROYO && 
				build < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			info.append("# radio: " + android.os.Build.RADIO + "\n");									// API Level 8 - 14
		if (build >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			info.append("# radio: " + android.os.Build.getRadioVersion() + "\n");						// API Level 14
		if (build >= android.os.Build.VERSION_CODES.GINGERBREAD)
			info.append("# serial: "+android.os.Build.SERIAL+"\n");  									// API Level 9
		if (build >= android.os.Build.VERSION_CODES.LOLLIPOP)
			info.append("# supported_32_bit_abis: " + Arrays.toString(android.os.Build.SUPPORTED_32_BIT_ABIS) + "\n");	// API Level 21
		if (build >= android.os.Build.VERSION_CODES.LOLLIPOP)
			info.append("# supported_64_bit_abis: " + Arrays.toString(android.os.Build.SUPPORTED_64_BIT_ABIS) + "\n");	// API Level 21
		if (build >= android.os.Build.VERSION_CODES.LOLLIPOP)
			info.append("# supported_abis: " + Arrays.toString(android.os.Build.SUPPORTED_ABIS) + "\n");					// API Level 21
			info.append("# tags: " + android.os.Build.TAGS + "\n");										// API Level 1
			info.append("# time: " + android.os.Build.TIME + "\n");										// API Level 1
			info.append("# type: " + android.os.Build.TYPE + "\n");										// API Level 1
			info.append("# user: " + android.os.Build.USER + "\n");										// API Level 1
		if (build >= android.os.Build.VERSION_CODES.DONUT)
			info.append("# version codename: " + android.os.Build.VERSION.CODENAME + "\n");				// API Level 4
			info.append("# version incremental: " + android.os.Build.VERSION.INCREMENTAL + "\n");		// API Level 1
			info.append("# version release: " + android.os.Build.VERSION.RELEASE + "\n");				// API Level 1
		if (build < android.os.Build.VERSION_CODES.DONUT)
			info.append("# version sdk: " + android.os.Build.VERSION.SDK + "\n");						// API Level 1 - 4
		if (build >= android.os.Build.VERSION_CODES.DONUT)
			info.append("# version sdk_int: " + android.os.Build.VERSION.SDK_INT);						// API Level 4
			
		return info.toString();
	}
	
	

	/**
	 * Set the record folder based on the name
	 * @return a full path if the folder has been created successfully, or null otherwise.
	 */
	public static String setupRecordFolder(Context context, String folderName) {
		
		String tempRecordFolderPath = null;
		
		if (!verifyStorageStatus()) {
			toastAllStorageStatus(context);
			return null;
		};
		
		// verify the storage path
		String storagePath = getStoragePath();
		File appFolder = new File(storagePath+"/"+context.getResources().getString(R.string.app_name));
		if (!appFolder.exists() && !appFolder.mkdirs()) {
			return null;
		} else if (!appFolder.isDirectory()) {
			return null;
		}
		
		// create the record folder path
		StringBuilder recordFolderPathBuilder = new StringBuilder();
		recordFolderPathBuilder.append(appFolder);
		recordFolderPathBuilder.append("/");
		recordFolderPathBuilder.append(folderName);
		tempRecordFolderPath = recordFolderPathBuilder.toString();
		File recordFolderFile = new File(tempRecordFolderPath);
		if (!recordFolderFile.exists() && !recordFolderFile.mkdirs()) {
			return null;
		} else if (!recordFolderFile.isDirectory()) {
			return null;
		}
		
		return tempRecordFolderPath;
	}
	
	/**
	 * Write data on file
	 * 
	 * @param filePath is the path to file to write the data
	 * @param fileName is the name of the file to write the data
	 * @param data to be written
	 * @return true if the data has been written successfully
	 */
	public static boolean writeDataOnFile(String filePath, String fileName, String data) {
		
		File file = new File(filePath, fileName);
		if (file.isDirectory()) {
			return false;
		} 
		
		try {
			FileOutputStream outputStream = new FileOutputStream(file, true); // Append the content
			OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
			outputWriter.append(data);
			outputWriter.close();
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	
	/**
	 * Verify possible settings of input audio for recording.
	 * The valid settings follow this order: 	
	 * <ul>
	 * 	<li>0 - audio source</li>
	 * 	<li>1 - sample rate in Hz</li>
	 * 	<li>2 - number of channels</li>
	 * 	<li>3 - PCM encoding format</li>
	 * 	<li>4 - bits per sample</li>
	 * 	<li>5 - frame rate</li>
	 * 	<li>6 - buffer size</li>
	 * </ul>
	 * 
	 * @return boolean if we have at least one valid setting
	 * 
	 */
	@SuppressLint("InlinedApi")
	private static boolean verifyValidAudioRecordSettings()
	{
		validAudioRecordingSettings = new LinkedList<int[]>();
		AudioRecord audioRecord = null;
		
		int audioSources[] = { MediaRecorder.AudioSource.DEFAULT,
				MediaRecorder.AudioSource.MIC,
				MediaRecorder.AudioSource.VOICE_UPLINK,
				MediaRecorder.AudioSource.VOICE_DOWNLINK,
				MediaRecorder.AudioSource.VOICE_CALL,
				MediaRecorder.AudioSource.CAMCORDER,
				MediaRecorder.AudioSource.VOICE_RECOGNITION,
				MediaRecorder.AudioSource.VOICE_COMMUNICATION,
				MediaRecorder.AudioSource.REMOTE_SUBMIX};
		int sampleRatesHz[] = { 8000, 11025, 16000, 22015, 44100 , 48000 , 88200 , 96000 };
		int channelTypes[] = { AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO };
		int encodingFormats[] = {AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT}; // 16Bit = 2, 8Bit = 3 on Android API

		short bitRates[] = { 16, 8 };
		short numbersOfChannels[] = { 1, 2 };
		
		// Audio source settings to be verified
		int mAudioSource;
		int mSampleRateHz;
		short mNumberOfChannels;
		int mEncodingFormat;
		short mBitRate;
		int mFrameRate;
		int mBufferSize;

		for(int aS = 0; aS < audioSources.length; aS++) {
			
			for(int sR = 0; sR < sampleRatesHz.length; sR++) {
				
				for(int nC = 0; nC < numbersOfChannels.length; nC++) {
					
					for(int bR = 0; bR < bitRates.length; bR++) {
						
						mAudioSource = audioSources[aS];
						mSampleRateHz = sampleRatesHz[sR];
						mNumberOfChannels = numbersOfChannels[nC];
						mEncodingFormat = encodingFormats[bR];
						mBitRate = bitRates[bR];
						
						mFrameRate = mSampleRateHz * TIME_INTERVAL / 1000;
						mBufferSize = mFrameRate * 2 * mBitRate * mNumberOfChannels / 8;
						
						// Verify if the buffer size has a bad value
						if ( AudioRecord.getMinBufferSize(mSampleRateHz, channelTypes[mNumberOfChannels], mEncodingFormat) 
								== AudioRecord.ERROR_BAD_VALUE ) {
							
//							Log.e(DeviceUtils.class.getName(), "Audio Record buffer size has bad value");
							continue;
						} else {
							
//							Log.i(DeviceUtils.class.getName(), "Audio Record buffer size has good value");
						}
						
						// Verify if the buffer is lower than the minimum allowed
						if (mBufferSize < AudioRecord.getMinBufferSize(mSampleRateHz, 
								channelTypes[mNumberOfChannels], mEncodingFormat))	{ 
							
							// change the value in this case
							mBufferSize = AudioRecord.getMinBufferSize(mSampleRateHz, channelTypes[mNumberOfChannels], mEncodingFormat);
							
							// Set the frame rate based on this result
							mFrameRate = mBufferSize / ( 2 * mBitRate * mNumberOfChannels / 8 );
//							Log.e(DeviceUtils.class.getName(), "Audio Record buffer lower than the minimum; buffer changed to minimum");
						} else {
							
//							Log.i(DeviceUtils.class.getName(), "Audio Record buffer is equal or higher than the minimum required");
						}
						
						try {
							
							// Try to initialize the AudioRecord
							audioRecord = new AudioRecord(mAudioSource, mSampleRateHz, channelTypes[mNumberOfChannels], mEncodingFormat, mBufferSize);
//							Log.i(DeviceUtils.class.getName(), "Audio Record possible initialization");
						}
						catch (IllegalArgumentException e) {
//							Log.e(DeviceUtils.class.getName(), "Audio Record initialization exception");
//							e.printStackTrace();
							continue;
						}
						
						if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
							
							int actualValidSetting[] = { mAudioSource,
									   			   mSampleRateHz,
									   			   mNumberOfChannels,
									   			   mEncodingFormat,
									   			   mBitRate,
									   			   mFrameRate,
									   			   mBufferSize };
							
							validAudioRecordingSettings.add(actualValidSetting);
//							Log.i(DeviceUtils.class.getName(), "Audio Recording valid setting " + 
//																							" " + audioSources[aS] +
//																							" " + sampleRatesHz[sR] +
//																							" " + numbersOfChannels[nC] +
//																							" " + encodingFormats[bR] +
//																							" " + bitRates[bR] +
//																							" " + mFrameRate +
//																							" " + mBufferSize);
						} else {
//							Log.e(DeviceUtils.class.getName(), "AudioRecord state != initialized");
						}
						audioRecord.release();
					}
				}
			}
		}

		audioRecord.release();
		audioRecord = null;
		
		Log.i(DeviceUtils.class.getName(), "Audio Record valid settings: " + validAudioRecordingSettings.size() +" from " + 
				audioSources.length * sampleRatesHz.length * numbersOfChannels.length * bitRates.length + " tested!") ;
				
		return validAudioRecordingSettings.size() > 0? true: false;
	}
	
	/**
	 * The valid settings follow this order: 	
	 * <ul>
	 * 	<li>0 - audio source</li>
	 * 	<li>1 - sample rate in Hz</li>
	 * 	<li>2 - number of channels</li>
	 * 	<li>3 - PCM encoding format</li>
	 * 	<li>4 - bits per sample</li>
	 * 	<li>5 - frame rate</li>
	 * 	<li>6 - buffer size</li>
	 * </ul>
	 * 
	 * The values are set using the method verifyValidAudioRecordSettings.
	 * 
	 * @return LinkedList<int[]> with the valid settings
	 */
	@SuppressWarnings("unchecked")
	public static LinkedList<int[]> getValidAudioRecordSettings() {
		
		LinkedList<int[]> validSettings;
		
		if (validAudioRecordingSettings == null) {
			
			verifyValidAudioRecordSettings();
		}
		
		try {
			validSettings = (LinkedList<int[]>) validAudioRecordingSettings.clone();			
		} catch (ClassCastException e) {
			
			validSettings = new LinkedList<int[]>();
			e.printStackTrace();
		}
		
		return validSettings;
	}
	
	/**
	 * Reload the valid audio settings for audio record.
	 * 
	 * @return LinkedList<int[]> with new valid settings
	 */
	public static LinkedList<int[]> getNewValidAudioRecordSettings() {
		
		validAudioRecordingSettings = null;
		
		return getValidAudioRecordSettings();
	}
	
	/**
	 * Verify the if the preferences are already set, and load defaults otherwise
	 * @param context
	 * @return true if the preferences have been loaded successfully
	 */
	public static boolean loadPreferences(Context context) {
		
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
		
		// Load default preferences
		if (sharedPreferences.getAll().size() == 0) {
			
			SharedPreferences.Editor editorSharedPreferences = 
					sharedPreferences.edit();
			
			editorSharedPreferences.putInt(recordingFormatPreference_name, 0); // 0 = MP3, 1 = WAVE, 2 = 3GP
			editorSharedPreferences.putInt(mp3kbpsPreference_name, 32); // 32, 64, 96, 128, 160, 256, 320
			
			editorSharedPreferences.putInt(audioSourcePreference_name, MediaRecorder.AudioSource.DEFAULT);
			editorSharedPreferences.putInt(sampleRateHzPreference_name, 8000);
			editorSharedPreferences.putInt(channelTypePreference_name, AudioFormat.CHANNEL_IN_DEFAULT);
			editorSharedPreferences.putInt(encodingFormatPreference_name, AudioFormat.ENCODING_DEFAULT);
			
			editorSharedPreferences.putInt(sensorsDelayPreference_name, SensorManager.SENSOR_DELAY_NORMAL);
			editorSharedPreferences.putInt(sensorsPreferences_name, 0); // -1 = ALL, decimal value converted from a reverse binary representation of the sensors to load, 0 = none 
			
			if (!editorSharedPreferences.commit()) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Update the preferences
	 * 
	 * @param context
	 * @param recordingFormat   0 = MP3, 1 = WAVE, 2 = 3GP
	 * @param mp3kbps           32, 64, 96, 128, 160, 256, 320
	 * @param audioSource
	 * @param sampleRateHz      8000, 11025, 16000, 22015, 44100 , 48000 , 88200 , 96000
	 * @param channelType
	 * @param encodingFormat
	 * @param sensorsDelay      0 = fastest, 1 = game, 2 = ui, 3 = normal
	 * @param sensors           -1 = ALL, decimal value converted from a reverse binary representation of the sensors to load, 0 = none
	 * 
	 * @return true if the preferences have been saves successfully
	 */
	public static boolean updatePreferences(Context context,
			int recordingFormat, int mp3kbps, 
			int audioSource, int sampleRateHz, int channelType, int encodingFormat, 
			int sensorsDelay, int sensors) {
	
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editorSharedPreferences = 
				sharedPreferences.edit();
		
		editorSharedPreferences.putInt(recordingFormatPreference_name, recordingFormat);
		editorSharedPreferences.putInt(mp3kbpsPreference_name, mp3kbps);
		
		editorSharedPreferences.putInt(audioSourcePreference_name, audioSource);
		editorSharedPreferences.putInt(sampleRateHzPreference_name, sampleRateHz);
		editorSharedPreferences.putInt(channelTypePreference_name, channelType);
		editorSharedPreferences.putInt(encodingFormatPreference_name, encodingFormat);
		
		editorSharedPreferences.putInt(sensorsDelayPreference_name, sensorsDelay);
		editorSharedPreferences.putInt(sensorsPreferences_name, sensors);  
		
		if (!editorSharedPreferences.commit()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Return the preferences following some definitions:
	 * 
	 * <ul>
	 *  <li> recordingFormat : 0 = MP3, 1 = WAVE, 2 = 3GP </li>  
	 *  <li> mp3kbps : 32, 64, 96, 128, 160, 256, 320 </li>
	 *  <li> sensorsDelay : 0 = fastest, 1 = game, 2 = ui, 3 = normal </li>
	 *  <li> sensors : -1 = ALL, decimal value converted from a reverse binary representation of the sensors to load, 0 = none </li>
	 * </ul>
	 * 
	 * @param context
	 * @return int[] :  0 - recordingFormat,  
	 * 					1 - mp3kbps,
	 * 					2 - audioSource, 
	 * 					3 - sampleRateHz, 
	 * 					4 - channelType, 
	 * 					5 - encodingFormat,
	 * 					6 - sensorsDelay,
	 * 					7 - sensors 
	 */
	public static int[] getSharedPreferences(Context context) {
	
		int[] preferences = new int[8];
		
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SETTINGS_PREFERENCES_NAME, Context.MODE_PRIVATE);
		
		preferences[0] = sharedPreferences.getInt(recordingFormatPreference_name, 0);
		preferences[1] = sharedPreferences.getInt(mp3kbpsPreference_name, 32); 
		
		preferences[2] = sharedPreferences.getInt(audioSourcePreference_name, MediaRecorder.AudioSource.DEFAULT);
		preferences[3] = sharedPreferences.getInt(sampleRateHzPreference_name, 8000);
		preferences[4] = sharedPreferences.getInt(channelTypePreference_name, AudioFormat.CHANNEL_IN_DEFAULT);
		preferences[5] = sharedPreferences.getInt(encodingFormatPreference_name, AudioFormat.ENCODING_DEFAULT);
		
		preferences[6] = sharedPreferences.getInt(sensorsDelayPreference_name, SensorManager.SENSOR_DELAY_NORMAL);
		preferences[7] = sharedPreferences.getInt(sensorsPreferences_name, 0); 
		
		return preferences;
	}
	
}
