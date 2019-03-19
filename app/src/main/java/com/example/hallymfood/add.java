package com.example.hallymfood;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add extends AppCompatActivity {
    private DatabaseReference mDatabase;

    EditText NameText,TimeText;
    Button addButton;
    String Name,Time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        final String index = intent.getExtras().getString("index");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        NameText = findViewById(R.id.nameEdittext);
        addButton = findViewById(R.id.addButton);

        ConstraintLayout LAY_add = findViewById(R.id.LAY_add);
        LAY_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(NameText.getWindowToken(), 0);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NameText.getText().toString().equals("")){
                    NameText.setError("Required");
                    return;
                }
                else {
                    Name=NameText.getText().toString();
                    submitPost(index, Name, "", "");
                    Intent intent = new Intent(getApplicationContext(), sub_List.class);
                    intent.putExtra("index", index);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void submitPost(String index,String Name,String Time,String Time2){
        Toast.makeText(getApplicationContext(),"등록 성공!",Toast.LENGTH_SHORT).show();
        Data_Info dataPost = new Data_Info(Name,Name, Time,Time2,"","");
        mDatabase.child(index).push().setValue(dataPost);
    }
}
