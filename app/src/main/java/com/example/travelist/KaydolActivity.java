package com.example.travelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class KaydolActivity extends AppCompatActivity {

    EditText edt_kullaniciAdi,edt_Ad,edt_Email,edt_Sifre;

    Button btn_Kaydol;

    TextView txt_GirisSayfasinagit;

    FirebaseAuth yetki;
    DatabaseReference yol;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydol);

        edt_kullaniciAdi=findViewById(R.id.edt_kullaniciAdi);
        edt_Ad=findViewById(R.id.edt_Ad);
        edt_Email=findViewById(R.id.edt_Email);
        edt_Sifre=findViewById(R.id.edt_sifre);

        btn_Kaydol=findViewById(R.id.btn_Kaydol_activity);

        txt_GirisSayfasinagit=findViewById(R.id.txt_girisSayfasina_git);

        yetki=FirebaseAuth.getInstance();

        txt_GirisSayfasinagit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(KaydolActivity.this,GirisActivity.class));
            }
        });

        btn_Kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(KaydolActivity.this);
                pd.setMessage("Please Wait..");
                pd.show();

                String str_kullaniciAdi=edt_kullaniciAdi.getText().toString();
                String str_Ad=edt_Ad.getText().toString();
                String str_Email=edt_Email.getText().toString();
                String str_Sifre=edt_Sifre.getText().toString();

                if (TextUtils.isEmpty(str_kullaniciAdi)||TextUtils.isEmpty(str_Ad)||TextUtils.isEmpty(str_Email)||TextUtils.isEmpty(str_Sifre)){
                    Toast.makeText(KaydolActivity.this,"Please fill in the blanks..",Toast.LENGTH_SHORT).show();
                }else if(str_Sifre.length()<6){
                    Toast.makeText(KaydolActivity.this,"Your Password should be more than 6 characters",Toast.LENGTH_SHORT).show();
                }else {
                    kaydet(str_kullaniciAdi,str_Ad,str_Email,str_Sifre);
                }
            }
        });

    }
    private void kaydet(String kullaniciadi,String ad,String email,String sifre){
        yetki.createUserWithEmailAndPassword(email,sifre)
                .addOnCompleteListener(KaydolActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseKullanici=yetki.getCurrentUser();

                            String kullaniciId=firebaseKullanici.getUid();

                            yol= FirebaseDatabase.getInstance().getReference().child("Users").child(kullaniciId);

                            HashMap<String ,Object> hashMap=new HashMap<>();
                            hashMap.put("id",kullaniciId);
                            hashMap.put("kullaniciadi",kullaniciadi.toLowerCase());
                            hashMap.put("ad",ad);
                            hashMap.put("bio",kullaniciId);
                            hashMap.put("resimler","https://firebasestorage.googleapis.com/v0/b/travelist-5b7a6.appspot.com/o/placeholder.jpg?alt=media&token=84069444-4c50-4bbd-b4ec-aec7d6606774");


                            yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        pd.dismiss();

                                        Intent intent=new Intent(KaydolActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }

                                }
                            });
                        }
                        else {
                            pd.dismiss();
                            Toast.makeText(KaydolActivity.this,"Fail..",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}