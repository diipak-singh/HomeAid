package com.tecnosols.homeaid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.categoryViewHolder> {
    ArrayList<MyBookingsModel> myBookingList = new ArrayList<>();


    public MyBookingAdapter(ArrayList<MyBookingsModel> myBookingList) {
        this.myBookingList = myBookingList;

    }

    @NonNull
    @Override
    public categoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_current_bookings_layout, parent, false);
        return new MyBookingAdapter.categoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryViewHolder holder, int position) {
        String img = myBookingList.get(position).getItemImg();
        String catg = myBookingList.get(position).getItemCatg();
        String name = myBookingList.get(position).getItemName();
        String price = myBookingList.get(position).getItemPrice();
        String bkdDate = myBookingList.get(position).getBookingDT();
        String srvcDate = myBookingList.get(position).getServiceDT();
        String paymentStatus = myBookingList.get(position).getPaymentStatus();
        String iscomplete = myBookingList.get(position).getIsCompleted();
        String rating = myBookingList.get(position).getRating();

        holder.setImg(img);
        holder.setCatg(catg);
        holder.setName(name);
        holder.setPrice(price);
        holder.setBookedDate(bkdDate);
        holder.setServiceDate(srvcDate);


        if (iscomplete.matches("notCompleted")) {
            holder.completeService.setVisibility(View.VISIBLE);
            holder.rate.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.srvcCompleted.setVisibility(View.GONE);
        } else if (iscomplete.matches("Completed")) {
            holder.completeService.setVisibility(View.GONE);
            holder.rate.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.srvcCompleted.setVisibility(View.GONE);
        }

        if (rating != null && rating.matches("0.0")==false) {
            holder.completeService.setText("Completed successfully.  Rated: " + rating);
            holder.completeService.setVisibility(View.GONE);
            holder.rate.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.GONE);
            holder.srvcCompleted.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return myBookingList.size();
    }

    public static class categoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView srvcImg;
        private TextView srvcCatg, srvcName, srvcPrice, bookedOn, srvcDay, srvcCompleted;
        private Switch completeService;
        private RatingBar ratingBar;
        private Button rate;

        public categoryViewHolder(@NonNull View itemView) {
            super(itemView);
            srvcImg = itemView.findViewById(R.id.imageView_serviceImg);
            srvcCatg = itemView.findViewById(R.id.textView_serviceCatg);
            srvcName = itemView.findViewById(R.id.textView_servicename);
            srvcPrice = itemView.findViewById(R.id.textView_servicePrice);
            bookedOn = itemView.findViewById(R.id.textView_serviceBookedTD);
            srvcDay = itemView.findViewById(R.id.textView1_serviceServedTD);
            completeService = itemView.findViewById(R.id.switchComplete);
            ratingBar = itemView.findViewById(R.id.ratingBar2);
            rate = itemView.findViewById(R.id.buttonRate);
            srvcCompleted = itemView.findViewById(R.id.serviceCompletedSuccessfully);

            completeService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        BookingsFragment.markComplete(getAdapterPosition());
                    }
                }
            });



            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float rtg = ratingBar.getRating();
                    final String rating = Float.toString(rtg);
                    BookingsFragment.submitRating(getAdapterPosition(), rating);
                }
            });
        }

        private void setImg(String img) {
            Picasso.get().load(img).transform(new CircleTransform()).into(srvcImg);
        }

        private void setCatg(String catg) {
            srvcCatg.setText(catg);
        }

        private void setName(String name) {
            srvcName.setText(name);
        }

        private void setPrice(String price) {
            srvcPrice.setText("Total Price: ₹" + price);
        }

        private void setBookedDate(String bkdate) {
            bookedOn.setText("Booked On: " + bkdate);
        }

        private void setServiceDate(String srvcDate) {
            srvcDay.setText("Will Serviced On: " + srvcDate);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

        }
    }
}
