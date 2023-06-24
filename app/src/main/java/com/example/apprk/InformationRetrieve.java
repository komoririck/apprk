package com.example.apprk;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InformationRetrieve {
    //reminder for myself and if someone read
    //i'm saving and checking a loot of stuff as string, because i started the project without indexing the lists of words
        //A better(future) aproach would be generate a index for each list, them, instead of comparing/saving frases we can just use the index
            //this would fix the problem with the wordlist class where you need to take extra care to the returned word to be the rightone
            //also will be way more efficient
    private static final String PROGRESS_FILE = "progress.ser";
    private static String LANGUAGE = null;
    private static  boolean randomizer;
    private static  boolean recomendation;
    private static  boolean onlyQuestions;
    private static  boolean onlySlides;
    private static  int numSlides;
    private static  int numQuestionType1;
    private static  int numQuestionType2;
    private static  int numQuestionType3;
    private static  int numQuestionType4;
    private static  int numCorrectQuestionType1;
    private static  int numCorrectQuestionType2;
    private static  int numCorrectQuestionType3;
    private static  int numCorrectQuestionType4;
    private static  List<String[]> characteresAcertados;
    private static  boolean kanjiMenuItemN5;
    private static  boolean kanjiMenuItemN4;
    private static  boolean kanjiMenuItemN3;
    private static  boolean kanjiMenuItemN2;
    private static  boolean kanjiMenuItemN1;
    private static  boolean wordMenuItemN5;
    private static  boolean wordMenuItemN4;
    private static  boolean wordMenuItemN3;
    private static  boolean wordMenuItemN2;
    private static  boolean wordMenuItemN1;
    private static List<String> favoritedWord;
    private static List<String> favoritedkanji;
    private static  boolean favoritedStatus;
    private static  boolean onlyFavorite;
    private static Context context;
    private static  int numQuestionType5;
    private static  int numQuestionType6;
    private static  int numCorrectQuestionType5;
    private static  int numCorrectQuestionType6;

    public InformationRetrieve(Context context) {
        InformationRetrieve.context = context;
        LANGUAGE = Locale.getDefault().getLanguage();
        if (new File(context.getFilesDir(), PROGRESS_FILE).exists()) {
            loadProgress();
        } else {
            initializeProgress();
        }
    }
    public static void saveProgress() {
        try {
            FileOutputStream fileOut = new FileOutputStream(new File(context.getFilesDir(), PROGRESS_FILE));
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeBoolean(randomizer);
            out.writeBoolean(recomendation);
            out.writeBoolean(onlyQuestions);
            out.writeBoolean(onlySlides);
            out.writeInt(numSlides);
            out.writeInt(numQuestionType1);
            out.writeInt(numQuestionType2);
            out.writeInt(numQuestionType3);
            out.writeInt(numQuestionType4);
            out.writeInt(numCorrectQuestionType1);
            out.writeInt(numCorrectQuestionType2);
            out.writeInt(numCorrectQuestionType3);
            out.writeInt(numCorrectQuestionType4);
            out.writeObject(characteresAcertados);
            out.writeBoolean(kanjiMenuItemN5);
            out.writeBoolean(kanjiMenuItemN4);
            out.writeBoolean(kanjiMenuItemN3);
            out.writeBoolean(kanjiMenuItemN2);
            out.writeBoolean(kanjiMenuItemN1);
            out.writeBoolean(wordMenuItemN5);
            out.writeBoolean(wordMenuItemN4);
            out.writeBoolean(wordMenuItemN3);
            out.writeBoolean(wordMenuItemN2);
            out.writeBoolean(wordMenuItemN1);
            out.writeObject(favoritedWord);
            out.writeObject(favoritedkanji);
            out.writeBoolean(favoritedStatus);
            out.writeBoolean(onlyFavorite);
            out.writeInt(numQuestionType5);
            out.writeInt(numQuestionType6);
            out.writeInt(numCorrectQuestionType5);
            out.writeInt(numCorrectQuestionType6);
            out.close();
            fileOut.close();
            System.out.println("Progress saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving progress to file: " + e.getMessage());
        }
    }
    public static void loadProgress() {
        try {
            FileInputStream fileIn = new FileInputStream(new File(context.getFilesDir(), PROGRESS_FILE));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            randomizer = in.readBoolean();
            recomendation = in.readBoolean();
            onlyQuestions = in.readBoolean();
            onlySlides = in.readBoolean();
            numSlides = in.readInt();
            numQuestionType1 = in.readInt();
            numQuestionType2 = in.readInt();
            numQuestionType3 = in.readInt();
            numQuestionType4 = in.readInt();
            numCorrectQuestionType1 = in.readInt();
            numCorrectQuestionType2 = in.readInt();
            numCorrectQuestionType3 = in.readInt();
            numCorrectQuestionType4 = in.readInt();
            characteresAcertados = (List<String[]>) in.readObject();
            kanjiMenuItemN5 = in.readBoolean();
            kanjiMenuItemN4 = in.readBoolean();
            kanjiMenuItemN3 = in.readBoolean();
            kanjiMenuItemN2 = in.readBoolean();
            kanjiMenuItemN1 = in.readBoolean();
            wordMenuItemN5 = in.readBoolean();
            wordMenuItemN4 = in.readBoolean();
            wordMenuItemN3 = in.readBoolean();
            wordMenuItemN2 = in.readBoolean();
            wordMenuItemN1 = in.readBoolean();
            favoritedWord = (List<String>) in.readObject();
            favoritedkanji = (List<String>) in.readObject();
            favoritedStatus = in.readBoolean();
            onlyFavorite = in.readBoolean();
            numQuestionType5 = in.readInt();
            numQuestionType6 = in.readInt();
            numCorrectQuestionType6 = in.readInt();
            numCorrectQuestionType6 = in.readInt();
            in.close();
            fileIn.close();
            System.out.println("Progress loaded from file.");
        } catch (IOException e) {
            System.out.println("Error loading progress from file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static  void initializeProgress() {
        randomizer = false;
        recomendation = false;
        onlyQuestions = false;
        onlySlides = false;
        numSlides = 5;
        numQuestionType1 = 3;
        numQuestionType2 = 3;
        numQuestionType3 = 3;
        numQuestionType4 = 3;
        numQuestionType5 = 3;
        numQuestionType6 = 3;
        numCorrectQuestionType1 = 0;
        numCorrectQuestionType2 = 0;
        numCorrectQuestionType3 = 0;
        numCorrectQuestionType4 = 0;
        numCorrectQuestionType5 = 0;
        numCorrectQuestionType6 = 0;
        characteresAcertados = new ArrayList<>();
        kanjiMenuItemN5 = true;
        kanjiMenuItemN4 = true;
        kanjiMenuItemN3 = true;
        kanjiMenuItemN2 = true;
        kanjiMenuItemN1 = true;
        wordMenuItemN5 = true;
        wordMenuItemN4 = true;
        wordMenuItemN3 = true;
        wordMenuItemN2 = true;
        wordMenuItemN1 = true;
        favoritedWord = new ArrayList<>();
        favoritedkanji = new ArrayList<>();
        favoritedStatus = false;
        onlyFavorite = false;
        saveProgress();
    }
    public static int getNumSlides() {
        return numSlides;
    }
    public static void addPoint(int n) {
        switch (n){
            case 1:
                numCorrectQuestionType1++;
                break;
            case 2:
                numCorrectQuestionType2++;
                break;
            case 3:
                numCorrectQuestionType3++;
                break;
            case 4:
                numCorrectQuestionType4++;
                break;
            case 5:
                numCorrectQuestionType5++;
                break;
            case 6:
                numCorrectQuestionType6++;
                break;
        }
        saveProgress();
    }
    public static void setNumSlides(int numSlides) {
        InformationRetrieve.numSlides = numSlides;
        saveProgress();
    }
    public static int getNumQuestionType1() {
        return numQuestionType1;
    }
    public static void setNumQuestionType1(int numQuestionType1) {
        InformationRetrieve.numQuestionType1 = numQuestionType1;
        saveProgress();
    }
    public static int getNumQuestionType2() {
        return numQuestionType2;
    }
    public static void setNumQuestionType2(int numQuestionType2) {
        InformationRetrieve.numQuestionType2 = numQuestionType2;
        saveProgress();
    }
    public static int getNumQuestionType3() {
        return numQuestionType3;
    }
    public static void setNumQuestionType3(int numQuestionType3) {
        InformationRetrieve.numQuestionType3 = numQuestionType3;
        saveProgress();
    }
    public static int getNumQuestionType4() {
        return numQuestionType4;
    }

    public static void setNumQuestionType4(int numQuestionType4) {
        InformationRetrieve.numQuestionType4 = numQuestionType4;
        saveProgress();
    }

    public static boolean isRandomizer() {
        return randomizer;
    }
    public static void setRandomizer(boolean randomizer) {
        InformationRetrieve.randomizer = randomizer;
        saveProgress();
    }
    public static boolean isRecomendation() {
        return recomendation;
    }
    public static void setRecomendation(boolean recomendation) {
        InformationRetrieve.recomendation = recomendation;
        saveProgress();
    }
    public static boolean isOnlyQuestions() {
        return onlyQuestions;
    }
    public static void setOnlyQuestions(boolean onlyQuestions) {
        InformationRetrieve.onlyQuestions = onlyQuestions;
        saveProgress();
    }
    public static boolean isOnlySlides() {
        return onlySlides;
    }
    public static void setOnlySlides(boolean onlySlides) {
        InformationRetrieve.onlySlides = onlySlides;
        saveProgress();
    }
    public static boolean isKanjiMenuItemN5() {
        return kanjiMenuItemN5;
    }

    public static void setKanjiMenuItemN5(boolean kanjiMenuItemN5) {
        InformationRetrieve.kanjiMenuItemN5 = kanjiMenuItemN5;
        saveProgress();
    }

    public static boolean isKanjiMenuItemN4() {
        return kanjiMenuItemN4;
    }

    public static void setKanjiMenuItemN4(boolean kanjiMenuItemN4) {
        InformationRetrieve.kanjiMenuItemN4 = kanjiMenuItemN4;
        saveProgress();
    }

    public static boolean isKanjiMenuItemN3() {
        return kanjiMenuItemN3;
    }

    public static void setKanjiMenuItemN3(boolean kanjiMenuItemN3) {
        InformationRetrieve.kanjiMenuItemN3 = kanjiMenuItemN3;
        saveProgress();
    }

    public static boolean isKanjiMenuItemN2() {
        return kanjiMenuItemN2;
    }

    public static void setKanjiMenuItemN2(boolean kanjiMenuItemN2) {
        InformationRetrieve.kanjiMenuItemN2 = kanjiMenuItemN2;
        saveProgress();
    }

    public static boolean isKanjiMenuItemN1() {
        return kanjiMenuItemN1;
    }

    public static void setKanjiMenuItemN1(boolean kanjiMenuItemN1) {
        InformationRetrieve.kanjiMenuItemN1 = kanjiMenuItemN1;
        saveProgress();
    }

    public static boolean isWordMenuItemN5() {
        return wordMenuItemN5;
    }

    public static void setWordMenuItemN5(boolean wordMenuItemN5) {
        InformationRetrieve.wordMenuItemN5 = wordMenuItemN5;
        saveProgress();
    }

    public static boolean isWordMenuItemN4() {
        return wordMenuItemN4;
    }

    public static void setWordMenuItemN4(boolean wordMenuItemN4) {
        InformationRetrieve.wordMenuItemN4 = wordMenuItemN4;
        saveProgress();
    }

    public static boolean isWordMenuItemN3() {
        return wordMenuItemN3;
    }

    public static void setWordMenuItemN3(boolean wordMenuItemN3) {
        InformationRetrieve.wordMenuItemN3 = wordMenuItemN3;
        saveProgress();
    }

    public static boolean isWordMenuItemN2() {
        return wordMenuItemN2;
    }

    public static void setWordMenuItemN2(boolean wordMenuItemN2) {
        InformationRetrieve.wordMenuItemN2 = wordMenuItemN2;
        saveProgress();
    }

    public static boolean isWordMenuItemN1() {
        return wordMenuItemN1;
    }

    public static void setWordMenuItemN1(boolean wordMenuItemN1) {
        InformationRetrieve.wordMenuItemN1 = wordMenuItemN1;
        saveProgress();
    }

    public static int getNumCorrectQuestionType1() {
        return numCorrectQuestionType1;
    }

    public static void setNumCorrectQuestionType1(int numCorrectQuestionType1) {
        InformationRetrieve.numCorrectQuestionType1 = numCorrectQuestionType1;
        saveProgress();
    }

    public static int getNumCorrectQuestionType2() {
        return numCorrectQuestionType2;
    }

    public static void setNumCorrectQuestionType2(int numCorrectQuestionType2) {
        InformationRetrieve.numCorrectQuestionType2 = numCorrectQuestionType2;
        saveProgress();
    }

    public static int getNumCorrectQuestionType3() {
        return numCorrectQuestionType3;
    }

    public static void setNumCorrectQuestionType3(int numCorrectQuestionType3) {
        InformationRetrieve.numCorrectQuestionType3 = numCorrectQuestionType3;
        saveProgress();
    }

    public static int getNumCorrectQuestionType4() {
        return numCorrectQuestionType4;
    }

    public static void setNumCorrectQuestionType4(int numCorrectQuestionType4) {
        InformationRetrieve.numCorrectQuestionType4 = numCorrectQuestionType4;
        saveProgress();
    }


    public static boolean hasFavoritedWord(String favoritedString) {
        return favoritedWord.contains(favoritedString);
    }
    public static boolean hasFavoritedKanji(String favoritedString) {
        return favoritedkanji.contains(favoritedString);
    }

    public static void setFavoritedKanji(String favoritedString) {
        favoritedkanji.add(favoritedString);
        saveProgress();
    }
    public static void setFavoritedWord(String favoritedString) {
        Log.d("", favoritedString);
        favoritedWord.add(favoritedString);
        saveProgress();
    }
    public static void removeFavoritedWord(String favoritedString) {
        favoritedWord.remove(favoritedString);
        saveProgress();
    }
    public static void removeFavoritedKanji(String favoritedString) {
        favoritedkanji.remove(favoritedString);
        saveProgress();
    }

    public static List<String> getFavoritedWord() {
        return favoritedWord;
    }

    public static List<String> getFavoritedkanji() {
        return favoritedkanji;
    }

    public static boolean isFavoritedStatus() {
        return favoritedStatus;
    }

    public static void setFavoritedStatus(boolean favoritedStatus) {
        InformationRetrieve.favoritedStatus = favoritedStatus;
        saveProgress();
    }

    public static boolean isOnlyFavorite() {
        return onlyFavorite;
    }

    public static void setOnlyFavorite(boolean onlyFavorite) {
        InformationRetrieve.onlyFavorite = onlyFavorite;
        saveProgress();
    }

    public static String getLANGUAGE() {
        return LANGUAGE;
    }

    public static void setLANGUAGE(String LANGUAGE) {
        InformationRetrieve.LANGUAGE = LANGUAGE;
    }

    public static int getNumQuestionType5() {
        return numQuestionType5;
    }

    public static void setNumQuestionType5(int numQuestionType5) {
        InformationRetrieve.numQuestionType5 = numQuestionType5;
        saveProgress();
    }

    public static int getNumQuestionType6() {
        return numQuestionType6;
    }

    public static void setNumQuestionType6(int numQuestionType6) {
        InformationRetrieve.numQuestionType6 = numQuestionType6;
        saveProgress();
    }

    public static int getNumCorrectQuestionType5() {
        return numCorrectQuestionType5;
    }

    public static void setNumCorrectQuestionType5(int numCorrectQuestionType5) {
        InformationRetrieve.numCorrectQuestionType5 = numCorrectQuestionType5;
        saveProgress();
    }

    public static int getNumCorrectQuestionType6() {
        return numCorrectQuestionType6;
    }

    public static void setNumCorrectQuestionType6(int numCorrectQuestionType6) {
        InformationRetrieve.numCorrectQuestionType6 = numCorrectQuestionType6;
        saveProgress();
    }

    public static List<String[]> getCharacteresAcertados() {
        return characteresAcertados;
    }

    public static void setCharacteresAcertados(List<String[]> characteresAcertados) {
        if (characteresAcertados.size() > 2000) {
            List<String[]> reSizeVar = new ArrayList<>();
            for (int i = 700; i < characteresAcertados.size(); i++)
                reSizeVar.add(characteresAcertados.get(i));
            InformationRetrieve.characteresAcertados = reSizeVar;
        } else {
            InformationRetrieve.characteresAcertados = characteresAcertados;
        }
        saveProgress();
    }
}