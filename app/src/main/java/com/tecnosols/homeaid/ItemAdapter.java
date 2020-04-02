package com.tecnosols.homeaid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.itemViewHolder> {
    ArrayList<ItemModel> itemsList = new ArrayList<>();

    public ItemAdapter(ArrayList<ItemModel> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_layout, parent, false);
        return new ItemAdapter.itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        String itmname=itemsList.get(position).getItemname();
        String itmprice=itemsList.get(position).getItemprice();

        holder.setItem_Name(itmname);
        holder.setItem_Price("Rs."+itmprice);

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class itemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemname, itemprice;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemname = itemView.findViewById(R.id.textView_item_name);
            itemprice = itemView.findViewById(R.id.textView8_price);

        }

        private void setItem_Name(String nam) {
            itemname.setText(nam);
        }

        private void setItem_Price(String pr) {
            itemprice.setText(pr);
        }
    }
}
