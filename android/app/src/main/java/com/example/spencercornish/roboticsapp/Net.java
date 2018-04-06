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
        System.out.println("Starting");

            try {

                ServerSocket socketAB = new ServerSocket(port);
                while(true) {
                    Socket clientSocket = socketAB.accept();
                    System.out.println("Here");

                    DataInputStream DIS = new DataInputStream(clientSocket.getInputStream());
                    String msg_received = DIS.readUTF();
                    System.out.println("INCOMING: " + msg_received);
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


//                    if (mc.name.equals(recipient) && mc.isloggedin==true)
//                    {
//                        mc.dos.writeUTF(this.name+" : "+MsgToSend);
//                        break;
//                    }
