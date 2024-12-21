package com.example;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CipherKey implements java.io.Serializable {
    private String name;
    private HashMap<Character, Character> map = new HashMap<Character, Character>();

    public CipherKey(String name, HashMap<Character, Character> map) {
        this.name = name;
        this.map = map;
    }

    public CipherKey() {
        this.name = "Default";
        this.map.put('a', 'z');
        this.map.put('b', 'y');
        this.map.put('c', 'x');
        this.map.put('d', 'w');
        this.map.put('e', 'v');
        this.map.put('f', 'u');
        this.map.put('g', 't');
        this.map.put('h', 's');
        this.map.put('i', 'r');
        this.map.put('j', 'q');
        this.map.put('k', 'p');
        this.map.put('l', 'o');
        this.map.put('m', 'n');
        this.map.put('n', 'm');
        this.map.put('o', 'l');
        this.map.put('p', 'k');
        this.map.put('q', 'j');
        this.map.put('r', 'i');
        this.map.put('s', 'h');
        this.map.put('t', 'g');
        this.map.put('u', 'f');
        this.map.put('v', 'e');
        this.map.put('w', 'd');
        this.map.put('x', 'c');
        this.map.put('y', 'b');
        this.map.put('z', 'a');
    }

    public String getName() {
        return name;
    }

    public HashMap<Character, Character> getKey() {
        return map;
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder("Name: " + name);
        for (Character k : map.keySet()) {
            returnString.append("\n" + k + " : " + map.get(k));
        }
        return returnString.toString();
    }

    public void encryptFile(String inputFile) {
        String outputFile = "output.txt";
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
    
            int charRead;
            while ((charRead = reader.read()) != -1) {
                char currentChar = (char) charRead;
    
                // Handle only alphabetic characters
                if (Character.isLetter(currentChar)) {
                    boolean isUpperCase = Character.isUpperCase(currentChar);
                    char lowerChar = Character.toUpperCase(currentChar);
    
                    // Substitute and convert back to uppercase if needed
                    char encryptedChar = map.getOrDefault(lowerChar, lowerChar);
                    if (isUpperCase) {
                        encryptedChar = Character.toUpperCase(encryptedChar);
                    }
                    writer.write(encryptedChar);
                } else {
                    // Write non-alphabetic characters as is
                    writer.write(currentChar);
                }
            }

            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("File encrypted successfully! Saved to: " + outputFile);

        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + inputFile);
        } catch (IOException e) {
            System.out.println("An error occurred while reading or writing the file: " + e.getMessage());
        }
    }

    public void decryptFile(String inputFileName) {
        String outputFileName = "output.txt";
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
    
            int charRead;
            while ((charRead = reader.read()) != -1) {
                char currentChar = (char) charRead;
    
                if (Character.isLetter(currentChar)) {
                    boolean isUpperCase = Character.isUpperCase(currentChar);
                    char lowerChar = Character.toLowerCase(currentChar);
    
                    // Reverse lookup: find the original character for the cipher character
                    char decryptedChar = getDecryptedCharacter(lowerChar);
    
                    // Match case
                    if (isUpperCase) {
                        decryptedChar = Character.toUpperCase(decryptedChar);
                    } else {
                        decryptedChar = Character.toLowerCase(decryptedChar);
                    }
                    writer.write(decryptedChar);
                } else {
                    writer.write(currentChar); // Keep non-letters as is
                }
            }


            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("File decrypted successfully! Saved to: " + outputFileName);
    
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + inputFileName);
        } catch (IOException e) {
            System.out.println("An error occurred while reading or writing the file: " + e.getMessage());
        }
    }

    //Decrypt string method using regular lookup (not get decrypted character)
    public static String decryptString(String input, HashMap<Character, Character> map) {
        StringBuilder decryptedString = new StringBuilder();
        for (char currentChar : input.toCharArray()) {
            if (Character.isLetter(currentChar)) {
                boolean isUpperCase = Character.isUpperCase(currentChar);
                char lowerChar = Character.toLowerCase(currentChar);
    
                // Reverse lookup: find the original character for the cipher character
                char decryptedChar = map.getOrDefault(lowerChar, lowerChar);
    
                // Match case
                if (isUpperCase) {
                    decryptedChar = Character.toUpperCase(decryptedChar);
                } else {
                    decryptedChar = Character.toLowerCase(decryptedChar);
                }
                decryptedString.append(decryptedChar);
            } else {
                decryptedString.append(currentChar); // Keep non-letters as is
            }
        }
        return decryptedString.toString();
    }
    
    // Helper method to reverse lookup the key
    private char getDecryptedCharacter(char cipherChar) {
        for (HashMap.Entry<Character, Character> entry : map.entrySet()) {
            if (entry.getValue().equals(cipherChar)) {
                return entry.getKey(); // Return the original character
            }
        }
        return cipherChar; // Return as is if not found
    }
    
}
