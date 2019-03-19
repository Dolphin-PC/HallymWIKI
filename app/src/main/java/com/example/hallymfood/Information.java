package com.example.hallymfood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class Information extends AppCompatActivity {
    String name, Time, Time2,phone;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference mData;


    FirebaseStorage storage  = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference uploadRef_title,uploadRef_menu;

    TextView editButton,edittext2,menuTextview;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Intent intent = getIntent();
        name = intent.getExtras().getString("Name");
        final String index = intent.getExtras().getString("index");
        StorageReference titleRef = storage.getReference().child("title/" + name + ".jpg"); // 이름과 같은 이미지 불러오기
        StorageReference menuRef = storage.getReference().child("menu/" + name + ".jpg");
        uploadRef_title = storageRef.child("title/" + name + ".jpg");                             //이미지를 이름과 같게 업로드 경로 정하기
        uploadRef_menu = storageRef.child("menu/" + name + ".jpg");
        imageView = findViewById(R.id.imageView);
        menuTextview = findViewById(R.id.menuTextview);

        final EditText nameTextview = findViewById(R.id.nameTextview);
        final EditText timeTextview = findViewById(R.id.timeTextview);
        Time = timeTextview.getText().toString();
        final EditText timeTextview2 = findViewById(R.id.timeTextview2);
        Time2 = timeTextview2.getText().toString();
        final TextView phoneTextview = findViewById(R.id.phoneTextview);
        phone = phoneTextview.getText().toString();
        TextView menuTextview3 = findViewById(R.id.menuTextview3);

        menuTextview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),hint_3.class);
                startActivity(intent1);
            }
        });

        ConstraintLayout LAY_info = findViewById(R.id.LAY_info);
        LAY_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(phoneTextview.getWindowToken(), 0);
            }
        });

        nameTextview.setEnabled(false);
        nameTextview.setTextColor(0xAA0225ef);
        timeTextview.setEnabled(false);
        timeTextview.setTextColor(0xAA000000);
        timeTextview2.setEnabled(false);
        timeTextview2.setTextColor(0xAA000000);
        phoneTextview.setEnabled(false);
        phoneTextview.setTextColor(0xAA000000);

        editButton = findViewById(R.id.editButton);
        edittext2 = findViewById(R.id.editText2);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editButton.getText().equals("정보수정")) {
                    timeTextview.setEnabled(true);
                    timeTextview2.setEnabled(true);
                    phoneTextview.setEnabled(true);
                    edittext2.setVisibility(View.VISIBLE);
                    editButton.setText("저장");
                } else {
                        name = nameTextview.getText().toString();
                        Time = timeTextview.getText().toString();
                        Time2 = timeTextview2.getText().toString();
                        changePost(index, name, Time, Time2,phone);
                        nameTextview.setEnabled(false);
                        timeTextview.setEnabled(false);
                        timeTextview2.setEnabled(false);
                        phoneTextview.setEnabled(false);
                        edittext2.setVisibility(View.INVISIBLE);
                        editButton.setText("정보수정");
                }
            }
        });

        Glide.with(this)
                .load(titleRef)
                .into(imageView);

        edittext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        mData = databaseReference.child(index);
        Query query = mData.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    Data_Info data_info = appleSnapshot.getValue(Data_Info.class);
                    nameTextview.setText(data_info.getName());
                    timeTextview.setText(data_info.getTime());
                    timeTextview2.setText(data_info.getTime2());
                    phoneTextview.setText(data_info.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        menuTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),menu.class);
                intent1.putExtra("index",index);
                intent1.putExtra("name",name);
                startActivity(intent1);
            }
        });
    }

    private void changePost(String index, String Name, String Time, String Time2,String phone) {
        final String t = Time;
        final String t2 = Time2;
        final String p = phone;
        final DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(index).orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                Data_Info data_info = nodeDataSnapshot.getValue(Data_Info.class);
                String key = nodeDataSnapshot.getKey();
                String path = "/" + dataSnapshot.getKey() + "/" + key;
                HashMap<String, Object> result = new HashMap<>();
                result.put("time", t);
                result.put("time2", t2);
                result.put("phone",p);
                reference.child(path).updateChildren(result);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //메시지 띄워주기
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    //imageView2.setImageBitmap(img);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] d = baos.toByteArray();

                    UploadTask uploadTask = uploadRef_title.putBytes(d);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "사진 등록 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "사진 등록 성공!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == 2){
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    //imageView2.setImageBitmap(img);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] d = baos.toByteArray();

                    UploadTask uploadTask = uploadRef_menu.putBytes(d);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "사진 등록 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "사진 등록 성공!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

