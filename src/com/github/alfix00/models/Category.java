package com.github.alfix00.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private ArrayList<Channel> channels = new ArrayList<>();
    private boolean isEmpty = true;
    private String name;
    private String info;
    private int size = 0;

    public Category(String name){
        this.name = name;
    }

    public void appendChannel(Channel channel){
        this.channels.add(channel);
        size = size + 1;
        isEmpty = false;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public Channel getChannelByName(String name) {

        for(Channel channel: this.channels){
            if(channel.getName().equals(name)){
                return channel;
            }
        }
        return null;
    }

    public ArrayList<Channel> haveChannel(String channel_name){
        ArrayList<Channel> new_channels = new ArrayList<>();
        for(Channel c: channels){
            if(c.getName().equalsIgnoreCase(channel_name)){
                new_channels.add(c);
            }
        }
        return new_channels;
    }


    public int getSize(){
        return this.size;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;

    }


    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
