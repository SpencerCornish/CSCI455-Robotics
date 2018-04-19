package com.example.spencercornish.roboticsapp;

import java.lang.Math;

public class Player {
    public int HP;
    public int xCoord;
    public int yCoord;

    public Player(int HP, int x, int y){
        this.HP = HP;
        this.xCoord = x;
        this.yCoord = y;
    }

    public boolean run(){
        int run = (int)(Math.random() * 4);
        if(run == 3){
            return false;
        }
        else{
            return true;
        }

    }

    public boolean fight(Controller.activity foe){
        if(foe == Controller.activity.WFoe){
            HP = (int)(HP * .22);
        }
        else if(foe == Controller.activity.SFoe){
            if(HP < 15){
                HP = 0;
                return false;
            }
            else {
                HP = (int) (HP * .9);
                return true;
            }
        }

        return false;

    }

    public int[] move(String direction){
        if(direction.equals("North")){
            xCoord -= 1;
        }
        else if(direction.equals("South")){
            xCoord += 1;
        }
        else if(direction.equals("West")){
            yCoord -= 1;
        }
        if(direction.equals("East")){
            yCoord += 1;
        }
        int[] coords = {xCoord, yCoord};
        return coords;
    }


}
