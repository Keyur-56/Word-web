package com.example.wordweb.managers;


import java.util.Random;

public class WordManager {
    private String[][] wordPairs = {
            {"Restaurant", "Cafeteria"},
            {"Beach", "Desert"},
            {"Mountain", "Hill"},
            {"River", "Stream"},
            {"Car", "Bicycle"},
            {"Phone", "Telephone"},
            {"Computer", "Laptop"},
            {"Book", "Novel"},
            {"Movie", "Film"},
            {"Music", "Song"},
            {"Coffee", "Tea"},
            {"Pizza", "Burger"},
            {"Football", "Soccer"},
            {"School", "College"},
            {"House", "Apartment"}
    };

    private Random random;

    public WordManager() {
        this.random = new Random();
    }

    public String[] getRandomWordPair() {
        int index = random.nextInt(wordPairs.length);
        return wordPairs[index];
    }

    public String[][] getAllWordPairs() {
        return wordPairs;
    }
}