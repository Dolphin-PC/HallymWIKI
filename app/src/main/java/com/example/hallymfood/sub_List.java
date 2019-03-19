package com.example.hallymfood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sub_List extends AppCompatActivity {
    String index;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub__list);

        ListView list = findViewById(R.id.List);
        Button addButton = findViewById(R.id.addButton);
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_single_choice);
        list.setAdapter(adapter);
        Intent intent = getIntent();
        index = intent.getExtras().getString("index");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String ListIndex = (String)adapterView.getAdapter().getItem(position);
                Intent intent1 = new Intent(getApplicationContext(),Information.class);
                intent1.putExtra("Name",ListIndex);
                intent1.putExtra("index",index);
                startActivity(intent1);
            }
        });

        databaseReference.child(index).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Data_Info data_info = dataSnapshot.getValue(Data_Info.class);
                adapter.add(data_info.getName());
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),add.class);
                intent1.putExtra("index",index);
                startActivity(intent1);
                finish();
            }
        });
    }
}
