package com.example.redhood.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.redhood.R;
import com.example.redhood.database.entities.Word;
import com.example.redhood.database.relations.SetWithWords;
import com.example.redhood.viewmodels.GameViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameFragment extends Fragment implements View.OnClickListener {

    private int setId;
    private GameViewModel gameViewModel;
    private ArrayList<String> originals = new ArrayList<>();
    private ArrayList<String> translations = new ArrayList<>();
    private ArrayList<Integer> questionsArray = new ArrayList<>();
    private String[] answers = new String[4];
    private int chosenWord = 0, rounds = 0, curRound = 0, locationOfCorrectAnswer = 0, correctAnswers = 0;
    private TextView originalWord, score;
    private Button btn1, btn2, btn3, btn4;
    private Random rand;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        rand = new Random();
        setId = Integer.parseInt(getArguments().getString("set_id", "0"));

        originalWord = view.findViewById(R.id.text_view_original);
        score = view.findViewById(R.id.score);

        btn1 = view.findViewById(R.id.button1); btn1.setOnClickListener(this);
        btn2 = view.findViewById(R.id.button2); btn2.setOnClickListener(this);
        btn3 = view.findViewById(R.id.button3); btn3.setOnClickListener(this);
        btn4 = view.findViewById(R.id.button4); btn4.setOnClickListener(this);

        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        gameViewModel.getSetWithWords(setId).observe(getViewLifecycleOwner(), setWithWords -> {
            List<Word> words = setWithWords.get(0).words;
            if(!words.isEmpty()){
                for(Word word : words){
                    originals.add(word.getOriginal());
                    translations.add(word.getTranslation());
                }
            }
            rounds = words.size();
            nextWord();
        });

        return view;
    }

    private void checkNextAction(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            if (curRound < rounds){
                nextWord();
            }else{
                GameResultsFragment fragment = new GameResultsFragment();
                Bundle arguments = new Bundle();
                arguments.putInt("game_correct", correctAnswers);
                arguments.putInt("game_all", rounds);
                fragment.setArguments(arguments);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragment, "game_results").addToBackStack(null).commit();
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        Handler handler = new Handler(Looper.getMainLooper());
        switch (v.getId()) {
            case R.id.button1:
                if (locationOfCorrectAnswer == 0){
                    correctAnswers++;
                    btn1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
                } else {
                    btn1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                }
                checkNextAction();
                break;
            case R.id.button2:
                if (locationOfCorrectAnswer == 1){
                    correctAnswers++;
                    btn2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
                } else {
                    btn2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                }
                checkNextAction();
                break;
            case R.id.button3:
                if (locationOfCorrectAnswer == 2){
                    correctAnswers++;
                    btn3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
                } else {
                    btn3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                }
                checkNextAction();
                break;
            case R.id.button4:
                if (locationOfCorrectAnswer == 3){
                    correctAnswers++;
                    btn4.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
                } else {
                    btn4.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                }
                checkNextAction();
                break;
        }
    }

    private boolean isAlreadyContained(ArrayList<Integer> answers, int answer){
        for(int i : answers){
            if(i == answer) return true;
        }
        return false;
    }

    private void nextWord(){

        btn1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
        btn2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
        btn3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
        btn4.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));

        //Setting the right score
        curRound++;
        score.setText(curRound+"/"+rounds);

        //Choosing a word from originals array
        chosenWord = rand.nextInt(originals.size());
        while(isAlreadyContained(questionsArray, chosenWord)){
            chosenWord = rand.nextInt(originals.size());
        }
        questionsArray.add(chosenWord);
        originalWord.setText(originals.get(chosenWord));

        locationOfCorrectAnswer = rand.nextInt(4);

        ArrayList<Integer> incorrectArray = new ArrayList<>();
        int incorrectAnswer;
        for(int i=0; i<4; i++){
            if(i == locationOfCorrectAnswer){
                answers[i] = translations.get(chosenWord);
            }else{
                incorrectAnswer = rand.nextInt(originals.size());
                while((incorrectAnswer == chosenWord) || isAlreadyContained(incorrectArray, incorrectAnswer)){
                    incorrectAnswer = rand.nextInt(originals.size());
                }
                incorrectArray.add(incorrectAnswer);
                answers[i] = translations.get(incorrectAnswer);
            }
        }
        btn1.setText(answers[0]);
        btn2.setText(answers[1]);
        btn3.setText(answers[2]);
        btn4.setText(answers[3]);

    }

}
