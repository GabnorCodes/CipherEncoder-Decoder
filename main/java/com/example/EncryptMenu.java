package com.example;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class EncryptMenu {
    public static void main(String[] args, Scanner scanner) {
        int index = 1;
        ArrayList<CipherKey> keys = SavedKeys.loadKeys();

        System.out.println("Which key would you like to use?");
        System.out.println("Create new key: 1");
        
        for (CipherKey key : keys) {
            index++;
            System.out.println(key.getName() + ": " + index);
        }

        System.out.println("Clear Keys: " + (index + 1));
        System.out.println("Back: " + (index + 2));

        int input = scanner.nextInt();

        if (input == 1) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            addToList(CreateKey(scanner));
            main(args, scanner);
        } else if (input > 1 && input <= index) {
            // Select the corresponding key
            System.out.print("\033[H\033[2J");
            System.out.flush();
            CipherKey selectedKey = keys.get(input - 2); // Adjust index to match 0-based array
            System.out.println("Selected Key: " + selectedKey.getName());
            encrypt(scanner, selectedKey);
            Main.main(args);
        } else if (input == (index + 1)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            SavedKeys.clear();
            main(args, scanner);
        } else if (input == (index + 2)) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            Main.main(args);
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Invalid option. Please try again.");
            main(args, scanner);
        }

    }

    public static CipherKey CreateKey(Scanner scanner) {
        System.out.println("Key Name (Warning - No spaces): ");
        String name = scanner.next();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        HashMap<Character, Character> substitutions = new HashMap<Character, Character>();
        for (int i = 0; i < 26; i++) {
            addChar(scanner, substitutions, i);
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
        CipherKey newKey = new CipherKey(name, substitutions);
        // System.out.println(newKey);
        return newKey;
    }

    public static void addToList(CipherKey key) {
        ArrayList<CipherKey> keys = new ArrayList<CipherKey>();
        if (SavedKeys.fileExists() == true) {
            keys = SavedKeys.loadKeys();
            SavedKeys.clear();
        }
        keys.add(key);
        SavedKeys.save(keys);
    }

    public static void addChar(Scanner scanner, HashMap<Character, Character> substitutions, int index) {
        System.out.println("Character " + ((char) (65 + index)) + " = ");
        char nextChar = scanner.next().charAt(0);
        try {
            if (substitutions.containsValue(nextChar)) {
                throw new SameCharException("Cannot repeat characters already used in key");
            }
        } catch (SameCharException e) {
            System.out.println("Cannot repeat characters in the key");
            addChar(scanner, substitutions, index);
            return;
        } finally {
            substitutions.put((char) (65 + index), nextChar);
        }
    }

    public static void encrypt(Scanner scanner, CipherKey key) {
        // System.out.println("Enter file name:");
        // key.encryptFile(scanner.next());
        key.encryptFile("input.txt");
    }
}
