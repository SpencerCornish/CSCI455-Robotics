package com.example.spencercornish.roboticsapp;

import android.speech.tts.TextToSpeech;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Net extends Thread {
    Thread t;
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;



    // constructor
    public Net(Socket s, String name,
                         DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }

    public void run() {
        while (true) {
            String received;
            try {
                // receive the string
                received = dis.readUTF();
                if(received != "") {
                    System.out.println(received);
                }


            }
            catch(Exception e){



            }
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
