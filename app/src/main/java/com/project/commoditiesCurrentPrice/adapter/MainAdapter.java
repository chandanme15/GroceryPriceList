package com.project.commoditiesCurrentPrice.adapter;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.commoditiesCurrentPrice.callbacks.RecordDiffCallback;
import com.project.commoditiesCurrentPrice.model.Record;
import com.project.commoditiesCurrentPrice.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final List<Record> mData = new ArrayList<>();

    public MainAdapter(){}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder mViewHolder, int position) {
        mViewHolder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return (mData == null ? 0 : mData.size());
    }

    public List<Record> getData() {
        return mData;
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(List<Record> recordList){
        int oldSize = mData.size();
        mData.addAll(recordList);
        notifyItemRangeInserted(oldSize, recordList.size());
    }

    public void sortDataByState(){
        new SortingTask(mData, this).execute(false);
    }

    public void sortDataByModalPrice(){
        new SortingTask(mData, this).execute(true);
    }

    @SuppressWarnings("rawtypes")
    private static class SortingTask extends AsyncTask<Boolean, Void, DiffUtil.DiffResult> {

        WeakReference<List<Record>> mListReference;
        WeakReference<RecyclerView.Adapter> mAdapterReference;

        SortingTask(List<Record> list, RecyclerView.Adapter adapter) {
            mListReference = new WeakReference<>(list);
            mAdapterReference = new WeakReference<>(adapter);
        }

        @Override
        protected DiffUtil.DiffResult doInBackground(Boolean... booleans) {
            List<Record> oldList = new ArrayList<>(mListReference.get());
            if(booleans[0]) {
                Collections.sort(mListReference.get(), (o1, o2) -> (Integer.compare(Integer.parseInt(o1.getModal_price()), Integer.parseInt(o2.getModal_price()))));
            }
            else {
                Collections.sort(mListReference.get(), (o1, o2) -> o1.getState().compareTo(o2.getState()));
            }
            RecordDiffCallback diffCallback = new RecordDiffCallback(oldList, mListReference.get());
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
            return diffResult;
        }

        @Override
        protected void onPostExecute(DiffUtil.DiffResult diffResult) {
            super.onPostExecute(diffResult);
            if(diffResult != null) {
                diffResult.dispatchUpdatesTo(mAdapterReference.get());
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mMarket;
        private final TextView mCommodity;
        private final TextView mRegion;
        private final TextView mModalPrice;
        private final TextView mMinPrice;
        private final TextView mMaxPrice;

        private final LinearLayout mSubItem;

        ViewHolder(@NonNull final View mView) {
            super(mView);

            mMarket = mView.findViewById(R.id.market);
            mCommodity = mView.findViewById(R.id.commodity);
            mRegion = mView.findViewById(R.id.region);
            mMinPrice = mView.findViewById(R.id.minPrice);
            mModalPrice = mView.findViewById(R.id.modalPrice);
            mMaxPrice = mView.findViewById(R.id.maxPrice);
            mSubItem = mView.findViewById(R.id.subItem);
        }

        public void bind(Record record) {
            if(record.getMarket() != null) mMarket.setText(record.getMarket());
            if(record.getCommodity() != null) mCommodity.setText(record.getCommodity());
            if(record.getDistrict() != null && record.getState() != null)
                mRegion.setText(String.format("%s, %s", record.getDistrict(), record.getState()));
            if (record.getMin_price() != null) mMinPrice.setText(record.getMin_price());
            if (record.getModal_price() != null) mModalPrice.setText(record.getModal_price());
            if (record.getMax_price() != null) mMaxPrice.setText(record.getMax_price());
        }
    }
}
