# CipherEncoder-Decoder
\
This project allows the user to encode and decode text with their own substitution cipher keys, through the use of an input file, output file, and the console.

**************************
Newly added features:
- Cipher decryption without key
- Adding the uncovered key from decryption to keylist
- Tokenization of words from dictionary for use in decryption
**************************

Usage features:
- Creation of any number of custom keys
- Encryption using any custom key
- Decryption using any custom key
- Clearing previously made custom keys
- Cipher decryption without key


Next features to be added:
- More possible cipher types other than substitution

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
