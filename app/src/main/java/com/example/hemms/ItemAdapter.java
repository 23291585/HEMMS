package com.example.hemms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context context;
    private List<Item> itemList;

    // Constructor
    public ItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    // ViewHolder sınıfı
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemCategory, itemStock, itemRoom;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemCategory = itemView.findViewById(R.id.item_category);
            itemStock = itemView.findViewById(R.id.item_stock);
            itemRoom = itemView.findViewById(R.id.item_room);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        // Veriyi View'a bağla
        holder.itemName.setText(item.getItemName());
        holder.itemCategory.setText(item.getItemCategory());
        holder.itemStock.setText("Stok: " + item.getQuantity());
        holder.itemRoom.setText("Oda: " + item.getRoomId());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
