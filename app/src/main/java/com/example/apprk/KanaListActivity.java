package com.example.apprk;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KanaListActivity extends AppCompatActivity implements KanaAdapter.OnItemClickListener  {

    private List<String[]> matrixFilter = new ArrayList<>();
    private KanaAdapter kanaAdapter;
    private TextToSpeech textToSpeech;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult( // starting audio
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
        setContentView(R.layout.activity_word_list); // reusando tela, criar outra

        InformationRetrieve info = new InformationRetrieve(this);

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        activityResultLauncher.launch(checkIntent);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.CENTER;
        mToolbar.setLayoutParams(layoutParams);
        mToolbar.setPopupTheme(R.style.Base_Theme_Apprk);

        findViewById(R.id.exit_button).setOnClickListener(v -> {
            Intent intent1 = new Intent(KanaListActivity.this, MenuActivity.class);
            startActivity(intent1);
        });

        EditText editText = findViewById(R.id.searchEditText);
        matrixFilter = setupList();

        RecyclerView recyclerView = findViewById(R.id.wordRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        kanaAdapter = new KanaAdapter(matrixFilter);
        kanaAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(kanaAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                kanaAdapter.setData(filterCharacter(setupList()));
            }
        });
    }
    @Override
    public void onItemClick(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    private List<String[]> filterCharacter(List<String[]> matrix) {
        EditText editText = findViewById(R.id.searchEditText);
        String searchText = editText.getText().toString();
        if (!searchText.isEmpty()) {
            List<String[]> tmpMatrix = new ArrayList<>();
            for(String[] s : matrix){
                    if (s[0].toLowerCase(Locale.ROOT).contains(searchText) || s[1].toLowerCase(Locale.ROOT).contains(searchText))
                        tmpMatrix.add(s);

            }
            return tmpMatrix;
        }
        return matrix;
    }
    List<String[]> setupList(){
        List<String[]> list5 = new ArrayList<>();
        List<String[]> list4 = new ArrayList<>();

        if(InformationRetrieve.isWordMenuItemN4())
            list4 = SpreadsheetReader.read(this,"katakana.csv");
        if(InformationRetrieve.isWordMenuItemN5())
            list5 = SpreadsheetReader.read(this,"hiragana.csv");

        list5.addAll(list4);
        return list5;
    }
}
