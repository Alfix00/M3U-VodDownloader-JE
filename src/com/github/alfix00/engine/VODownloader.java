package com.github.alfix00.engine;
import com.github.alfix00.models.Vault;

public class VODownloader {

    private Vault v = new Vault();
    private final Functions functions = new Functions();
    private final ReadM3U readm3u = new ReadM3U();

    private boolean checkWorkSpace(){
        functions.createWorkspace();
        v = readm3u.initialize();
        if(v.isEmpty()){
            System.out.println("[!] File Empty - Check if [M3U-VodDownloader/m3u_folder/your_file.m3u] is valid (or exist).");
            return false;
        }else{
            return true;
        }
    }

    public Vault initialize(){
        checkWorkSpace();
        return v;
    }
}
