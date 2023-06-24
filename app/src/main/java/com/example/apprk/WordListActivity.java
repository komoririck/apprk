package com.example.apprk;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordListActivity extends AppCompatActivity implements WordAdapter.OnItemClickListener  {

    private List<String[]> matrixFilter = new ArrayList<>();
    private WordAdapter WordAdapter;
    MenuItem starItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        InformationRetrieve info = new InformationRetrieve(this);

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
            Intent intent1 = new Intent(WordListActivity.this, MenuActivity.class);
            startActivity(intent1);
        });

        EditText editText = findViewById(R.id.searchEditText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (starItem.isChecked())
                    matrixFilter = favoriteFilter(setupList());

                WordAdapter.setData(filterCharacter(matrixFilter));
            }
        });
    }
    @Override
    public void onItemClick(String text) {
        Intent intent = new Intent(WordListActivity.this, WordViewActivity.class);
        intent.putExtra("word", text);
        startActivity(intent);
    }
    private List<String[]> filterCharacter(List<String[]> matrix) {
        EditText editText = findViewById(R.id.searchEditText);
        String searchText = editText.getText().toString();
        if (!searchText.isEmpty()) {
            List<String[]> tmpMatrix = new ArrayList<>();
            for(String[] s : matrix){
                if(s.length > 2) {
                    if (s[0].toLowerCase(Locale.ROOT).contains(searchText) || s[1].toLowerCase(Locale.ROOT).contains(searchText) || s[2].toLowerCase(Locale.ROOT).contains(searchText))
                        tmpMatrix.add(s);
                }
            }
            return tmpMatrix;
        }
        return matrix;
    }

    public List<String[]> favoriteFilter(List<String[]> list){
        List<String[]> tempFavoritedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).length >2) {
                if (InformationRetrieve.hasFavoritedWord(list.get(i)[0] + "$" + list.get(i)[2])){
                    tempFavoritedList.add(list.get(i));
                }
            }
        }
        return tempFavoritedList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_filter_wordlist, menu);

        int menuItemId = R.id.item6;
        starItem = menu.findItem(menuItemId);

        menu.findItem(R.id.item1).setChecked(InformationRetrieve.isWordMenuItemN1());
        menu.findItem(R.id.item2).setChecked(InformationRetrieve.isWordMenuItemN2());
        menu.findItem(R.id.item3).setChecked(InformationRetrieve.isWordMenuItemN3());
        menu.findItem(R.id.item4).setChecked(InformationRetrieve.isWordMenuItemN4());
        menu.findItem(R.id.item5).setChecked(InformationRetrieve.isWordMenuItemN5());
        menu.findItem(R.id.item6).setChecked(InformationRetrieve.isFavoritedStatus());

        if (starItem.isChecked())
            matrixFilter = favoriteFilter(setupList());
        else
            matrixFilter = setupList();

        RecyclerView recyclerView = findViewById(R.id.wordRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        WordAdapter = new WordAdapter(matrixFilter);
        WordAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(WordAdapter);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());
        InformationRetrieve.setFavoritedStatus(starItem.isChecked());
        switch (item.getItemId()) {
            case R.id.item1:
                InformationRetrieve.setWordMenuItemN1(item.isChecked());
                break;
            case R.id.item2:
                InformationRetrieve.setWordMenuItemN2(item.isChecked());
                break;
            case R.id.item3:
                InformationRetrieve.setWordMenuItemN3(item.isChecked());
                break;
            case R.id.item4:
                InformationRetrieve.setWordMenuItemN4(item.isChecked());
                break;
            case R.id.item5:
                InformationRetrieve.setWordMenuItemN5(item.isChecked());
                break;
            case R.id.item6:
                InformationRetrieve.setFavoritedStatus(item.isChecked());
                break;
        }
        if (starItem.isChecked())
            matrixFilter = favoriteFilter(setupList());
        else
            matrixFilter = setupList();

        WordAdapter.setData(filterCharacter(matrixFilter));
        return super.onOptionsItemSelected(item);
    }
    List<String[]> setupList(){
        List<String[]> list5 = new ArrayList<>();
        List<String[]> list4 = new ArrayList<>();
        List<String[]> list3 = new ArrayList<>();
        List<String[]> list2 = new ArrayList<>();
        List<String[]> list1 = new ArrayList<>();

        if(InformationRetrieve.isWordMenuItemN1())
            list1 = SpreadsheetReader.read(this,"wordn1.csv");
        if(InformationRetrieve.isWordMenuItemN2())
            list2 = SpreadsheetReader.read(this,"wordn2.csv");
        if(InformationRetrieve.isWordMenuItemN3())
            list3 = SpreadsheetReader.read(this,"wordn3.csv");
        if(InformationRetrieve.isWordMenuItemN4())
            list4 = SpreadsheetReader.read(this,"wordn4.csv");
        if(InformationRetrieve.isWordMenuItemN5())
            list5 = SpreadsheetReader.read(this,"wordn5.csv");

        list5.addAll(list4);
        list5.addAll(list3);
        list5.addAll(list2);
        list5.addAll(list1);
        return list5;
    }
}
