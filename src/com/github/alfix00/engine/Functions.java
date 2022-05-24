package com.github.alfix00.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class Functions {

    private String m3uPath = null;

    public Functions() {
    }

/* ----------------------------------------------------------------------------------------------------------- */
    private boolean isFolderExist() {
        try {
            String currentPath = System.getProperty("user.dir");
            String vod_downloader = currentPath + "/M3U-VodDownloader";
            String m3u_folder = currentPath + "/M3U-VodDownloader/m3u_folder";
            String download_folder = currentPath + "/M3U-VodDownloader/download_folder";
            String proxy_folder = currentPath + "/M3U-VodDownloader/proxy_folder";
            ArrayList<String> folders = new ArrayList<>();
            folders.add(vod_downloader);
            folders.add(m3u_folder);
            folders.add(download_folder);
            folders.add(proxy_folder);
            boolean ok = true;
            for (String folder : folders) {
                File file = new File(folder);
                if (!file.isDirectory()) {
                    boolean createFolder = new File(folder).mkdirs();
                    if (!createFolder) {
                        System.out.println("Error while creating working folder [ " + folder + "] ");
                        ok = false;
                    }else{
                        System.out.println("[OK] Folder : "+folder+" created.");
                        ok = true;
                    }
                }else{
                    ok = false;
                }
            }
            return ok;
        } catch (Exception e) {
            System.out.println("Exception" + e);
            return false;
        }
    }

    public boolean checkM3U(){
        assert isFolderExist();
        File folder = new File(System.getProperty("user.dir") + "/M3U-VodDownloader/m3u_folder");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        if (listOfFiles.length > 0){
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    String fileName = listOfFile.getName();
                    if (fileName.contains(".m3u") || fileName.contains(".m3u8")) {
                        setM3uPath(System.getProperty("user.dir") + "/M3U-VodDownloader/m3u_folder/" + fileName);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* ----------------------------------------------------------------------------------------------------------- */

    public void createWorkspace() {
        boolean initialize = isFolderExist();
        assert initialize;
        boolean checkM3U = checkM3U();
        if(!checkM3U){ errors(1);}
    }


    public void errors(int code){
        if (code == 1) {
            System.out.println("[!] M3U not found! please put into /m3u_folder the  .m3u file");
        } else {
            System.out.println("[Error] While pricessing Workspace ...");
        }
    }

    public String getM3uPath(){
        return m3uPath;
    }

    public void pressAnyKeyToContinue()
    {
        System.out.println("Press Enter key to continue...");
        try {
            System.in.read();
        }
        catch(Exception e){}
    }

    public void setM3uPath (String m3uPath){
        this.m3uPath = m3uPath;
    }

    public String getIp(){
        try{
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine(); //you get the IP as a String
            return ip;
        } catch (Exception e){
            return "Cannot reach your IP - (Maybe offline?)";
        }
    }


}