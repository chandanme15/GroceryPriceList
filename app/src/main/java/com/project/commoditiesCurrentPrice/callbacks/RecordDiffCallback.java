package com.project.commoditiesCurrentPrice.callbacks;

import androidx.recyclerview.widget.DiffUtil;

import com.project.commoditiesCurrentPrice.model.Record;

import java.util.List;

public class RecordDiffCallback extends DiffUtil.Callback {

    private final List<Record> oldList;
    private final List<Record> newList;

    public RecordDiffCallback(List<Record> oldList, List<Record> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
