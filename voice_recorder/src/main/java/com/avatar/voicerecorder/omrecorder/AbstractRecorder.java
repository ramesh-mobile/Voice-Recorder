package com.avatar.voicerecorder.omrecorder;

import com.avatar.voicerecorder.utils.Config;
import com.avatar.voicerecorder.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Kailash Dabhi on 22-08-2016.
 * You can contact us at kailash09dabhi@gmail.com OR on skype(kailash.09)
 * Copyright (c) 2016 Kingbull Technology. All rights reserved.
 */
abstract class AbstractRecorder implements Recorder {
  protected final PullTransport pullTransport;
  protected final File file;
  private final OutputStream outputStream;

  protected AbstractRecorder(PullTransport pullTransport, File file) {
    this.pullTransport = pullTransport;
    this.file = file;
    this.outputStream = outputStream(file);
  }

  @Override public void startRecording() {
    new Thread(new Runnable() {
      @Override public void run() {
        try {
          pullTransport.start(outputStream);
        } catch (IOException e) {
          new RuntimeException(e);
        }
      }
    }).start();
  }

  private static final String TAG = "AbstractRecorder";

  private OutputStream outputStream(File file) {
    if (file == null) throw new RuntimeException("file is null !");
    OutputStream outputStream = null;
    outputStream = FileUtils.getOutPutStream(Config.AUDIO_MIME_TYPE, file);
    try {

      //outputStream = new FileOutputStream(file);logp
      //contentValues.clear();
    } catch (Exception e){e.printStackTrace();}
    return outputStream;
  }

  @Override public void stopRecording() {
    pullTransport.stop();
  }

  @Override public void pauseRecording() {
    pullTransport.source().isEnableToBePulled(false);
  }

  @Override public void resumeRecording() {
    pullTransport.source().isEnableToBePulled(true);
    startRecording();
  }
}
