package com.example.redhood;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redhood.database.entities.Set;
import com.example.redhood.database.entities.Word;

public class WordAdapter extends ListAdapter<Word, WordAdapter.WordHolder> {

    private OnItemClickListener listener;

    public WordAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Word> DIFF_CALLBACK = new DiffUtil.ItemCallback<Word>() {
        @Override
        public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
            return (oldItem.getOriginal().equals(newItem.getOriginal()) && oldItem.getTranslation().equals(newItem.getTranslation()));
        }
    };

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.words_row_item, parent, false);
        return new WordHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHolder holder, int position) {
        Word currentWord = getItem(position);
        Log.d("Word: ", "Orig: "+currentWord.getOriginal());
        holder.textViewTranslation.setText(currentWord.getTranslation());
        holder.textViewOriginal.setText(currentWord.getOriginal());
    }

    public Word getWordAt(int position) {
        return getItem(position);
    }

    class WordHolder extends RecyclerView.ViewHolder {
        private TextView textViewOriginal;
        private TextView textViewTranslation;

        public WordHolder(@NonNull View itemView) {
            super(itemView);
            textViewOriginal = itemView.findViewById(R.id.text_view_original);
            textViewTranslation = itemView.findViewById(R.id.text_view_translation);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Word word);
    }

    public void wordOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
