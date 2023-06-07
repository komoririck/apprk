package com.example.apprk;

import java.util.List;

public class Character {
    int type;
    char character;
    String romaji;
    String onyomi;
    String kunyomi;
    String meaning;
    List<String> exemples;

    public static int getJapaneseCharType(char c) {
        if ((c >= '\u3040' && c <= '\u309F') || (c >= '\u30A0' && c <= '\u30FF')) {
            // Hiragana range: \u3040 to \u309F
            // Katakana range: \u30A0 to \u30FF
            if (c >= '\u3040' && c <= '\u309F') {
                return 1; // Hiragana
            } else {
                return 2; // Katakana
            }
        } else {
            return 0; // Not a Japanese character
        }
    }

    public Character(char character) {
        this.type = getJapaneseCharType(character);
        this.character = character;
    }

    public String getRomaji() {
        return romaji;
    }

    public void setRomaji(String romaji) {
        this.romaji = romaji;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public String getOnyomi() {
        return onyomi;
    }

    public void setOnyomi(String onyomi) {
        this.onyomi = onyomi;
    }

    public String getKunyomi() {
        return kunyomi;
    }

    public void setKunyomi(String kunyomi) {
        this.kunyomi = kunyomi;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public List<String> getExemples() {
        return exemples;
    }

    public void setExemples(List<String> exemples) {
        this.exemples = exemples;
    }

}
