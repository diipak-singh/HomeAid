package com.tecnosols.homeaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

    DatabaseReference dref;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<ItemModel> itemList = new ArrayList<>();
    String addr;

    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        recyclerView=findViewById(R.id.recyclerView_Items);

        Intent intent = getIntent();
        addr = intent.getStringExtra("address");
        image = intent.getStringExtra("image");

        getSupportActionBar().setTitle(addr);
        getSupportActionBar().setElevation(3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getItems();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        /*Intent intent=new Intent(Subcategory.this,ItemActivity.class);
                        String addr=catg+"/"+catList.get(position).getCatName();
                        intent.putExtra("address",addr);
                        startActivity(intent);*/
                        Intent in = new Intent(ItemActivity.this,SubmitActivity.class);
                        in.putExtra("itemname",itemList.get(position).itemname);
                        in.putExtra("itemprice",itemList.get(position).itemprice);
                        in.putExtra("cat",addr);
                        in.putExtra("image",image);
                        startActivity(in);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

    }
    private void getItems(){

        dref = FirebaseDatabase.getInstance().getReference().child("items/" + addr);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    itemList.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ItemModel im = ds.getValue(ItemModel.class);
                        itemList.add(new ItemModel(im.itemname, im.itemprice, im.itemid));
                    }
                    adapter = new ItemAdapter((ArrayList<ItemModel>) itemList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "No Services.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //finishAffinity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
