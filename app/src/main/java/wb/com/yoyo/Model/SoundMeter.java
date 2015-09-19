package wb.com.yoyo.Model;



import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public  class SoundMeter {
	static final private double EMA_FILTER = 0.6;

	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;

	public String start(String name) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}
		String path=null;
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//			File file;
//			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
//			 	file=new File(Environment.getExternalStorageDirectory()+"/yoyo/voice/");
//			else {
//				file=new File("/data/data/wb.com.yoyo/files/yoyo/voice/");
//				System.out.println(file);
//			}
			File file=new File(android.os.Environment.getExternalStorageDirectory()+"/yoyo/voice/");
			if(!file.exists()){
				file.mkdirs();
			}
			mRecorder.setOutputFile(new File(file,name).toString());
			System.out.println(file);
			if(!file.exists()){
				file.mkdirs();
			}
			path=new File(file,name).toString();
			mRecorder.setOutputFile(path);
			try {
				mRecorder.prepare();
				mRecorder.start();
				mEMA = 0.0;
				return path;
			} catch (IllegalStateException e) {
				System.out.print(e.getMessage());
			} catch (IOException e) {
				System.out.print(e.getMessage());
			}

		}
		return path;
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}
}
