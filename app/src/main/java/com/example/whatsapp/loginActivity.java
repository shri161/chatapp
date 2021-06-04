package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class loginActivity extends AppCompatActivity {
    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;
    private Button mLoginBtn;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        mLoginEmailField = (EditText) findViewById( R.id.email );
        mLoginPasswordField = (EditText) findViewById( R.id.password );
        mLoginBtn = (Button) findViewById( R.id.button );
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog( this );
        mDatabase = FirebaseDatabase.getInstance().getReference().child( "users" );
        mLoginBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmailField.getText().toString().trim();
                String password = mLoginPasswordField.getText().toString().trim();
                if (!TextUtils.isEmpty( email ) || !TextUtils.isEmpty( password )) {
                    mProgress.setMessage( "logging in" );
                    mProgress.setCanceledOnTouchOutside( true );
                    mProgress.show();
                    loginUser( email, password );
                }
            }
        } );
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword( email, password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mProgress.dismiss();
                    Intent mainIntent = new Intent( loginActivity.this, MainActivity.class );
                    mainIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity( mainIntent );
                    finish();
                } else {
                    mProgress.hide();
                    Toast.makeText( loginActivity.this, "cannot sign in user", Toast.LENGTH_LONG );

                }
            }
        } );
    }
}