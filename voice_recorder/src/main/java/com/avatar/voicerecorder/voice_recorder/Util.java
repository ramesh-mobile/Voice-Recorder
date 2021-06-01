package com.avatar.voicerecorder.voice_recorder;

import android.graphics.Color;
import android.media.AudioFormat;
import android.os.Handler;

import com.avatar.voicerecorder.omrecorder.AudioSource;
//import com.avatarins.avatarclaiminspection.ui.home.home_tab.field_home.field_voice.recordvoice.model.AudioSource;


public class Util {
    private static final Handler HANDLER = new Handler();

    private Util() {
    }

    public static void wait(int millis, Runnable callback) {
        HANDLER.postDelayed(callback, millis);
    }

    public static AudioSource getMic(AudioSourceEnum source,
                                     AudioChannel channel,
                                     AudioSampleRate sampleRate) {
        return new AudioSource.Smart(
                source.getSource(),
                AudioFormat.ENCODING_PCM_16BIT,
                channel.getChannel(),
                sampleRate.getSampleRate());
    }

    public static boolean isBrightColor(int color) {
        if (android.R.color.transparent == color) {
            return true;
        }
        int[] rgb = {Color.red(color), Color.green(color), Color.blue(color)};
        int brightness = (int) Math.sqrt(
                rgb[0] * rgb[0] * 0.241 +
                        rgb[1] * rgb[1] * 0.691 +
                        rgb[2] * rgb[2] * 0.068);
        return brightness >= 200;
    }

    public static int getDarkerColor(int color) {
        float factor = 0.8f;
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb(a,
                Math.max((int) (r * factor), 0),
                Math.max((int) (g * factor), 0),
                Math.max((int) (b * factor), 0));
    }

    public static String formatSeconds(int seconds) {
        return getTwoDecimalsValue(seconds / 3600) + ":"
                + getTwoDecimalsValue(seconds / 60) + ":"
                + getTwoDecimalsValue(seconds % 60);
    }

    private static String getTwoDecimalsValue(int value) {
        if (value >= 0 && value <= 9) {
            return "0" + value;
        } else {
            return value + "";
        }
    }

}