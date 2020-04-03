package com.example.simplebottomnav.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplebottomnav.R;
import com.example.simplebottomnav.bean.Tag;

public class SearchTagAdapter extends ListAdapter<Tag, SearchTagAdapter.TagViewHolder> {
    public SearchTagAdapter() {
        super(new DiffUtil.ItemCallback<Tag>() {
            @Override
            public boolean areItemsTheSame(@NonNull Tag oldItem, @NonNull Tag newItem) {
                return oldItem.getT_id() == newItem.getT_id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Tag oldItem, @NonNull Tag newItem) {
                return oldItem.getTagName().equals(newItem.getTagName());
            }
        });
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final TagViewHolder holder = new TagViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_tag_cell, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.tagName.setText(getItem(position).getTagName());
    }

    class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tagName;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tagName);
        }
    }
}

