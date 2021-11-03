/*
 * Copyright (C) 2016 Kailash Dabhi (Kingbull Technology)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avatar.voicerecorder.omrecorder;

import android.content.Context;

import java.io.File;

/**
 * Essential APIs for working with OmRecorder.
 *
 * @author Kailash Dabhi (kailash09dabhi@gmail.com)
 * @date 31-07-2016
 * @skype kailash.09
 */
public final class OmRecorder {
  private OmRecorder() {
  }

  public static Recorder pcm(Context context,PullTransport pullTransport, File file) {
    return new Pcm(context,pullTransport, file);
  }

  public static Recorder wav(Context context, PullTransport pullTransport, File file) {
    return new Wav(context,pullTransport, file);
  }

}
