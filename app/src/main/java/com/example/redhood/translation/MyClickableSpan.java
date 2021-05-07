package com.example.redhood.translation;

import android.text.style.ClickableSpan;
import android.view.View;

public class MyClickableSpan extends ClickableSpan {
    private String text;
    private final int start, end;
    private boolean selected;
    private OnSpanClickedListener listener;

    public MyClickableSpan(String text, int start, int end) {
        this.text = text.trim();
        this.start = start;
        this.end = end;
        this.selected = false;
    }

    public void setOnSpanClickListener(OnSpanClickedListener listener) {
        this.listener = listener;
    }

    public void setSelected(boolean b){
        selected = b;
    }

    @Override
    public void onClick(View view) {
        if(listener != null) listener.onSpanClicked(start, end, selected, text);
    }
}