package com.example.spencercornish.roboticsapp;

import java.util.Random;
import java.lang.Math;
import java.util.Scanner;

public class Controller {
    public enum activity {
        Start,
        End,
        Recharge,
        WFoe,
        SFoe
    }
    public static Player player;
    public static Node[][] map = new Node[3][3];
    public static Node currentLoc;
    public static boolean endGame = false;
    public static boolean dead = false;
    public static boolean hasRun = false;

    public static void main(String[] args){
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
        if(act != activity.Start){
            for(int a = 0; a < 3; a++){
                for(int b = 0; b < 3; b++){
                    if(map[a][b].Activity == activity.Start && map[a][b] != currentLoc){
                        map[a][b].Activity = act;
                    }
                }
            }
            currentLoc.Activity = activity.Start;
        }

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){

                System.out.println(map[i][j].name);
                System.out.println(map[i][j].Activity);
                System.out.println(map[i][j].visited);
                for(int k = 0; k < map[i][j].directions.length; k++){
                    System.out.println(map[i][j].directions[k]);
                }
            }
        }

        //take turns
        while(!endGame && !dead){
            if(hasRun){
                player.move("Random");
                currentLoc = map[player.xCoord][player.yCoord];
                currentLoc.visited = true;
                System.out.println("Current node: " + currentLoc.name);
                hasRun = false;
            }
            else{
                startTurn();
                if(!hasRun){
                    executeAction();
                }
            }
        }


        if(endGame){
            //end game as a win
            System.out.println("End Game. You win");
        }
        else if(dead){
            //end game as a loss
            System.out.println("End Game. You died");
        }


    }

    public static void startTurn(){
        //send currentLoc.directions to be spoken
        //Ask for direction
        Scanner scanner = new Scanner(System.in);

        System.out.println("Current node: " + currentLoc.name);

        String input = scanner.next();
        for(int i = 0; i < currentLoc.directions.length; i++){
            if(input.equals(currentLoc.directions[i])){
                //move robot that direction;
                player.move(input);

            }
        }
        currentLoc = map[player.xCoord][player.yCoord];
        currentLoc.visited = true;
        System.out.println("New current node: " + currentLoc.name);

    }

    public static void executeAction(){
        System.out.println("Activity: " + currentLoc.Activity);
        //recharge
        if(currentLoc.Activity == activity.Recharge){
            player.recharge();
            hasRun = false;
            System.out.println("Recharged. HP: "+ player.HP);
        }
        //weak foe && strong foe
        else if(currentLoc.Activity == activity.WFoe || currentLoc.Activity == activity.SFoe){
            boolean fight = runOrFight();
            //if run
            if(fight == false){
                boolean successful = player.run();
                //if successful run
                if(successful == true){
                    System.out.println("You ran");
                    hasRun = true;
                }
                else{
                    System.out.println("You didn't run away");
                    fight = true;
                }
            }
            //if fight
            if(fight == true){
                player.fight(currentLoc.Activity);
                System.out.println("You fought. HP: " + player.HP);
                hasRun = false;
                //if dead
                if(player.HP <= 0){
                    dead = true;
                }
                //if alive
                else{
                    //send updated HP to player
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
                System.out.println("You can't end yet");
                //tell player they can't end yet
            }
        }

    }


    public static boolean canFinish(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(map[i][j].visited == false){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean runOrFight(){
        //ask user run or run or run or fight
        //if run return false
        //if fight return true
        Scanner scanner = new Scanner(System.in);

        System.out.println("Run or Fight: ");

        String input = scanner.next();
        if(input.equals("Run")){
            return false;
        }
        return true;
    }



    public static void shuffleArray(int[] a) {
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }

    private static void swap(int[] a, int i, int change) {
        int helper = a[i];
        a[i] = a[change];
        a[change] = helper;
    }

}
