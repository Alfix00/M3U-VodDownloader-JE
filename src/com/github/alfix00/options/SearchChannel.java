package com.github.alfix00.options;

import com.github.alfix00.engine.WriterReader;
import com.github.alfix00.models.Category;
import com.github.alfix00.models.Channel;
import com.github.alfix00.models.Vault;
import com.github.alfix00.view.Menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class SearchChannel {

    private final Scanner scancode = new Scanner(System.in);

    public void initialize(Vault v){
        System.out.flush();
        System.out.println("\n1) Search Channel");
        System.out.println("2) Search into Categories");
        System.out.println("\n0) Back to menu'\n");
        System.out.print("\nChoice: ");
        int input = scancode.nextInt();

        switch (input) {
            case 1 -> searchChannel(v);
            case 2 -> searchCategory(v);
            default -> {
            }
        }

    }

    // --------------------------------------------------------------- Searching Channels
    private void searchChannel(Vault v){
        try{
            ArrayList<Channel> channels = v.getAllChannels();
            ArrayList<Channel> search_list = new ArrayList<>();
            System.out.flush();
            System.out.print("\n 1) Search by name ");
            System.out.println(" 2) Search by index ");
            System.out.println("\n0) Back to menu \n");
            System.out.print("Choice : ");
            int choice = scancode.nextInt();
            switch (choice) {
                case 0 -> { return; }
                case 1 -> searchByName(channels, search_list);
                case 2 -> searchByIndex(channels, search_list, v);
                default -> {}
            }
         searchListResult(search_list, v);
        }catch (Exception e ){
            System.out.println("Error while searching channels.\n" + e);
        }
    }

    private void searchByName(ArrayList<Channel> channels, ArrayList<Channel> search_list){
        Scanner scanner = new Scanner(System.in);
        System.out.flush();
        System.out.print("\n Digit the channel name: ");
        String input = scanner.nextLine();
        if(input.length() > 1) {
            for (Channel c : channels) {
                if(c.getName().toLowerCase(Locale.ROOT).contains(input.toLowerCase(Locale.ROOT))){
                    search_list.add(c);
                }
            }
        }
    }

    private void searchByIndex(ArrayList<Channel> channels, ArrayList<Channel> search_list, Vault v){
        System.out.flush();
        System.out.print("\n Digit the channel index (only numbers) from 0 to "+v.getAllChannels().size()+" : ");
        int input_int = scancode.nextInt();
        if(input_int > -1 && input_int <= v.getAllChannels().size()){
            search_list.add(channels.get(input_int));
        }
    }


    // --------------------------------------------------------------- Searching Category
    private void searchCategory(Vault v){
        try{
            ArrayList<Category> categories = v.getAllCategory();
            ArrayList<Category> categories_list = new ArrayList<>();
            System.out.flush();
            System.out.print("\n 1) Search by name ");
            System.out.println(" 2) Search by index ");
            System.out.println("\n0) Back to menu \n");
            System.out.print("Choice : ");
            int choice = scancode.nextInt();
            switch (choice) {
                case 0 -> { return; }
                case 1 -> searchByNameCategory(categories, categories_list);
                case 2 -> searchByIndexCategory(categories, categories_list, v);
                default -> {}
            }
            searchListResultCategory(categories_list, v);
        }catch (Exception e ){
            System.out.println("Error while searching into categories.\n" + e);
        }
    }

    private void searchByNameCategory(ArrayList<Category> categories, ArrayList<Category> search_list){
        Scanner scanner = new Scanner(System.in);
        System.out.flush();
        System.out.print("\n Digit the category name: ");
        String input = scanner.nextLine();
        if(input.length() > 1) {
            for (Category c : categories) {
                if(c.getName().toLowerCase(Locale.ROOT).contains(input.toLowerCase(Locale.ROOT))){
                    search_list.add(c);
                }
            }
        }
    }

    private void searchByIndexCategory(ArrayList<Category> categories, ArrayList<Category> search_list, Vault v){
        System.out.flush();
        System.out.print("\n Digit the channel index (only numbers) from 0 to "+v.getAllChannels().size()+" : ");
        int input_int = scancode.nextInt();
        if(input_int > -1 && input_int <= v.getAllCategory().size()){
            search_list.add(categories.get(input_int));
        }
    }

    private void searchListResultCategory(ArrayList<Category> search_list, Vault v){
        try{
            System.out.flush();
            int index = 0;
            System.out.println("Result List:\n");
            for(Category c : search_list){
                System.out.println(index+"] "+c.getName()+" | Size ["+c.getSize()+"]");
                index = index + 1;
            }
            System.out.println("\nWanna search into category? \n");
            System.out.println(" 1) Search channel into category ");
            System.out.println(" 0) Back to menu \n");
            System.out.print("Choice : ");
            int choice = scancode.nextInt();
            if(choice == 1){
                System.out.print("\nInsert index number: ");
                int index2 = scancode.nextInt();
                if(index2 > -1 && index2 <= search_list.size()){
                    searchListResult(search_list.get(index2).getChannels(), v);
                }
            }
        }catch (Exception e ){
            System.out.println("[!] Wrong input. Back to menu\n" + e);
        }
    }

    // --------------------- Search into result list

    private void searchListResult(ArrayList<Channel> search_list, Vault v) throws IOException, ClassNotFoundException {
        if(search_list.size() > 0){
            System.out.flush();
            int index = 0;
            System.out.println();
            for(Channel c : search_list){
                System.out.println(index+"] "+c.getName() + " | [Category] : "+c.getCategory()+"\n\t"+c.getUrl());
                index = index + 1;
            }
            System.out.println("\n0) back to menu'\n");
            System.out.println("1) to add channel(s) to download list.");
            System.out.println("2) Search by channel");
            System.out.println("3) Search by category");

            System.out.print("Choice: ");
            int result = scancode.nextInt();
            if( result == 1){
                addIntoDownloadList(search_list, v);
            }else if(result == 2) {
                searchChannel(v);
            }else if(result == 3){
                searchCategory(v);
            }
            WriterReader wr = new WriterReader();
            wr.printChannels(v.getDwnList());
        }

    }

    private void addIntoDownloadList(ArrayList<Channel> search_list, Vault v){
        System.out.println("\n0) back to menu'\n");
        System.out.println("1) Insert by range ( ex 1 to 10)" );
        System.out.println("2) Insert single channel");
        System.out.println("3) Insert multiple index");

        System.out.print("Choice: ");
        int result = scancode.nextInt();
        if( result == 1){
            insertByRange(search_list, v);
        } else if ( result == 2 ){
            insertSingleChannel(search_list, v);
        } else if ( result == 3 ){
            insertMultipleIndex(search_list, v);
        }
    }

    private void insertByRange(ArrayList<Channel> search_list, Vault v){
        ArrayList<Channel> download_list = new ArrayList<>();
        System.out.println("[i] The index(s) must be in a range from 0 to "+(search_list.size()-1));
        try{
            boolean block = true;
            int first = 1, last = -1;
            while(block){
                System.out.print("\nInsert the first index: ");
                first = scancode.nextInt();
                if(first < 0 || first > search_list.size()-1){
                    System.out.println("[!] Index out of range, please enter a number from 0 to "+(search_list.size()-1));
                    insertByRange(search_list, v);
                }else{
                    block = false;
                }
            }
            block = true;
            while(block){
                System.out.print("Insert the last index: ");
                last = scancode.nextInt();
                if(last < first || last > search_list.size()-1){
                    System.out.println("[!] Index out of range, please enter a number from "+first+" to "+(search_list.size()-1));
                    insertByRange(search_list, v);
                }else{
                    block = false;
                }
            }
            while(first <= last){
                download_list.add(search_list.get(first));
                first = first + 1;
            }
            if(download_list.size() > 0){
                for(Channel c : download_list){
                    v.addIntoDwnList(c);
                    System.out.println("[i] "+ c.getName()+" Added to download list! ");
                    Thread.sleep(250);
                }
            }
        }catch (Exception e){
            System.out.println("[*] Iterruption catched - Back to menu...");
        }

    }

    private void insertSingleChannel(ArrayList<Channel> search_list, Vault v){
        System.out.print("\n[i] Insert the index of channel: ");
        int index = scancode.nextInt();
        if(index > -1 && index <= search_list.size()){
            v.addIntoDwnList(search_list.get(index));
        }
    }

    private void insertMultipleIndex(ArrayList<Channel> search_list, Vault v){
        System.out.print("\n[i] Insert (-2) to back to menu ");
        try{
            int index = -1;
            while(index != -2){
                System.out.print("\n[i] Insert the index of channel: ");
                index = scancode.nextInt();
                if(index > -1 && index <= search_list.size()){
                    v.addIntoDwnList(search_list.get(index));
                    System.out.println("[i] "+ search_list.get(index).getName()+" Added to download list! ");
                }
            }
        }catch (Exception e){
            System.out.println("[i] Going back ...");
        }
    }

}
