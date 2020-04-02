package com.tecnosols.homeaid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText name, mobile, city, address_line1, address_line2;
    private Button saveProfile;
    private ImageView userPic, goBack;

    private static final int CHOOSE_IMAGE = 1;
    private Uri imageUrl;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    String userPhone, ImageUrl = null;
    String name_user;
    FirebaseUser user;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        loadDetail();
        progressBar.setVisibility(View.GONE);
        Picasso.get().load(R.drawable.ic_user).transform(new CircleTransform()).into(userPic);


        user = FirebaseAuth.getInstance().getCurrentUser();
        name_user = user.getPhoneNumber();
        mobile.setText(user.getPhoneNumber());

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("user_details");
        mStorageRef = FirebaseStorage.getInstance().getReference("profile_pics");


        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChoose();
            }
        });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closekeyboard();
                progressBar.setVisibility(View.VISIBLE);

                saveImage();

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initViews() {
        name = (TextInputEditText) findViewById(R.id.signup_name);
        mobile = (TextInputEditText) findViewById(R.id.signup_phone);
        city = (TextInputEditText) findViewById(R.id.signup_city);
        address_line1 = (TextInputEditText) findViewById(R.id.signup_addressLine1);
        address_line2 = (TextInputEditText) findViewById(R.id.signup_permanent_address);
        saveProfile = (Button) findViewById(R.id.button_update);
        userPic = (ImageView) findViewById(R.id.signup_pic);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        goBack = findViewById(R.id.imageView6_goBack);
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUrl = data.getData();

            Picasso.get().load(imageUrl).transform(new CircleTransform()).into(userPic);

        }
    }

    private void saveImage() {

        if (imageUrl != null) {

            final StorageReference fileRefrence = mStorageRef.child(name_user + "." + getFileExtension(imageUrl));

            mUploadTask = fileRefrence.putFile(imageUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // uploadProgress.setProgress(0);
                                }
                            }, 500);

                            fileRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ImageUrl = uri.toString();
                                    uploadData();
                                }
                            });


                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            // uploadProgress.setProgress((int) progress);
                        }
                    });

        } else {
            imageUrl = user.getPhotoUrl();

            // Toast.makeText(getApplicationContext(), "Please choose an image to continue.", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadData() {


        String username = name.getText().toString().trim();
        String userPhone = mobile.getText().toString().trim();
        String usercity = city.getText().toString().trim();
        String user_addL1 = address_line1.getText().toString().trim();
        String user_addl2 = address_line2.getText().toString().trim();

        if (username.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            name.setError("Name can't be Empty");
            name.requestFocus();

            return;
        }
        if (user_addL1.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            address_line1.setError("Address can't be Empty");
            address_line1.requestFocus();

            return;
        }
        if (userPhone.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            mobile.setError("Address can't be Empty");
            mobile.requestFocus();

            return;
        }

        if (user_addl2.isEmpty()) user_addl2 = " ";

        String user_address = user_addL1 + ',' + user_addl2;
        String user_id = user.getUid();
        // Log.i("userId",user_id);

        String id = userPhone;

        user_details udt = new user_details(ImageUrl, username, userPhone, usercity, user_address, user_id);
        mDatabaseRef.child(id).setValue(udt).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    //saveUserDetails();
                    //uploadProgress.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "All Data Saved Successfully", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);*/
                    onBackPressed();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


                }
            }
        });


    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void closekeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

    private void loadDetail()
    {
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

                            Picasso.get().load(ud.user_pic).transform(new CircleTransform()).into(userPic);
                            name.setText(ud.user_name);
                            city.setText(ud.user_city);
                            mobile.setText(ud.user_mobile);

                            String[] splited = ud.user_address.split(",");
                            address_line1.setText(splited[0]);
                            address_line2.setText(splited[1]);
                        }
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Please Update ur profile by tapping on Setting Button", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //pd.cancel();

            }
        });
    }
}
