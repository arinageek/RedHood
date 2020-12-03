package com.example.redhood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;
import java.util.zip.Inflater;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    protected ArrayList<Dictionary> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v){
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public CustomAdapter(ArrayList<Dictionary> dataSet) {
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextView().setText(mDataSet.get(position).original+" - "+mDataSet.get(position).translation);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}


