package com.aroldev.messagingnetwork;

import com.aroldev.messagingnetwork.Enum.MessageType;
import com.aroldev.messagingnetwork.Interfaces.Exportable;

import java.util.ArrayList;
import java.util.List;

public class Phone extends Device implements Exportable<List<String>> {

    private int capacity;
    Message [] messages;

    public Phone(String name, int id, int capacity) {
        super(name, id);
        this.capacity = capacity;
        messages = new Message[capacity];
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void sendMessage(Message msg) {
        boolean assigned = false;
        System.out.println("msg = " + msg);
        if (msg.getContent() == null || msg.getContent().trim().isEmpty()) {
            return;
        }
        if (msg.getType() == MessageType.SYSTEM && !msg.getSender().equals("SYSTEM")) {
            return;
        }
       for (int i = 0; i < capacity; i++){
            if (messages [i] == null) {
                messages [i] = msg;
                assigned = true;
                break;
            }
        }
       if (!assigned) {
           messages [0] = msg;
       }
    }

    public Message[] getMessages() {
        return messages;
    }

    @Override
    public void receiveMessage(Message msg) {

    }

    @Override
    public List<String> export() {
        List<String> listStr = new ArrayList<>();
        for (int i = 0; i < capacity; i++){
            if (messages [i] != null){
                listStr.add(messages [i].getContent());
            }
        }
        return listStr;
    }
}
