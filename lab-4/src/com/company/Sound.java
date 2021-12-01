package com.company;

public class Sound {
    String name;

    public Sound(String name) {
        this.name = name;
    }

    public void sound() {
        System.out.println("Звучит " + name);
    }
}
