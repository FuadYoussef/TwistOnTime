package com.atakmap.android.plugintemplate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.plugintemplate.plugin.R;

import java.util.ArrayList;

/**
 * The PresetTimerListAdapter is what allows us to take our preset_timer_cell.xml file and place multiple
 * instances of it in a RecyclerView. It also populates each of these cells with the relevant data.
 * Please see the Android API docs for more details as to how RecyclerView, ViewHolder and ListAdapters work.
 */
public class PresetTimerListAdapter extends RecyclerView.Adapter<PresetTimerListAdapter.ViewHolder> {

    private ArrayList<Timer> preset_timers;

    /**
     * modified version of RecyclerView.ViewHolder that contains the relevant UI elements for a timer_cell
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView presetName;
        TextView presetDuration;
        RelativeLayout parentLayout;
        Button setPresetButton;
        Button deletePresetButton;

        /**
         * Binds xml elements in the view we are putting into the recyclerview to Java variables
         * @param itemView the specific view whose elements are being bound
         */
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            presetName = itemView.findViewById(R.id.preset_timer_name);
            presetDuration = itemView.findViewById(R.id.preset_timer_time);
            parentLayout = itemView.findViewById(R.id.timer_cell);
            setPresetButton = itemView.findViewById(R.id.preset_timer_set_button);
            deletePresetButton = itemView.findViewById(R.id.preset_timer_delete_button);

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
     * @param preset_timers the arraylist of Timers we want to display in our recyclerview
     */
    PresetTimerListAdapter(ArrayList<Timer> preset_timers) {
        this.preset_timers = preset_timers;
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
    public PresetTimerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preset_timer_cell, parent, false);
        return  new PresetTimerListAdapter.ViewHolder(view);
    }

    /** From Android API Website (https://developer.android.com/guide/topics/ui/layout/recyclerview)
     * RecyclerView calls this method to associate a ViewHolder with data.
     * The method fetches the appropriate data and uses the data to fill in the view holder's layout.
     * @param viewHolder the viewholder we created by inflating the timer_cell that we want to fill with data
     * @param position  The position of the item within the recyclerview
     */
    @Override
    public void onBindViewHolder(@NonNull final PresetTimerListAdapter.ViewHolder viewHolder, int position) {
        // get corresponding timer
        Timer currentTimer = preset_timers.get(position);

        viewHolder.presetName.setText(currentTimer.getName());
        viewHolder.presetDuration.setText(currentTimer.getDuration());

        /*
        sets the set button so that when it is clicked, the plugin returns to the home screen and
        creates a new activeTimer corresponding to the current timer. Uses a deepcopy of the timer
        so that changes to the preset timer don't affect the running ActiveTimer
         */
        viewHolder.setPresetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create clone of preset timer
                Timer currentTimer = preset_timers.get(viewHolder.getAdapterPosition());
                Timer timer = new Timer();
                timer.setName(currentTimer.getName());
                timer.setSound(currentTimer.getSound());
                timer.setPreset(currentTimer.isPreset());
                timer.setHours(currentTimer.getHours());
                timer.setMinutes(currentTimer.getMinutes());
                timer.setSeconds(currentTimer.getSeconds());
                ArrayList<String> notificationsCopy = (ArrayList<String>) currentTimer.getNotifications().clone();
                timer.setNotifications(notificationsCopy);

                Intent i = new Intent();
                i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
                i.putExtra("TIMER", timer);
                AtakBroadcast.getInstance().sendBroadcast(i);
            }
        });

        /*
        set the delete button so that when clicked it deletes the selected timer from the preset
        view as well as the JSON file for preset timers
        */
        viewHolder.deletePresetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer currentTimer = preset_timers.get(viewHolder.getAdapterPosition());
                //TODO: delete timer from JSON file as well
                int pos = preset_timers.indexOf(currentTimer);
                preset_timers.remove(currentTimer);
                notifyItemRemoved(pos);
                PluginTemplateDropDownReceiver.presets = preset_timers;
                PluginTemplateDropDownReceiver.writePresetsToJSON(preset_timers);
            }

        });

        // set the name textview so that when clicked it brings up the edit screen for the preset timer
        viewHolder.presetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer currentTimer = preset_timers.get(viewHolder.getAdapterPosition());
                goToEditScreen(currentTimer);
            }
        });

        // set the duration textview so that when clicked it brings up the edit screen for the preset timer
        viewHolder.presetDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer currentTimer = preset_timers.get(viewHolder.getAdapterPosition());
                goToEditScreen(currentTimer);
            }
        });

    }

    /**
     * @return the amount of items in our recyclerview
     */
    @Override
    public int getItemCount() {
        return preset_timers.size();
    }

    /**
     * This function creates and broadcasts the intent to go to the edit screen with the selected
     * preset timer
     * @param presetTimer the preset timer that should be edited
     */
    private void goToEditScreen(Timer presetTimer) {
        Intent i = new Intent();
        i.setAction(CreateTimerDropDown.SHOW_CREATE);
        i.putExtra("TIMER", presetTimer);
        i.putExtra("PRESET", "Pre");
        AtakBroadcast.getInstance().sendBroadcast(i);
    }
}