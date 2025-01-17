package com.example.hemms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private List<Staff> staffList;

    public StaffAdapter(List<Staff> staffList) {
        this.staffList = staffList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staff, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Staff staff = staffList.get(position);
        holder.staffName.setText(staff.getStaffFirstName() + " " + staff.getStaffLastName());
        holder.staffTitle.setText(staff.getStaffTitle());
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView staffName, staffTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            staffName = itemView.findViewById(R.id.staff_name);
            staffTitle = itemView.findViewById(R.id.staff_title);
        }
    }
}
