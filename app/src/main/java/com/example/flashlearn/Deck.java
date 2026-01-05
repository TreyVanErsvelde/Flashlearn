package com.example.flashlearn;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    public String name;
    public String description;
    public List<Card> cards;

    public Deck(String name, String description) {
        this.name = name;
        this.description = description;
        this.cards = new ArrayList<>();
    }
}
