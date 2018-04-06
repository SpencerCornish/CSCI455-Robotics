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
    TextToSpeech tts;
    TextView speechResult;
    Socket socket;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et=findViewById(R.id.ttsInput);
        ip=findViewById(R.id.IpBox);

        speechResult=findViewById(R.id.textView2);

//        tts=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//
//                if(status == TextToSpeech.SUCCESS){
//                    int result=tts.setLanguage(Locale.US);
//                }
//                else
//                    Log.e("error", "Init Failed!");
//            }
//        });



    }

    public void onSpeakClick(View v){
        if(tts == null) {
            System.out.println("bork");
        }
        TextTS speak = new TextTS(tts, et.getText().toString());
        speak.start();
    }

    public void onListenClick(View v) {
        promptSpeechInput();
    }

    public void onSendCLick(View v) {
        NetReply replyThread = new NetReply(socket, this, "Hello from 80");
        replyThread.start();
    }

    public void onConnectClick(View v) {

        try {
            Sock socketHandler = new Sock(ip.getText().toString(), 8091);
            socketHandler.start();

            while(true) {
                if(socketHandler.getSocket() != null) {
                    break;
                }
            }
            socket = socketHandler.getSocket();

        } catch (Exception e) {
            System.out.println(e);

        }

        Net listener = new Net(socket,8091,this);
        listener.start();



    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechResult.setText(result.get(0));
                    NetReply rep = new NetReply(socket, this, result.get(0));
                    rep.start();
                }
                break;
            }

        }
    }


}