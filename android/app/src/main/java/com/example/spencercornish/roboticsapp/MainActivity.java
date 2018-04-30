package com.example.spencercornish.roboticsapp;

import java.net.Socket;
import java.net.SocketAddress;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    String text;
    EditText et;
    EditText ip;
    TextView speechResult;
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip=findViewById(R.id.IpBox);

        speechResult=findViewById(R.id.textView2);



    }



    public void onSendCLick(View v) {
        NetReply replyThread = new NetReply(socket, this, "Hello from 80");
        replyThread.start();
    }

    public void onConnectClick(View v) {
        String ipInput = ip.getText().toString();

        Intent myIntent = new Intent(this, MoveActivity.class);
        startActivity(myIntent);


    }
}