package com.example.apprk;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SpreadsheetReader {

    private static final String FILE_NAME = "charactersheet.csv";
    private static final String DELIMITER = ",";

    public static  List<String[]> read(Context context, String FILE) {
        List<String[]> data = new ArrayList<>();
        try {
            InputStream is;
            if (InformationRetrieve.getLANGUAGE().equals("pt"))
                is = context.getAssets().open(InformationRetrieve.getLANGUAGE()+"/"+FILE);
            else
                is = context.getAssets().open("en"+"/"+FILE);

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(DELIMITER);
                data.add(row);
            }

            br.close();
            isr.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static  List<String[]> read(Context context) {
        List<String[]> data = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(DELIMITER);
                data.add(row);
            }

            br.close();
            isr.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
    public static  List<String[]> rangeRead(Context context, int min, int max) {
        List<String[]> data = read(context);
        List<String[]> subData = new ArrayList<>();
        for(int i = min; i < max; i++)
            subData.add(data.get(i));
        return subData;
    }
}