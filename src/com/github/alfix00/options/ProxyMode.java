package com.github.alfix00.options;

import com.github.alfix00.engine.Functions;
import com.github.alfix00.models.Channel;
import com.github.alfix00.models.Vault;
import com.github.alfix00.view.Menu;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ProxyMode {


    private String getProxyPath(){
        String currentPath = System.getProperty("user.dir");
        String proxy_folder =  currentPath +"\\M3U-VodDownloader\\proxy_folder\\proxy.txt";
        return proxy_folder;
    }

    private void downloadProxyFromWeb(Vault v) throws IOException {
        ArrayList<String> proxies = getProxyFromWeb();
        if (proxies.size() > 0) {
            printProxyToFile(proxies, v);
        }else{
            System.out.println("Can't retrive proxies [0 added]. (Check your connection or  https://www.free-proxy-list.net if is up.");
        }
    }

    private static ArrayList<String> getProxyFromWeb() {
        try {
            ArrayList<String> proxies = new ArrayList<>();
            Document doc = Jsoup.connect("https://www.free-proxy-list.net").get();
            Elements tables = doc.select("tbody");
            for (Element table : tables) {
                for (Element row : tables.select("tr")) {
                    Elements tds = row.select("td");
                    if (tds.size() == 8) {
                        String ip = tds.get(0).text();
                        String port = tds.get(1).text();
                        String freshIP = ip + ":" + port;
                        if (!freshIP.contains("-")) proxies.add(freshIP);
                    }
                }
            }
            return proxies;
        } catch (Exception e) {
            System.out.println("[!] Error while catching new proxies [!]");
            return new ArrayList<String>();
        }
    }

    private void printProxyToFile(ArrayList<String> proxies, Vault v) throws IOException {
        boolean added = false;
        String proxy_folder = getProxyPath();
        File proxy_file = new File(proxy_folder);
        if(!proxy_file.isDirectory()){
            proxy_file.createNewFile();
        }
        Scanner s = new Scanner(new File(proxy_folder));
        ArrayList<String> old_proxies = new ArrayList<String>();
        while (s.hasNext()) {
            old_proxies.add(s.next());
        }
        s.close();
        if (old_proxies.size() > 0) {
            if (old_proxies.addAll(proxies)) {
                System.out.println("[i] Removing duplicate proxy (if exists) ...");
                Set<String> listWithoutDuplicates = new HashSet<>(old_proxies);
                HashSet<String> tmp =  new HashSet<>(old_proxies);
                tmp.clear();
                tmp.addAll(listWithoutDuplicates);
                ArrayList<String> list = new ArrayList<String>(tmp);
                old_proxies = list;
            }
        } else if(proxies.size() > 0){
            old_proxies = proxies;
        }
        OutputStream os = new FileOutputStream(proxy_folder);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
        File file = new File(proxy_folder);
        if (file.exists() && !file.isDirectory()) {
            System.out.println("[**] Creating empty proxy.txt file... ");
            FileOutputStream f = new FileOutputStream(proxy_folder);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(proxy_folder);
            o.close();
            f.close();
        }
        if(old_proxies.size() > 0) {
            int index = 0;
            for (String p : old_proxies) {
                v.appendProxy(p);
                pw.println(p);
                System.out.println("["+index+"] Added: " + p);
                added = true;
                index = index + 1;
            }
        }
        pw.close();
        if (added) System.out.println("[!] Proxy written in file folder! - Added [" + old_proxies.size() + "] proxies");
        else System.out.println("[*] No proxy(s) added - maybe duplicates in list ? ");

    }



    public void loadProxyList(Vault v){
        try{
            String proxy_folder = getProxyPath();
            File directory = new File(proxy_folder);
            if (directory.exists()) {
                Scanner s = new Scanner(new File(proxy_folder));
                ArrayList<String> prx = new ArrayList<String>();
                while (s.hasNext()) {
                    prx.add(s.next());
                }
                s.close();
                if (prx.size() > 0) {
                    {
                        v.setProxies(prx);
                    }
                }
            }
        }catch (IOException e){
            System.out.println("[!] Can't read line from proxy file. please check format [IP:PORT] ex [xxx.xxx.xxx.xxx:yyyy] ");
        }

    }


    private boolean checkProxyList() throws IOException {
        String proxy_folder = getProxyPath();
        File file = new File(proxy_folder);
        if (file.exists() && !file.isDirectory()) {
            if (file.length() == 0) {
                System.out.println("[Proxy File is Empty!]");
                return false;
            }
            List<String> lines = Files.readAllLines(Paths.get(proxy_folder), StandardCharsets.UTF_8);
            if (lines.size() > 0) {
                return lines.get(0).contains(":");
            } else {
                System.out.println("[!] Can't read first line from proxy.txt file [!]");
                return false;
            }
        } else {
            System.out.println("[!] Cannot find [proxy.txt] in [./proxy_folder]");
            return false;
        }
    }

    private boolean isAlphanumeric(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    private Vault insertProxyManually(Vault v){
        try {
            Scanner sc = new Scanner(System.in);
            System.out.flush();
            System.out.print("[i] Insert new proxy (format IP:PORT) = ");
            String proxy = sc.next();
            if (proxy.contains(":") && !isAlphanumeric(proxy)) {
                String port;
                port = proxy.substring(proxy.lastIndexOf(":") + 1);
                proxy = proxy.substring(0, proxy.indexOf(':'));
                if(pingHost(proxy,Integer.parseInt(port),500)){
                    v.appendProxy(proxy);
                    ArrayList<String> proxies = v.getProxies();
                    proxies.add(proxy);
                    printProxyToFile(proxies, v);
                    System.out.println("[+] Added proxy : " + proxy);
                    System.out.println("\n[!] Connected to a new proxy: " + proxy + " " + port);
                    v.setCurrentProxy(proxy+":"+port);
                    v.setProxyMode(true);
                }else{
                    System.out.println("[Error] Can't get connection with this proxy ["+proxy+":"+port+"]");
                }
            } else {
                System.out.println("[Error] Wrong proxy input -- correct format = IP:PORT");
            }
            return v;
        } catch (IOException e){
            System.out.println("[i] Proxy list empty");
            createEmptyProxyFile();
        }
        return v;
    }

    public Vault setRandomProxy(Vault v){
        String proxy = null, port = null;
        Random rn = new Random();
        ArrayList<String> proxies = v.getProxies();
        int size = proxies.size();
        int attempts = 0;
        boolean condition = false;
        while(!condition){
            int number = rn.nextInt(size) + 1;
            attempts = attempts + 1;
            proxy = proxies.get(number);
            System.out.println("* Attempt(s) "+attempts + " | proxy = "+proxy);
            port = proxy.substring(proxy.lastIndexOf(":") + 1);
            proxy = proxy.substring(0, proxy.indexOf(':'));
            condition = pingHost(proxy,Integer.parseInt(port),800);
            if(attempts == 200){
                System.out.println("Can't connect to a proxy, check if the proxy list is valid.");
                break;
            }
        }
        if(condition) {
            System.setProperty("http.proxyHost", proxy);
            System.setProperty("http.proxyPort", port);
            v.setProxyMode(true);
            v.setCurrentProxy(proxy+":"+port);
            System.out.println("Connected to a new proxy: " + proxy + " " + port);
        }
        return v;
    }

    public static URL url;

    private static boolean pingHost(String host, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            HttpURLConnection http = null;
            url = new URL("http://www.google.com");
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
            http = (HttpURLConnection) url.openConnection(proxy);
            http.setConnectTimeout(600);
            http.setReadTimeout(600);
            int code = http.getResponseCode();
            if (code == 200) {
                return true;
            }
            return false;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    private Vault deleteProxyList(Vault v) throws IOException {
        String currentPath = System.getProperty("user.dir");
        String proxy_folder = currentPath + "\\M3U-VodDownloader\\proxy_folder\\proxy.txt";
        ArrayList<String> proxies = new ArrayList<>();
        FileOutputStream f = new FileOutputStream(new File(proxy_folder));
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(proxies);
        o.close();
        f.close();
        System.out.println("[!] proxy.txt deleted.");
        return v;
    }

    private void createEmptyProxyFile(){
        try {
            File proxy = new File(getProxyPath());
            if (proxy.createNewFile()) {
                System.out.println("[*] Created: " + proxy.getName());
            } else {
                System.out.println("[!] File already exists.");
            }
        } catch (IOException e) {
            System.out.println("[!!] An error occurred.");
            e.printStackTrace();
        }
    }


    public Vault setProxyMode(Vault v) throws IOException {
        Scanner scanner = new Scanner(System.in);
        if(v.isProxyMode()) System.out.print("\nProxy Mode: ON | ip: "+v.getCurrentProxy());
        else System.out.print("\nProxy Mode: OFF");
        System.out.print("\n ---------------------------------------");
        System.out.print("\n1) Load proxy list from file");
        System.out.print("\n2) Download proxy list from web");
        System.out.print("\n3) Set random proxy | (from proxy.txt)" );
        System.out.print("\n4) Connect to a proxy manually");
        System.out.print("\n5) Delete proxy list.");
        System.out.print("\n\n0) Back to menu.");
        System.out.print("\n ---------------------------------------");
        System.out.print("\n\n Choice: ");
        String in = scanner.next();
        int input = Integer.parseInt(in);
        switch (input) {
            case 1:
                if (checkProxyList()) {
                    System.out.println("[*] Loading proxy list from file ...");
                    loadProxyList(v);
                    System.out.println("[*] Loaded "+v.getProxies().size()+" proxies");
                }
                break;
            case 2:
                downloadProxyFromWeb(v);
                break;
            case 3:
                if (v.getProxies().size() > 0){
                    v = setRandomProxy(v);
                    setProxyMode(v);
                    v.setProxyMode(true);
                }else{
                    System.out.println("[Proxy] List is empty!");
                }
                break;
            case 4:
                ///setRandomProxy(v);
                v = insertProxyManually(v);
                setProxyMode(v);
                break;
            case 5:
                v = deleteProxyList(v);
                break;
            case 0:
                break;
            default:
                return v;
        }
        return v;
    }

}
