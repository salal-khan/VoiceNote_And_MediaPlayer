package com.example.salal.voicenote;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    ListView lv;
    ImageButton btnplay,btnback,btnforward;
    TextView tv_totalmc;
    Button btn_recoder_back;
    String[] itemlist;
    int point_position;
    ArrayList<File> Al;
    MediaPlayer mp;
    SeekBar sek;
    Uri u;
    private double startTime = 0;
    private double finalTime = 0;
    public static int oneTimeOnly = 0;
    boolean playcondition=false;
    private Handler myHandler = new Handler();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        lv = (ListView)findViewById(R.id.recordinglist);
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VoiceRecord");
        final ArrayList<File> Songlist=findsongs(path);
        mp = new MediaPlayer();
        itemlist = new String[Songlist.size()];
        for(int i=0 ; i<Songlist.size();i++)
        {
            itemlist[i] = Songlist.get(i).getName();

        }

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.item,R.id.textView,itemlist);
        lv.setAdapter(adp);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  startActivity(new Intent(getApplicationContext(),player.class).putExtra("pos",position).putExtra("Songlist",Songlist));
                point_position =position;
                play();
                btnplay.setImageResource(R.drawable.ic_media_pauses);
                playcondition=true;
              //  Toast.makeText(getApplicationContext(),"pos"+point_position,Toast.LENGTH_SHORT).show();
            }
        });

        btnplay=(ImageButton)findViewById(R.id.btn_play);
        btnback=(ImageButton)findViewById(R.id.btn_back);
        btnforward=(ImageButton)findViewById(R.id.btn_forward);
        sek=(SeekBar)findViewById(R.id.seekBar2);
        btn_recoder_back=(Button)findViewById(R.id.btn_recoder_back);
        tv_totalmc =(TextView)findViewById(R.id.tv_totalmc);

        tv_totalmc.setText(point_position+"/"+ Al.size());

        btn_recoder_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


        sek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backforward();
            }
        });

        btnforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fastforward();
            }
        });


        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playcondition==false){
                    mp.start();
                    btnplay.setImageResource(R.drawable.ic_media_pauses);
                    playcondition=true;
                }
                else {
                    mp.pause();
                    btnplay.setImageResource(R.drawable.ic_media_play);
                    playcondition=false;
                }


            }
        });

    }

    public void backforward(){
        mp.stop();

        mp.release();

        int total =point_position;
        if(0==total){
            point_position=Al.size()-1;
        }
        else {
            point_position = point_position- 1;
        }
        u=Uri.parse(Al.get(point_position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);

        mp.start();
        sek.setMax(mp.getDuration());
    }

    public void fastforward(){
        mp.stop();

        mp.release();

        int total =point_position+1;
        if(Al.size()==total){
            point_position=0;
        }
        else {
            point_position = point_position + 1;
        }
        u=Uri.parse(Al.get(point_position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);

        mp.start();
        sek.setMax(mp.getDuration());
    }

    public void play()  {
        mp.stop();
        u= Uri.parse(Al.get(point_position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);

        mp.start();
        finalTime = mp.getDuration();
        startTime = mp.getCurrentPosition();

            sek.setMax((int) finalTime);


        sek.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
       // Toast.makeText(getApplicationContext(),"pos"+point_position+"total "+Al.size(),Toast.LENGTH_SHORT).show();
    }



    public ArrayList<File> findsongs(File path)
    {
         Al = new ArrayList<>();
        File[] files = path.listFiles();
        for(File EverySingleFile:files)
        {
            if(EverySingleFile.isDirectory())
            {
                Al.addAll(findsongs(EverySingleFile));

            }
            else
            {
                if (EverySingleFile.getName().endsWith(".mp3"))
                {
                    Al.add(EverySingleFile);
                }

            }
        }
        return Al;
    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mp.getCurrentPosition();

            sek.setProgress((int)startTime);
            if(finalTime==startTime){
                btnplay.setImageResource(R.drawable.ic_media_play);
                playcondition=false;
            }
            tv_totalmc.setText(point_position+1+"/"+ Al.size());
            myHandler.postDelayed(this, 100);
        }
    };
}
