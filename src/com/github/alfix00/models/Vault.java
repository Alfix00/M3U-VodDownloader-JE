package com.github.alfix00.models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.github.alfix00.options.ProxyMode;

public class Vault {

    private String refresh_m3u = "";
    private boolean isEmpty = true;
    private boolean proxyMode = false;
    private ArrayList<Channel> allChannels = new ArrayList<>();
    private ArrayList<Category> allCategories = new ArrayList<>();
    private ArrayList<Channel> downloadList = new ArrayList<>();
    private ArrayList<String> proxies = new ArrayList<String>();
    private String currentProxy = null;
    private int sizeProxy = 0;

    public Vault(){}

    public ArrayList<Channel> getAllChannels() {
        return allChannels;
    }

    public void setAllChannels(ArrayList<Channel> allChannels) {
        if(allChannels.size() > 0){
            this.allChannels = allChannels;
            isEmpty = false;
        }
    }

    private String getProxyPath(){
        String currentPath = System.getProperty("user.dir");
        String proxy_folder =  currentPath +"\\M3U-VodDownloader\\proxy_folder\\proxy.txt";
        return proxy_folder;
    }

    public Vault checkProxyFileExist(Vault v){
        File proxy_file = new File (getProxyPath());
        if(proxy_file.exists()){
            ProxyMode pm = new ProxyMode();
            pm.loadProxyList(v);
        }
        return v;
    }

    public ArrayList<Category> getAllCategory() {
        return allCategories;
    }

    public void setAllCategory(ArrayList<Category> allCategory) {
        if(allCategory.size() > 0){
            this.allCategories = allCategory;
            isEmpty = false;
        }

    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void printAllChannels(){
        int index = -1;
        for(Channel channel: this.allChannels){
            index = index + 1;
            System.out.println(index+"] "+channel.getName()+"\n"+channel.getCategory()+"\n"+channel.getUrl()+"\n");
        }
    }

    public void printAllCategories(){
        int index = -1;
        for(Category category: this.allCategories){
            index = index + 1;
            System.out.println(index+"] "+category.getName()+" ----- size ["+category.getSize()+"]");
        }
    }

    public boolean isDownloadListEmpty(){
        return downloadList.size() < 1;
    }
    public void addIntoDwnList(Channel c){
        downloadList.add(c);
    }

    public void appendChannelList(ArrayList<Channel> c){
        downloadList.addAll(c);
    }

    public ArrayList<Channel> getDwnList(){
        return downloadList;
    }

    public boolean isProxyMode() {
        return proxyMode;
    }

    public void setProxyMode(boolean proxyMode) {
        this.proxyMode = proxyMode;
    }

    public ArrayList<String> getProxies() {
        return proxies;
    }

    public void setProxies(ArrayList<String> proxies) {
        this.proxies = proxies;
        this.sizeProxy = this.sizeProxy + this.proxies.size();
    }

    public void appendProxy(String proxy){
        this.proxies.add(proxy);
        this.sizeProxy = this.sizeProxy + 1;
    }

    public int getProxySize(Vault v){
        return this.proxies.size();
    }

    public String getCurrentProxy() {
        return currentProxy;
    }

    public String getCurrentProxyIP() {
        return currentProxy.substring(0, currentProxy.indexOf(':'));
    }

    public String getCurrentProxyPort() {
        return currentProxy.substring(currentProxy.lastIndexOf(":") + 1);
    }

    public void setCurrentProxy(String currentProxy) {
        this.currentProxy = currentProxy;
    }

    public void removeChannelByName(String title){
        ArrayList<Channel> tmp = this.getDwnList();
        for(Channel c : tmp){
            if(c.getName().contains(title)){
                tmp.remove(c);
                this.downloadList = tmp;
                break;
            }
        }
    }

}
