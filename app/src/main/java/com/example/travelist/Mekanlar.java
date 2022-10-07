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
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.travelist.Arayuz.ItemClickListener;
import com.example.travelist.Model.Mekan;
import com.example.travelist.Model.Turler;
import com.example.travelist.ViewHolder.MekanViewHolder;
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

public class Mekanlar extends AppCompatActivity {

    Button btn_mekan_ekle;
    MaterialEditText edtMekanAdi;
    MaterialEditText edtMekanOzet;
    Button btnMekanSec,btnMekanYukle;

    FirebaseDatabase database;
    DatabaseReference mekanYolu;
    FirebaseStorage storage;
    StorageReference resimYolu;
    FirebaseRecyclerAdapter<Mekan, MekanViewHolder> adapter;

    RecyclerView recycler_mekanlar;
    RecyclerView.LayoutManager layoutManager;

    public static final int PICK_IMAGE_REQUEST=71;
    Uri kaydetmeUrisi;

    String turId="";

    Mekan yeniMekan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mekanlar);


        database=FirebaseDatabase.getInstance();
        mekanYolu=database.getReference("Mekanlar");
        storage=FirebaseStorage.getInstance();
        resimYolu=storage.getReference();

        recycler_mekanlar=findViewById(R.id.recyler_mekanlar);
        recycler_mekanlar.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_mekanlar.setLayoutManager(layoutManager);

      /*  btn_mekan_ekle=findViewById(R.id.btn_mekan_ekle);
        btn_mekan_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mekanEklemePenceresiGoster();
            }
        });*/
        if(getIntent()!=null){
            turId=getIntent().getStringExtra("TurId");

        }
        if(!turId.isEmpty()){
            MekanlarıYükle(turId);
        }
    }

    private void MekanlarıYükle(String turiId) {
        Query filtrele=mekanYolu.orderByChild("turid").equalTo(turId);

        FirebaseRecyclerOptions<Mekan> secenekler=new FirebaseRecyclerOptions.Builder<Mekan>()
                .setQuery(filtrele,Mekan.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<Mekan, MekanViewHolder>(secenekler) {
            @Override
            protected void onBindViewHolder(@NonNull MekanViewHolder mekanViewHolder, int i, @NonNull Mekan mekan) {

                mekanViewHolder.txtMekanAdi.setText(mekan.getMekanadi());
                mekanViewHolder.txtMekanOzet.setText(mekan.getMekanozet());
                Picasso.with(getBaseContext()).load(mekan.getResim()).into(mekanViewHolder.imageView);


                Mekan tiklandiğinda=mekan;

                mekanViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        /*Intent turler=new Intent(Mekanlar.this,Mekanlar.class);
                        turler.putExtra("TurId",adapter.getRef(position).getKey());
                        startActivity(turler);*/
                    }
                });
            }

            @NonNull
            @Override
            public MekanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView=LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.mekan_detay,parent,false);
                return new MekanViewHolder(itemView);

            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recycler_mekanlar.setAdapter(adapter);

    }

    private void mekanEklemePenceresiGoster() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Mekanlar.this);
        builder.setTitle("Yeni mekan ekle");
        builder.setMessage("Lütfen bilgilerinizi yazın..");

        LayoutInflater layoutInflater=this.getLayoutInflater();
        View yeni_mekan_ekleme_penceresi=layoutInflater.inflate(R.layout.yeni_mekan_ekleme_penceresi,null);

        edtMekanAdi=yeni_mekan_ekleme_penceresi.findViewById(R.id.edtMekanAdi);
        edtMekanOzet=yeni_mekan_ekleme_penceresi.findViewById(R.id.edtMekanOzet);
        btnMekanSec=yeni_mekan_ekleme_penceresi.findViewById(R.id.btnMekanSec);
        btnMekanYukle=yeni_mekan_ekleme_penceresi.findViewById(R.id.btnMekanYukle);

        btnMekanSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimSec();
            }
        });

        btnMekanYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimYukle();
            }
        });

        builder.setView(yeni_mekan_ekleme_penceresi);
        builder.setIcon(R.drawable.home_icon);

        builder.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(yeniMekan!=null){
                    mekanYolu.push().setValue(yeniMekan);
                    Toast.makeText(Mekanlar.this,yeniMekan.getMekanadi()+""+"Tür eklendi"+"",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Mekanlar.this,"Resim Yüklendi",Toast.LENGTH_SHORT).show();
                            resimDosyasi.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    yeniMekan=new Mekan();
                                    yeniMekan.setMekanadi(edtMekanAdi.getText().toString());
                                    yeniMekan.setMekanozet(edtMekanOzet.getText().toString());
                                    yeniMekan.setTurid(turId);
                                    yeniMekan.setResim(uri.toString());

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    nDialog.dismiss();
                    Toast.makeText(Mekanlar.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData()!=null){
            kaydetmeUrisi=data.getData();
            btnMekanSec.setText("SEÇİLDİ");
        }
    }

    private void resimSec() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Resim Seç"),PICK_IMAGE_REQUEST);
    }
}