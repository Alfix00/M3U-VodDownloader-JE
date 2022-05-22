package com.github.alfix00.view;
import com.github.alfix00.engine.Functions;
import com.github.alfix00.engine.VODownloader;
import com.github.alfix00.engine.WriterReader;
import com.github.alfix00.models.DownloadList;
import com.github.alfix00.models.Vault;
import com.github.alfix00.options.ProxyMode;
import com.github.alfix00.options.SearchChannel;
import com.github.alfix00.options.ShowOutputList;


import java.net.InetAddress;
import java.util.Scanner;


public class Menu {

    private Vault vault;


    public void index() throws InterruptedException {
        VODownloader vDwn = new VODownloader();
        vault = vDwn.initialize();
        menu(vDwn);
    }

    public void menu(VODownloader vDwn) throws InterruptedException {
       runMenu();
    }

    private void runMenu() throws InterruptedException {
        if(!vault.isEmpty()){
            vault.checkProxyFileExist(vault);
            try {
                System.out.flush();
                int choiceentry;
                Scanner scanchoice = new Scanner(System.in);
                do {
                    Functions f = new Functions();
                    System.out.println("\n    GitHub: https://github.com/Alfix00");
                    System.out.println("\n---------------| VODownloader |------------\n");
                    System.out.println("    1) Show Categories/Channel list.");
                    System.out.println("    2) Search Channels by name.");
                    System.out.println("    3) Show download list and download.\n ");
                    System.out.println("    4) Reload m3u file.");
                    System.out.println("    5) Proxy Mode.\n");
                    System.out.println("    0) Exit.");
                    System.out.println("---------------------------------------------[Dev by Alfix00]");
                    System.out.print("\tCurrent Machine: ");
                    System.out.print(InetAddress.getLocalHost()+"\n");
                    System.out.print("\tCurrent IP: ");
                    if(!vault.isProxyMode()){
                        System.out.print(f.getIp()+"\n");
                    }else{
                        System.out.print(vault.getCurrentProxy()+"\n");
                    }
                    System.out.println("\tCategories: " + vault.getAllCategory().size());
                    System.out.println("\tChannels: " + vault.getAllChannels().size());
                    System.out.println("---------------------------------------------");
                    if(vault.isProxyMode()) System.out.println("Proxy Mode: ON | proxy ip = "+ vault.getCurrentProxy());
                    else System.out.println("Proxy Mode: OFF");
                    System.out.println("Proxy list size: ["+vault.getProxySize(vault)+"]");
                    System.out.print("\n\tChoice: ");
                    choiceentry = scanchoice.nextInt();
                    switch (choiceentry) {
                        case 1:
                            ShowOutputList sol = new ShowOutputList();
                            sol.initialize(vault);
                            runMenu();
                        case 2:
                            SearchChannel sc = new SearchChannel();
                            sc.initialize(vault);
                            runMenu();
                        case 3:
                            DownloadList dl = new DownloadList();
                            dl.showDownloadList(vault);
                            runMenu();
                        case 4:
                            System.out.println("\n[i] Reloading M3U ...");
                            VODownloader vDwn = new VODownloader();
                            vault = vDwn.initialize();
                            runMenu();
                        case 5:
                            ProxyMode pm = new ProxyMode();
                            vault = pm.setProxyMode(vault);
                            runMenu();
                        case 0:
                            System.exit(0);
                        default:
                            System.out.println("Choice must be a value between 1 and 4. [Press 0 to exit]");
                    }
                } while (true);
            } catch (Exception InputMismatchException){
                System.out.println("\n[!] Warning - Digit only numbers in this section!.");
                System.out.println("[* * *] Reloading Menu...\n");
                Thread.sleep(2000);
                runMenu();
            }
        }
    }

}
