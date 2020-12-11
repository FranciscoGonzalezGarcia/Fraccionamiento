package com.example.fraccionamiento.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fraccionamiento.Classes.CalendarClass;
import com.example.fraccionamiento.R;

import java.util.ArrayList;

public class AdapterCalendarClient extends RecyclerView.Adapter<AdapterCalendarClient.ViewHolderData> implements View.OnClickListener {
    private View.OnClickListener listener;
    private ArrayList<CalendarClass> calendarList;

    public AdapterCalendarClient(ArrayList<CalendarClass> calendarList) {
        this.calendarList = calendarList;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_calendar, null, false);
        view.setOnClickListener(this);
        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.title.setText(calendarList.get(position).getTitle());
        holder.description.setText(calendarList.get(position).getDescription());
        String fullDate = calendarList.get(position).getDay()+"/"+calendarList.get(position).getMonth()+"/"+calendarList.get(position).getYear();
        holder.date.setText(fullDate);
        String hour = calendarList.get(position).getHour1()+calendarList.get(position).getHour2()+":"+calendarList.get(position).getMinute1();
        holder.hour.setText(hour);
        holder.hourSys.setText(calendarList.get(position).getHourSystem());

    }

    @Override
    public int getItemCount() { return calendarList.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        private TextView title, description, date, hour, hourSys;
        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtCalendarTitle);
            description = itemView.findViewById(R.id.txtCalendarDescription);
            date = itemView.findViewById(R.id.txtDate);
            hour = itemView.findViewById(R.id.txtHour);
            hourSys = itemView.findViewById(R.id.txtHourSys);
        }


    }
}
