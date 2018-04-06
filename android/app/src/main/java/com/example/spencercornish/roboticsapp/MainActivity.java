package com.example.spencercornish.roboticsapp;

import java.net.Socket;
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
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et=findViewById(R.id.ttsInput);
        ip=findViewById(R.id.IpBox);

        speechResult=findViewById(R.id.textView2);

        tts=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                }
                else
                    Log.e("error", "Init Failed!");
            }
        });



    }

    public void onSpeakClick(View v){
        if(tts == null) {
            System.out.println("bork");
        }
        TextTS speak = new TextTS(tts, et.getText().toString());
        speak.start();
    }

    public void onListenClick(View v){
        promptSpeechInput();
    }

    public void onConnectClick(View v) {
        Net net = new Net(8082, ip.getText().toString(), 8081 );
        net.start();
        net.sendData("Hello");

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
                    // We get speech text here, send it to the PI?
                }
                break;
            }

        }
    }


}