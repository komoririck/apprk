package com.example.apprk;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KanaAdapter extends RecyclerView.Adapter<KanaAdapter.ViewHolder> {
    private final List<String[]> kanaAdapter;
    private OnItemClickListener onItemClickListener;
    public KanaAdapter(List<String[]> kanaAdapter) {
        this.kanaAdapter = kanaAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kana_list_menu_item, parent, false);
        return new ViewHolder(view);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setData(List<String[]> newData) {
        kanaAdapter.clear();
        kanaAdapter.addAll(newData);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(String text);


    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.wordKanji.setText(kanaAdapter.get(position)[0]);
        holder.wordKana.setText(kanaAdapter.get(position)[1]);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(kanaAdapter.get(position)[0]);
            }
        });
    }
    @Override
    public int getItemCount() {
        return kanaAdapter.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordKanji;
        TextView wordKana;

        public ViewHolder(View itemView) {
            super(itemView);
            wordKanji = itemView.findViewById(R.id.word_kanji);
            wordKana = itemView.findViewById(R.id.word_kana);
        }
    }
}
