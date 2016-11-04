package com.example.salal.voicenote;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.example.salal.voicenote.R.id.btnplay;

public class MainActivity extends AppCompatActivity {
    Button play,stop,record;
    private MediaRecorder myAudioRecorder;
    private AudioManager _audioManager;
    private String outputFile = null;
    ImageView ivspeak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        record=(Button)findViewById(R.id.btnstartrecoding);
        stop=(Button)findViewById(R.id.btnstoprecoding);
        play=(Button)findViewById(btnplay);
        ivspeak =(ImageView)findViewById(R.id.iv_speak);

        final Chronometer myChronometer = (Chronometer)findViewById(R.id.chronometer3);
        stop.setEnabled(false);
        play.setEnabled(true);






        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VoiceRecord";
                    File path = new File(outputFile);
                    if (!path.exists())
                        path.mkdirs();

                    //Log.w("BluetoothReceiver.java | startRecord", "|" + path.toString() + "|");

                    File file = null;
                    try
                    {
                        file = File.createTempFile("voice_", ".mp3", path);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    myAudioRecorder=new MediaRecorder();
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(file.toString());
                   myChronometer.setBase(SystemClock.elapsedRealtime());

                    myChronometer.start();
                   ivspeak.setImageResource(R.drawable.presence_audio_busy);
                    _audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    _audioManager.startBluetoothSco();
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                    stop.setEnabled(true);

                }

                catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                record.setEnabled(false);
                stop.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder  = null;
                ivspeak.setImageResource(R.drawable.ic_btn_speak_now);
                myChronometer.stop();
                stop.setEnabled(false);
                record.setEnabled(true);
                play.setEnabled(true);
                _audioManager.stopBluetoothSco();
                Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {

                Intent intent =new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(intent);
            }
        });
    }

}



