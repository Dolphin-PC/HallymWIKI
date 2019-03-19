package com.example.hallymfood;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class menu extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference mDatabase;
    private DatabaseReference mData;
    private ArrayAdapter mAdapter;
    FirebaseStorage storage  = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    StorageReference uploadRef_menu;

    String index,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageView imageView = findViewById(R.id.imageView);
        final TextView edit = findViewById(R.id.editButton);
        final TextView editimage = findViewById(R.id.editButton2);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        final EditText priceText = findViewById(R.id.priceText);
        final EditText commentText = findViewById(R.id.commentText);
        Button commentButton = findViewById(R.id.commentButton);
        Button ratingButton = findViewById(R.id.ratingButton);
        ListView listView = findViewById(R.id.listview);

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),hint_4.class);
                startActivity(intent);
            }
        });

        ConstraintLayout LAY_menu = findViewById(R.id.LAY_menu);
        LAY_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
            }
        });

        Intent intent = getIntent();
        index = intent.getExtras().getString("index");
        name = intent.getExtras().getString("name");
        StorageReference menuRef = storage.getReference().child("menu/" + name + ".jpg");
        uploadRef_menu = storageRef.child("menu/" + name + ".jpg");
        mDatabase = FirebaseDatabase.getInstance().getReference();


        priceText.setEnabled(false);
        priceText.setTextColor(0xAA000000);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(mAdapter);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit.getText().equals("정보수정")){
                    priceText.setEnabled(true);
                    editimage.setVisibility(View.VISIBLE);
                    edit.setText("저장");
                }else{
                    priceText.setEnabled(false);
                    changePost(priceText.getText().toString());
                    editimage.setVisibility(View.INVISIBLE);
                    edit.setText("정보수정");
                }
            }
        });

        Glide.with(this)
                .load(menuRef)
                .into(imageView);

        editimage.setOnClickListener(new View.OnClickListener() {
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
                    priceText.setText(data_info.getPrice());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_Info comment_info = new comment_Info(name,commentText.getText().toString());
                mDatabase.child("comments").push().setValue(comment_info);
                commentText.setText("");
            }
        });

        mDatabase.child("comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                comment_Info comment_info = dataSnapshot.getValue(comment_Info.class);
                if(comment_info.getName() == name){
                    mAdapter.add(comment_info.getComment());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void changePost(String price){
        final String p = price;
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
                result.put("price",p);
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
