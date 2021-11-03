# Voice-Recorder
For implement  - implementation "com.github.ramesh-mobile.Voice-Recorder:voice_recorder:tag"

AndroidAudioRecorder.with(this) // Required<br>
								.setFilePath(voiceFilePath) // optional<br>
                .setColor(color)<br>
                .setRequestCode(requestCode) // Optional<br>
                .setSource(AudioSourceEnum.MIC)<br>
                .setChannel(AudioChannel.STEREO)<br>
                .setSampleRate(AudioSampleRate.HZ_8000)<br>
                .setAutoStart(false)<br>
                .setKeepDisplayOn(true) // Start recording<br>
                .record()
