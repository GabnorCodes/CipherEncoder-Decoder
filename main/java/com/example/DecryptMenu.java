package com.example;

import java.util.Scanner;
import java.util.ArrayList;

public class DecryptMenu {

    public static void main(String[] args, Scanner scanner) {
        ArrayList<CipherKey> keys = SavedKeys.loadKeys();

        if (keys.isEmpty()) {
            System.out.println("No keys found. Please create an encryption key first in the EncryptMenu.");
            EncryptMenu.main(args, scanner);
            return;
        }

        System.out.println("Which key would you like to use for decryption?");
        System.out.println("Create new key: 1");
        int index = 1;
        for (CipherKey key : keys) {
            index++;
            System.out.println(key.getName() + ": " + index);
        }

        System.out.println("Clear Keys: " + (index + 1));
        System.out.println("Back: " + (index + 2));
        int input = scanner.nextInt();

        if (input > 1 && input <= keys.size() + 1) {
            // Select the corresponding key
            CipherKey selectedKey = keys.get(input - 2);
            System.out.println("Selected Key: " + selectedKey.getName());

            // System.out.println("Enter the name of the file you want to decrypt:");
            // String fileName = scanner.next();
            String fileName = "input.txt";

            selectedKey.decryptFile(fileName);

            System.out.println("Decryption complete.");
            main(args, scanner); // Return to DecryptMenu after decryption
        } else if (input == index + 2) {
            Main.main(args); // Return to the main menu
        } else if (input == (index + 1)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            SavedKeys.clear();
            main(args, scanner);
        } else if (input == 1) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            EncryptMenu.addToList(EncryptMenu.CreateKey(scanner));
            System.out.print("\033[H\033[2J");
            System.out.flush();
            main(args, scanner);
        } else {
            System.out.println("Invalid option. Please try again.");
            main(args, scanner);
        }
    }
}
