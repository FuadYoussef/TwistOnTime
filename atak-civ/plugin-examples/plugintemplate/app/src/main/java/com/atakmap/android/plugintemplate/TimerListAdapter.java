package com.atakmap.android.plugintemplate;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atakmap.android.plugintemplate.plugin.R;

import java.util.ArrayList;

public class TimerListAdapter extends RecyclerView.Adapter<TimerListAdapter.ViewHolder> {

    private ArrayList<ActiveTimer> timers;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView timerName;
        TextView timerDuration;
        RelativeLayout parentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            timerName = itemView.findViewById(R.id.first_timer_name);
            timerDuration = itemView.findViewById(R.id.first_timer_time);
            parentLayout = itemView.findViewById(R.id.timer_cell);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    TimerListAdapter(ArrayList<ActiveTimer> timers) {
        this.timers = timers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_cell, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.timerDuration.setText(timers.get(position).getDurationRemainingString());
        viewHolder.timerName.setText(timers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return timers.size();
    }
}