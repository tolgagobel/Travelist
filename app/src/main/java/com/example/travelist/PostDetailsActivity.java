package com.example.travelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.travelist.Adapter.CommentAdapter;
import com.example.travelist.Model.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {

    ImageView imgPost;
    TextView txtPostDesc,txtPostTitle,postMail;
    EditText editTextComment;
    Button button;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static String COMMENT_KEY="Comment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_details);

        Window w=getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //RvComment=findViewById(R.id.rv_comment);
        imgPost=findViewById(R.id.post_detail_image);
        txtPostDesc=findViewById(R.id.post_detail_desc);
        txtPostTitle=findViewById(R.id.post_detail_title);
        postMail=findViewById(R.id.detail_email);
       // button=findViewById(R.id.detail_addComment);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
       // editTextComment=findViewById(R.id.post_detail_comment);

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference commentreference=firebaseDatabase.getReference(COMMENT_KEY).push();
                String comment_content=editTextComment.getText().toString();
                String uid=firebaseUser.getUid();
                String uname=firebaseUser.getDisplayName();
               // String uimg=firebaseUser.getPhotoUrl().toString();

                Comment comment=new Comment(comment_content,uid,uname);
                commentreference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showMessage("comment added");
                        editTextComment.setText("");
                        button.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage(" fail to add comment :"+e.getMessage());
                    }
                });

            }
        });*/

       String postImage=getIntent().getExtras().getString("pImage");
        Glide.with(this).load(postImage).into(imgPost);

        String postTitle=getIntent().getExtras().getString("pTitle");
        txtPostTitle.setText(postTitle);

        String postDesc=getIntent().getExtras().getString("pDescription");
        txtPostDesc.setText(postDesc);

       String postemail=getIntent().getExtras().getString ( "uEmail") ;
        postMail.setText(postemail);

        //iniRvComment();



    }

    /*private void iniRvComment() {

        RvComment.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentRef=firebaseDatabase.getReference(COMMENT_KEY);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment=new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    Comment comment=snap.getValue(Comment.class);
                    listComment.add(comment);
                }
                commentAdapter=new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }*/
}