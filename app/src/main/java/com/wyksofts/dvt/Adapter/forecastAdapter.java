package com.wyksofts.dvt.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyksofts.dvt.R;
import com.wyksofts.dvt.Util.DVTConverter;
import com.wyksofts.dvt.Util.DVTWeatherIconManager;
import com.wyksofts.dvt.Util.Helpers.DVTDatesHelper;
import com.wyksofts.dvt.ui.TodayView.DateTimeEntry;

import java.util.ArrayList;

public class forecastAdapter extends RecyclerView.Adapter<forecastAdapter.MyViewHolder>{

    ArrayList<DateTimeEntry> data;
    Context context;

    //String days[] = {"Monday","Tuesday","Wednesday", "Thursday","Friday"};

    public forecastAdapter(Context context,ArrayList<DateTimeEntry> data) {
        this.data = data;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.other_days_temp_card, parent, false));
    }

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        DateTimeEntry current = data.get(position);

        holder.temp.setText(new DVTConverter().getCelcius(Float.parseFloat(current.temperature))+"°C");
        holder.day.setText(new DVTDatesHelper().getToday()[position]);
        //holder.day.setText(current.date);

        String icon = String.valueOf(current.icon);
        Glide.with(context)
                .load(new DVTWeatherIconManager().getIcon(icon))
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView day, temp;
        ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day =  itemView.findViewById(R.id.another_day);
            temp = itemView.findViewById(R.id.another_day_temp);
            icon = itemView.findViewById(R.id.another_day_image);
        }
    }
}
