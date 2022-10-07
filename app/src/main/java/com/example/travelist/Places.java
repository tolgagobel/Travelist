package com.example.travelist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.travelist.Arayuz.ItemClickListener;
import com.example.travelist.Model.Kategori;
import com.example.travelist.ViewHolder.KategoriViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class Places extends AppCompatActivity {



    BottomNavigationView bottomNavigationView;
    Fragment seciliCerceve=null;
    FirebaseAuth auth;
    Toolbar toolbar;


    MaterialEditText edtKategoriAdi;
    Button btnSec,btnYukle,btn_kategori_ekle;


    public static final int PICK_IMAGE_REQUEST=71;
    Uri kaydetmeUrisi;

    FirebaseDatabase database;
    DatabaseReference kategoriYolu;
    FirebaseStorage storage;
    StorageReference resimYolu;
    FirebaseRecyclerAdapter<Kategori, KategoriViewHolder>adapter;


    RecyclerView recycler_kategori;
    RecyclerView.LayoutManager layoutManager;

    Kategori yeniKategori;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

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
                    Intent intent=new Intent(Places.this,GirisActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });



        database=FirebaseDatabase.getInstance();
        kategoriYolu=database.getReference("Kategori");
        storage=FirebaseStorage.getInstance();
        resimYolu=storage.getReference();

        recycler_kategori=findViewById(R.id.recyler_kategori);
        recycler_kategori.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_kategori.setLayoutManager(layoutManager);

        /*btn_kategori_ekle=findViewById(R.id.btn_kategori_ekle);

        btn_kategori_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kategoriEklePenceresiGoster();
            }
        });*/

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.add_place) {
                    auth.signOut();
                    Intent intent=new Intent(Places.this,GirisActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


        kategoriYukle();
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
            Intent intent=new Intent(Places.this,GirisActivity.class);
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
                        startActivity(new Intent(Places.this,MainActivity.class));
                            break;
                        case R.id.nav_place:
                            seciliCerceve=null;
                            startActivity(new Intent(Places.this,Places.class));
                            break;
                        case R.id.nav_map:
                            seciliCerceve=null;
                            startActivity(new Intent(Places.this,MapsActivity.class));
                            break;
                        case R.id.nav_addpost:
                            startActivity(new Intent(Places.this,AddPostActivity.class));
                            break;
                        case R.id.nav_readpost:
                            startActivity(new Intent(Places.this,Home.class));
                            break;
                    }
                    if (seciliCerceve!=null){
                        //  getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapayici,seciliCerceve).commit();
                    }

                    return true;
                }
            };




    private void kategoriYukle() {
        FirebaseRecyclerOptions<Kategori>secenekler=new FirebaseRecyclerOptions.Builder<Kategori>()
                .setQuery(kategoriYolu,Kategori.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<Kategori, KategoriViewHolder>(secenekler) {
            @Override
            protected void onBindViewHolder(@NonNull KategoriViewHolder kategoriViewHolder, int i, @NonNull Kategori kategori) {

                kategoriViewHolder.txtKategoriAdi.setText(kategori.getAd());
                Picasso.with(getBaseContext()).load(kategori.getResim()).into(kategoriViewHolder.imageView);


                Kategori tiklandiğinda=kategori;

                kategoriViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent turler=new Intent(Places.this,TurlerActivity.class);
                        turler.putExtra("KategoriId",adapter.getRef(position).getKey());
                        startActivity(turler);
                    }
                });
            }

            @NonNull
            @Override
            public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView=LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.kategori_satiri_ogesi,parent,false);
                return new KategoriViewHolder(itemView);

            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recycler_kategori.setAdapter(adapter);
    }

    private void kategoriEklePenceresiGoster() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Places.this);
        builder.setTitle("Yeni kategori ekle");
        builder.setMessage("Lütfen bilgilerinizi yazın..");

        LayoutInflater layoutInflater=this.getLayoutInflater();
        View yeni_kategori_ekle_Penceresi=layoutInflater.inflate(R.layout.yeni_kategori_ekleme_penceresi,null);

        edtKategoriAdi=yeni_kategori_ekle_Penceresi.findViewById(R.id.edtKategoriAdi);
        btnSec=yeni_kategori_ekle_Penceresi.findViewById(R.id.btnSec);
        btnYukle=yeni_kategori_ekle_Penceresi.findViewById(R.id.btnYukle);

        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimSec();
            }
        });

        btnYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimYukle();
            }
        });

        builder.setView(yeni_kategori_ekle_Penceresi);
        builder.setIcon(R.drawable.home_icon);

        builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(yeniKategori!=null){
                    kategoriYolu.push().setValue(yeniKategori);
                    Toast.makeText(Places.this,yeniKategori.getAd()+"kategori eklendi"+"",Toast.LENGTH_SHORT).show();
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        builder.show();
    }

    private void resimYukle() {
        if (kaydetmeUrisi!=null ){
            ProgressDialog nDialog=new ProgressDialog(this);
            nDialog.setMessage("Yükleniyor...");
            nDialog.show();

            String resimadi= UUID.randomUUID().toString();
            StorageReference resimDosyasi=resimYolu.child("resimler/"+resimadi);
            resimDosyasi.putFile(kaydetmeUrisi)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            nDialog.dismiss();
                            Toast.makeText(Places.this,"Resim Yüklendi",Toast.LENGTH_SHORT).show();
                            resimDosyasi.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    yeniKategori=new Kategori(edtKategoriAdi.getText().toString(),uri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    nDialog.dismiss();
                    Toast.makeText(Places.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData()!=null){
            kaydetmeUrisi=data.getData();
            btnSec.setText("SEÇİLDİ");
        }
    }

    private void resimSec() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Resim Seç"),PICK_IMAGE_REQUEST);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals("Güncelle")){
            kategoriGuncellemePenceresiGoster(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else  if (item.getTitle().equals("Sil")){
            kategoriSil(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void kategoriGuncellemePenceresiGoster(String key, Kategori item) {
        AlertDialog.Builder builder=new AlertDialog.Builder(Places.this);
        builder.setTitle("Yeni kategori ekle");
        builder.setMessage("Lütfen bilgilerinizi yazın..");

        LayoutInflater layoutInflater=this.getLayoutInflater();
        View yeni_kategori_ekle_Penceresi=layoutInflater.inflate(R.layout.yeni_kategori_ekleme_penceresi,null);

        edtKategoriAdi=yeni_kategori_ekle_Penceresi.findViewById(R.id.edtKategoriAdi);
        btnSec=yeni_kategori_ekle_Penceresi.findViewById(R.id.btnSec);
        btnYukle=yeni_kategori_ekle_Penceresi.findViewById(R.id.btnYukle);

        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimSec();
            }
        });

        btnYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimDegis(item);
            }
        });

        builder.setView(yeni_kategori_ekle_Penceresi);
        builder.setIcon(R.drawable.home_icon);

        builder.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setAd(edtKategoriAdi.getText().toString());
                kategoriYolu.child(key).setValue(item);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        builder.show();
    }

    private void resimDegis(Kategori item) {
        if (kaydetmeUrisi!=null ){
            ProgressDialog nDialog=new ProgressDialog(this);
            nDialog.setMessage("Yükleniyor...");
            nDialog.show();

            String resimadi= UUID.randomUUID().toString();
            StorageReference resimDosyasi=resimYolu.child("resimler/"+resimadi);
            resimDosyasi.putFile(kaydetmeUrisi)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            nDialog.dismiss();
                            Toast.makeText(Places.this,"Resim Güncellendi",Toast.LENGTH_SHORT).show();
                            resimDosyasi.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    item.setResim(uri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    nDialog.dismiss();
                    Toast.makeText(Places.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void kategoriSil(String key) {
        kategoriYolu.child(key).removeValue();
    }
}