package com.example.flashlearn;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FlashRepository {

    private static FlashRepository instance;

    private static final String PREFS = "flashlearn_prefs";
    private static final String KEY_DECKS = "decks_json";

    private SharedPreferences prefs;
    private Gson gson = new Gson();

    public List<Deck> decks = new ArrayList<>();

    // Singleton Access
    public static FlashRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FlashRepository(context.getApplicationContext());
        }
        return instance;
    }

    // Private constructor
    private FlashRepository(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        load();         // Load existing saved decks
        preloadData();  // Auto-create the default deck if empty
    }

    // Save all decks to SharedPreferences
    private void save() {
        prefs.edit().putString(KEY_DECKS, gson.toJson(decks)).apply();
    }

    // Load decks at app startup
    private void load() {
        String json = prefs.getString(KEY_DECKS, "[]");
        Type type = new TypeToken<List<Deck>>() {}.getType();
        decks = gson.fromJson(json, type);

        if (decks == null) {
            decks = new ArrayList<>();
        }

        ensureDeckValidity();   // ⭐ IMPORTANT FIX

        if (decks.isEmpty()) {
            preloadData();
            save();
        }
    }


    // PRELOAD QUESTIONS ONLY IF APP HAS NO DECKS
    private void preloadData() {

        if (!decks.isEmpty()) return;  // Already have decks—don’t recreate

        Deck general = new Deck("General Trivia", "Automatically added");

        general.cards.add(new Card("What is the capital of France?", "Paris"));
        general.cards.add(new Card("How many continents are there?", "7"));
        general.cards.add(new Card("Which planet is known as the Red Planet?", "Mars"));
        general.cards.add(new Card("What gas do plants breathe in?", "Carbon Dioxide"));
        general.cards.add(new Card("What is the largest mammal?", "Blue Whale"));
        general.cards.add(new Card("Who wrote the play 'Romeo and Juliet'?", "William Shakespeare"));
        general.cards.add(new Card("What is H2O?", "Water"));
        general.cards.add(new Card("What year did the Titanic sink?", "1912"));
        general.cards.add(new Card("Who painted the Mona Lisa?", "Leonardo da Vinci"));
        general.cards.add(new Card("What is the fastest land animal?", "Cheetah"));
        general.cards.add(new Card("What is the capital of France?", "Paris"));
        general.cards.add(new Card("What is the largest planet in our solar system?", "Jupiter"));
        general.cards.add(new Card("Who wrote 'Romeo and Juliet'?", "William Shakespeare"));
        general.cards.add(new Card("How many continents are there?", "Seven"));
        general.cards.add(new Card("What is the chemical symbol for gold?", "Au"));
        general.cards.add(new Card("Who painted the Mona Lisa?", "Leonardo da Vinci"));
        general.cards.add(new Card("What is the fastest land animal?", "Cheetah"));
        general.cards.add(new Card("Which ocean is the largest?", "Pacific Ocean"));
        general.cards.add(new Card("What year did the Titanic sink?", "1912"));
        general.cards.add(new Card("What gas do plants absorb?", "Carbon Dioxide"));
        general.cards.add(new Card("What is the hardest natural substance?", "Diamond"));
        general.cards.add(new Card("Which country invented pizza?", "Italy"));
        general.cards.add(new Card("What is H2O?", "Water"));
        general.cards.add(new Card("Which planet is known as the Red Planet?", "Mars"));
        general.cards.add(new Card("What is the tallest mountain in the world?", "Mount Everest"));
        general.cards.add(new Card("What is the square root of 64?", "8"));
        general.cards.add(new Card("How many sides does a hexagon have?", "Six"));
        general.cards.add(new Card("Who discovered gravity?", "Isaac Newton"));
        general.cards.add(new Card("What is the capital of Japan?", "Tokyo"));
        general.cards.add(new Card("Which animal is known as the King of the Jungle?", "Lion"));
        general.cards.add(new Card("What do bees make?", "Honey"));
        general.cards.add(new Card("Which blood type is known as the universal donor?", "O Negative"));
        general.cards.add(new Card("What is the longest river in the world?", "Nile River"));
        general.cards.add(new Card("Which organ pumps blood?", "Heart"));
        general.cards.add(new Card("What is the first element on the periodic table?", "Hydrogen"));
        general.cards.add(new Card("Which country is known as the Land of the Rising Sun?", "Japan"));
        general.cards.add(new Card("Who invented the telephone?", "Alexander Graham Bell"));
        general.cards.add(new Card("What color is chlorophyll?", "Green"));
        general.cards.add(new Card("How many planets are in our solar system?", "Eight"));
        general.cards.add(new Card("What is the capital of Australia?", "Canberra"));
        general.cards.add(new Card("Which sport uses a bat and ball?", "Baseball"));
        general.cards.add(new Card("What galaxy do we live in?", "The Milky Way"));
        general.cards.add(new Card("How many days are in a leap year?", "366"));
        general.cards.add(new Card("Which country hosted the 2016 Olympics?", "Brazil"));
        general.cards.add(new Card("What is Earth's largest continent?", "Asia"));
        general.cards.add(new Card("Which animal can live both on land and water?", "Amphibian"));
        general.cards.add(new Card("What do you call frozen water?", "Ice"));
        general.cards.add(new Card("Who is known as the Father of Computers?", "Charles Babbage"));
        general.cards.add(new Card("What vitamin do humans get from sunlight?", "Vitamin D"));
        general.cards.add(new Card("How many stripes are on the U.S. flag?", "13"));
        general.cards.add(new Card("Which metal is liquid at room temperature?", "Mercury"));
        general.cards.add(new Card("What is the capital of Canada?", "Ottawa"));
        general.cards.add(new Card("What is the largest desert in the world?", "Sahara Desert"));
        general.cards.add(new Card("Which river runs through Egypt?", "Nile"));
        general.cards.add(new Card("How many hearts does an octopus have?", "Three"));
        general.cards.add(new Card("What is the smallest country in the world?", "Vatican City"));
        general.cards.add(new Card("What is a baby sheep called?", "Lamb"));
        general.cards.add(new Card("What gas do humans need to survive?", "Oxygen"));
        general.cards.add(new Card("Who invented the light bulb?", "Thomas Edison"));
        general.cards.add(new Card("Which animal is known for its black and white stripes?", "Zebra"));
        general.cards.add(new Card("Who was the first President of the United States?", "George Washington"));
        general.cards.add(new Card("Which continent is the coldest?", "Antarctica"));
        general.cards.add(new Card("What is the boiling point of water in Celsius?", "100°C"));
        general.cards.add(new Card("How many teeth do adult humans have?", "32"));
        general.cards.add(new Card("Which country is famous for sushi?", "Japan"));
        general.cards.add(new Card("What is the capital of India?", "New Delhi"));
        general.cards.add(new Card("How many legs does a spider have?", "Eight"));
        general.cards.add(new Card("What is the largest bird in the world?", "Ostrich"));
        general.cards.add(new Card("Which gas makes up most of Earth's atmosphere?", "Nitrogen"));
        general.cards.add(new Card("What is the largest organ in the human body?", "Skin"));
        general.cards.add(new Card("Which country invented paper?", "China"));
        general.cards.add(new Card("How many bones does a baby have?", "Around 300"));
        general.cards.add(new Card("Which U.S. state is famous for Hollywood?", "California"));
        general.cards.add(new Card("What is the capital of Russia?", "Moscow"));
        general.cards.add(new Card("How many colors are in a rainbow?", "Seven"));
        general.cards.add(new Card("What is the capital of England?", "London"));
        general.cards.add(new Card("Which insect has 100 legs?", "Centipede"));
        general.cards.add(new Card("Who discovered America?", "Christopher Columbus"));
        general.cards.add(new Card("What is the largest island in the world?", "Greenland"));
        general.cards.add(new Card("Which country gifted the Statue of Liberty?", "France"));
        general.cards.add(new Card("Which musician is known as the King of Pop?", "Michael Jackson"));
        general.cards.add(new Card("What is the capital of Germany?", "Berlin"));
        general.cards.add(new Card("Which ocean is the warmest?", "Indian Ocean"));
        general.cards.add(new Card("What part of the plant makes food?", "Leaves"));
        general.cards.add(new Card("What is the currency of the UK?", "Pound Sterling"));
        general.cards.add(new Card("Which country is known for kangaroos?", "Australia"));
        general.cards.add(new Card("Which bird can mimic human speech?", "Parrot"));
        general.cards.add(new Card("Who was the 16th President of the United States?", "Abraham Lincoln"));
        general.cards.add(new Card("What is the capital of China?", "Beijing"));
        general.cards.add(new Card("Which animal is the largest in the ocean?", "Blue Whale"));
        general.cards.add(new Card("How many hours are in a day?", "24"));
        general.cards.add(new Card("Which fruit keeps the doctor away?", "Apple"));
        general.cards.add(new Card("What is the tallest building in the world?", "Burj Khalifa"));
        general.cards.add(new Card("What is the largest volcano on Earth?", "Mauna Loa"));
        general.cards.add(new Card("What is the capital of South Korea?", "Seoul"));
        general.cards.add(new Card("Which country is home to the Eiffel Tower?", "France"));
        general.cards.add(new Card("How many meters are in a kilometer?", "1000"));
        general.cards.add(new Card("Which planet has rings?", "Saturn"));
        general.cards.add(new Card("Who founded Microsoft?", "Bill Gates"));
        general.cards.add(new Card("What animal is known for its hump?", "Camel"));
        general.cards.add(new Card("What do cows drink?", "Water"));
        decks.add(general);

        // Save the preloaded deck
        save();
    }

    // Add new deck
    public void addDeck(String name, String description) {
        decks.add(new Deck(name, description));
        save();
    }

    // Add card to deck
    public void addCard(int deckIndex, Card card) {
        decks.get(deckIndex).cards.add(card);
        save();
    }

    // Delete card
    public void deleteCard(int deckIndex, int cardIndex) {
        decks.get(deckIndex).cards.remove(cardIndex);
        save();
    }
    private void ensureDeckValidity() {
        for (Deck d : decks) {
            if (d.cards == null) {
                d.cards = new ArrayList<>();
            }
        }
    }
    public void deleteDeck(int deckIndex) {
        if (deckIndex >= 0 && deckIndex < decks.size()) {
            decks.remove(deckIndex);
            save();
        }
    }
}
