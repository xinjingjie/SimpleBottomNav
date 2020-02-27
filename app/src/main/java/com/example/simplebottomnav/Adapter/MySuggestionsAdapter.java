package com.example.simplebottomnav.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplebottomnav.R;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.List;

public class MySuggestionsAdapter extends SuggestionsAdapter<String, MySuggestionsAdapter.SuggestionHolder> {
    //List<String> suggestionsList=new ArrayList<>(10);

    public MySuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public void setSuggestions(List<String> suggestions) {
        super.setSuggestions(suggestions);
    }

    @Override
    public void onBindSuggestionHolder(String s, SuggestionHolder suggestionHolder, int position) {

        suggestionHolder.word_tip.setText(s);
    }

    @Override
    public int getSingleViewHeight() {
        return 0;
    }

    @NonNull
    @Override
    public SuggestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionHolder(view);
    }

    static class SuggestionHolder extends RecyclerView.ViewHolder {
        TextView word_tip;

        public SuggestionHolder(@NonNull View itemView) {
            super(itemView);
            word_tip = itemView.findViewById(R.id.word_tip);
        }
    }
}
