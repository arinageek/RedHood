package com.example.redhood.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.redhood.R;


public class GameResultsFragment extends Fragment {

    private int correct, all;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_results, container, false);
        correct = getArguments().getInt("game_correct", 0);
        all = getArguments().getInt("game_all", 0);
        ((TextView) view.findViewById(R.id.tv_results)).setText("You got "+correct+" out of "+all+" !");
        return view;
    }
}
