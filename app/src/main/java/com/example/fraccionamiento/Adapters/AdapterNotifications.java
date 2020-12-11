package com.example.fraccionamiento.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fraccionamiento.Classes.NotificationClass;
import com.example.fraccionamiento.R;

import java.util.ArrayList;

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.ViewHolderData> {

    ArrayList<NotificationClass> lstNotifications;


    public AdapterNotifications(ArrayList<NotificationClass> listNotifications) {
        this.lstNotifications = listNotifications;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_rows,null,false);

        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {

        if (lstNotifications.get(position).getChecked()){
            holder.linearLayout.setBackgroundResource(R.color.colorWhite);
        }else {
            holder.linearLayout.setBackgroundResource(R.color.colorUnChecked);
        }


        holder.notification.setText(lstNotifications.get(position).getNotification());
        holder.date.setText(lstNotifications.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return lstNotifications.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {
        TextView notification;
        TextView date;
        LinearLayout linearLayout;
        public ViewHolderData(View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.lblNotificationItem);
            date = itemView.findViewById(R.id.lblNotificationDate);
            linearLayout = itemView.findViewById(R.id.linearLayoutNotification);

        }

        public void assignData(String text) {

            notification.setText(text);
        }
    }
}