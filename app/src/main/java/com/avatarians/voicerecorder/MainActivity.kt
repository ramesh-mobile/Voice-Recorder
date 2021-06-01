package com.avatarians.voicerecorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        redirectToVoiceRecorder()
    }

    fun redirectToVoiceRecorder() {
        /*var voiceFilePath = com.avatar.voicerecorder.utils.FileUtils.generateFilePathWithTimeStamp(com.avatar.voicerecorder.utils.Config.WAV)
        com.avatar.voicerecorder.utils.ViewUtils.print(RecorderMainActivity.TAG, "redirectToVoiceRecorder: file path :\$voiceFilePath")
        val color = resources.getColor(R.color.green)
        val requestCode = 0
        AndroidAudioRecorder.with(this) // Required
                .setFilePath(voiceFilePath)
                .setColor(color)
                .setRequestCode(requestCode) // Optional
                .setSource(AudioSourceEnum.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_8000)
                .setAutoStart(false)
                .setKeepDisplayOn(true) // Start recording
                .record()*/
    }
}