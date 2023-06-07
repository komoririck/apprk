package com.example.apprk;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordViewActivity extends AppCompatActivity {
    private TextView characterTextView;
    private TextView readingATextView;
    private TextView readingBTextView;
    private List<String[]> matrix = new ArrayList<>();
    private ViewFlipper viewFlipper;
    private TextToSpeech textToSpeech;
    private String currentText;
    private ImageButton star;
    private Drawable starOn;
    private Drawable starOff;
    String[] characterInfo;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS) {
                                int result = textToSpeech.setLanguage(Locale.JAPANESE);
                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Log.e("TTS", "Language is not supported");
                                }
                            } else {
                                Log.e("TTS", "Initialization failed" + status);
                            }
                        }
                    });
                } else {
                    Intent installIntent = new Intent();
                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_view);
        Intent checkIntent = new Intent();

        InformationRetrieve info = new InformationRetrieve(this);

        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        activityResultLauncher.launch(checkIntent);

        Intent intent = getIntent();
        currentText = intent.getStringExtra("word");


        List<String[]> list5 = SpreadsheetReader.read(this,"wordn5.csv");
        List<String[]> list4 = SpreadsheetReader.read(this,"wordn4.csv");
        List<String[]> list3 = SpreadsheetReader.read(this,"wordn3.csv");
        List<String[]> list2 = SpreadsheetReader.read(this,"wordn2.csv");
        List<String[]> list1 = SpreadsheetReader.read(this,"wordn1.csv");

        list5.addAll(list4);
        list5.addAll(list3);
        list5.addAll(list2);
        list5.addAll(list1);

        matrix = list5;


        Animation inAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        viewFlipper = findViewById(R.id.view_flipper);
        viewFlipper.setInAnimation(inAnim);

        characterTextView = findViewById(R.id.word_text_view);
        readingATextView = findViewById(R.id.kana_text_view);
        readingBTextView = findViewById(R.id.transl_text_view);

        star = findViewById(R.id.star_image_button);
        starOn = ContextCompat.getDrawable(this, R.drawable.baseline_star_24);
        starOff = ContextCompat.getDrawable(this, R.drawable.baseline_star_border_24);


        ImageButton exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(v -> {
            onBackPressed();
        });

        ImageButton audioButton = findViewById(R.id.button3);
        audioButton.setOnClickListener(v -> textToSpeech.speak(currentText, TextToSpeech.QUEUE_FLUSH, null));

        star.setOnClickListener(v -> {
                if (star.getBackground().equals(starOff)) {
                    if(!InformationRetrieve.hasFavoritedWord(characterInfo[0] +"$"+ characterInfo[2])){
                        InformationRetrieve.setFavoritedWord(characterInfo[0] +"$"+ characterInfo[2]);
                        star.setBackground(starOn);
                    }
                } else if (star.getBackground().equals(starOn)) {
                    if(InformationRetrieve.hasFavoritedWord(characterInfo[0] +"$"+ characterInfo[2])) {
                        InformationRetrieve.removeFavoritedWord(characterInfo[0] +"$"+ characterInfo[2]);
                        star.setBackground(starOff);
                    }
                }
        });
        setupViewFlipper();
        setupFavoritedStar();
    }
    private void setupViewFlipper() {
        viewFlipper = findViewById(R.id.view_flipper);
        viewFlipper.setDisplayedChild(0);
        characterInfo = new String[matrix.get(0).length];
        String[] currentTextSplit = currentText.split("\\$");
        for(int i = 0; i < matrix.size(); i++)
            if (matrix.get(i)[0].equals(currentTextSplit[0]) && matrix.get(i)[2].equals(currentTextSplit[1]))
                characterInfo = (matrix.get(i));

        characterTextView.setText(characterInfo[0]);
        currentText = characterInfo[0];

        String[] strArray = characterInfo[1].split("-");
        setTextViewText(readingATextView, "Writing:\n", strArray);

        strArray = characterInfo[2].split("-");
        setTextViewText(readingBTextView, "Translate:\n", strArray);
    }

    private void setTextViewText(TextView textView, String title, String[] strArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : strArray)
                stringBuilder.append(s).append("\n");
        textView.setText(title + stringBuilder);
    }

    void setupFavoritedStar(){
            if(InformationRetrieve.hasFavoritedWord(characterInfo[0] +"$"+ characterInfo[2]))
                star.setBackground(starOn);
            else
                star.setBackground(starOff);
    }
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(WordViewActivity.this, WordListActivity.class);
        startActivity(intent1);
        finish();
    }
}