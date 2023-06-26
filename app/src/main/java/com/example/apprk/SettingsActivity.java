package com.example.apprk;

        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.Html;
        import android.text.SpannableString;
        import android.text.Spanned;
        import android.text.TextWatcher;
        import android.text.method.LinkMovementMethod;
        import android.text.style.ClickableSpan;
        import android.view.View;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.TextView;

        import androidx.appcompat.app.AppCompatActivity;

        import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    EditText text4;
    EditText text5;
    EditText text6;
    EditText text7;
    EditText text8;
    EditText text11;
    EditText text12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        InformationRetrieve userData = new InformationRetrieve(this);

        findViewById(R.id.exit_button).setOnClickListener(v -> {
            Intent intent1 = new Intent(SettingsActivity.this, MenuActivity.class);
            startActivity(intent1);
        });

        CheckBox questionOnly = findViewById(R.id.edit_text0);
        questionOnly.setChecked(InformationRetrieve.isOnlyQuestions());

        CheckBox slideOnly = findViewById(R.id.edit_text9);
        slideOnly.setChecked(InformationRetrieve.isOnlySlides());
        questionOnly.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                slideOnly.setChecked(false);
            }
            InformationRetrieve.setOnlyQuestions(questionOnly.isChecked());
            InformationRetrieve.setOnlySlides(slideOnly.isChecked());
        });

        slideOnly.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                questionOnly.setChecked(false);
            }
            InformationRetrieve.setOnlyQuestions(questionOnly.isChecked());
            InformationRetrieve.setOnlySlides(slideOnly.isChecked());
        });

        CheckBox randBox = findViewById(R.id.edit_text1);
        randBox.setChecked(InformationRetrieve.isRandomizer());
        randBox.setOnClickListener(v -> InformationRetrieve.setRandomizer(randBox.isChecked()));

        CheckBox recomBox = findViewById(R.id.edit_text2);
        recomBox.setChecked(InformationRetrieve.isRecomendation());
        recomBox.setOnClickListener(v -> InformationRetrieve.setRecomendation(recomBox.isChecked()));

        CheckBox onlyFavorite = findViewById(R.id.edit_text10);
        onlyFavorite.setChecked(InformationRetrieve.isOnlyFavorite());
        onlyFavorite.setOnClickListener(v -> InformationRetrieve.setOnlyFavorite(onlyFavorite.isChecked()));

        findViewById(R.id.edit_text3).setOnClickListener(v -> InformationRetrieve.setCharacteresAcertados(new ArrayList<>()));

        text4 = findViewById(R.id.edit_text4);
        text5 = findViewById(R.id.edit_text5);
        text6 = findViewById(R.id.edit_text6);
        text7 = findViewById(R.id.edit_text7);
        text8 = findViewById(R.id.edit_text8);
        text11 = findViewById(R.id.edit_text11);
        text12 = findViewById(R.id.edit_text12);
        text4.setText(String.valueOf(InformationRetrieve.getNumSlides()));
        text5.setText(String.valueOf(InformationRetrieve.getNumQuestionType1()));
        text6.setText(String.valueOf(InformationRetrieve.getNumQuestionType2()));
        text7.setText(String.valueOf(InformationRetrieve.getNumQuestionType3()));
        text8.setText(String.valueOf(InformationRetrieve.getNumQuestionType4()));
        text11.setText(String.valueOf(InformationRetrieve.getNumQuestionType5()));
        text12.setText(String.valueOf(InformationRetrieve.getNumQuestionType6()));
        setupEditText(text4);
        setupEditText(text5);
        setupEditText(text6);
        setupEditText(text7);
        setupEditText(text8);
        setupEditText(text11);
        setupEditText(text12);


        // hyperlinks
        TextView creditsTextView = findViewById(R.id.credits_textview);
        String creditsString = getString(R.string.credits);

        SpannableString spannableString = new SpannableString(creditsString);

        int githubStartIndex = creditsString.indexOf("Github");
        int githubEndIndex = githubStartIndex + "Github".length();

        int tanosStartIndex = creditsString.indexOf("Jonathan Waller");
        int tanosEndIndex = tanosStartIndex + "Jonathan Waller".length();

        int channelStartIndex = creditsString.indexOf("Channel");
        int channelEndIndex = channelStartIndex + "Channel".length();

        int contactStartIndex = creditsString.indexOf("Contact-me");

        ClickableSpan tanosClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Handle click action for the Tanos hyperlink
                Uri uri = Uri.parse("http://www.tanos.co.uk/jlpt/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };

        spannableString.setSpan(tanosClickableSpan, tanosStartIndex, tanosEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        creditsTextView.setText(spannableString);
        creditsTextView.setMovementMethod(LinkMovementMethod.getInstance());


    }
    private void setupEditText(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int value = 0;
                try {
                    value = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (editText.getId() == R.id.edit_text4) {
                    InformationRetrieve.setNumSlides(value);
                } else if (editText.getId() == R.id.edit_text5) {
                    InformationRetrieve.setNumQuestionType1(value);
                } else if (editText.getId() == R.id.edit_text6) {
                    InformationRetrieve.setNumQuestionType2(value);
                } else if (editText.getId() == R.id.edit_text7) {
                    InformationRetrieve.setNumQuestionType3(value);
                } else if (editText.getId() == R.id.edit_text8) {
                    InformationRetrieve.setNumQuestionType4(value);
                } else if (editText.getId() == R.id.edit_text11) {
                    InformationRetrieve.setNumQuestionType5(value);
                } else if (editText.getId() == R.id.edit_text12) {
                    InformationRetrieve.setNumQuestionType6(value);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(SettingsActivity.this, MenuActivity.class);
        startActivity(intent1);
        super.onBackPressed();
    }
}