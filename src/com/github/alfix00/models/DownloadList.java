package com.github.alfix00.models;

import com.github.alfix00.engine.WriterReader;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;



public class DownloadList {

    private Vault v;
    private final Scanner sc = new Scanner(System.in);
    private final WriterReader wr = new WriterReader();
    private final String currentPath = System.getProperty("user.dir");
    private boolean changed = false;
    public boolean downloadComplete = false;

    public void showDownloadList(Vault v) {
        this.v = v;
        try {
            if (!wr.getChannels().isEmpty()) {
                removeDuplicates();
                ArrayList<Channel> downloadList = wr.getChannels();
                int index = 0;
                if (downloadList.size() > 0) {
                    for (Channel c : downloadList) {
                        System.out.println(index + "] " + c.getName() + " | [Category] : " + c.getCategory());
                        index = index + 1;
                    }
                    System.out.println("\n0) Back to menu\n");
                    System.out.println("1) Single download ");
                    System.out.println("2) Download entire list ");
                    System.out.println("3) Remove channel(s) from list ");
                    System.out.println("4) Empty List ");
                    System.out.print("\nChoice: ");
                    int choice = sc.nextInt();
                    System.out.print("\n");
                    switch (choice) {
                        case 1 -> startSingleDownload();
                        case 2 -> startFullDownload();
                        case 3 -> {
                            removeChannel(downloadList);
                            showDownloadList(v);
                        }
                        case 4 -> cleanDownloadList();
                        default -> System.out.println("Going back ...");
                    }
                } else {
                    System.out.println("[!] Download list is Empty. -- Going back...");
                    Thread.sleep(1000);
                }
            } else {
                System.out.println("[!] Download list is Empty. -- Going back...");
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Going back ... - Exception: " + e);
        }


    }

    public void removeDuplicates() throws IOException {
        ArrayList<Channel> downloadList = wr.getChannels();
        File[] listOfFiles = new File(currentPath+"/M3U-VodDownloader/download_folder").listFiles();
        ArrayList<Channel> withoutDuplicates = new ArrayList<>();
        ArrayList<String> arr_t = new ArrayList<>();
        for(Channel c: downloadList){
            String title = c.getName();
            if(!arr_t.contains(title)){
                arr_t.add(title);
                withoutDuplicates.add(c);
            }
        }
        if(listOfFiles != null && downloadList.size() > 0){
            for(Channel c : withoutDuplicates) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        String tmp_name = file.getName();
                        tmp_name = tmp_name.replace(".mkv", "")
                                .replaceAll(".mp4", "")
                                .replaceAll("\\{.*?}", "")
                                .replaceAll("\\[.*?]", "")
                                .replaceAll("\\(.*\\)", "")
                                .replaceAll(" ","");
                        String tmp_cname = c.getName().replaceAll(" ","");
                        if (tmp_name.equalsIgnoreCase(tmp_cname)) {
                            wr.removeByChannel(c);
                            withoutDuplicates.remove(c);
                            downloadList.remove(c);
                        }
                    }
                }
            }
            wr.emptyChannels();
            wr.printChannels(withoutDuplicates);
        }
    }



    private void cleanDownloadList() {
        File myObj = new File(wr.getDownloadPath());
        if (myObj.delete()) {
            System.out.println("[ " + myObj.getName() + " ] Removed.");
        } else {
            System.out.println("Failed to delete [" + myObj.getName() + "]");
        }
    }

    private void switchChange() {
        this.changed = !this.changed;
    }

    private boolean getChange() {
        return this.changed;
    }

    private void removeChannel(ArrayList<Channel> channels) throws IOException {
        try {
            if (channels.size() > 0) {
                System.out.println("\n[*] Insert channel index to remove (digit -1 or a letter to exit) : ");
                int choice = 0;
                int channel_size;
                channel_size = channels.size();
                while (choice > -1 && choice < channel_size) {
                    choice = sc.nextInt();
                    if (choice > -1 && choice < channel_size) {
                        String name = channels.get(choice).getName();
                        channels.remove(choice);
                        channel_size = channel_size - 1;
                        switchChange();
                        System.out.println("[-] " + name + " Removed.");
                        System.out.println("\n[Remove index ( 0-" + channel_size + ")] ");
                        showDownloadList(v);
                        System.out.println("[*] Insert channel index to remove (digit -1 or a letter to exit) :  ");
                        choice = sc.nextInt();
                    } else {
                        if (choice != -1) System.out.println("[*] Index out border.");
                    }
                }
            }
            if (getChange()) {
                wr.emptyChannels();
                wr.printChannels(channels);
            }
        } catch (InputMismatchException e) {
            if (getChange()) {
                wr.emptyChannels();
                wr.printChannels(channels);
            }
            System.out.println("[*] Exit...");
        }

    }

    private void startSingleDownload() {
        try {
            System.out.print("Insert index: ");
            int index = sc.nextInt();
            if (index > -1 && index <= wr.getChannels().size()) {
                startDownloadHTTP(wr.getChannels().get(index), index);
            }

        } catch (Exception e) {
            System.out.println("Going back ...");
        }

    }

    private void startFullDownload() {
        try {
            int index = 0;
            for (Channel c : wr.getChannels()) {
                startDownloadHTTP(c, index);
                index = index + 1;
            }
        } catch (Exception e) {
            System.out.println("Going back ...");
        }
    }


    private void startDownloadHTTP(Channel c, int index) throws InterruptedException, IOException {
        try {
            downloadComplete = false;
            System.out.println("\nPress [CTRL+C] or [CTRL+X] to stop the download.");
            String currentPath = System.getProperty("user.dir");
            String download_folder = "\\M3U-VodDownloader\\download_folder\\";
            download_folder = currentPath + download_folder;
            String download_URL = c.getUrl();
            String title = c.getName();
            String format_video = c.getUrl().substring(c.getUrl().lastIndexOf('.'));
            if (format_video.length() < 2) {
                format_video = "mp4";
            }
            download_folder = download_folder + title + format_video;
            File file = new File(download_folder);
            Thread download = new Thread(new DownloadTask(download_URL, file, title));
            download.start();
            download.join();
            assert (!download.isAlive());
            if (file.isFile() && downloadComplete) {
                URL url;
                url = new URL(download_URL);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setConnectTimeout(5000);
                http.setReadTimeout(5000);
                double fileSize = (double) http.getContentLengthLong();
                if (file.length() == fileSize) {
                    System.out.println("\n" + c.getName() + "\n-> Download complete!.");
                    wr.removeIndex(index);
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("[i] Can't connect to the host (can't reach m3u server) [network problem? proxy?] - UnknownHostException");
        }

    }

    private class DownloadTask implements Runnable {

        private static final DecimalFormat df = new DecimalFormat("#,###.00");
        private final File out;
        private final String download_url;
        private final String title;

        public DownloadTask(String download_url, File out, String title) {
            this.download_url = download_url;
            this.out = out;
            this.title = title;
        }

        @Override
        public void run() {
            try {
                HttpURLConnection http;
                URL url;
                url = new URL(download_url);
                if (v.isProxyMode()) {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(v.getCurrentProxyIP(), Integer.parseInt(v.getCurrentProxyPort())));
                    http = (HttpURLConnection) url.openConnection(proxy);
                    System.out.println("[***] Using proxy: " + v.getCurrentProxy());
                } else {
                    http = (HttpURLConnection) url.openConnection();
                }
                http.setConnectTimeout(5000);
                http.setReadTimeout(5000);
                long contentLong = http.getContentLengthLong();
                double fileSize = (double) contentLong;
                double fileSizeDwm;
                int code = http.getResponseCode();
                if (code == 200) {
                    boolean ok = checkFileExist(contentLong, title);
                    if (!ok) {
                        BufferedInputStream in = new BufferedInputStream(http.getInputStream());
                        FileOutputStream fos = new FileOutputStream(this.out);
                        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                        byte[] buffer = new byte[1024];
                        double downloaded = 0.00;
                        double fileSizeComplete;
                        int read;
                        double percentDownload;
                        long startTime = System.nanoTime();
                        while ((read = in.read(buffer, 0, 1024)) >= 0) {
                            bout.write(buffer, 0, read);
                            downloaded += read;
                            fileSizeDwm = (downloaded / 1024) / 1024;
                            fileSizeComplete = (fileSize / 1024) / 1024;
                            percentDownload = (downloaded * 100) / fileSize;
                            String percent = String.format("%.2f", percentDownload);
                            float bytesPerSec = (float) downloaded / ((System.nanoTime() - startTime) / 1000000000.0f);
                            float kbPerSec = bytesPerSec / (1024);
                            //float mbPerSec = kbPerSec / (1024);
                            String out = "Downloaded " + percent + "% - Speed: " + df.format(kbPerSec) + " Kbps -- " + title + " -- Size:  " + df.format(fileSizeDwm) + " MB /" + df.format(fileSizeComplete) + " MB";
                            System.out.print("\r" + out);
                            downloadComplete = true;
                        }
                        bout.close();
                        in.close();
                    }
                } else {
                    System.out.println("[!] Can't connect to host - try with a different IP -- "+title+" Skipped.");
                    downloadComplete = false;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("[!] Download stopped. back to menu...");
                Thread.currentThread().interrupt();
                e.printStackTrace();
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("[*] Error while connecting to the url (check if your connection/proxy working).");
            }
        }
    }

    private boolean checkFileExist(long contentLong, String title) throws IOException {
        String currentPath = System.getProperty("user.dir");
        String pathTitle = currentPath + "/M3U-VodDownloader/" + title;
        File file = new File(pathTitle);
        if (file.isDirectory()) {
            if (file.length() == contentLong) {
                v.removeChannelByName(title);
                return true;
            } else {
                wr.removeByName(title);
            }
        }
        return false;
    }
}

