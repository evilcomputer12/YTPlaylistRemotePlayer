package com.martin.androidwebplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.io.IOException;

public class MainActivity extends YouTubeBaseActivity {

    AndroidWebServer aws;
    DatabaseManager dbManager;
    YouTubePlayer youTubePlayer;
    //private MyReceiver receiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter("com.martin.poraka");
        //MyReceiver objReceiver = new MyReceiver();
        PowerManager powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.acquire();
        //registerReceiver(objReceiver, intentFilter);
        aws = new AndroidWebServer(8180, this);
        dbManager = new DatabaseManager(this);
        try{
            dbManager.open();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        Cursor cursor = dbManager.fetch();
        String link = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LINK));
        youtube(link);

        dbManager.close();
    }



    public void youtube(String link){
        YouTubePlayerView ytPlayer = (YouTubePlayerView) findViewById(R.id.ytPlayer);
        String api_key = "AIzaSyCAynRTWn9MaLkH9ACTIvOLoH1Zi0SXJFQ";
        ytPlayer.initialize(
                api_key,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(
                            YouTubePlayer.Provider provider,
                            YouTubePlayer youTubePlayer, boolean b) {
                        if(link.length() > 12 && link.length() <= 35) {
                                youTubePlayer.cuePlaylist(link);
                                youTubePlayer.setFullscreen(true);
                            }
                            else {
                                youTubePlayer.loadVideo(link);
                                youTubePlayer.setFullscreen(true);
                                //youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                                //youTubePlayer.play();
                            }
                            youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                                @Override
                                public void onLoading() {

                                }

                                @Override
                                public void onLoaded(String s) {
                                    youTubePlayer.play();
                                }

                                @Override
                                public void onAdStarted() {
                                    youTubePlayer.pause();

                                }

                                @Override
                                public void onVideoStarted() {

                                }

                                @Override
                                public void onVideoEnded() {
                                    if (youTubePlayer.hasNext()) {
                                        youTubePlayer.next();
                                    }

                                }

                                @Override
                                public void onError(YouTubePlayer.ErrorReason errorReason) {

                                }
                            });
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult
                                                                youTubeInitializationResult) {
                    }
                });
    }




    public class MyReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(MainActivity.this,MainActivity.class);
            context.startActivity(intent1);
            finish();
        }

    }



}