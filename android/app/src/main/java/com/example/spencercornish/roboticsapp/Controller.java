package com.example.spencercornish.roboticsapp;

import java.util.Random;
import java.lang.Math;

public class Controller {
    public enum activity {
        Start,
        End,
        Recharge,
        WFoe,
        SFoe
    }

    public static void main(String[] args){
        int[] startpos = {0,0,2,2};
        shuffleArray(startpos);
        Player player = new Player(50, startpos[0], startpos[1]);

        Node[][] map = new Node[3][3];
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
        current++;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                System.out.println(map[i][j].name);
                System.out.println(map[i][j].Activity);
                for(int k = 0; k < map[i][j].directions.length; k++){
                    System.out.println(map[i][j].directions[k]);
                }
            }
        }


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
