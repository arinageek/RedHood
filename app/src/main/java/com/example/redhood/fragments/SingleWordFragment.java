package com.example.redhood.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.redhood.R;

public class SingleWordFragment extends Fragment {

    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_word, container, false);

        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        String word = getArguments().getString("word", "error");
        webView.loadUrl("https://youglish.com/pronounce/"+word);

        return view;
    }


}
