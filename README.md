# Voice-Recorder
For implement  - implementation "com.github.ramesh-mobile.Voice-Recorder:voice_recorder:tag"

AndroidAudioRecorder.with(this) // Required
                .setFilePath(voiceFilePath) // optional
                .setColor(color)
                .setRequestCode(requestCode) // Optional
                .setSource(AudioSourceEnum.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_8000)
                .setAutoStart(false)
                .setKeepDisplayOn(true) // Start recording
                .record()
