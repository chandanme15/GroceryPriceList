package com.project.commoditiesCurrentPrice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.commoditiesCurrentPrice.model.Record;
import com.project.commoditiesCurrentPrice.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private static List<Record> mData = new ArrayList<>();

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
        Record data = mData.get(position);
        if(data.getMarket() != null) mViewHolder.setMarket(data.getMarket());
        if(data.getCommodity() != null) mViewHolder.setCommodity(data.getCommodity());
        if(data.getDistrict() != null && data.getState() != null)
            mViewHolder.setRegion(data.getDistrict() + ", " + data.getState());
        if (data.getMin_price() != null) mViewHolder.setMinPrice(data.getMin_price());
        if (data.getModal_price() != null) mViewHolder.setModalPrice(data.getModal_price());
        if (data.getMax_price() != null) mViewHolder.setMaxPrice(data.getMax_price());
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

    public boolean addData(List<Record> recordList){
        boolean bRet = mData.addAll(recordList);
        notifyDataSetChanged();
        return bRet;
    }

    public void sortDataByState(){
        try{
            Collections.sort(mData, (o1, o2) -> o1.getState().compareTo(o2.getState()));
        }
        catch (Exception e) {

        }
        notifyDataSetChanged();
    }

    public void sortDataByModalPrice(){
        try{
            Collections.sort(mData, (o1, o2) -> (Integer.compare(Integer.parseInt(o1.getModal_price()), Integer.parseInt(o2.getModal_price()))));
        }
        catch (Exception e) {

        }
        notifyDataSetChanged();
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

        void setMarket(String market) {
            mMarket.setText(market);
        }

        void setCommodity(String commodity) {
            mCommodity.setText(commodity);
        }

        void setRegion(String region) {
            mRegion.setText(region);
        }

        void setModalPrice(String modalPrice) {
            mModalPrice.setText(modalPrice);
        }

        void setMinPrice(String minPrice) {
            mMinPrice.setText(minPrice);
        }

        void setMaxPrice(String maxPrice) {
            mMaxPrice.setText(maxPrice);
        }

        void setSubItemVisibility(boolean isVisible) {
            mSubItem.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}
