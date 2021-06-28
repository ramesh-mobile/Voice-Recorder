package com.avatar.voicerecorder.voice_recorder;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.avatar.voicerecorder.R;
import com.avatar.voicerecorder.RecorderApplication;
import com.avatar.voicerecorder.omrecorder.AudioChunk;
import com.avatar.voicerecorder.omrecorder.OmRecorder;
import com.avatar.voicerecorder.omrecorder.PullTransport;
import com.avatar.voicerecorder.omrecorder.Recorder;
import com.avatar.voicerecorder.utils.ParamsUtils;
import com.avatar.voicerecorder.utils.ViewUtils;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/*import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;*/


public class AudioRecorderActivity extends AppCompatActivity
        implements PullTransport.OnAudioChunkPulledListener, MediaPlayer.OnCompletionListener {

    private String filePath;
    private AudioSourceEnum source;
    private AudioChannel channel;
    private AudioSampleRate sampleRate;
    private int color;
    private boolean autoStart;
    private boolean keepDisplayOn;

    private MediaPlayer player;
    private Recorder recorder;
    private VisualizerHandler visualizerHandler;

    private Timer timer;
    private MenuItem saveMenuItem;
    private int recorderSecondsElapsed;
    private int playerSecondsElapsed;
    private boolean isRecording;

    private RelativeLayout contentLayout;
    private GLAudioVisualizationView visualizerView;
    private TextView statusView;
    private TextView timerView;
    private ImageButton restartView;
    private ImageButton recordView;
    private ImageButton playView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aar_activity_audio_recorder);

        RecorderApplication.currActivityInstance = this;
        if(savedInstanceState != null) {
            filePath = savedInstanceState.getString(ParamsUtils.EXTRA_FILE_PATH);
            source = (AudioSourceEnum) savedInstanceState.getSerializable(ParamsUtils.EXTRA_SOURCE);
            channel = (AudioChannel) savedInstanceState.getSerializable(ParamsUtils.EXTRA_CHANNEL);
            sampleRate = (AudioSampleRate) savedInstanceState.getSerializable(ParamsUtils.EXTRA_SAMPLE_RATE);
            color = savedInstanceState.getInt(ParamsUtils.EXTRA_COLOR);
            autoStart = savedInstanceState.getBoolean(ParamsUtils.EXTRA_AUTO_START);
            keepDisplayOn = savedInstanceState.getBoolean(ParamsUtils.EXTRA_KEEP_DISPLAY_ON);
        } else {
            filePath = getIntent().getStringExtra(ParamsUtils.EXTRA_FILE_PATH);
            source = (AudioSourceEnum) getIntent().getSerializableExtra(ParamsUtils.EXTRA_SOURCE);
            channel = (AudioChannel) getIntent().getSerializableExtra(ParamsUtils.EXTRA_CHANNEL);
            sampleRate = (AudioSampleRate) getIntent().getSerializableExtra(ParamsUtils.EXTRA_SAMPLE_RATE);
            color = getIntent().getIntExtra(ParamsUtils.EXTRA_COLOR, Color.BLACK);
            autoStart = getIntent().getBooleanExtra(ParamsUtils.EXTRA_AUTO_START, false);
            keepDisplayOn = getIntent().getBooleanExtra(ParamsUtils.EXTRA_KEEP_DISPLAY_ON, false);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Make to run your application only in portrait mode

        if(keepDisplayOn){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(Util.getDarkerColor(color)));
            getSupportActionBar().setHomeAsUpIndicator(
                    ContextCompat.getDrawable(this, R.drawable.aar_ic_clear));
                    //ContextCompat.getDrawable(this, R.drawable.ic_delete_blue_24dp));
        }

        visualizerView = new GLAudioVisualizationView.Builder(this)
                .setLayersCount(1)
                .setWavesCount(6)
                .setWavesHeight(R.dimen.aar_wave_height)
                .setWavesFooterHeight(R.dimen.aar_footer_height)
                .setBubblesPerLayer(20)
                .setBubblesSize(R.dimen.aar_bubble_size)
                .setBubblesRandomizeSize(true)
                .setBackgroundColor(Util.getDarkerColor(color))
                .setLayerColors(new int[]{color})
                .build();

        contentLayout = (RelativeLayout) findViewById(R.id.content);
        statusView = (TextView) findViewById(R.id.status);
        timerView = (TextView) findViewById(R.id.timer);
        restartView = (ImageButton) findViewById(R.id.restart);
        recordView = (ImageButton) findViewById(R.id.record);
        playView = (ImageButton) findViewById(R.id.play);

        //contentLayout.setBackgroundColor(Util.getDarkerColor(color));
        contentLayout.setBackground(getDrawable(R.drawable.toolbar_background_gradient));
        contentLayout.addView(visualizerView, 0);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);

        if(Util.isBrightColor(color)) {
            ContextCompat.getDrawable(this, R.drawable.aar_ic_clear)
            //ContextCompat.getDrawable(this, R.drawable.ic_delete_blue_24dp)
                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            ContextCompat.getDrawable(this, R.drawable.aar_ic_check)
            //ContextCompat.getDrawable(this, R.drawable.ic_delete_blue_24dp)
                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            statusView.setTextColor(Color.BLACK);
            timerView.setTextColor(Color.BLACK);
            restartView.setColorFilter(Color.BLACK);
            recordView.setColorFilter(Color.BLACK);
            playView.setColorFilter(Color.BLACK);
        }

//        if (!checkUserPermission()) {
//            requestPermission();
//
//        }else{
//            initiateRecordingTask(savedInstanceState);
//        }


        TelephonyManager telephonyManager =
                (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new AvatarPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(autoStart && !isRecording){
            toggleRecording(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //toggleRecording(null);
        try {
            visualizerView.onResume();
        } catch (Exception e){ }
    }

    @Override
    protected void onPause() {

        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    pauseRecording();
                }
            }
        });
        try {
            visualizerView.onPause();
        } catch (Exception e){ }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        restartRecording(null);
        setResult(RESULT_CANCELED);
        try {
            visualizerView.release();
        } catch (Exception e){ }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ParamsUtils.EXTRA_FILE_PATH, filePath);
        outState.putInt(ParamsUtils.EXTRA_COLOR, color);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.aar_audio_recorder, menu);
        saveMenuItem = menu.findItem(R.id.action_save);
        saveMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.aar_ic_check));
        //saveMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_delete_blue_24dp));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {

         //   Toast.makeText(this, "You are cross", Toast.LENGTH_SHORT).show();
        if(isRecording) {
            final AlertDialog.Builder builder = ViewUtils.showConfirmationBox(this, "Discard", "Are you sure?");
            builder.setNegativeButton("No", (dialogInterface, i1) -> {})
                    .setPositiveButton("Yes", (dialogInterface, i1) -> finish()).create().show();
        }
        else
            finish();

        } else if (i == R.id.action_save) {
            try {
                selectAudio();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAudioChunkPulled(AudioChunk audioChunk) {
        float amplitude = isRecording ? (float) audioChunk.maxAmplitude() : 0f;
        visualizerHandler.onDataReceived(amplitude);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopPlaying();
    }

    private void selectAudio() throws IOException {
        stopRecording();
        setResult(RESULT_OK);
        finish();
    }

    public void toggleRecording(View v) {
        if (!checkUserPermission()) {
            requestPermission();

        }else{
            initiateRecordingTask();

        }
    }


    public void togglePlaying(View v){
        pauseRecording();
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if(isPlaying()){
                    stopPlaying();
                } else {
                    startPlaying();
                }
            }
        });
    }

    private boolean checkUserPermission() {
        int storagePer = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int phnPer = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int audPer = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (storagePer == PackageManager.PERMISSION_GRANTED  && phnPer == PackageManager.PERMISSION_GRANTED
                && audPer == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ViewUtils.snackForPermission(this,playView);
            return false;
        }
    }

    private static final int PERMISSION_REQUEST_CODE = 1;
    private void requestPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0){

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        initiateRecordingTask();
                    }
                    else {
                        Toast.makeText(this, "Please enable permission!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:{}
        }
    }

    private void initiateRecordingTask() {
        stopPlaying();
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    pauseRecording();
                } else {
                    resumeRecording();
                }
            }
        });
    }


    public void restartRecording(View v){
        if(isRecording) {
            try {
                stopRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(isPlaying()) {
            stopPlaying();
        } else {
            visualizerHandler = new VisualizerHandler();
            visualizerView.linkTo(visualizerHandler);
            visualizerView.release();
            if(visualizerHandler != null) {
                visualizerHandler.stop();
            }
        }
        if(saveMenuItem!=null)
        saveMenuItem.setVisible(false);
        statusView.setVisibility(View.INVISIBLE);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_rec);
        timerView.setText("00:00:00");
        recorderSecondsElapsed = 0;
        playerSecondsElapsed = 0;
    }

    private void resumeRecording() {
        isRecording = true;
        if(saveMenuItem!=null)
        saveMenuItem.setVisible(false);
        statusView.setText(R.string.aar_recording);
        statusView.setVisibility(View.VISIBLE);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_pause);
        playView.setImageResource(R.drawable.aar_ic_play);

        visualizerHandler = new VisualizerHandler();
        visualizerView.linkTo(visualizerHandler);

        if(recorder == null) {
            timerView.setText("00:00:00");
            recorder = OmRecorder.wav(
                    new PullTransport.Default(Util.getMic(source, channel, sampleRate), AudioRecorderActivity.this),
                    new File(filePath));
        }
        recorder.resumeRecording();

        startTimer();
    }

    private void pauseRecording() {
        isRecording = false;
        if(!isFinishing()) {
            saveMenuItem.setVisible(true);
        }
        statusView.setText(R.string.aar_paused);
        statusView.setVisibility(View.VISIBLE);
        restartView.setVisibility(View.VISIBLE);
        playView.setVisibility(View.VISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_rec);
        playView.setImageResource(R.drawable.aar_ic_play);

        visualizerView.release();
        if(visualizerHandler != null) {
            visualizerHandler.stop();
        }

        if (recorder != null) {
            recorder.pauseRecording();
        }

        stopTimer();
    }

    private void stopRecording() throws IOException {
        visualizerView.release();
        if(visualizerHandler != null) {
            visualizerHandler.stop();
        }

        recorderSecondsElapsed = 0;
        if (recorder != null) {
            recorder.stopRecording();
            recorder = null;
        }

        stopTimer();
    }

    private void startPlaying(){
        try {
            stopRecording();
            player = new MediaPlayer();
            player.setDataSource(filePath);
            player.prepare();
            player.start();

            visualizerView.linkTo(DbmHandler.Factory.newVisualizerHandler(this, player));
            visualizerView.post(new Runnable() {
                @Override
                public void run() {
                    player.setOnCompletionListener(AudioRecorderActivity.this);
                }
            });

            timerView.setText("00:00:00");
            statusView.setText(R.string.aar_playing);
            statusView.setVisibility(View.VISIBLE);
            playView.setImageResource(R.drawable.aar_ic_stop);

            playerSecondsElapsed = 0;
            startTimer();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void stopPlaying(){
        statusView.setText("");
        statusView.setVisibility(View.INVISIBLE);
        playView.setImageResource(R.drawable.aar_ic_play);

        visualizerView.release();
        if(visualizerHandler != null) {
            visualizerHandler.stop();
        }

        if(player != null){
            try {
                player.stop();
                player.reset();
            } catch (Exception e){ }
        }

        stopTimer();
    }

    private boolean isPlaying(){
        try {
            return player != null && player.isPlaying() && !isRecording;
        } catch (Exception e){
            return false;
        }
    }

    private void startTimer(){
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void updateTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isRecording) {
                    recorderSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(recorderSecondsElapsed));
                } else if(isPlaying()){
                    playerSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(playerSecondsElapsed));
                }
            }
        });
    }


    class AvatarPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber)
        {
            if(state== TelephonyManager.CALL_STATE_RINGING){
                //pauseRecording();
                Toast.makeText(getApplicationContext(),"Phone Is Riging",
                        Toast.LENGTH_LONG).show();
            }
            if(state== TelephonyManager.CALL_STATE_OFFHOOK){
                Toast.makeText(getApplicationContext(),"Phone is Currently in A call",
                        Toast.LENGTH_LONG).show();
            }

            if(state== TelephonyManager.CALL_STATE_IDLE){
                Toast.makeText(getApplicationContext(),"phone is neither ringing nor in a call",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed(){
      if(isRecording) {
            //final AlertDialog.Builder builder =
                    ViewUtils.showConfirmationBox(this, "Discard", "Are you sure?")
                    .setNegativeButton("No", (dialogInterface, i) -> {})
                    .setPositiveButton("Yes", (dialogInterface, i) ->
                    AudioRecorderActivity.super.onBackPressed()).create().show();
        }
        else
            super.onBackPressed();

    }
}
