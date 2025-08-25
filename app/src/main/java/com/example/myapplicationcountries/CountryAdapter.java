package com.example.myapplicationcountries;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private Context context;
    private List<Country> countryList;

    public CountryAdapter(Context context, List<Country> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = countryList.get(position);

        holder.txtName.setText(country.getName());
        holder.txtCapital.setText(country.getCapital());

        if (country.getFlagResId() != 0) {
            holder.imgFlag.setImageResource(country.getFlagResId());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("country", country);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public void setCountries(List<Country> countries) {
        this.countryList = countries;
        notifyDataSetChanged();
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtCapital;
        ImageView imgFlag;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtCountryName);
            txtCapital = itemView.findViewById(R.id.txtCapital);
            imgFlag = itemView.findViewById(R.id.imgFlag);
        }
    }
}
