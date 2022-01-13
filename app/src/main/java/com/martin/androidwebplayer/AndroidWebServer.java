package com.martin.androidwebplayer;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;

import fi.iki.elonen.NanoHTTPD;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class AndroidWebServer extends NanoHTTPD {

    DatabaseManager dbManager;

    private static final String TAG = "HTTPServer";
    private Context ctx;


    public AndroidWebServer(int port, Context ctx) {
        super(port);
        this.ctx = ctx;
        try {
            Log.d("TAG", "Starting web server..");
            start();
        }
        catch(IOException ioe) {
            Log.e(TAG, "Unable to start the server");
            ioe.printStackTrace();
        }

    }



    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> parms = session.getParms();
        String content = null;
        content = readFile().toString();
        if (session.getMethod() == Method.POST) {
            Map<String, String> files = new HashMap<String, String>();
            try {
                session.parseBody(files);
                session.getParms().get("fname");

                dbManager = new DatabaseManager(ctx.getApplicationContext());
                try{
                    dbManager.open();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                if(session.getParms().get("fname")!=null) {
                    dbManager.update(1, session.getParms().get("fname").toString());
                    ctx.startActivity(new Intent(ctx, MainActivity.class));
                }
//                Intent intent = new Intent();
//                intent.setAction("com.martin.poraka");
//                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                //ctx.sendBroadcast(intent);
                //ctx.startActivity(new Intent(ctx, MainActivity.class));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ResponseException e) {
                e.printStackTrace();
            }


            //youtube(String.valueOf(session.getParms().get("fname")));
            //return newFixedLengthResponse(link);


        }

        return newFixedLengthResponse(content );
    }

    private StringBuffer readFile() {
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        try {
            reader = new BufferedReader(
                    new InputStreamReader
                            (ctx.getAssets().open("index.html"), "UTF-8"));


            String mLine;
            while ((mLine = reader.readLine()) != null) {
                buffer.append(mLine);
                buffer.append("\n");
            }
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            if (reader != null)
                try {
                    reader.close();
                }
                catch (IOException ioe) {}
        }

        return buffer;
    }

    public void stopServer() {
        this.stop();
    }

    }
