package com.example.travelist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.travelist.Arayuz.ItemClickListener;
import com.example.travelist.Model.Turler;
import com.example.travelist.ViewHolder.TurlerViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class TurlerActivity extends AppCompatActivity {
    Button btn_tür_ekle;
    MaterialEditText edtTurAdi;
    Button btnTurSec,btnTurYukle;

    public static final int PICK_IMAGE_REQUEST=71;
    Uri kaydetmeUrisi;

    FirebaseDatabase database;
    DatabaseReference turYolu;
    FirebaseStorage storage;
    StorageReference resimYolu;
    FirebaseRecyclerAdapter<Turler, TurlerViewHolder> adapter;

    RecyclerView recycler_turler;
    RecyclerView.LayoutManager layoutManager;

    String kategoriId="";

    Turler yeniTur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turler);
       // btn_tür_ekle=findViewById(R.id.btn_tur_ekle);


        database=FirebaseDatabase.getInstance();
        turYolu=database.getReference("Türler");
        storage=FirebaseStorage.getInstance();
        resimYolu=storage.getReference();

        recycler_turler=findViewById(R.id.recyler_turler);
        recycler_turler.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_turler.setLayoutManager(layoutManager);


       /* btn_tür_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turEklemePenceresiGoster();
            }
        });*/
        if(getIntent()!=null){
            kategoriId=getIntent().getStringExtra("KategoriId");

        }
        if(!kategoriId.isEmpty()){
            turleriYukle(kategoriId);
        }

    }

    private void turleriYukle(String kategoriId) {

        Query filtrele=turYolu.orderByChild("kategoriId").equalTo(kategoriId);

        FirebaseRecyclerOptions<Turler> secenekler=new FirebaseRecyclerOptions.Builder<Turler>()
                .setQuery(filtrele,Turler.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<Turler, TurlerViewHolder>(secenekler) {
            @Override
            protected void onBindViewHolder(@NonNull TurlerViewHolder turlerViewHolder, int i, @NonNull Turler turler) {

                turlerViewHolder.txtTurAdi.setText(turler.getAd());
                Picasso.with(getBaseContext()).load(turler.getResim()).into(turlerViewHolder.imageView);


                Turler tiklandiğinda=turler;

                turlerViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent turler=new Intent(TurlerActivity.this,Mekanlar.class);
                        turler.putExtra("TurId",adapter.getRef(position).getKey());
                        startActivity(turler);
                    }
                });
            }

            @NonNull
            @Override
            public TurlerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView=LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.tur_satiri_ogesi,parent,false);
                return new TurlerViewHolder(itemView);

            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recycler_turler.setAdapter(adapter);


    }



    private void turEklemePenceresiGoster() {
        AlertDialog.Builder builder=new AlertDialog.Builder(TurlerActivity.this);
        builder.setTitle("Yeni tür ekle");
        builder.setMessage("Lütfen bilgilerinizi yazın..");

        LayoutInflater layoutInflater=this.getLayoutInflater();
        View yeni_tur_ekleme_penceresi=layoutInflater.inflate(R.layout.yeni_tur_ekleme_projesi,null);

        edtTurAdi=yeni_tur_ekleme_penceresi.findViewById(R.id.edtTurAdi);
        btnTurSec=yeni_tur_ekleme_penceresi.findViewById(R.id.btnTurSec);
        btnTurYukle=yeni_tur_ekleme_penceresi.findViewById(R.id.btnTurYukle);

        btnTurSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimSec();
            }
        });

        btnTurYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimYukle();
            }
        });

        builder.setView(yeni_tur_ekleme_penceresi);
        builder.setIcon(R.drawable.home_icon);

        builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(yeniTur!=null){
                    turYolu.push().setValue(yeniTur);
                    Toast.makeText(TurlerActivity.this,yeniTur.getAd()+""+"Tür eklendi"+"",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(TurlerActivity.this,"Resim Yüklendi",Toast.LENGTH_SHORT).show();
                            resimDosyasi.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    yeniTur=new Turler(edtTurAdi.getText().toString(),uri.toString(),kategoriId);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    nDialog.dismiss();
                    Toast.makeText(TurlerActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals("Güncelle")){
            turGuncellemePenceresiGoster(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else  if (item.getTitle().equals("Sil")){
            turSil(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void turGuncellemePenceresiGoster(String key, Turler item) {
        AlertDialog.Builder builder=new AlertDialog.Builder(TurlerActivity.this);
        builder.setTitle("Yeni tür ekle");
        builder.setMessage("Lütfen bilgilerinizi yazın..");

        LayoutInflater layoutInflater=this.getLayoutInflater();
        View yeni_kategori_ekle_Penceresi=layoutInflater.inflate(R.layout.yeni_tur_ekleme_projesi,null);

        edtTurAdi=yeni_kategori_ekle_Penceresi.findViewById(R.id.edtTurAdi);
        btnTurSec=yeni_kategori_ekle_Penceresi.findViewById(R.id.btnTurSec);
        btnTurYukle=yeni_kategori_ekle_Penceresi.findViewById(R.id.btnTurYukle);

        btnTurSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimSec();
            }
        });

        btnTurYukle.setOnClickListener(new View.OnClickListener() {
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
                item.setAd(edtTurAdi.getText().toString());
                turYolu.child(key).setValue(item);
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

    private void resimDegis(Turler item) {
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
                            Toast.makeText(TurlerActivity.this,"Resim Güncellendi",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TurlerActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void turSil(String key) {
        turYolu.child(key).removeValue();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData()!=null){
            kaydetmeUrisi=data.getData();
            btnTurSec.setText("SEÇİLDİ");
        }
    }

    private void resimSec() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Resim Seç"),PICK_IMAGE_REQUEST);
    }

}
