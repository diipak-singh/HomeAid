package com.tecnosols.homeaid;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter adapter;
    public static ArrayList<MyBookingsModel> myBookingList = new ArrayList<>();
    DatabaseReference dref;
    FirebaseUser user;
    static Context ctx;

    public BookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        recyclerView = view.findViewById(R.id.myBookingsRecyclerView);
        ctx=getContext();

        dref = FirebaseDatabase.getInstance().getReference().child("myBookings");
        user = FirebaseAuth.getInstance().getCurrentUser();
        layoutManager = new LinearLayoutManager(container.getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myBookingList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        MyBookingsModel mb = ds.getValue(MyBookingsModel.class);

                        if (user.getUid().matches(mb.userID)) {
                            myBookingList.add(new MyBookingsModel(mb.itemImg, mb.itemCatg, mb.itemName, mb.itemPrice, mb.bookingDT, mb.serviceDT, mb.isCompleted, mb.rating, mb.paymentStatus, mb.bookingID,mb.serviceID, mb.userID,mb.assignedWid));
                        }
                    }
                    adapter = new MyBookingAdapter((ArrayList<MyBookingsModel>) myBookingList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(container.getContext(), "Not booked a service yet.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    public static void markComplete(Integer position) {
        String service_id = myBookingList.get(position).getServiceID();
        String wid=myBookingList.get(position).getAssignedWid();
        if(wid==null){
            Toast.makeText(ctx, "No worker assigned yet.", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference drf = FirebaseDatabase.getInstance().getReference().child("myBookings");
        DatabaseReference sdrf=FirebaseDatabase.getInstance().getReference().child("service_requests");
        DatabaseReference ad2w=FirebaseDatabase.getInstance().getReference().child("admin2workers/"+wid);

        sdrf.child(service_id).child("isCompleted").setValue("Completed");
        ad2w.child(service_id).child("isCompleted").setValue("Completed");

        drf.child(service_id).child("isCompleted").setValue("Completed").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static void submitRating(Integer position, String rating) {
        String service_id = myBookingList.get(position).getServiceID();
        String wid=myBookingList.get(position).getAssignedWid();
        if(wid==null){
            Toast.makeText(ctx, "No worker assigned yet.", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference drf = FirebaseDatabase.getInstance().getReference().child("myBookings");
        DatabaseReference sdrf=FirebaseDatabase.getInstance().getReference().child("service_requests");
        DatabaseReference ad2w=FirebaseDatabase.getInstance().getReference().child("admin2workers/"+wid);

        sdrf.child(service_id).child("rating").setValue(rating);
        ad2w.child(service_id).child("rating").setValue(rating);

        drf.child(service_id).child("rating").setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

}
