package com.github.alfix00.options;

import com.github.alfix00.models.Vault;
import com.github.alfix00.engine.Functions;
import java.util.Scanner;

public class ShowOutputList {

    final private Functions f = new Functions();
    final private Scanner scancode = new Scanner(System.in);

    public void initialize(Vault v){
        System.out.flush();
        System.out.println("\n1) Show Channels");
        System.out.println("2) Show Categories");
        System.out.println("\n0) Back to menu'\n");
        System.out.print("\nChoice: ");
        int input = scancode.nextInt();
        switch (input) {
            case 1:
                v.printAllChannels();
                f.pressAnyKeyToContinue();
                break;
            case 2:
                v.printAllCategories();
                f.pressAnyKeyToContinue();
                break;
            default:
                f.pressAnyKeyToContinue();
                break;
        }
    }
}
