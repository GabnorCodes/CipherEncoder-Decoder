package com.example;

import java.io.*;
import java.util.ArrayList;

public class SavedKeys {
    private static final String FILENAME = "cipherkeys.dat";

    public static boolean fileExists() {
        File file = new File(FILENAME);
        return file.exists();
    }

    public static void save(ArrayList<CipherKey> keys) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(keys);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<CipherKey> loadKeys() {
        ArrayList<CipherKey> keys = new ArrayList<>();
        if (fileExists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
                keys = (ArrayList<CipherKey>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading keys: " + e.getMessage());
            }
        } else {
            // System.out.println("No saved keys found.");
        }
        return keys;
    }

    public static void clear() {
        File file = new File(FILENAME);
        if (file.exists()) {
            if (file.delete()) {
                // System.out.println("Saved keys cleared.");
            } else {
                // System.err.println("Failed to clear saved keys.");
            }
        } else {
            System.out.println("No file to clear.");
        }
    }
}
