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
    final String piAddr;
    final String piPort;
    Socket s;



    // constructor
    public Net(Socket s, DataInputStream dis, DataOutputStream dos, String piAddr, String piPort) {
        this.dis = dis;
        this.dos = dos;
        this.s = s;
        this.piAddr = piAddr;
        this.piPort = piPort;
    }

    public void run() {
    try {
        dos.writeChars("Test");

    } catch (Exception e) {
        System.out.println(e.toString());
    }
    }


//        while (true) {
//            String received;
//            try {
//                // receive the string
//                received = dis.readUTF();
//                if(received != "") {
//                    System.out.println(received);
//                }
//
//
//            }
//            catch(Exception e){
//
//
//
//            }
//        }

//    }

    public void sendData(String ip, String port, String message) {

    }

    public void start () {
        System.out.println("Starting Network Send Thread");
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
