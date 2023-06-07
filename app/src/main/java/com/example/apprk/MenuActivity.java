package com.example.apprk;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private GridLayout mGridLayout;
    private List<AppCompatButton> mSelectedButtons;
    private ArrayList<String> lettersToShow = new ArrayList<>();
    private int currentSection = -1;
    Drawable selected;
    Drawable defaultt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

         selected = ContextCompat.getDrawable(this, R.drawable.menu_cardview_selected_border);
         defaultt = ContextCompat.getDrawable(this, R.drawable.menu_cardview_default_border);

        findViewById(R.id.settins_button).setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        mGridLayout = findViewById(R.id.gridLayout);
        mSelectedButtons = new ArrayList<>();

        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            if (i > 14)
                break;
            View child = mGridLayout.getChildAt(i);
            if (child instanceof AppCompatButton) {
                child.setOnClickListener(this);
            }
        }

        Button saveButton = findViewById(R.id.forwardButton);
        saveButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            for (int i = 0; i < mGridLayout.getChildCount(); i++) {
                View child = mGridLayout.getChildAt(i);
                if (child instanceof AppCompatButton) {
                    if (child.getBackground().equals(selected)) {
                        String text = (String) ((AppCompatButton) child).getText();
                        lettersToShow.add(text);
                    }
                }
            }
            if (!lettersToShow.isEmpty()) {
                intent = new Intent(MenuActivity.this, SlideActivity.class);
                switch (lettersToShow.get(0)) {
                    case "Hiragana":
                    case "Katakana":
                        intent.putExtra("displayType", "kana");
                        break;
                    case "Kanji N5":
                    case "Kanji N4":
                    case "Kanji N3":
                    case "Kanji N2":
                    case "Kanji N1":
                        intent.putExtra("displayType","kanji");
                        break;
                    case "Word N5":
                    case "Word N4":
                    case "Word N3":
                    case "Word N2":
                    case "Word N1":
                        intent.putExtra("displayType","word");
                        break;
                }
                intent.putStringArrayListExtra("letters_to_show", lettersToShow);
                startActivity(intent);
            }
        });

        findViewById(R.id.button14).setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, WordListActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button15).setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CharacterListActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button7).setOnClickListener(null);
        findViewById(R.id.button7).setEnabled(false);
        findViewById(R.id.button12).setOnClickListener(null);
        findViewById(R.id.button12).setEnabled(false);
        findViewById(R.id.button13).setOnClickListener(null);
        findViewById(R.id.button13).setEnabled(false);
    }


    @Override
    public void onClick(View view) {
        boolean isSelected = (view.getBackground().equals(selected));
        int appCompatButtonSection = getAppCompatButtonSection((AppCompatButton) view);
        if (!isSelected) {
            if (currentSection == -1 || currentSection == appCompatButtonSection) {
                mSelectedButtons.add((AppCompatButton) view);
                currentSection = appCompatButtonSection;
            } else {
                mSelectedButtons.clear();
                mSelectedButtons.add((AppCompatButton) view);
                currentSection = appCompatButtonSection;
            }
        } else {
            mSelectedButtons.remove(view);
            view.setBackground(defaultt);
        }

        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            if (mGridLayout.getChildAt(i) instanceof androidx.appcompat.widget.AppCompatButton)
                mGridLayout.getChildAt(i).setBackground(defaultt);
        }
        for (androidx.appcompat.widget.AppCompatButton selectedButton : mSelectedButtons) {
            selectedButton.setBackground(selected);
        }
    }

    private int getAppCompatButtonSection(AppCompatButton appCompatButton) {
        int index = mGridLayout.indexOfChild(appCompatButton);
        if (index >= 0 && index <= 2) {
            return 1;
        } else if (index >= 3 && index <= 9) {
            return 2;
        } else if (index >= 10 && index <= 15) {
            return 3;
        }
        return -1;
    }
}