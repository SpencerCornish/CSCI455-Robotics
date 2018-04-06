package com.example.spencercornish.roboticsapp;

import android.app.Activity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;


public class NetReply extends Thread {
        private Socket hostThreadSocket;
        private Activity activity;
        private String message;

        NetReply(Socket socket, Activity activity, String message) {
            hostThreadSocket = socket;
            this.activity = activity;
            this.message = message;
        }

        @Override
        public void run() {
            OutputStream outputStream;

            try {
                OutputStream os = hostThreadSocket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                pw.println(message);
//                pw.flush();
//               os.flush();


            } catch (IOException e) {
                e.printStackTrace();
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //activity.msg.setText(message);
                }
            });
        }
    }


