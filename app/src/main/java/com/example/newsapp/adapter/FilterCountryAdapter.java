package com.example.newsapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.model.FilterData;
import com.example.newsapp.data.FilterDataProvider;

import java.util.ArrayList;
import java.util.List;

public class FilterCountryAdapter extends RecyclerView.Adapter<FilterCountryAdapter.FilterViewHolder>{
    private List<FilterData> mData;
    private static final String TAG = "MyTAG:Filter:CnA";
    Context mContext;
    public FilterCountryAdapter(Context context, String filterData) {
        this.mData = FilterDataProvider.getFilterCountries();
        this.mContext = context;
        setFilterData(filterData);
    }

    public void setFilterData(String filter){
        if(filter.trim().equals(""));
        Log.d(TAG, "setFilterData: data : "+filter);
        String [] filters = filter.split(",");
        for (int i = 0; i < filters.length; i++) {
            try {
                int position = findPosition(filters[i]);
                mData.get(position).selected = true;
                notifyItemChanged(position);
                Log.d(TAG, "setFilterData: position: "+position);
            } catch (Exception e) {
                Log.e(TAG, "setFilterData: ", e);
            }
        }
    }

    public List<FilterData> getSelectedData(){
        List<FilterData> selectedData = new ArrayList<>();
        for (FilterData data: mData) {
            if(data.selected){
                selectedData.add(data);
            }
        }
        return selectedData;
    }
    private int findPosition(String data) throws Exception {
        Log.d(TAG, "findPosition: "+data);
        int position = -1;
        for (int i = 0; i < mData.size(); i++) {
            if(mData.get(i).code.equalsIgnoreCase(data)){
                Log.d(TAG, "findPosition: position: "+i);
                position = i;
                break;
            }
        }
        if (position == -1){
            throw new Exception("Not found exception");
        }
        return position;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_filter_text,null,false);
        return new FilterCountryAdapter.FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterCountryAdapter.FilterViewHolder holder, int position) {

        holder.dataTextView.setText(mData.get(position).name);
        if (mData.get(position).selected){
            holder.dataTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        else{
            holder.dataTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        holder.dataTextView.setOnClickListener(v -> {
            mData.get(position).selected = !mData.get(position).isSelected();
            if (mData.get(position).selected){
                holder.dataTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
            else{
                holder.dataTextView.setTextColor(mContext.getResources().getColor(R.color.black));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class FilterViewHolder extends RecyclerView.ViewHolder{
        private TextView dataTextView;
        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            dataTextView = itemView.findViewById(R.id.singleFilterTextId);
        }
    }
}