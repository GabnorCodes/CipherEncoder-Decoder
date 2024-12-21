package com.example;

import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SavePatternWordPairs {
    public static void main(String[] args) {
        readInWords();
    }

    public static void saveMapToFile(HashMap<String, Set<String>> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("map.dat"))) {
            oos.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToTxt() {}

    @SuppressWarnings("unchecked")
    public static HashMap<String, Set<String>> loadMapFromFile() {
        HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("map.dat"))) {
            map = (HashMap<String, Set<String>>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Set<String> readWordsOnly() {
        Set<String> set = new TreeSet<String>();
        try (BufferedReader br = new BufferedReader(new FileReader("complete_wordlist.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\n");
                for (String word : words) {
                    set.add(word);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return set;
    }

    public static void readInWords() {
        HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
        try (BufferedReader br = new BufferedReader(new FileReader("complete_wordlist.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\n");
                for (String word : words) {
                    word = word.toLowerCase();
                    String pattern = tokenize(word);
                    if (map.containsKey(pattern)) {
                        map.get(pattern).add(word);
                    } else {
                        Set<String> set = new TreeSet<String>();
                        set.add(word);
                        map.put(pattern, set);
                    }
                    System.out.println(word + " : " + pattern);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        saveMapToFile(map);
        HashMap<String, Set<String>> loadedMap = loadMapFromFile();
        Set<String> set = loadedMap.keySet();
        try (BufferedWriter br = new BufferedWriter(new FileWriter("words_alpha.txt"))) {
            for (String val : set) {
                br.write(val + " " + "\n");
                System.out.println(val);
                for (String word : loadedMap.get(val)) {
                    br.write(word + ",");
                    System.out.print(word + ",");
                }
                br.write("\n");
                System.out.println("\n");
            }
        } catch (IOException e) {
            System.err.println("Error Writing txt file: " + e.getMessage());
        }
    }

    public static String tokenize(String word) {
        List<Character> chars = new ArrayList<>();
        String outString = "";
        int index = 0;
        for (int i = 0; i < word.length(); i++) {
            if (i > 0) {
                outString += ".";
            }
            if (chars.contains(word.charAt(i))) {
                outString += chars.indexOf(word.charAt(i));
            } else {
                outString += index++;
                chars.add(word.charAt(i));
            }
        }
        return outString;
    }
}
