package com.example.spencercornish.roboticsapp;

import android.speech.tts.TextToSpeech;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Net extends Thread {
    Thread t;
    Scanner scn = new Scanner(System.in);
    private String name;
    final String piAddr;
    final int piPort;
    final int port;
    Socket s;



    // constructor
    public Net(int port, String piAddr, int piPort) {
        this.port = port;
        this.piAddr = piAddr;
        this.piPort = piPort;
    }

    public void run() {
        while (true) {
            try {
                ServerSocket socket = new ServerSocket(port);
                Socket clientSocket = socket.accept();       //This is blocking. It will wait.
                DataInputStream DIS = new DataInputStream(clientSocket.getInputStream());
                String msg_received = DIS.readUTF();
                System.out.println(msg_received);
                clientSocket.close();
                socket.close();


            }
            catch(Exception e){



            }
        }

    }

    public void sendData(String message) {
        try {
            Socket socket = new Socket(piAddr, piPort);

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
