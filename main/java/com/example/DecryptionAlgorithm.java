package com.example;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class DecryptionAlgorithm {
    private static String[] finalWords;

    public static String rebuildMessageInOriginalOrder(String[] words, int[] originalIndices) {
        String[] reorderedWords = new String[words.length];

        for (int i = 0; i < words.length; i++) {
            reorderedWords[originalIndices[i]] = words[i];
        }

        StringBuilder rebuiltMessage = new StringBuilder();
        for (String word : reorderedWords) {
            rebuiltMessage.append(word).append(" ");
        }

        return rebuiltMessage.toString().trim();
    }

    public static void substitution(Scanner scanner) {
        String outputFile = "output.txt";
        String encryptedMessage = "";

        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            System.out.println("Decryption in progress...");
            StringBuilder encryptedMessageBuilder = new StringBuilder();
            int charRead;
            while ((charRead = reader.read()) != -1) {
                encryptedMessageBuilder.append((char) charRead);
            }
            encryptedMessage = encryptedMessageBuilder.toString().trim();

            if (encryptedMessage.isEmpty()) {
                System.out.println("Input file is empty.");
                return;
            }

            System.out.println("Encrypted Message: " + encryptedMessage);

            String[] words = encryptedMessage.split(" ");
            String[] tokenizedWords = new String[words.length];
            int[] originalIndices = new int[words.length];

            for (int i = 0; i < words.length; i++) {
                originalIndices[i] = i;
            }

            HashMap<String, Set<String>> dict = SavePatternWordPairs.loadMapFromFile();
            if (dict == null || dict.isEmpty()) {
                System.out.println("Dictionary is empty or could not be loaded.");
                return;
            }

            for (int i = 0; i < words.length; i++) {
                String pattern = SavePatternWordPairs.tokenize(words[i]);
                tokenizedWords[i] = pattern;
            }

            sortByTokenRarity(tokenizedWords, words, dict, originalIndices);

            System.out.println("Sorted Tokenized Words: " + Arrays.toString(tokenizedWords));
            System.out.println("Sorted Words: " + Arrays.toString(words));
            System.out.println("Original Indices: " + Arrays.toString(originalIndices));

            HashMap<Character, Character> replacedChars = new HashMap<>();
            finalWords = Arrays.copyOf(words, words.length);

            Map.Entry<String[], HashMap<Character, Character>> map = new AbstractMap.SimpleEntry<>(finalWords, replacedChars);
            List<String> allSolutions = new ArrayList<>();
            List<HashMap<Character,Character>> solutionKeys = new ArrayList<>();

            substituteWord(tokenizedWords, words, dict, originalIndices, 0, map, allSolutions, solutionKeys);
            System.out.print("\033[H\033[2J");
            System.out.flush();
            if (allSolutions.isEmpty()) {
                System.out.println("No possible solutions found.");
            } else {
                System.out.println("---------------------------------------------------------------------------------------------------------------");
                if (allSolutions.size() > 1) {
                    System.out.println("There are " + allSolutions.size() + " possible solutions:");
                }
                else {
                    System.out.println("There is " + allSolutions.size() + " possible solution:");
                }
                System.out.println();

                for (int i = 0; i < allSolutions.size(); i++) {
                    String solution = allSolutions.get(i);
                    writer.write("Potential Solution #" + (i + 1) + ": " + solution + System.lineSeparator());
                    System.out.println("Potential Solution #" + (i + 1) + ": " + solution);
                }

                System.out.println("---------------------------------------------------------------------------------------------------------------");
                System.out.println();
                System.out.print("Please pick the solution that makes the most sense and the key dictionary will be updated accordingly: ");
                int choice = scanner.nextInt();
                while (choice < 1 || choice > allSolutions.size()) {
                    System.out.println("Invalid choice. Please enter a valid index.");
                    choice = scanner.nextInt();
                }

                System.out.println();
                System.out.println("Please enter the name of the key: ");
                String keyName = scanner.next();
                
                HashMap<Character, Character> solutionKey = solutionKeys.get(choice - 1);
                HashMap<Character, Character> decryptionKey = new HashMap<>();

                for (int i = 0; i < 26; i++) {
                    char encryptedChar = (char) (i + 97);
                    Character decryptedChar = getKeyByValue(solutionKey, encryptedChar);

                    if (decryptedChar != null) {
                        decryptionKey.put(encryptedChar, decryptedChar);
                    } else {
                        decryptionKey.put(encryptedChar, encryptedChar);
                    }
                }
                
                EncryptMenu.addToList(EncryptMenu.CreateKey(keyName, decryptionKey));
                            
            }

        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + "input.txt");
        } catch (IOException e) {
            System.out.println("An error occurred while reading or writing the file: " + e.getMessage());
        }
    }

    public static void substituteWord(
            String[] tokenArray,
            String[] wordArray,
            HashMap<String, Set<String>> dict,
            int[] originalIndices,
            int index,
            Map.Entry<String[], HashMap<Character, Character>> arrayAndMap,
            List<String> allSolutions, List<HashMap<Character, Character>> solutionKeys) {

        if (index >= tokenArray.length) {
            return;
        }

        HashMap<Character, Character> replacedChars = arrayAndMap.getValue();
        String[] finalArray = arrayAndMap.getKey();
        Set<String> candidateWords = dict.get(tokenArray[index]);

        if (candidateWords == null) {
            System.out.println("No candidate words found for token: " + tokenArray[index]);
            return;
        }

        for (String candidateWord : candidateWords) {
            if (checkWordtoMap(finalArray, wordArray, candidateWord, index, replacedChars)) {
                Map.Entry<String[], HashMap<Character, Character>> updatedState =
                        refactor(candidateWord, wordArray[index], wordArray, replacedChars, finalArray, index);

                if (updatedState != null) {
                    finalWords = updatedState.getKey();
                    if (index + 1 < tokenArray.length) {
                        substituteWord(tokenArray, wordArray, dict, originalIndices, index + 1, updatedState, allSolutions, solutionKeys);
                    } else {
                        String solution = rebuildMessageInOriginalOrder(finalWords, originalIndices);
                        allSolutions.add(solution);
                        solutionKeys.add(updatedState.getValue());
                    }
                }
            }
        }
    }

    public static Boolean checkWordtoMap(String[] finalArray, String[] wordArray, String candidateWord, int index, HashMap<Character, Character> checkMap) {
        String encryptedWord = wordArray[index];

        for (int i = 0; i < candidateWord.length(); i++) {
            char candidateChar = candidateWord.charAt(i);
            char encryptedChar = encryptedWord.charAt(i);

            if (checkMap.containsValue(candidateChar) && !Objects.equals(getKeyByValue(checkMap, candidateChar), encryptedChar)) {
                return false;
            }

            if (checkMap.containsKey(encryptedChar) && !Objects.equals(checkMap.get(encryptedChar), candidateChar)) {
                return false;
            }
        }

        return true;
    }

    public static void sortByTokenRarity(String[] tokenArray, String[] wordArray, HashMap<String, Set<String>> dict, int[] originalIndices) {
        Integer[] indices = new Integer[tokenArray.length];
        for (int i = 0; i < tokenArray.length; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, Comparator.comparingInt(i -> dict.getOrDefault(tokenArray[i], Collections.emptySet()).size()));

        String[] sortedTokens = new String[tokenArray.length];
        String[] sortedWords = new String[wordArray.length];
        for (int i = 0; i < indices.length; i++) {
            sortedTokens[i] = tokenArray[indices[i]];
            sortedWords[i] = wordArray[indices[i]];
        }

        System.arraycopy(sortedTokens, 0, tokenArray, 0, tokenArray.length);
        System.arraycopy(sortedWords, 0, wordArray, 0, wordArray.length);

        for (int i = 0; i < indices.length; i++) {
            originalIndices[i] = indices[i];
        }
    }

    public static Map.Entry<String[], HashMap<Character, Character>> refactor(
            String match,
            String cipherWord,
            String[] wordArray,
            HashMap<Character, Character> replacedChars,
            String[] finalArray,
            int index) {

        HashMap<Character, Character> replacedCharsCopy = new HashMap<>(replacedChars);
        String[] finalArrayCopy = finalArray.clone();

        for (int i = 0; i < match.length(); i++) {
            char matchChar = match.charAt(i);
            char cipherChar = cipherWord.charAt(i);

            if (replacedCharsCopy.containsKey(cipherChar) && replacedCharsCopy.get(cipherChar) != matchChar) {
                System.out.println("Conflict in refactor: " + cipherChar + " -> " + matchChar);
                return null;
            }

            replacedCharsCopy.put(cipherChar, matchChar);
        }

        for (int i = 0; i < wordArray.length; i++) {
            StringBuilder refactoredWord = new StringBuilder();

            for (char c : wordArray[i].toCharArray()) {
                refactoredWord.append(replacedCharsCopy.getOrDefault(c, c));
            }

            finalArrayCopy[i] = refactoredWord.toString();
        }

        return new AbstractMap.SimpleEntry<>(finalArrayCopy, replacedCharsCopy);
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}