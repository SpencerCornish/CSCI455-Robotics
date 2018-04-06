package com.example.spencercornish.roboticsapp;

import java.net.Socket;

public class Sock extends Thread {
    Thread t;
    String ip;
    int port;

    Socket socket;


    Sock(String ip, int port) {
        this.port = port;
        this.ip = ip;
    }

    public void run() {
        try {

            socket = new Socket(ip, port);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }



    public void start () {
        System.out.println("Opening Sock");
        if (t == null) {
            t = new Thread(this, "socket");
            t.start();
        }
    }
}


