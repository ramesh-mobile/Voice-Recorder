package com.avatar.voicerecorder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.avatar.voicerecorder.utils.Config;
import com.avatar.voicerecorder.utils.FileUtils;
import com.avatar.voicerecorder.utils.ViewUtils;
import com.avatar.voicerecorder.voice_recorder.AndroidAudioRecorder;
import com.avatar.voicerecorder.voice_recorder.AudioChannel;
import com.avatar.voicerecorder.voice_recorder.AudioSampleRate;
import com.avatar.voicerecorder.voice_recorder.AudioSourceEnum;

/**
 * Created by ramesh on 03-05-21
 */
public class RecorderMainActivity extends AppCompatActivity {

    private static final String TAG = "RecorderMainActivity";

    String voiceFilePath = null;
    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redirectToVoiceRecorder();
    }
    public void redirectToVoiceRecorder() {
        voiceFilePath = FileUtils.generateFilePathWithTimeStamp(Config.WAV);
        ViewUtils.print(TAG, "redirectToVoiceRecorder: file path :$voiceFilePath");
        int color = getResources().getColor(R.color.green);
        int requestCode = 0;
        AndroidAudioRecorder.with(this) // Required
                .setFilePath(voiceFilePath)
                .setColor(color)
                .setRequestCode(requestCode) // Optional
                .setSource(AudioSourceEnum.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_8000)
                .setAutoStart(false)
                .setKeepDisplayOn(true) // Start recording
                .record();
    }
}
