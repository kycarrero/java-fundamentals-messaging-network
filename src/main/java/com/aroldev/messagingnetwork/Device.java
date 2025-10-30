package com.aroldev.messagingnetwork;

public abstract class Device {

    private String name;
    private int id;
    private boolean darkMode;

    public Device(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public abstract void sendMessage(Message msg);
    public abstract void receiveMessage(Message msg);
}
