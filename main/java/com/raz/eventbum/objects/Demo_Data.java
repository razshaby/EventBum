package com.raz.eventbum.objects;

import java.util.HashMap;

public class Demo_Data {
    private String id;
    private String name;
    private int number;
    private HashMap<String,String> map;
    private boolean active;

    public Demo_Data() {
    }

    public Demo_Data(String id,boolean active) {
        this.active = active;
        this.id = id;
    }

    public Demo_Data(String id, String name, int number, HashMap<String, String> map) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.map = map;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }



    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Demo_Data{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", map=" + map +
                ", isTrue=" + active +
                '}';
    }
}
