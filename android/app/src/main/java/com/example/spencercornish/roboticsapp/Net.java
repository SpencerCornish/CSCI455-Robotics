package com.example.spencercornish.roboticsapp;

import android.app.Activity;
import android.speech.tts.TextToSpeech;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Net extends Thread {
    Activity activity;
    Thread t;
    Socket socket;
    final int port;
    Socket s;



    // constructor
    public Net(Socket socket, int port, Activity activity) {
        this.socket = socket;
        this.activity = activity;
        this.port = port;
    }

    public void run() {
        System.out.println("Starting to listen...");

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                br.reset();
                while(true) {
                    String incomingString = br.readLine();
                    if (incomingString != null) {
                        System.out.println("INCOMING STRING from RPI: " + incomingString);

                    }
//                    if(incomingString != null) {
//                        if(incomingString.contains("promptForVoice")) {
//                            activity.runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    activity.p
//                                    //activity.msg.setText(message);
//                                }
//                            });
//                        }
//
//                    }
                }
            }
            catch(Exception e){
                System.out.println(e.toString());


            }


    }

    public void start () {
        System.out.println("Starting Network Thread");
        if (t == null) {
            t = new Thread(this, "network");
            t.start();
        }
    }
}
