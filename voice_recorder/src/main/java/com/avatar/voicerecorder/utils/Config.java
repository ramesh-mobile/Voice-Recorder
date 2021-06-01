package com.avatar.voicerecorder.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Shyam on 3/23/2018.
 */

public class Config {
    public static final String APP_FOLDER = "Avatar Inspection";

    public static final String APP_INSIDE_FOLDER_FOR_AUDIO_SCOPED_STORAGE = Environment.getExternalStorageDirectory()+ File.separator+Environment.DIRECTORY_MUSIC + File.separator + APP_FOLDER+File.separator;

    //PERMISSION CONSTANTS
    public static final int PERMISSION_REQUEST_CODE = 1;

    //FILE EXTENSIONS
    public static final String JPEG = ".jpg";
    public static final String WAV = ".wav";
    public static final String AUDIO_MIME_TYPE = "audio/wav";
    public static final String IMAGE_MIME_TYPE = "image/jpg";
    public static final String PDF_MIME_TYPE = "application/pdf";
}