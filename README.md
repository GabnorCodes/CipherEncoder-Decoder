# CipherEncoder-Decoder
\
This project allows the user to encode and decode text with their own substitution cipher keys, through the use of either a custom web based UI, or the use of an input file, output file, and the console. Users can also decrypt substitution ciphers without a key using a custom substitution cipher decryption algorithm.

**************************
Newly added features:
- Full custom UI written in HTML and CSS using the Bootstrap framework.
- Java backend using Spring Boot to connect UI with program.
**************************

Usage features:
- Full usage of program with a web UI.
- Creation of any number of custom keys
- Encryption using any custom key
- Decryption using any custom key
- Clearing previously made custom keys
- Cipher decryption without key

Next features to be added:
- More possible cipher types other than substitution

How the UI works with Spring Boot:
- The UI runs using a CipherController script containing get and post mappings which can be referenced within the html
- These get and post mappings allow the UI to call different java methods necessary to run the script, and display stored information
- Using the UI, the user can complete actions such as decrypting or encrypting a file, or even creating objects by passing the necessary parameters through forms

How the decryption algorithm works:
-----------------------------------------------------------------------------------------------------------------------------------------------
- First our dictionary text file is read and tokenized using SavePatternWordPairs script (only needs to be done once)
- This tokenization of the dictionary is saved in a text and data file as a map with the token, and set of words assigned to it
- When we run the decryption algorithm, we first call in this map and load it into memory
- Next, we use the tokenization method from earlier, and tokenize each word in the input
- These words are next sorted based on how unique each token is (how many words are assigned to it)
- Both the tokenized words and regular words are sorted with this method
- Starting the recursive decryption method, for the first word it just takes the first word that is assigned with the same token, and reassigns all characters in the sentance to match the new reassigned word
- The method calls itself again, and each time tries the same thing, while first checking to make sure the attempted word contains the same letters that have been reassigned already
- This continues until no matching words have been found for a word, in which case it returns false and goes backwards, repeating like this until it gets to the end
- Once the method reaches the last word, the method adds this decryption to the list of final decryptions, and adds the decryption key into the list of final keys
- The method keeps repeating until it reaches the start again, and then it prints out all of the completed sentances for selection
-----------------------------------------------------------------------------------------------------------------------------------------------

Why I don't have an interactive demo for this project (yet):
This cipher decoder runs on Java, with a front end designed in html and css using springboot to call java methods based on interactions with the UI. The issue with this, however, is the original java program ran on the basis of storing important data such as cipherkeys in .dat files using serialization. With the new UI, I tried to keep the program functioning as normal if you run it on the console, while alternatively working with the UI. This means that if I just hosted the site with a backend in java, all user's data would be stored together in the same .dat file. To fix this in the future, I plan on caching this data on the user side, but for this version, I am trying to simply push the UI update out. This update will come soon in the future, along with a running server and interactive demo, but for now, the video is the best demo we can have.
