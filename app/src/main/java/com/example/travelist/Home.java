package com.example.travelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.travelist.Adapter.PostAdapter;
import com.example.travelist.Model.PostModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    RecyclerView recyclerView;

    PostAdapter postAdapter;
    List<PostModel> postModelList;


    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    Fragment seciliCerceve=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Posts");
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        postModelList=new ArrayList<>();
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapayici,new HomeFragment()).commit();

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_place) {
                    auth.signOut();
                    Intent intent=new Intent(Home.this,GirisActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        //loadPosts();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.travel_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_place) {
            auth.signOut();
            Intent intent=new Intent(Home.this,GirisActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_home:
                        seciliCerceve=null;
                        startActivity(new Intent(Home.this,MainActivity.class));
                            break;
                        case R.id.nav_place:
                            seciliCerceve=null;
                            startActivity(new Intent(Home.this,Places.class));
                            break;
                        case R.id.nav_map:
                            seciliCerceve=null;
                            startActivity(new Intent(Home.this,MapsActivity.class));
                            break;
                        case R.id.nav_addpost:
                            startActivity(new Intent(Home.this,AddPostActivity.class));
                            break;
                        case R.id.nav_readpost:
                            startActivity(new Intent(Home.this,Home.class));
                            break;
                    }
                    if (seciliCerceve!=null){
                        //  getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapayici,seciliCerceve).commit();
                    }

                    return true;
                }
            };

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postModelList=new ArrayList<>();
                for (DataSnapshot postsnap:snapshot.getChildren()){
                    PostModel postmodel=postsnap.getValue(PostModel.class);
                    postModelList.add(postmodel);
                }
                postAdapter=new PostAdapter(getApplicationContext(),postModelList);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPosts() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postModelList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    PostModel postModel=ds.getValue(PostModel.class);
                    postModelList.add(postModel);
                    postAdapter=new PostAdapter(Home.this,postModelList);
                    recyclerView.setAdapter(postAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this,""+error,Toast.LENGTH_SHORT).show();
            }
        });
    }
}