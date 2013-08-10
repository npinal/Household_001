package com.dmc.camera.assist;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class ACVoicePlayer {
	private static final String TAG = "ACVoicePlayer";

	private static ACVoicePlayer Instance = null;
	private static MediaRecorder recorder = null;
	private static MediaPlayer player = null;

	public static ACVoicePlayer GetInstance() {
		if (Instance == null) {
			Instance = new ACVoicePlayer();
		}
		return Instance;
	}

	private ACVoicePlayer() {
		File file = Environment.getRootDirectory();
		// filePath = file.getAbsolutePath() +
		// String.format("/myvoice0001.mp4");
	}

	// 재생
	private static void _playRec(String filePath) {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}

		try {
			player = new MediaPlayer();
			player.setDataSource(filePath);
			player.prepare();
			player.start();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 녹음 중지
	private static void _stopRec() {
		if (recorder == null) {
			return;
		}

		recorder.stop();
		recorder.release();
		recorder = null;
	}

	// 재생 중지
	private static void _stop() {
		if (player == null) {
			return;
		}

		player.stop();
		player.release();
		player = null;
	}

	// 녹음
	private static void _record(String filePath) {
		if (recorder != null) {
			recorder.stop();
			recorder.release();
			recorder = null;
		}

		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		recorder.setOutputFile(filePath);

		try {
			recorder.prepare();
			recorder.start();
			Log.i(TAG, "recording start");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 재생
	public void PlayRec(String filePath) {
		Log.e(TAG, "PlayRec!!");
		_playRec(filePath);
	}

	// 녹음 중지
	public void StopRec() {
		Log.e(TAG, "StopRec!!");
		_stopRec();
	}

	// 재생 중지
	public void Stop() {
		Log.e(TAG, "Stop!!");
		_stop();
	}

	// 녹음
	public void Record(String filePath) {
		Log.e(TAG, "Record!!");
		_record(filePath);
	}
}
