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

/**
 * The TimerListAdapter is what allows us to take our timer_cell.xml file and place multiple
 * instances of it in a RecyclerView. It also populates each of these cells with the relevant data.
 * Please see the Android API docs for more details as to how RecyclerView, ViewHolder and ListAdapters work.
 */
public class TimerListAdapter extends RecyclerView.Adapter<TimerListAdapter.ViewHolder> {

    private ArrayList<ActiveTimer> timers;

    /**
     * modified version of RecyclerView.ViewHolder that contains the relevant UI elements for a timer_cell
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView timerName;
        TextView timerDuration;
        RelativeLayout parentLayout;

        /**
         * Binds xml elements in the view we are putting into the recyclerview to Java variables
         * @param itemView the specific view whose elements are being bound
         */
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            timerName = itemView.findViewById(R.id.timer_name);
            timerDuration = itemView.findViewById(R.id.timer_time);
            parentLayout = itemView.findViewById(R.id.timer_cell);

            itemView.setOnClickListener(this);
        }


        /**
         * determines what happens if an element in the list view is clicked on
         * @param view the view that is being clicked (we don't have to actually call this ourselves)
         */
        @Override
        public void onClick(View view) {
        }
    }

    /**
     * Constructer for the TimerListAdapter
     * @param timers the arraylist of ActiveTimers we want to display in our recyclerview
     */
    TimerListAdapter(ArrayList<ActiveTimer> timers) {
        this.timers = timers;
    }

    /** From Android API Website (https://developer.android.com/guide/topics/ui/layout/recyclerview)
     * RecyclerView calls this method whenever it needs to create a new ViewHolder.
     * The method creates and initializes the ViewHolder and its associated View,
     * but does not fill in the view's contents—the ViewHolder has not yet been bound to specific data.
     * @param parent the recyclerview that the view is being loaded into
     * @param viewType view type of the new view
     * @return a new viewholder based on the timer_cell xml
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_cell, parent, false);
        return  new ViewHolder(view);
    }

    /** From Android API Website (https://developer.android.com/guide/topics/ui/layout/recyclerview)
     * RecyclerView calls this method to associate a ViewHolder with data.
     * The method fetches the appropriate data and uses the data to fill in the view holder's layout.
     * @param viewHolder the viewholder we created by inflating the timer_cell that we want to fill with data
     * @param position  The position of the item within the recyclerview
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.timerDuration.setText(timers.get(position).getDurationRemainingString());
        viewHolder.timerName.setText(timers.get(position).getName());
    }

    /**
     * @return the amount of items in our recylcerview
     */
    @Override
    public int getItemCount() {
        return timers.size();
    }
}