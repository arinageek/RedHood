package com.example.redhood.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redhood.R;
import com.example.redhood.database.entities.Set;

public class SetAdapter extends ListAdapter<Set, SetAdapter.SetHolder> {

    private OnItemClickListener listener;

    public SetAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Set> DIFF_CALLBACK = new DiffUtil.ItemCallback<Set>() {
        @Override
        public boolean areItemsTheSame(@NonNull Set oldItem, @NonNull Set newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Set oldItem, @NonNull Set newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public SetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sets_row_item, parent, false);
        return new SetHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SetHolder holder, int position) {
        Set currentSet = getItem(position);
        holder.textViewTitle.setText(currentSet.getName());
    }

    public Set getSetAt(int position) {
        return getItem(position);
    }

    class SetHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        public SetHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(getItem(position), position);
                }
                return false;
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Set set);
        void onItemLongClick(Set set, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
