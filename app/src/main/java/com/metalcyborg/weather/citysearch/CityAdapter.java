package com.metalcyborg.weather.citysearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metalcyborg.weather.R;
import com.metalcyborg.weather.data.City;

import java.util.ArrayList;
import java.util.List;


public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<City> mItems = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CityClickListener mCityClickListener;

    public interface CityClickListener {
        void onClick(City city);
    }

    public CityAdapter(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item,
                parent, false);
        return new CityViewHolder(item);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        City city = mItems.get(position);
        holder.mCityName.setText(city.getName());
        holder.mCountryName.setText(city.getCountry());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setCityList(List<City> items) {
        mItems = items;
    }

    public void setCityClickListener(CityClickListener listener) {
        mCityClickListener = listener;
    }

    public class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mCityName;
        private TextView mCountryName;

        public CityViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mCityName = (TextView) itemView.findViewById(R.id.cityName);
            mCountryName = (TextView) itemView.findViewById(R.id.countryName);
        }

        @Override
        public void onClick(View v) {
            if(mCityClickListener != null) {
                int position = mRecyclerView.getLayoutManager().getPosition(v);
                mCityClickListener.onClick(mItems.get(position));
            }
        }
    }
}
