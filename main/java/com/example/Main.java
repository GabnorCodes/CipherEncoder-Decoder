package com.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, welcome to the Cipher Hub!");

        Scanner scanner = new Scanner(System.in);
        boolean releaseInput = false;

        while (!releaseInput) {
            System.out.println("Would you like to encrypt or decrypt a message?");
            System.out.println("Encrypt: 1");
            System.out.println("Decrypt: 2");
            try {
                int input = scanner.nextInt();
                if (input == 1) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    // System.out.println("You selected to Encrypt a message.");
                    EncryptMenu.main(args, scanner);
                    releaseInput = true;
                } else if (input == 2) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    // System.out.println("You selected to Decrypt a message.");
                    DecryptMenu.main(args, scanner);
                    releaseInput = true;
                } else {
                    System.out.println("Invalid option. Please choose 1 for Encryption or 2 for Decryption.");
                }
            } catch (InputMismatchException e) {
                System.out.println("You must choose one of the menu options.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
}
