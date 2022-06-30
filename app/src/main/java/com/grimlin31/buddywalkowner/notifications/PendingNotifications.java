package com.grimlin31.buddywalkowner.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grimlin31.buddywalkowner.R;
import com.grimlin31.buddywalkowner.model.Item;

import java.util.ArrayList;
import java.util.List;

public class PendingNotifications extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String walkerIndex;
    private ListView lv;
    private ArrayList<String> coursesArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_notifications);
        Intent intent = getIntent();
        if(intent != null)
            walkerIndex = intent.getStringExtra("walkerIndex");

        List<Item> dataForTheAdapter = new ArrayList<Item>();
        lv = findViewById(R.id.lv);

        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_dropdown_item_1line, dataForTheAdapter);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                child("walker").child(walkerIndex).child("notifications");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    String userIndex = String.valueOf(child.child("userIndex").getValue());
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(String.valueOf(child.child("pending").getValue()).equals("2")) {
                                dataForTheAdapter.add(new Item(String.valueOf(dataSnapshot.child(userIndex).child("username").getValue()),
                                        userIndex, child.getKey()));
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent walkPage = new Intent(PendingNotifications.this, PendingPage.class);
                walkPage.putExtra("userIndex", adapter.getItem(position).getUserIndex());
                walkPage.putExtra("walkerIndex", walkerIndex);
                walkPage.putExtra("notification", adapter.getItem(position).getNotification());
                startActivity(walkPage);
            }
        });
    }
}
