package com.github.alfix00.engine;
import com.github.alfix00.models.Category;
import com.github.alfix00.models.Channel;
import com.github.alfix00.models.Vault;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadM3U {

    private ArrayList<Channel> channels = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();

    private final Functions functions = new Functions();

    public ReadM3U(){
    }

    public Vault initialize(){
        boolean isValid = ScrapeList();
        Vault v = new Vault();
        if(isValid){
            v.setAllCategory(getCategories());
            v.setAllChannels(getChannels());
        }
        return v;
    }

    private boolean ScrapeList(){
        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        String channelName = null;
        String channelURL = null;
        String categoryName = null;
        String logoURL = null;
        int index_key = 0;
        int index = 0;

        if (!functions.checkM3U()) return false;
        String path = functions.getM3uPath();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("#EXTINF")) {
                    /* find name of channel */
                    Pattern pattern = Pattern.compile("tvg-name=\"(.*?)\"");
                    Matcher matcher = pattern.matcher(line);
                    if( matcher.find() ) {
                        channelName =  matcher.group(1);
                    }
                    /* find category of channel */
                    pattern = Pattern.compile("group-title=\"(.*?)\"");
                    matcher = pattern.matcher(line);
                    if( matcher.find() ) {
                        categoryName =  matcher.group(1);
                        if(!titles.contains(categoryName)) {
                            titles.add(categoryName);
                            Category cat = new Category(categoryName);
                            categories.add(cat);
                            index_key = index_key + 1;
                        }
                    }
                    /* find LOGO of channel */
                    pattern = Pattern.compile("tvg-logo=\"(.*?)\"");
                    matcher = pattern.matcher(line);
                    if( matcher.find() ) {
                        logoURL =  matcher.group(1);
                    }
                } else if (line.contains("http")) {
                    channelURL = line;
                    if(index != 0){
                        index = index - 1;
                    }
                }
                if(channelName != null && channelURL != null ){
                    Channel newChannel;
                    if (logoURL != null){
                        newChannel = new Channel(channelName,channelURL,logoURL);
                    } else {
                        newChannel = new Channel(channelName, channelURL);
                    }
                    if(categoryName != null){
                        newChannel.setCategory(categoryName);
                        categoryName = null;
                    }
                    newChannel.setIndex(index);
                    appendChannel(newChannel);
                    channelName = null;
                    channelURL = null;
                    logoURL = null;
                }
                index = index + 1;
            }
            addCategories(categories);
            fillCategories(getChannels(),getCategories());          // fill categories with channels
            categories.removeIf(c -> c.getSize() < 1);              // remove empty categories
            return true;
        }catch(IOException e){
            System.out.println("Error while scraping m3u file "+ e);
        }
        return false;
    }

    private void fillCategories(ArrayList<Channel> channels, ArrayList<Category> categories){
       for(Category category : categories){
           for(Channel channel: channels){
               if(Objects.equals(category.getName(), channel.getCategory())){
                   category.appendChannel(channel);
               }
           }
       }
    }


    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels_) {
        channels = channels_;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories_) {
        categories = categories_;
    }

    public void addCategories(ArrayList<Category> categories){
        this.categories = categories;
    }

    public void appendChannel(Channel c){
        channels.add(c);
    }

}
