package com.example.spencercornish.roboticsapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveActivity extends Activity implements RecognitionListener {

    Controller gameController;
    ImageView imageBox;

    TextView questionText;
    TextView optionsText;

    String questionContentText;
    List<String> optionsContentText;


    Button optionsDebug;
    Button questionDebug;
    Button imageDebug;

    private Intent speechIntent;
    private SpeechRecognizer speechRecognizer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        requestRecordAudioPermission();

        // Get view refs
        questionText = findViewById(R.id.questionText);
        optionsText = findViewById(R.id.optionsText);
        imageBox = findViewById(R.id.imageBox);

        optionsDebug = findViewById(R.id.optionsDebug);
        questionDebug = findViewById(R.id.questionDebug);
        imageDebug = findViewById(R.id.imageDebug2);


        // Reestablish strings and stuffs
        if (questionContentText == null) {
            questionContentText = "Loading question, please wait...";
        }
        setQuestionText(questionContentText);


        if (optionsContentText == null) {
            optionsContentText = Arrays.asList("loading...");
        }
        setOptionsText(optionsContentText);





        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        //gameController = new Controller(this);

        // In a thread:
        //gameController.begin();



    }

    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;

            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }






    public void onImageDebugClick(View v) {
        setImageBox(R.drawable.ss);
    }

    public void onOptionsDebugClick(View v) {
        setOptionsText(Arrays.asList("Option 1", "Option 2", "Option 3", "Option 4"));

    }

    public void onQuestionDebugCLick(View v) {
        setQuestionText("Hello world!!!!1");
    }


    public void onListen(View v) {
        startSpeechRecognition();
    }


    public void setImageBox(int resourceId) {
        if (imageBox == null) return;

        try {
            imageBox.setImageResource(resourceId);
        } catch (Exception e) {
            System.out.println("Error setting image: " + e.toString());
            e.printStackTrace();
        }
    }

    public void setQuestionText(String question) {
        if (questionText == null) return;

        try {
            questionText.setText(question);
        } catch (Exception e) {
            System.out.println("Error Setting questionText:" + e.toString());
            e.printStackTrace();
        }

    }

    public void startSpeechRecognition() {

        speechRecognizer.startListening(speechIntent);
    }

    public void setOptionsText(List<String> options) {
        // There can only be a max of four options
        if(options.size() > 4) return;
        // We must have initialized our variables
        if (optionsText == null) return;


        // Concatenate the strings so that they look good
        String newText = "";
        for(String option : options) {
            System.out.println(option);
            newText = newText.concat("- " + option + "\n");
        }
        try {
            System.out.println("Setting it!!!");
            optionsText.setText(newText);
        } catch (Exception e) {
            System.out.println("Error Setting optionsText:" + e.toString());
            e.printStackTrace();
        }
    }


    ///////////
    // Voice Recognition Callbacks
    ///////////


    @Override
    public void onReadyForSpeech(Bundle results) {
        Toast.makeText(getApplicationContext(),"onReadyForSpeech", Toast.LENGTH_SHORT);

    }

    @Override
    public void onBufferReceived(byte[] results) {
        String str = new String(results, StandardCharsets.UTF_8);

        System.out.println("onBuffer received:: " + str);


    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String spokenSentence = matches.get(0);

        Toast.makeText(getApplicationContext(), spokenSentence, Toast.LENGTH_SHORT);
        //gameController.recievedVoiceData(spokenSentence);
        // TODO
    }

    @Override
    public void onPartialResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String spokenSentence = matches.get(0);

        System.out.println("Partial: " + spokenSentence);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.v("onBeginningOfSpeech","Started talking.");

    }
    @Override
    public void onEndOfSpeech() {
        Log.v("onEndOfSpeech","Stopped talking.");

    }

    @Override
    public void onError(int i) {
        System.out.println("Error: " + i);
        String errorString;
        switch(i) {
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                errorString = "Network Timeout";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                errorString = "Network Error";

                break;
            case SpeechRecognizer.ERROR_AUDIO:
                errorString = "Audio Error";

                break;
            case SpeechRecognizer.ERROR_SERVER:
                errorString = "Server Error";

                break;
            case SpeechRecognizer.ERROR_CLIENT:
                errorString = "Clientside error";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                errorString = "Speech Timeout";

                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                errorString = "No Match";

                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                errorString = "Recog. Busy";

                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                errorString = "Permissions error";
                break;
            default:
                errorString = "Unhandled error";
                break;

        }
        Log.w("speechOnError", errorString);
    }


    @Override
    public void onRmsChanged(float f) {}

    @Override
    public void onEvent(int i, Bundle b) {}

}