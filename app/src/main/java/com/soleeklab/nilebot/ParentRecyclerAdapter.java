package com.soleeklab.nilebot;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.soleeklab.nilebot.utils.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * is a base class to extend from it the recyclerview adapter
 */
public abstract class ParentRecyclerAdapter<Item> extends RecyclerView.Adapter<ParentRecyclerViewHolder> {
    private List<Item> data = new ArrayList<>();
    public OnItemClickListener itemClickListener;

    public ParentRecyclerAdapter(List<Item> data) {
        this.data = data;
    }

    ParentRecyclerAdapter(Item[] data) {
        this.data = Arrays.asList(data);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public List<Item> getData() {
        return data;
    }

    public OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void insertAll(List<Item> items) {
        data.addAll(items);
        notifyDataSetChanged();

    }

    public void insertAll(int position, List<Item> items) {
        data.addAll(position, items);
        notifyDataSetChanged();
    }

    public void reverseAll(){
        Collections.reverse(data);
        notifyDataSetChanged();
    }

    public void insert(int position, Item item) {
        data.add(position, item);
        notifyDataSetChanged();
    }

    public void insert(Item item) {
        data.add(item);
        notifyDataSetChanged();
    }

    public void update(int position, Item item) {
        data.remove(position);
        data.add(position, item);
        notifyDataSetChanged();
    }

    public void updateAll(List<Item> items) {
        data.clear();
        data.addAll(items);
        notifyDataSetChanged();
    }

    public void replaceItem(int position, Item item) {
        data.remove(position);
        data.add(position, item);
        notifyDataSetChanged();
    }


    public void remove(int postion) {
        data.remove(postion);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addFooterProgress() {
        this.data.add(null);
        notifyItemInserted(data.size() - 1);
    }

    public void removeFooterProgress() {
        if (data.size() > 0) {
            data.remove(data.size() - 1);
            notifyItemRemoved(data.size());
        }
    }

}
