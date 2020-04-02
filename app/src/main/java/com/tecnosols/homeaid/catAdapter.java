package com.tecnosols.homeaid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class catAdapter extends RecyclerView.Adapter<catAdapter.categoryViewHolder> {
    ArrayList<catModel> catList = new ArrayList<>();

    public catAdapter(ArrayList<catModel> catList) {
        this.catList = catList;
    }

    @NonNull
    @Override
    public categoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_layout, parent, false);
        return new categoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryViewHolder holder, int position) {
        String name = catList.get(position).getCatName();
        String img = catList.get(position).getCatImg();
        holder.setName(name);
        holder.setcatImg(img);

    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public static class categoryViewHolder extends RecyclerView.ViewHolder {
        private TextView cat_name;
        private ImageView catImg;

        public categoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_name = itemView.findViewById(R.id.textView_catName);
            catImg=itemView.findViewById(R.id.imageView4);
        }

        private void setName(String name) {
            cat_name.setText(name);
        }
        private void setcatImg(String img){
            Picasso.get().load(img).into(catImg);
        }
    }
}
