package com.github.alfix00.engine;

import com.github.alfix00.models.Channel;

import java.io.*;
import java.util.ArrayList;

public class WriterReader implements Serializable {

    private final String currentPath = System.getProperty("user.dir");
    private final String download_folder = currentPath + "\\M3U-VodDownloader\\m3u_folder\\dwn_list.bak";

    public void printChannels(ArrayList<Channel> channels) throws IOException {
        ArrayList<Channel> download_list = getChannels();
        if(download_list.size() > 0) {
            download_list.addAll(channels);
            FileOutputStream f = new FileOutputStream(download_folder);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(download_list);
            o.close();
            f.close();
        }else if(channels.size() > 0 ){
            FileOutputStream f = new FileOutputStream(download_folder);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(channels);
            o.close();
            f.close();
        }
    }


    public void  emptyChannels() throws IOException {
        ArrayList<Channel> channels = new ArrayList<>();
        FileOutputStream f = new FileOutputStream(download_folder);
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(channels);
        o.close();
        f.close();
    }

    public ArrayList<Channel> getChannels(){
        ArrayList<Channel> channels;
        try {
            WriterReader objectIO = new WriterReader();
            channels = (ArrayList<Channel>) objectIO.ReadObjectFromFile();
            if (channels != null) {
                if (channels.size() > 0) {
                    return channels;
                }
            }
        } catch (Exception e){
            return new ArrayList<>(0);
        }
        return new ArrayList<>(0);
    }


    private Object ReadObjectFromFile() {
        try {
            File file = new File(download_folder);
            boolean empty = !file.exists() || file.length() == 0;
            if(empty){
                FileOutputStream f = new FileOutputStream(download_folder);
                ObjectOutputStream o = new ObjectOutputStream(f);
                o.writeObject(download_folder);
                o.close();
                f.close();
            }
            FileInputStream fileIn = new FileInputStream(download_folder);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = objectIn.readObject();
            objectIn.close();
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getDownloadPath(){
        return download_folder;
    }

    public void removeIndex(int index) throws IOException {
        ArrayList<Channel> c = getChannels();
        c.remove(index);
        FileOutputStream f = new FileOutputStream(download_folder);
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(c);
        o.close();
        f.close();
    }

    public void removeByName(String title) throws IOException {
        ArrayList<Channel> ch = getChannels();
        for(int i = 0; i < ch.size() ; i++){
            if(ch.get(i).getName().equalsIgnoreCase(title) && ch.get(i).getName().length() == title.length()){
                removeIndex(i);
            }
        }
    }

    public void removeByChannel(Channel c){
        ArrayList<Channel> ch = getChannels();
        ch.removeIf(c1 -> c1.equals(c));
    }

}