package com.example.apprk;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CharacterListActivity extends AppCompatActivity implements CharacterAdapter.OnItemClickListener  {

    private List<String[]> matrixFilter = new ArrayList<>();
    private CharacterAdapter characterAdapter;
    MenuItem starItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

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
            Intent intent1 = new Intent(CharacterListActivity.this, MenuActivity.class);
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

                characterAdapter.setData(filterCharacter(matrixFilter));
            }
        });
    }
    @Override
    public void onItemClick(String text) {
        Intent intent = new Intent(CharacterListActivity.this, CharacterViewActivity.class);
        intent.putExtra("kanji", text);
        startActivity(intent);
    }
    public List<String[]> favoriteFilter(List<String[]> list){
        List<String[]> tempFavoritedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (InformationRetrieve.hasFavoritedKanji(list.get(i)[0]))
                if (list.get(i).length > 2)
                    tempFavoritedList.add(list.get(i));
        }
        return tempFavoritedList;
    }

    private List<String[]> filterCharacter(List<String[]> matrix) {
        EditText editText = findViewById(R.id.searchEditText);
        String searchText = editText.getText().toString();
        List<String[]> tmpMatrix = new ArrayList<>();
        if (!searchText.isEmpty()) {
            for(String[] s : matrix){
                if (s.length > 2)
                    if(s[0].contains(searchText) || s[1].contains(searchText) || s[2].contains(searchText) || s[3].contains(searchText))
                        tmpMatrix.add(s);
            }
            return tmpMatrix;
        }
        return matrix;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_filter_charlist, menu);
        int menuItemId = R.id.item6;
        starItem = menu.findItem(menuItemId);

        menu.findItem(R.id.item1).setChecked(InformationRetrieve.isKanjiMenuItemN1());
        menu.findItem(R.id.item2).setChecked(InformationRetrieve.isKanjiMenuItemN2());
        menu.findItem(R.id.item3).setChecked(InformationRetrieve.isKanjiMenuItemN3());
        menu.findItem(R.id.item4).setChecked(InformationRetrieve.isKanjiMenuItemN4());
        menu.findItem(R.id.item5).setChecked(InformationRetrieve.isKanjiMenuItemN5());
        menu.findItem(R.id.item6).setChecked(InformationRetrieve.isFavoritedStatus());

        if (starItem.isChecked())
            matrixFilter = favoriteFilter(setupList());
        else
            matrixFilter = setupList();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        characterAdapter = new CharacterAdapter(matrixFilter);
        characterAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(characterAdapter);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(!item.isChecked());
        InformationRetrieve.setFavoritedStatus(starItem.isChecked());
        switch (item.getItemId()) {
            case R.id.item1:
                InformationRetrieve.setKanjiMenuItemN1(item.isChecked());
                break;
            case R.id.item2:
                InformationRetrieve.setKanjiMenuItemN2(item.isChecked());
                break;
            case R.id.item3:
                InformationRetrieve.setKanjiMenuItemN3(item.isChecked());
                break;
            case R.id.item4:
                InformationRetrieve.setKanjiMenuItemN4(item.isChecked());
                break;
            case R.id.item5:
                InformationRetrieve.setKanjiMenuItemN5(item.isChecked());
                break;
            case R.id.item6:
                InformationRetrieve.setFavoritedStatus(item.isChecked());
                break;
        }

        if (starItem.isChecked())
            matrixFilter = favoriteFilter(setupList());
        else
            matrixFilter = setupList();
        
        characterAdapter.setData(filterCharacter(matrixFilter));
        
        return super.onOptionsItemSelected(item);
    }
    List<String[]> setupList(){
        List<String[]> list5 = new ArrayList<>();
        List<String[]> list4 = new ArrayList<>();
        List<String[]> list3 = new ArrayList<>();
        List<String[]> list2 = new ArrayList<>();
        List<String[]> list1 = new ArrayList<>();

        if(InformationRetrieve.isKanjiMenuItemN1())
            list1 = SpreadsheetReader.read(this,"kanjin1.csv");
        if(InformationRetrieve.isKanjiMenuItemN2())
            list2 = SpreadsheetReader.read(this,"kanjin2.csv");
        if(InformationRetrieve.isKanjiMenuItemN3())
            list3 = SpreadsheetReader.read(this,"kanjin3.csv");
        if(InformationRetrieve.isKanjiMenuItemN4())
            list4 = SpreadsheetReader.read(this,"kanjin4.csv");
        if(InformationRetrieve.isKanjiMenuItemN5())
            list5 = SpreadsheetReader.read(this,"kanjin5.csv");

        list5.addAll(list4);
        list5.addAll(list3);
        list5.addAll(list2);
        list5.addAll(list1);
        
        return list5;
    }
}
