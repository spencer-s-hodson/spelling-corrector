package spell;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SpellCorrector implements ISpellCorrector{
    private ITrie dictionary;

    public SpellCorrector() {
        dictionary = new Trie();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File words = new File(dictionaryFileName);
        Scanner scanner = new Scanner(words);
        while (scanner.hasNext()) {
            String word = scanner.next().toLowerCase();
            dictionary.add(word);
        }
        scanner.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();
        if (dictionary.find(inputWord) != null) {
            return inputWord;
        }

        Set<String> words1 = new HashSet<>();
        deletion(words1, inputWord);
        transposition(words1, inputWord);
        alteration(words1, inputWord);
        insertion(words1, inputWord);
        if (getWinner(words1) == null) {
            Set<String> words2 = new HashSet<>();
            for (String word : words1) {
                deletion(words2, word);
                transposition(words2, word);
                alteration(words2, word);
                insertion(words2, word);
            }
            return getWinner(words2);
        }
        return getWinner(words1);
    }

    private void deletion(Set<String> words, String inputWord) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < inputWord.length(); i++) {
            str.append(inputWord);
            str.deleteCharAt(i);
            words.add(str.toString());
            str.setLength(0);
        }
    }

    private void transposition(Set<String> words, String inputWord) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < inputWord.length() - 1; i++) {
            str.append(inputWord);
            char first = str.charAt(i);
            char second = str.charAt(i+1);
            str.delete(i, i+2);
            str.insert(i, first);
            str.insert(i, second);
            words.add(str.toString());
            str.setLength(0);
        }
    }

    private void alteration(Set<String> words, String inputWord) {
        StringBuilder str = new StringBuilder(inputWord);
        for (int i = 0; i < inputWord.length(); i++) {
            char original = str.charAt(i);
            for (char c = 'a'; c < 'z'; c++) {
                str.deleteCharAt(i);
                str.insert(i, c);
                if (c != original) {
                    words.add(str.toString());
                }
            }
            str.deleteCharAt(i);
            str.insert(i, original);
        }
    }

    private void insertion(Set<String> words, String inputWord) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i <= inputWord.length(); i++) {
            for (char c = 'a'; c < 'z'; c++) {
                str.append(inputWord);
                str.insert(i, c);
                words.add(str.toString());
                str.setLength(0);
            }
        }
    }

    private String getWinner(Set<String> words) {
        int highest = 0;
        String winner = null;
        for (String word : words) {
            if (dictionary.find(word) != null) {
                int value = dictionary.find(word).getValue();
                if (value > highest) {
                    highest = value;
                    winner = word;
                }
            }
        }
        return winner;
    }
}
