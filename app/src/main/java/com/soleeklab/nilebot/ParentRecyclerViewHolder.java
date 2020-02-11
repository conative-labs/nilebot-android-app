package com.soleeklab.nilebot;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.soleeklab.nilebot.utils.listeners.OnItemClickListener;


/**
 * is a base class to extend from it the viewholder
 */
public class ParentRecyclerViewHolder extends RecyclerView.ViewHolder {
    private View clickableRootView; // this is used to change the default onItemClickListener
    private int holderType;

    public ParentRecyclerViewHolder(final View itemView, int holderType) {
        super(itemView);
        this.holderType = holderType;

    }


    public ParentRecyclerViewHolder(final View itemView) {
        super(itemView);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        if (clickableRootView != null) {
            clickableRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getPosition());
                    }
                }
            });
        } else {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getPosition());
                    }
                }
            });
        }
    }

    public void setClickableRootView(View clickableRootView) {
        this.clickableRootView = clickableRootView;
    }

    public View findViewById(int viewId) {
        if (itemView != null) {
            return itemView.findViewById(viewId);
        } else {
            return null;
        }
    }

    public int getHolderType() {
        return holderType;
    }
}
