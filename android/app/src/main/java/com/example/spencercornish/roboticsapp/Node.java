package com.example.spencercornish.roboticsapp;

public class Node {
    public Controller.activity Activity;
    public String name;
    public String[] directions;
    public boolean visited = false;

    public Node(Controller.activity Activity, String name, String[] directions){
        this.Activity = Activity;
        this.name = name;
        this.directions = directions;
    }
}
