package com.buihha.audiorecorder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

public class Mp3Recorder {
	
	private static final String TAG = Mp3Recorder.class.getSimpleName();
	
	private String recordFolderPath;
	
	static {
		System.loadLibrary("mp3lame");
	}

//	private static final int DEFAULT_SAMPLING_RATE = 22050;
	private static final int DEFAULT_SAMPLING_RATE = 44100;
	
	private static final int FRAME_COUNT = 160;

	private AudioRecord audioRecord = null;

	private int bufferSize;

	private File mp3File;
	
	private RingBuffer ringBuffer;
	
	private byte[] buffer;

	private FileOutputStream os = null;

	private DataEncodeThread encodeThread;
	
	/* Encoded bit rate in kbps */
	private int mp3kbps;
	
	private int audioSource;

	private int samplingRate;

	private int channelConfig;

	private int encodingFormat;
	
	private boolean isRecording = false;

	/**
	 * Initialize the recorder
	 * @param recordFolderPath
	 * @param mp3kbps
	 * @param audioSource
	 * @param samplingRate
	 * @param channelConfig
	 * @param audioFormat
	 */
	public Mp3Recorder(String recordFolderPath,
			int mp3kbps,
			int audioSource, 
			int samplingRate, 
			int channelConfig,
			int audioFormat) {
		this.recordFolderPath = recordFolderPath;
		this.mp3kbps = mp3kbps;
		this.audioSource = audioSource;
		this.samplingRate = samplingRate;
		this.channelConfig = channelConfig;
		this.encodingFormat = audioFormat;
	}

	/**
	 * Default constructor. Setup mp3Recorder with default sampling rate 1 channel,
	 * 16 bits pcm
	 */
	public Mp3Recorder() {
		this(Environment.getExternalStorageDirectory().getAbsolutePath(),
				32, // 32 kbps
				MediaRecorder.AudioSource.MIC,
				DEFAULT_SAMPLING_RATE, 
				AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
	}

	/**
	 * Start recording. Create an encoding thread. Start record from this
	 * thread.
	 * 
	 * @throws IOException
	 */
	public void startRecording() throws IOException {
		if (isRecording) return;
		Log.d(TAG, "Start recording");
		Log.d(TAG, "BufferSize = " + bufferSize);
		// Initialize audioRecord if it's null.
		if (audioRecord == null) {
			initAudioRecorder();
		}
		audioRecord.startRecording();
		
		new Thread() {

			@Override
			public void run() {
				isRecording = true;
				while (isRecording) {
					int bytes = audioRecord.read(buffer, 0, bufferSize);
					if (bytes > 0) {
						ringBuffer.write(buffer, bytes);
					}
				}
				
				// release and finalize audioRecord
				try {
					audioRecord.stop();
					audioRecord.release();
					audioRecord = null;

					// stop the encoding thread and try to wait
					// until the thread finishes its job
					Message msg = Message.obtain(encodeThread.getHandler(),
							DataEncodeThread.PROCESS_STOP);
					msg.sendToTarget();
					
					Log.d(TAG, "waiting for encoding thread");
					encodeThread.join();
					Log.d(TAG, "done encoding thread");
				} catch (InterruptedException e) {
					Log.d(TAG, "Faile to join encode thread");
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}			
		}.start();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void stopRecording() throws IOException {
		Log.d(TAG, "stop recording");
		isRecording = false;
	}

	/**
	 * Initialize audio mp3Recorder
	 */
	private void initAudioRecorder() throws IOException {
		int bytesPerFrame;
		if (encodingFormat == AudioFormat.ENCODING_PCM_16BIT) {
			bytesPerFrame = 2;
		} else {
			bytesPerFrame = 1;
		}
		/* Get number of samples. Calculate the buffer size (round up to the
		   factor of given frame size) */
		int frameSize = AudioRecord.getMinBufferSize(samplingRate,
				channelConfig, encodingFormat) / bytesPerFrame;
		if (frameSize % FRAME_COUNT != 0) {
			frameSize = frameSize + (FRAME_COUNT - frameSize % FRAME_COUNT);
			Log.d(TAG, "Frame size: " + frameSize);
		}
		
		bufferSize = frameSize * bytesPerFrame;

		/* Setup audio mp3Recorder */
		audioRecord = new AudioRecord(audioSource,
				samplingRate, channelConfig, encodingFormat,
				bufferSize);
		
		// Setup RingBuffer. Currently is 10 times size of hardware buffer
		// Initialize buffer to hold data
		ringBuffer = new RingBuffer(10 * bufferSize);
		buffer = new byte[bufferSize];
		
		// Initialize lame buffer
		// mp3 sampling rate is the same as the recorded pcm sampling rate 
		// The bit rate is 32kbps
		SimpleLame.init(samplingRate, 1, samplingRate, mp3kbps);
		
		// Initialize the place to put mp3 file
//		String externalPath = Environment.getExternalStorageDirectory()
//				.getAbsolutePath();
		File directory = new File(recordFolderPath);
		if (!directory.exists()) {
			directory.mkdirs();
			Log.d(TAG, "Created directory");
		}
		mp3File = new File(directory, "recording.mp3");
		os = new FileOutputStream(mp3File);

		// Create and run thread used to encode data
		// The thread will 
		encodeThread = new DataEncodeThread(ringBuffer, os, bufferSize);
		encodeThread.start();
		audioRecord.setRecordPositionUpdateListener(encodeThread, encodeThread.getHandler());
		audioRecord.setPositionNotificationPeriod(FRAME_COUNT);
	}
}