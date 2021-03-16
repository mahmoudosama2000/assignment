package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editTextUserName;
    EditText editTextUserNumber;
    EditText editTextUserAddress;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView listView;
    List<String> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextUserName = findViewById(R.id.userName);
        editTextUserNumber = findViewById(R.id.userNumber);
        editTextUserAddress = findViewById(R.id.useraddress);
        listView=(ListView)findViewById(R.id.listView);


    }
    public void saveToFirebase(View v){

        String UserName = editTextUserName.getText().toString();
        String UserNumber = editTextUserNumber.getText().toString();
        String UserAddress = editTextUserAddress.getText().toString();

        Map<String,Object> product = new HashMap<>();
        product.put("User Name" , UserName);
        product.put("User Number" , UserNumber);
        product.put("User Address" , UserAddress);



        db.collection("Personal Information")
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Data added successfuly to db",Toast.LENGTH_LONG).show();
                        editTextUserAddress.setText("");
                        editTextUserName.setText("");
                        editTextUserNumber.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed add data to db",Toast.LENGTH_LONG).show();

                        editTextUserAddress.setText("");
                        editTextUserName.setText("");
                        editTextUserNumber.setText("");
                    }
                });


        FirebaseDatabase.getInstance().getReference("Personal Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    int i = 0;

                    for(DataSnapshot  snapshott: snapshot.getChildren()) {
                      String  uids = snapshot.getValue() + ",";
                        list.add(uids);
                        Log.d("hhh", String.valueOf(list));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, android.R.id.text1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String value=adapter.getItem(position);
                Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();

            }
        });
    }



}