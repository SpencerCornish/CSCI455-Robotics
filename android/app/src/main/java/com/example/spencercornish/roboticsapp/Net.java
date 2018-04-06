package com.example.spencercornish.roboticsapp;

import android.speech.tts.TextToSpeech;

import java.io.*;
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
        while (true) {
            String received;
            try {
                // receive the string
                received = dis.readUTF();
                if(received != "") {
                    System.out.println(received);
                    // If startlistening

                }


            }
            catch(Exception e){



            }
        }

    }

    public void sendData(String ip, int port, String message) {
        try {
            Socket socket = new Socket(ip, port);

            OutputStream out = socket.getOutputStream();
            PrintWriter output = new PrintWriter(out);

            output.print(message);

            output.flush();
            output.close();
            socket.close();
        } catch (Exception e) {
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
