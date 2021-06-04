package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mRegisterBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        mAuth=FirebaseAuth.getInstance();
        setContentView( R.layout.activity_register );
        mProgress=new ProgressDialog( this );
        mNameField=(EditText) findViewById(R.id.name);
        mEmailField=(EditText) findViewById(R.id.email);
        mPasswordField=(EditText) findViewById(R.id.password);
        mRegisterBtn=(Button) findViewById(R.id.button);
        mRegisterBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=mNameField.getText().toString().trim();
                String email=mEmailField.getText().toString().trim();
                String password=mPasswordField.getText().toString().trim();
                if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
                    mProgress.setMessage( "registering user" );
                    mProgress.setCanceledOnTouchOutside( true );
                    mProgress.show();
                startRegister(name,email,password);}
            }
        } );
    }

    private void startRegister(String name, String email, String password) {
   mAuth.createUserWithEmailAndPassword( email,password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
       @Override
       public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful())
           {
               FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
               String uid=current_user.getUid();
               String tokenId= FirebaseMessaging.getInstance().getToken().toString();
               mDatabase= FirebaseDatabase.getInstance().getReference().child( "users" ).child( uid );
               HashMap<String,String> userMap=new HashMap<>();
               userMap.put( "name",name );
               userMap.put("status","default");
               userMap.put("image","default");
               userMap.put( "thumb image","default" );
               mDatabase.setValue( userMap ).addOnCompleteListener( new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful())
                       { mProgress.dismiss();

                                   Intent mainIntent =new Intent(registerActivity.this,MainActivity.class);
                                   mainIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                   startActivity( mainIntent );

                               }

                       }
                   });

           }
           else
           {   mProgress.hide();
               String message=task.getException().toString();
               Toast.makeText(registerActivity.this,message,Toast.LENGTH_LONG).show();

           }
       }
   } );

    }
}