package com.example.apprk;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SlideActivity extends AppCompatActivity {
    private TextView characterTextView;
    private TextView readingATextView;
    private TextView readingBTextView;
    private TextView readingCTextView;
    private TextView meaningTextView;
    private List<String[]> matrix = new ArrayList<>();
    private final List<String[]> seenMatrix = new ArrayList<>();
    private int currentIndex = 0;
    private String displayType;
    private ViewFlipper viewFlipper;
    private DrawingView drawingView;
    private ImageView imageView;
    private TextView questionTextView;
    private Button option1Button ;
    private Button option2Button ;
    private Button option3Button ;
    int selectedOption = -1;
    private boolean checkdraw = false;
    private boolean checkTranslate = false;
    private String translateAwnser = "";
    private int QuestionType = 0;
    private Bitmap letterBitmap;
    private int currentState = 0;
    private ImageButton star;
    private Drawable starOn;
    private Drawable starOff;
    private final int[] maxIterations = {6, 3, 3, 3, 3}; // maximum number of iterations for each state
    private final int[] currentIterations = {1, 0, 0, 0, 0}; // current number of iterations for each state
    private final List<String> characteresAcertados = new ArrayList<>();
    private final ArrayList<String> arrayFirstItemMatrix = new ArrayList<>();
    private final SlideLib Lib = new SlideLib();
    private TextToSpeech textToSpeech;
    private String currentText;
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
        Intent intent = getIntent();
        displayType = intent.getStringExtra("displayType");

        switch (displayType) {
            case "kana":
                setContentView(R.layout.activity_slidekana);
                break;
            case "kanji":
                setContentView(R.layout.activity_slidekanji);
                break;
            case "word":
                setContentView(R.layout.activity_slideword);
                break;
        }
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        activityResultLauncher.launch(checkIntent);

        InformationRetrieve info = new InformationRetrieve(this);

        maxIterations[0] = InformationRetrieve.getNumSlides();
        maxIterations[1] = InformationRetrieve.getNumQuestionType1();
        maxIterations[2] = InformationRetrieve.getNumQuestionType2();
        maxIterations[3] = InformationRetrieve.getNumQuestionType3();
        maxIterations[4] = InformationRetrieve.getNumQuestionType4();


        ArrayList lettersToShow = new ArrayList(intent.getStringArrayListExtra("letters_to_show"));

        List<String[]> list5 = new ArrayList<>();
        List<String[]> list4 = new ArrayList<>();
        List<String[]> list3 = new ArrayList<>();
        List<String[]> list2 = new ArrayList<>();
        List<String[]> list1 = new ArrayList<>();

        switch (displayType) {
            case "kana":
                if (lettersToShow.contains("Hiragana"))
                    list1 = SpreadsheetReader.read(this, "hiragana.csv");
                if (lettersToShow.contains("Katakana"))
                    list2 = SpreadsheetReader.read(this, "katakana.csv");
                break;
            case "kanji":
                if (lettersToShow.contains("Kanji N1"))
                    list1 = SpreadsheetReader.read(this, "kanjin1.csv");
                if (lettersToShow.contains("Kanji N2"))
                    list2 = SpreadsheetReader.read(this, "kanjin2.csv");
                if (lettersToShow.contains("Kanji N3"))
                    list3 = SpreadsheetReader.read(this, "kanjin3.csv");
                if (lettersToShow.contains("Kanji N4"))
                    list4 = SpreadsheetReader.read(this, "kanjin4.csv");
                if (lettersToShow.contains("Kanji N5"))
                    list5 = SpreadsheetReader.read(this, "kanjin5.csv");
                break;
            case "word":
                if (lettersToShow.contains("Word N1"))
                    list1 = SpreadsheetReader.read(this, "wordn1.csv");
                if (lettersToShow.contains("Word N2"))
                    list2 = SpreadsheetReader.read(this, "wordn2.csv");
                if (lettersToShow.contains("Word N3"))
                    list3 = SpreadsheetReader.read(this, "wordn3.csv");
                if (lettersToShow.contains("Word N4"))
                    list4 = SpreadsheetReader.read(this, "wordn4.csv");
                if (lettersToShow.contains("Word N5"))
                    list5 = SpreadsheetReader.read(this, "wordn5.csv");
                break;
        }
        list5.addAll(list4);
        list5.addAll(list3);
        list5.addAll(list2);
        list5.addAll(list1);
        matrix = list5;

        for (String[] array : matrix)
            arrayFirstItemMatrix.add(array[0]);

        if (InformationRetrieve.isRandomizer())
            Collections.shuffle(matrix);

        if (InformationRetrieve.isOnlyQuestions())
            seenMatrix.addAll(matrix);

        if (InformationRetrieve.isOnlyFavorite() && !displayType.equals("kana")){
            List<String[]> tmpFavoriteMatrix = new ArrayList<>();
            for(int i = 0; i < arrayFirstItemMatrix.size(); i++){
                if(displayType.equals("kanji") && InformationRetrieve.hasFavoritedKanji(arrayFirstItemMatrix.get(i))){
                    tmpFavoriteMatrix.add(matrix.get(i));
                } else if (displayType.equals("word") && InformationRetrieve.hasFavoritedWord(matrix.get(i)[0]+"$"+matrix.get(i)[2])){
                    tmpFavoriteMatrix.add(matrix.get(i));
                }
            }
            if(!tmpFavoriteMatrix.isEmpty())
                matrix = tmpFavoriteMatrix;
        }
        star = findViewById(R.id.star_image_button);
        starOn = ContextCompat.getDrawable(this, R.drawable.baseline_star_24);
        starOff = ContextCompat.getDrawable(this, R.drawable.baseline_star_border_24);

        Animation inAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        viewFlipper = findViewById(R.id.view_flipper);
        viewFlipper.setInAnimation(inAnim);

        characterTextView = findViewById(R.id.word_text_view);
        readingATextView = findViewById(R.id.kana_text_view);
        readingBTextView = findViewById(R.id.transl_text_view);
        readingCTextView = findViewById(R.id.translate_text_view);
        meaningTextView = findViewById(R.id.example_translate_text_view);

        imageView = findViewById(R.id.image_view);
        questionTextView = findViewById(R.id.translate_letter_text_view);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);

        option1Button.setOnClickListener(v -> {selectedOption = 1; Lib.highlightSelectedOption(SlideActivity.this,Arrays.asList(option1Button, option2Button, option3Button));});
        option2Button.setOnClickListener(v -> {selectedOption = 2; Lib.highlightSelectedOption(SlideActivity.this,Arrays.asList(option2Button, option1Button, option3Button));});
        option3Button.setOnClickListener(v -> {selectedOption = 3; Lib.highlightSelectedOption(SlideActivity.this,Arrays.asList(option3Button, option2Button, option1Button));});

        findViewById(R.id.button3).setOnClickListener(v -> textToSpeech.speak(currentText, TextToSpeech.QUEUE_FLUSH, null));
        findViewById(R.id.translate_button3).setOnClickListener(v -> textToSpeech.speak(currentText, TextToSpeech.QUEUE_FLUSH, null));

        findViewById(R.id.exit_button).setOnClickListener(v -> onBackPressed());

        setupViewFlipper();

        SlideLib.debugLog(InformationRetrieve.getFavoritedWord());
        SlideLib.debugLog(InformationRetrieve.getFavoritedkanji());
        star.setOnClickListener(v -> {
            if(currentState == 0)
                if (star.getBackground().equals(starOff)) {
                    if(displayType.equals("kanji")) {
                        if (!InformationRetrieve.hasFavoritedKanji(characterInfo[0]))
                            InformationRetrieve.setFavoritedKanji(characterInfo[0]);
                    } else if(displayType.equals("word")) {
                        if (!InformationRetrieve.hasFavoritedWord(characterInfo[0] + "$" + characterInfo[2]))
                            InformationRetrieve.setFavoritedWord(characterInfo[0] + "$" + characterInfo[2]);
                    }
                    star.setBackground(starOn);
                } else if (star.getBackground().equals(starOn)) {
                    if (displayType.equals("kanji")) {
                        if (InformationRetrieve.hasFavoritedKanji(characterInfo[0]))
                            InformationRetrieve.removeFavoritedKanji(characterInfo[0]);
                    } else if (displayType.equals("word")){
                        if (InformationRetrieve.hasFavoritedWord(characterInfo[0] + "$" + characterInfo[2]))
                            InformationRetrieve.removeFavoritedWord(characterInfo[0] + "$" + characterInfo[2]);
                    }star.setBackground(starOff);
                }
        });
    }
    private void setupViewFlipper() {
        viewFlipper = findViewById(R.id.view_flipper);
        findViewById(R.id.next_button).setOnClickListener(v -> {
            currentIterations[currentState]++;
            fillViewFlipper();
        });
        fillViewFlipper();
    }
    private void fillViewFlipper() {

        int lastIndex = characteresAcertados.size() -1;
        if (checkdraw) {
            if (Lib.compareBitmaps(letterBitmap, drawingView.getBitmap()) < 0.1){
                characteresAcertados.remove(lastIndex);
            } else {
                InformationRetrieve.addPoint(1);
            }
            checkdraw = false;
        } else if (checkTranslate) {
            switch (selectedOption) {
                case -1:
                    characteresAcertados.remove(lastIndex);
                    break;
                case 1:
                    if (!Lib.checkTranslationAnswer(option1Button.getText(), translateAwnser)){
                        characteresAcertados.remove(lastIndex);
                        showAnwserDialogue(R.layout.wrong_answer_sign);
                    } else {
                        showAnwserDialogue(R.layout.right_answer_sign);
                    }
                    break;
                case 2:
                    if (!Lib.checkTranslationAnswer(option2Button.getText(), translateAwnser)){
                        characteresAcertados.remove(lastIndex);
                        showAnwserDialogue(R.layout.wrong_answer_sign);
                    } else {
                        showAnwserDialogue(R.layout.right_answer_sign);
                    }
                    break;
                case 3:
                    if (!Lib.checkTranslationAnswer(option3Button.getText(), translateAwnser)){
                        characteresAcertados.remove(lastIndex);
                        showAnwserDialogue(R.layout.wrong_answer_sign);
                    } else {
                        showAnwserDialogue(R.layout.right_answer_sign);
                    }
                    break;
            }
            checkTranslate = false;
        }
        Lib.cleanButtons(this, Arrays.asList(option1Button, option2Button, option3Button));
        translateAwnser = "";
        selectedOption = -1;
        Lib.updateList(matrix, arrayFirstItemMatrix,characteresAcertados);
        if (InformationRetrieve.isOnlyQuestions())
            maxIterations[0] = 0;

        if (currentIndex >= matrix.size())
                currentIndex = 0;

        star.setVisibility(View.INVISIBLE);
        switch (currentState) {
            case 0:
                slideCaseZeroSlide();
                break;
            case 1:
                slideCaseOneDrawing();
                break;
            case 2:
                slideCaseTwoKanaToKanji();
                break;
            case 3:
                slideCaseThreeKanjiToKana();
                break;
            case 4:
                slideCaseFourListening();
                break;
        }
    }
    private void slideCaseZeroSlide(){
        if (currentIterations[0] < maxIterations[0]) {
            setCharacterAndPhrase(currentIndex);
            viewFlipper.setDisplayedChild(0);
            setupFavoritedStar();
            seenMatrix.add(matrix.get(currentIndex));
            currentIndex++;
        } else {
            if (!InformationRetrieve.isOnlySlides())
                currentState = getRandomState();
            currentIterations[0] = 0;
            fillViewFlipper();
        }
    }
    private void slideCaseOneDrawing() {
        if (displayType.equals("word")){
            currentState = getRandomState();
            fillViewFlipper();
        } else if (currentIterations[1] < maxIterations[1]) {
            String[] charac = seenMatrix.get(new Random().nextInt(seenMatrix.size()));
            characteresAcertados.add(charac[0]);
            drawingView = findViewById(R.id.drawing_view);
            drawingView.clearCanvas();
            letterBitmap = Lib.generateCaptchaBitmap(charac[0]);
            imageView.setImageBitmap(letterBitmap);
            viewFlipper.setDisplayedChild(1);
            checkdraw = true;
        } else {
            currentState = 0;
            currentIterations[1] = 0;
            fillViewFlipper();
        }
    }
    private void slideCaseTwoKanaToKanji() {
        if (currentIterations[2] < maxIterations[2]) {
            List<Integer> randomIndexes = SlideLib.generateRandomIndexes(3, seenMatrix.size());
            String[] rightAnswer = seenMatrix.get(randomIndexes.get(0));
            String[] optionB = seenMatrix.get(randomIndexes.get(1));
            String[] optionC = seenMatrix.get(randomIndexes.get(2));

            switch (displayType) {
                case "kana":
                    characteresAcertados.add(rightAnswer[0]);
                    questionTextView.setText(rightAnswer[1]);
                    break;
                case "kanji":
                    if (!(rightAnswer[2].isEmpty() && rightAnswer[1].isEmpty())){
                        Random random = new Random();
                        boolean chooseVariable1 = random.nextBoolean();
                        int selectedVariable = chooseVariable1 ? 2 : 1;
                        questionTextView.setText(rightAnswer[selectedVariable]);
                    } else if (rightAnswer[2].isEmpty()){
                        questionTextView.setText(rightAnswer[1]);
                    } else if (rightAnswer[1].isEmpty()){
                        questionTextView.setText(rightAnswer[2]);
                    }

                    questionTextView.setText(rightAnswer[2]);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        questionTextView.setAutoSizeTextTypeUniformWithConfiguration(12, 70, 1, TypedValue.COMPLEX_UNIT_DIP);
                    }
                    characteresAcertados.add(rightAnswer[0]);
                    break;
                case "word":
                    setTextViewText(questionTextView, "", rightAnswer[2].split("-"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        questionTextView.setAutoSizeTextTypeUniformWithConfiguration(12, 70, 1, TypedValue.COMPLEX_UNIT_DIP);
                    characteresAcertados.add(rightAnswer[0]);
                    break;
            }
            List<String> options = new ArrayList<>();
            options.add(rightAnswer[0]);
            translateAwnser = options.get(0);
            currentText = translateAwnser;
            options.add(optionB[0]);
            options.add(optionC[0]);
            Collections.shuffle(options);
            option1Button.setText(options.get(0));
            option2Button.setText(options.get(1));
            option3Button.setText(options.get(2));

            checkTranslate = true;
            QuestionType = 2;
            viewFlipper.setDisplayedChild(2);
        } else {
            currentState = 0;
            currentIterations[2] = 0;
            fillViewFlipper();
        }
    }
    private void slideCaseThreeKanjiToKana() {
        int listPosition = 0;
        switch (displayType) {
            case "kana":
                listPosition = 1;
                break;
            case "kanji":
            case "word":
                listPosition = 2;
                break;
        }
        if (currentIterations[3] < maxIterations[3]) {
            //here at the kanji display we translate kanji to kun and on
                //the buttons display all the translations at once
                    //is possible to add randomness in which reading the buttons will display
            List<Integer> randomIndexes = SlideLib.generateRandomIndexes(3, seenMatrix.size());
            String[] rightAnswer = seenMatrix.get(randomIndexes.get(0));
            String[] optionB = seenMatrix.get(randomIndexes.get(1));
            String[] optionC = seenMatrix.get(randomIndexes.get(2));

            characteresAcertados.add(rightAnswer[0]);
            questionTextView.setText(rightAnswer[0]);

            List<String> options = new ArrayList<>();
            options.add(rightAnswer[listPosition]);
            translateAwnser = options.get(0);
            currentText = translateAwnser;
            options.add(optionB[listPosition]);
            options.add(optionC[listPosition]);
            Collections.shuffle(options);
            option1Button.setText(options.get(0));
            option2Button.setText(options.get(1));
            option3Button.setText(options.get(2));

            checkTranslate = true;
            QuestionType = 3;
            viewFlipper.setDisplayedChild(2);
        } else {
            currentState = 0;
            currentIterations[3] = 0;
            fillViewFlipper();
        }
    }
    private void slideCaseFourListening() {
        if (currentIterations[4] < maxIterations[4]) {
            List<Integer> randomIndexes = SlideLib.generateRandomIndexes(3, seenMatrix.size());
            String[] rightAnswer = seenMatrix.get(randomIndexes.get(0));
            String[] optionB = seenMatrix.get(randomIndexes.get(1));
            String[] optionC = seenMatrix.get(randomIndexes.get(2));

            characteresAcertados.add(rightAnswer[0]);
            // set question and answer options
            questionTextView.setVisibility(View.GONE);
            List<String> options = new ArrayList<>();
            options.add(rightAnswer[0]);
            translateAwnser = options.get(0);
            currentText = translateAwnser;
            options.add(optionB[0]);
            options.add(optionC[0]);
            Collections.shuffle(options);
            option1Button.setText(options.get(0));
            option2Button.setText(options.get(1));
            option3Button.setText(options.get(2));

            checkTranslate = true;
            QuestionType = 4;
            viewFlipper.setDisplayedChild(2);
        } else {
            questionTextView.setVisibility(View.VISIBLE);
            currentState = 0;
            currentIterations[4] = 0;
            fillViewFlipper();
        }
    }
    private void setCharacterAndPhrase(int index) {
        characterInfo = matrix.get(index);
        String[] strArray;
        switch (displayType) {
            case "kana":
                currentText = characterInfo[0];
                characterTextView.setText(characterInfo[0]);
                readingATextView.setText(characterInfo[1]);
                break;
            case "kanji":
                characterTextView.setText(characterInfo[0]);
                currentText = characterInfo[0];

                strArray = characterInfo[1].split("-");
                setTextViewText(readingATextView, "On Reading:\n", strArray);

                strArray = characterInfo[2].split("-");
                setTextViewText(readingBTextView, "Kun Reading:\n", strArray);

                strArray = characterInfo[3].split("-");
                setTextViewText(readingCTextView, "Translation:\n", strArray);

                strArray = characterInfo[4].split("-");
                setTextViewText(meaningTextView, "Example:\n", strArray);
                break;
            case "word":
                strArray = characterInfo[0].split("-");
                setTextViewText(characterTextView, "", strArray);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    characterTextView.setAutoSizeTextTypeUniformWithConfiguration(12, 70, 1, TypedValue.COMPLEX_UNIT_DIP);
                }
                currentText = characterInfo[0];

                strArray = characterInfo[1].split("-");
                setTextViewText(readingATextView, "Kanji Reading:\n", strArray);

                strArray = characterInfo[2].split("-");
                setTextViewText(readingBTextView, "Translation:\n", strArray);
                break;
        }
    }
    private void setTextViewText(TextView textView, String title, String[] strArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : strArray) {
            stringBuilder.append(s).append("\n");
        }
        textView.setText(title + stringBuilder);
    }

    private int getRandomState(){
        List<Integer> reachableStates = new ArrayList<>();
        if (InformationRetrieve.getNumQuestionType1() > 0)
            reachableStates.add(1);
        if (InformationRetrieve.getNumQuestionType2() > 0)
            reachableStates.add(2);
        if (InformationRetrieve.getNumQuestionType3() > 0)
            reachableStates.add(3);
        if (InformationRetrieve.getNumQuestionType4() > 0)
            reachableStates.add(4);
        Collections.shuffle(reachableStates);
        return reachableStates.get(0);
    }

    void showAnwserDialogue(int v){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        InformationRetrieve.addPoint(QuestionType);
    }

    void setupFavoritedStar(){
        if (displayType.equals("kanji") || displayType.equals("word"))
            star.setVisibility(View.VISIBLE);

        if(displayType.equals("kanji"))
            if(InformationRetrieve.hasFavoritedKanji(characterInfo[0]))
                star.setBackground(starOn);
            else
                star.setBackground(starOff);

        if(displayType.equals("word"))
            if(InformationRetrieve.hasFavoritedWord(characterInfo[0] +"$"+ characterInfo[2]))
                star.setBackground(starOn);
            else
                star.setBackground(starOff);
    }
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(SlideActivity.this, MenuActivity.class);
        startActivity(intent1);
        finish();
    }
}