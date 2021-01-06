package com.example.insertandnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView recyclerView;
    private InstaDataAdapter userAdapter;
    ArrayList<Categories> arrayList;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        firebaseAuth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        arrayList=new ArrayList<Categories>();


        databaseReference= FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Categories data=dataSnapshot.getValue(Categories.class);
                    arrayList.add(data);
                    userAdapter= new InstaDataAdapter(MainActivity2.this,arrayList);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity2.this, "Opps something went to wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}