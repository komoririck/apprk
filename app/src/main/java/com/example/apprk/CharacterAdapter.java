package com.example.apprk;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {
    private List<String[]> alphabet;
    private OnItemClickListener onItemClickListener;

    public CharacterAdapter(List<String[]> alphabet) {
        this.alphabet = alphabet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.character_list_menu_item, parent, false);
        return new ViewHolder(view);
    }

    public void setData(List<String[]> newData) {
        alphabet.clear();
        alphabet.addAll(newData);
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(String text);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String letter = alphabet.get(position)[0];
        holder.textView.setText(letter);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(letter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alphabet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
