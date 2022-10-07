package com.example.travelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.travelist.Adapter.SliderAdapter;
import com.example.travelist.Cerceve.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment seciliCerceve=null;
    SliderView sliderView;
    int[]images={R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five,
            R.drawable.six};

        FirebaseAuth auth;
        Toolbar toolbar;
        //BottomNavigationView bottomNavigationView;
        // Fragment seciliCerceve=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();



        sliderView=findViewById(R.id.image_slider);
        SliderAdapter sliderAdapter=new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

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
                    Intent intent=new Intent(MainActivity.this,GirisActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


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
            Intent intent=new Intent(MainActivity.this,GirisActivity.class);
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
                        /*case R.id.nav_home:
                        seciliCerceve=null;
                        startActivity(new Intent(MainActivity.this,ListMap.class));
                            break;*/
                        case R.id.nav_place:
                            seciliCerceve=null;
                            startActivity(new Intent(MainActivity.this,Places.class));
                            break;
                        case R.id.nav_map:
                            seciliCerceve=null;
                            startActivity(new Intent(MainActivity.this,MapsActivity.class));
                            break;
                        case R.id.nav_addpost:
                            startActivity(new Intent(MainActivity.this,AddPostActivity.class));
                            break;
                        case R.id.nav_readpost:
                            startActivity(new Intent(MainActivity.this,Home.class));
                            break;
                    }
                    if (seciliCerceve!=null){
                      //  getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapayici,seciliCerceve).commit();
                    }

                    return true;
                }
            };



}
















