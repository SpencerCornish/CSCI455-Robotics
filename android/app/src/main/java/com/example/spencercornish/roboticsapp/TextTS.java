package com.example.spencercornish.roboticsapp;

import android.util.Log;

import java.util.Locale;
import java.util.Locale;

import android.os.HandlerThread;
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


public class TextTS extends Thread {

    Thread t;
    TextToSpeech tts;
    String whatToSay;

    TextTS(TextToSpeech tts, String whatToSay) {
        this.tts = tts;
        this.whatToSay = whatToSay;
    }

    public void run() {
        convertTextToSpeech();
    }

    public void start () {
        System.out.println("Starting " + whatToSay);
        if (t == null) {
            t = new Thread(this, whatToSay);
            t.start();
        }
    }

    private void convertTextToSpeech() {
        if(whatToSay!=null||"".equals(whatToSay))
        {
            tts.speak(whatToSay, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


}
