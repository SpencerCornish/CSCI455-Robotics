package com.example.spencercornish.roboticsapp;

import android.app.Activity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(message);
                printStream.close();

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //activity.msg.setText(message);
                    }
                });

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //activity.msg.setText(message);
                }
            });
        }
    }


