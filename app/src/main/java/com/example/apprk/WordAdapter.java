package com.example.apprk;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
    private final List<String[]> wordListData;
    private OnItemClickListener onItemClickListener;

    public WordAdapter(List<String[]> wordListData) {
        this.wordListData = wordListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_list_menu_item, parent, false);
        return new ViewHolder(view);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setData(List<String[]> newData) {
        wordListData.clear();
        wordListData.addAll(newData);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(String text);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String word = wordListData.get(position)[0];
        holder.wordKanji.setText(word);
        holder.wordKana.setText(wordListData.get(position)[1]);
        holder.wordTranslate.setText(wordListData.get(position)[2]);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(word +"$"+ wordListData.get(position)[2]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordKanji;
        TextView wordKana;
        TextView wordTranslate;

        public ViewHolder(View itemView) {
            super(itemView);
            wordKanji = itemView.findViewById(R.id.word_kanji);
            wordKana = itemView.findViewById(R.id.word_kana);
            wordTranslate = itemView.findViewById(R.id.word_translate);
        }
    }
}
