package com.example.spencercornish.roboticsapp;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.lang.Math;
import java.util.Scanner;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.view.View;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Controller extends Thread {
    public enum activity {
        Start,
        End,
        Recharge,
        WFoe,
        SFoe
    }
    Thread t;
    public Player player;
    public Node[][] map = new Node[3][3];
    public Node currentLoc;
    public boolean endGame = false;
    public boolean dead = false;
    public boolean hasRun = false;
    public MoveActivity moveActivity;
    public int numMoves = 30;


    public Controller(MoveActivity moveActivity){
        this.moveActivity = moveActivity;
        int[] startpos = {0,0,2,2};
        shuffleArray(startpos);
        player = new Player(20, startpos[0], startpos[1]);

        int[] nodes = {0,1,2,3,3,3,3,4,4};
        shuffleArray(nodes);
        int current = 0;
        String[] d1 = {"East"};
        map[0][0] = new Node(activity.values()[nodes[current]], "1", d1);
        current++;
        String[] d2 = {"South", "East", "West"};
        map[0][1] = new Node(activity.values()[nodes[current]], "2", d2);
        current++;
        String[] d3 = {"South", "West"};
        map[0][2] = new Node(activity.values()[nodes[current]], "3", d3);
        current++;
        String[] d6 = {"South", "East"};
        map[1][0] = new Node(activity.values()[nodes[current]], "6", d6);
        current++;
        String[] d7 = {"North","South","West"};
        map[1][1] = new Node(activity.values()[nodes[current]], "7", d7);
        current++;
        String[] d8 = {"North"};
        map[1][2] = new Node(activity.values()[nodes[current]], "8", d8);
        current++;
        String[] d11 = {"North"};
        map[2][0] = new Node(activity.values()[nodes[current]], "11", d11);
        current++;
        String[] d12 = {"North","East"};
        map[2][1] = new Node(activity.values()[nodes[current]], "12", d12);
        current++;
        String[] d13 = {"West"};
        map[2][2] = new Node(activity.values()[nodes[current]], "13", d13);

        currentLoc = map[player.xCoord][player.yCoord];
        currentLoc.visited = true;
        activity act = currentLoc.Activity;
        boolean setKey = false;
        if(act != activity.Start) {
            for (int a = 0; a < 3; a++) {
                for (int b = 0; b < 3; b++) {
                    if(map[a][b].Activity == activity.Start && map[a][b] != currentLoc) {
                        map[a][b].Activity = act;
                    }
                    if(map[a][b].Activity == activity.SFoe && setKey == false){
                        map[a][b].hasKey = true;
                        setKey = true;
                    }
                    if(map[a][b].Activity == activity.SFoe){
                        map[a][b].HP = 20;
                    }
                    if(map[a][b].Activity == activity.WFoe){
                        map[a][b].HP = 10;
                    }
                }
            }
            currentLoc.Activity = activity.Start;
        }
    }

    public void run() {
        System.out.println("Starting game controller");
        while (!endGame && !dead && numMoves > 0) {
            if (hasRun) {
                player.move("Random");
                numMoves--;
                currentLoc = map[player.xCoord][player.yCoord];
                currentLoc.visited = true;
                hasRun = false;
            } else {
                startTurn();
                executeAction();
            }
        }

        if (endGame) {
            //end game as a win
            setImage(R.drawable.ss);
            setQuestionText("End of Game. You Win!!");
            setOptionsText(Arrays.asList(""));
        } else if (dead || numMoves == 0) {
            //end game as a loss
            setImage(R.drawable.lose);
            setQuestionText("End of Game. You died.");
            setOptionsText(Arrays.asList(""));
        }
        System.out.println("Ending game controller thread");
    }


    public void start () {
        System.out.println("Starting Controller Thread");
        if (t == null) {
            t = new Thread(this, "Controller");

            t = new Thread(this, "controller");

            t.start();
        }
    }


    public void startTurn(){
        //send currentLoc.directions to be spoken
        //Ask for direction
        String dirs = "";
        for ( int i = 0; i < currentLoc.directions.length; i++) {
            if (i == currentLoc.directions.length-1) {
                dirs = dirs + currentLoc.directions[i];

            } else {
                dirs = dirs + currentLoc.directions[i] + " or ";
            }
        }


        setImage(R.drawable.questionmark);
        setQuestionText("Which direction?");
        setOptionsText(Arrays.asList(currentLoc.directions));
        speak("Which direction? You can go " + dirs);
        sleepThread(5000);
        startListening();
        sleepThread(500);

        while(true) {
            if(moveActivity.lastSpoken == null) continue;
            if(moveActivity.lastSpoken.equals("")) continue;
            if(moveActivity.lastSpoken.isEmpty()) continue;
            for(int i = 0; i < currentLoc.directions.length; i++){
                if(moveActivity.lastSpoken.toLowerCase().contains(currentLoc.directions[i].toLowerCase())){
                    //move robot that direction
                    move(moveActivity.lastSpoken.toLowerCase());
                    player.move(moveActivity.lastSpoken);
                    numMoves--;
                    currentLoc = map[player.xCoord][player.yCoord];
                    currentLoc.visited = true;
                    setQuestionText("New current node: " + currentLoc.name);
                    speak("New current node is " + currentLoc.name);
                    sleepThread(4000);
                    return;
                }
            }
            setImage(R.drawable.questionmark);
            setQuestionText("Which direction?");
            setOptionsText(Arrays.asList(currentLoc.directions));
            speak("Which direction? You can go " + dirs);
            sleepThread(5000);
            startListening();
            sleepThread(500);

        }

        //System.out.println("New current node: " + currentLoc.name);
    }

    public void fightFoe(){
        setImage(R.drawable.fight);
        if (currentLoc.HP <= 0){
            setQuestionText("Foe already defeated.");
            speak("Foe already defeated.");
            sleepThread(4000);
        }
        else {
            if (currentLoc.Activity == activity.SFoe && currentLoc.HP >= 10) {
                  player.fight(currentLoc.Activity);
                  move("fight");
                  currentLoc.HP = currentLoc.HP - 10;
                  setQuestionText("You fought. HP: " + player.HP + " Foe HP: " + currentLoc.HP);
                  speak("You fought. HP is now " + player.HP + ". Foe HP is now " + currentLoc.HP);
                  sleepThread(5000);
            }
            else if (currentLoc.Activity == activity.WFoe && currentLoc.HP >= 5) {
                  player.fight(currentLoc.Activity);
                  move("fight");
                  currentLoc.HP = currentLoc.HP - 5;
                  setQuestionText("You fought. HP: " + player.HP + " Foe HP: " + currentLoc.HP);
                  speak("You fought. HP is now " + player.HP + ". Foe HP is now " + currentLoc.HP);
                  sleepThread(5000);
            }
        }
    }




    public void executeAction(){
        System.out.println("Activity: " + currentLoc.Activity);
        //recharge
        if(currentLoc.Activity == activity.Recharge){
            player.recharge();
            hasRun = false;
            //System.out.println("Recharged. HP: "+ player.HP);
            setImage(R.drawable.recharge);
            move("recharge");
            setQuestionText("Recharged. HP: " + player.HP);
            speak("Recharged. HP is " + player.HP);
            sleepThread(4000);
        }
        //weak foe && strong foe
        else if(currentLoc.Activity == activity.WFoe || currentLoc.Activity == activity.SFoe) {

            fightFoe();
            if(currentLoc.hasKey == true){
              speak("You found the key!");
              sleepThread(2000);
            }
            hasRun = false;
            //if dead
            if (player.HP <= 0) {
                dead = true;
            }

            else {
              while (true) {
                  if(currentLoc.HP <=0){
                      setQuestionText("You defeated the foe.");
                      speak("You defeated the foe.");
                      sleepThread(4000);
                      break;
                  }
                  boolean fight = runOrFight();
                  //if run
                  if (!fight) {
                      setImage(R.drawable.runaway);
                      boolean successful = player.run();
                      //if successful run
                      if (successful) {
                          //System.out.println("You ran");
                          setQuestionText("You ran away successfully");
                          speak("You ran away successfully.");
                          hasRun = true;
                          sleepThread(4000);
                          break;

                      } else {
                          //System.out.println("You didn't run away");
                          setQuestionText("You didn't run away successfully");
                          speak("You didn't run away successfully.");
                          fight = true;
                          sleepThread(4000);
                      }
                  }
                  //if fight
                  if (fight) {
                      fightFoe();
                      hasRun = false;
                      //if dead
                      if (player.HP <= 0) {
                          dead = true;
                          break;
                      }
                      if(currentLoc.HP <= 0){
                          break;
                      }
                  }
              }
          }
        }
        //if at end
        else if(currentLoc.Activity == activity.End){
            if(canFinish()){
                //end game with a win
                endGame = true;
            }
            else{
                setQuestionText("You can't end yet. You don't have the key");
                speak("I see a box of gold. You don't have the key.");
                sleepThread(4000);

                //System.out.println("You can't end yet");
                //tell player they can't end yet
            }
        }

    }


    public boolean canFinish(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(map[i][j].hasKey == true && map[i][j].visited == true){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean runOrFight(){
        //ask user run or run or run or fight
        //if run return false
        //if fight return true
        //Scanner scanner = new Scanner(System.in);

        //System.out.println("Run or Fight: ");
        setImage(R.drawable.questionmark);
        setQuestionText("Run or Fight?");
        setOptionsText(Arrays.asList("Run", "Fight"));
        speak("Run or fight?");
        sleepThread(4000);
        startListening();
        sleepThread(500);

        //String input = scanner.next();
        while(true) {
            if(moveActivity.lastSpoken == null) continue;
            if(moveActivity.lastSpoken == "") continue;
            if (moveActivity.lastSpoken.toLowerCase().contains("run")) {
                return false;
            } else if (moveActivity.lastSpoken.toLowerCase().contains("fight")) {
                return true;

            } else {
                startListening();
                sleepThread(500);
            }
        }
    }

    public void shuffleArray(int[] a) {
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }

    private void swap(int[] a, int i, int change) {
        int helper = a[i];
        a[i] = a[change];
        a[change] = helper;
    }

    private void setImage(final int id) {
        moveActivity.runOnUiThread(new Runnable() {
            public void run() {
                moveActivity.setImageBox(id);
            }
        });

    }

    private void setQuestionText(final String id) {
        moveActivity.runOnUiThread(new Runnable() {
            public void run() {
                moveActivity.setQuestionText(id);
            }
        });

    }

    private void setOptionsText(final List<String> id) {
        moveActivity.runOnUiThread(new Runnable() {
            public void run() {
                moveActivity.setOptionsText(id);
            }
        });

    }

    private void startListening() {
        moveActivity.runOnUiThread(new Runnable() {
            public void run() {
                moveActivity.clearSpeech();
                moveActivity.startSpeechRecognition();
            }
        });
    }

    private void speak(final String toSay) {
        moveActivity.runOnUiThread(new Runnable() {
            public void run() {
                moveActivity.speak(toSay);
            }
        });
    }

    private void move(final String move) {
        moveActivity.runOnUiThread(new Runnable() {
            public void run() {
                moveActivity.moveRobot(move);
            }
        });
    }

    private void sleepThread(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            System.out.println("Interrupted");
        }
    }
}

//    public static void main(String[] args){
//        int[] startpos = {0,0,2,2};
//        shuffleArray(startpos);
//        player = new Player(20, startpos[0], startpos[1]);
//
//        int[] nodes = {0,1,2,3,3,3,3,4,4};
//        shuffleArray(nodes);
//        int current = 0;
//        String[] d1 = {"East"};
//        map[0][0] = new Node(activity.values()[nodes[current]], "1", d1);
//        current++;
//        String[] d2 = {"South", "East", "West"};
//        map[0][1] = new Node(activity.values()[nodes[current]], "2", d2);
//        current++;
//        String[] d3 = {"South", "West"};
//        map[0][2] = new Node(activity.values()[nodes[current]], "3", d3);
//        current++;
//        String[] d6 = {"South", "East"};
//        map[1][0] = new Node(activity.values()[nodes[current]], "6", d6);
//        current++;
//        String[] d7 = {"North","South","West"};
//        map[1][1] = new Node(activity.values()[nodes[current]], "7", d7);
//        current++;
//        String[] d8 = {"North"};
//        map[1][2] = new Node(activity.values()[nodes[current]], "8", d8);
//        current++;
//        String[] d11 = {"North"};
//        map[2][0] = new Node(activity.values()[nodes[current]], "11", d11);
//        current++;
//        String[] d12 = {"North","East"};
//        map[2][1] = new Node(activity.values()[nodes[current]], "12", d12);
//        current++;
//        String[] d13 = {"West"};
//        map[2][2] = new Node(activity.values()[nodes[current]], "13", d13);
//
//        currentLoc = map[player.xCoord][player.yCoord];
//        currentLoc.visited = true;
//        activity act = currentLoc.Activity;
//        if(act != activity.Start){
//            for(int a = 0; a < 3; a++){
//                for(int b = 0; b < 3; b++){
//                    if(map[a][b].Activity == activity.Start && map[a][b] != currentLoc){
//                        map[a][b].Activity = act;
//                    }
//                }
//            }
//            currentLoc.Activity = activity.Start;
//        }
//
//        for(int i = 0; i < 3; i++){
//            for(int j = 0; j < 3; j++){
//
//                System.out.println(map[i][j].name);
//                System.out.println(map[i][j].Activity);
//                System.out.println(map[i][j].visited);
//                for(int k = 0; k < map[i][j].directions.length; k++){
//                    System.out.println(map[i][j].directions[k]);
//                }
//            }
//        }
//
//        //take turns
//        while(!endGame && !dead){
//            if(hasRun){
//                player.move("Random");
//                currentLoc = map[player.xCoord][player.yCoord];
//                currentLoc.visited = true;
//                System.out.println("Current node: " + currentLoc.name);
//                hasRun = false;
//            }
//            else{
//                startTurn();
//                if(!hasRun){
//                    executeAction();
//                }
//            }
//        }
//
//
//        if(endGame){
//            //end game as a win
//            System.out.println("End Game. You win");
//        }
//        else if(dead){
//            //end game as a loss
//            System.out.println("End Game. You died");
//        }
//
//
//    }
