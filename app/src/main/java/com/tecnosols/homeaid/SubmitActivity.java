package com.tecnosols.homeaid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SubmitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    String itemname, itemprice, category, image, serviceDay, serviceTime;
    private TextView serviceDesc, servicePrice, selectedDate, selectedTime;
    private ImageView img;
    private Button book_service, d_back;
    private TextInputEditText otherDetails;
    private ProgressBar progressBar;

    FirebaseUser user;

    int day, month, year, hour, minute;
    int day_x, month_x, year_x, hour_x, minute_x;

    String userImg, userName, userPhone, userCity, userAddress;
    private Dialog serviceCompleteDialog;
    private RadioButton rdbCOD, rdbUPI;

    private final String upiID = "8953982411@paytm";
    private final String note = "Paying to HomeAid";
    private final String name = "HomeAid ";
    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        serviceDesc = findViewById(R.id.textView_serviceDesc);
        servicePrice = findViewById(R.id.textView_serviceTotal);
        img = findViewById(R.id.imageView_image);
        selectedDate = findViewById(R.id.textView_serviceDate);
        selectedTime = findViewById(R.id.textView_serviceTime);
        book_service = findViewById(R.id.button_bookService);
        otherDetails = findViewById(R.id.editText_otherdetails);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        rdbCOD = findViewById(R.id.radioButton_cod);
        rdbUPI = findViewById(R.id.radioButton_upi);
        rdbCOD.setChecked(true);

        user = FirebaseAuth.getInstance().getCurrentUser();

        getSupportActionBar().setTitle("Confirmation");
        getSupportActionBar().setElevation(3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        itemname = intent.getStringExtra("itemname");
        itemprice = intent.getStringExtra("itemprice");
        category = intent.getStringExtra("cat");
        image = intent.getStringExtra("image");

        serviceDesc.setText(category + "\n" + itemname);
        servicePrice.setText("Total Price: ₹" + itemprice);
        Picasso.get().load(image).into(img);

        getUserData();

        selectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SubmitActivity.this, SubmitActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });
        selectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SubmitActivity.this, SubmitActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

        serviceCompleteDialog = new Dialog(SubmitActivity.this);
        serviceCompleteDialog.setContentView(R.layout.s_complete_layout);
        //serviceCompleteDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corner));
        serviceCompleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        serviceCompleteDialog.setCancelable(false);
        d_back = serviceCompleteDialog.findViewById(R.id.button_dialogback);

        d_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceCompleteDialog.dismiss();
                onBackPressed();
            }
        });

        rdbCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdbCOD.setChecked(true);
                rdbUPI.setChecked(false);
            }
        });

        rdbUPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdbCOD.setChecked(false);
                rdbUPI.setChecked(true);
            }
        });

        book_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (rdbCOD.isChecked()) {
                    saveUserRequest();
                } else if (rdbUPI.isChecked()) {
                    payUsingUPI(itemprice, upiID, name, note);

                } else {
                    Toast.makeText(SubmitActivity.this, "No Payment Method Selected", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        year_x = year;
        month_x = month + 1;
        day_x = dayOfMonth;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(SubmitActivity.this, SubmitActivity.this, hour, minute, true);
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        hour_x = hourOfDay;
        minute_x = minute;

        serviceDay = day_x + "/" + month_x + "/" + year_x;
        serviceTime = hour_x + ":" + minute_x;

        selectedDate.setText("Selected Date:  " + serviceDay);
        selectedTime.setText("Selected Time:  " + serviceTime);

    }

    private void getUserData() {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("user_details");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //pd.cancel();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        user_details ud = ds.getValue(user_details.class);

                        if (user.getUid().contentEquals(ud.user_id)) {
                            userImg = ud.user_pic;
                            userName = ud.user_name;
                            userPhone = ud.user_mobile;
                            userCity = ud.user_city;
                            userAddress = ud.user_address;

                        }
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Please Update ur profile.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //pd.cancel();

            }
        });

    }

    public void saveUserRequest() {
        progressBar.setVisibility(View.VISIBLE);
        String paymentStatus = "Cash Payment";
        String isComplete = "NO";
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("service_requests");
        String id = dref.push().getKey();
        String other_details = otherDetails.getText().toString().trim();
        String rating = "0.0";

        /*Log.i("Catg",serviceDesc.getText().toString().split("\n")[0]);
        String cat_tmp =serviceDesc.getText().toString().split("\n")[0];
        String[] tmp=cat_tmp.split("/");
        String catg=tmp[1]+" in "+tmp[0];
        Log.i("asd",catg);*/

        if (serviceDay == null || serviceTime == null) {
            Toast.makeText(getApplicationContext(), "Please Select Date and time by selecting on those texts or icons.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (userName == null || userAddress == null || userCity == null) {
            Toast.makeText(getApplicationContext(), "Please Update your profile from profile section.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        saveMyBooking(0, id);

        ServiceDetail svd = new ServiceDetail(userImg, userName, userPhone, userCity, userAddress, image, itemname, category, itemprice, serviceDay, serviceTime, getCurrentDate(), getCurrentTime(), other_details, id, user.getUid(), paymentStatus, isComplete, rating);

        dref.child(id).setValue(svd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(getApplicationContext(), "Appointment booked sucessfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    serviceCompleteDialog.show();

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy ");
        String strDate = mdformat.format(calendar.getTime());
        //display(strDate);
        return strDate;

    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strTime = mdformat.format(calendar.getTime());
        return strTime;
    }

    private void saveMyBooking(Integer flag, String serviceID) {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("myBookings");
        String bookingID = dref.push().getKey();
        String status = "notCompleted";
        String rating = "0.0";
        String paymentStatus;
        paymentStatus = null;
        String assignedWid = null;

        if (flag.equals(0)) {
            paymentStatus = "Cash Payment";
        } else if (flag.equals(1)) {
            paymentStatus = "Paid By UPI";
        }

        MyBookingsModel mb = new MyBookingsModel(image, category, itemname, itemprice, getCurrentDate() + " at " + getCurrentTime(), serviceDay + " at " + serviceTime, status, rating, paymentStatus, bookingID, serviceID, user.getUid(), assignedWid);

        dref.child(serviceID).setValue(mb).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void payUsingUPI(String amount, String upiId, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(SubmitActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(SubmitActivity.this)) {
            String str = data.get(0);
            Log.d("UPI_PAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(SubmitActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                makeOrderUPI();
                //Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(SubmitActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SubmitActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SubmitActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    private void makeOrderUPI() {
        progressBar.setVisibility(View.VISIBLE);
        String paymentStatus = "Paid By UPI";
        String isComplete = "NO";
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("service_requests");
        String id = dref.push().getKey();
        String other_details = otherDetails.getText().toString().trim();
        String rating = "0.0";

        if (serviceDay == null || serviceTime == null) {
            Toast.makeText(getApplicationContext(), "Please Select Date and time by selecting on those texts or icons.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (userName == null || userAddress == null || userCity == null) {
            Toast.makeText(getApplicationContext(), "Please Update your profile from profile section.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        saveMyBooking(1, id);

        ServiceDetail svd = new ServiceDetail(userImg, userName, userPhone, userCity, userAddress, image, itemname, category, itemprice, serviceDay, serviceTime, getCurrentDate(), getCurrentTime(), other_details, id, user.getUid(), paymentStatus, isComplete, rating);

        dref.child(id).setValue(svd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(getApplicationContext(), "Appointment booked sucessfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    serviceCompleteDialog.show();

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
