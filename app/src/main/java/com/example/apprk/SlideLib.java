package com.example.apprk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlideLib {

    public static ArrayList<String> removeOutsideNorm(ArrayList<String> inputList, List<String> norm) {
        norm.addAll(inputList);
        double thresholdFactor = 1.5;
        // Find the norm
        Map<String, Integer> countMap = new HashMap<>();
        for (String s : norm) {
            countMap.put(s, countMap.getOrDefault(s, 0) + 1);
        }
        double totalCount = norm.size();
        double normThreshold = totalCount / countMap.size();

        normThreshold *= thresholdFactor;

        // Filter out elements outside the norm
        ArrayList<String> outputList = new ArrayList<>();
        for (String s : inputList) {
            if (countMap.containsKey(s) && countMap.get(s) > normThreshold) {
                countMap.remove(s); // Remove the element from the norm once it's outside the norm
            } else {
                outputList.add(s);
            }
        }
        return outputList;
    }

    public List<String[]> updateList (List<String[]> matrizInicial, ArrayList<String> MatIniChar, List<String> charAcertados){
        MatIniChar = removeOutsideNorm(MatIniChar, charAcertados);
        //turning the matrix into a List and saving it
        List<String[]> tempM = new ArrayList<>();
        for (String[] array : matrizInicial) {
            if(MatIniChar.contains(array[0])){
                tempM.add(array);
            }
        }
        return tempM;
    }
    public boolean isBlack(int pixel) {
        return Color.red(pixel) == 0 && Color.green(pixel) == 0 && Color.blue(pixel) == 0;
    }
    public Boolean checkTranslationAnswer(CharSequence selectedAnswer, CharSequence correctAnswer) {
        return selectedAnswer.equals(correctAnswer);
    }
    public double compareBitmaps(Bitmap bm1, Bitmap bm2) {
        int width = bm1.getWidth();
        int height = bm1.getHeight();
        int blackPixels1 = 0;
        int blackPixels2 = 0;
        int intersectingPixels = 0;
        int nonIntersectingPixels1 = 0;
        int nonIntersectingPixels2 = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel1 = bm1.getPixel(x, y);
                int pixel2 = bm2.getPixel(x, y);

                if (isBlack(pixel1)) {
                    blackPixels1++;
                    if (!isBlack(pixel2)) {
                        nonIntersectingPixels1++;
                    }
                }
                if (isBlack(pixel2)) {
                    blackPixels2++;
                    if (!isBlack(pixel1)) {
                        nonIntersectingPixels2++;
                    }
                }
                if (isBlack(pixel1) && isBlack(pixel2)) {
                    intersectingPixels++;
                }
            }
        }

        if (blackPixels1 == 0 && blackPixels2 == 0) {
            return 1.0f;
        } else if (blackPixels1 == 0 || blackPixels2 == 0) {
            return 0.0f;
        } else {
            float similarity = (float) intersectingPixels / (float) Math.min(blackPixels1, blackPixels2);
            float nonIntersection1 = (float) nonIntersectingPixels1 / (float) blackPixels1;
            float nonIntersection2 = (float) nonIntersectingPixels2 / (float) blackPixels2;
            similarity -= Math.min(nonIntersection1, nonIntersection2) / 2;
            return Math.max(similarity, 0.0f);
        }
    }

    public Bitmap generateCaptchaBitmap(String code) {
        Bitmap bitmap = Bitmap.createBitmap(473, 473, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize((float) (473 * 0.9));
        paint.setTextAlign(Paint.Align.CENTER);

        // Calculate the x and y coordinates to center the text
        float x = canvas.getWidth() / 2f;
        float y = canvas.getHeight() / 2f - ((paint.descent() + paint.ascent()) / 2f);

        canvas.drawText(code, x, y, paint);

        return bitmap;
    }

    public void cleanButtons(Context context, List<Button> buttonList) {
        int color = ContextCompat.getColor(context, R.color.simple_button_color);
        for (Button button : buttonList) {
            button.setBackgroundColor(color);
        }
    }
    public void highlightSelectedOption(Context context, List<Button> buttonList) {
        cleanButtons(context, buttonList);
        buttonList.get(0).setBackgroundColor(Color.parseColor("#FCD535"));
        buttonList.get(0).setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
    }

    public static List<Integer> generateRandomIndexes(int numIndexes, int maxIndex) {
        List<Integer> allIndexes = new ArrayList<>();
        for (int i = 0; i < maxIndex; i++) {
            allIndexes.add(i);
        }
        Collections.shuffle(allIndexes);
        return allIndexes.subList(0, numIndexes);
    }

    public static void debugLogM(List<String[]> dataList) {
        for (String[] array : dataList) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String item : array) {
                stringBuilder.append(item).append(", ");
            }
            String logMessage = stringBuilder.toString();
            if (logMessage.length() > 2) {
                logMessage = logMessage.substring(0, logMessage.length() - 2);
            }
            Log.d("DebugLog", logMessage);
        }
    }
    public static void debugLog(List<String> dataList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : dataList) {
            stringBuilder.append(item).append(", ");
        }
        String logMessage = stringBuilder.toString();
        if (logMessage.length() > 2) {
            logMessage = logMessage.substring(0, logMessage.length() - 2);
        }
        Log.d("DebugLog", logMessage);
    }
    public static boolean isKanji(char character) {
        return (int) character >= 0x4E00 && (int) character <= 0x9FBF || (int) character >= 0x3400 && (int) character <= 0x4DBF || (int) character >= 0xF900 && (int) character <= 0xFAFF;   // CJK Compatibility Ideographs Supplement
    }

    public static void compareStrings(String str1, String str2) {
        StringBuilder differingCharacters = new StringBuilder();

        for (char ch : str1.toCharArray()) {
            if (str2.indexOf(ch) == -1) {
                differingCharacters.append(ch);
            }
        }

        System.out.println(differingCharacters.toString());
    }



}
