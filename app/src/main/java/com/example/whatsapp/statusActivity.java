package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class statusActivity extends AppCompatActivity {
 private EditText mStatus;
 private Button mStatusBtn;
 private DatabaseReference mDatabase;
 private FirebaseAuth mAuth;
 private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_status );
        String status_value=getIntent().getStringExtra( "status" );
        mStatus=(EditText) findViewById( R.id.status );
        mStatusBtn=(Button) findViewById( R.id.button );
        mStatus.setText( status_value );
        mAuth= FirebaseAuth.getInstance();
        String uid=mAuth.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child( "users" ).child( uid );
        mStatusBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress=new ProgressDialog(statusActivity.this );
                mProgress.setMessage( "saving changes" );
                mProgress.show();
                String status=mStatus.getText().toString();
                 mDatabase.child( "status" ).setValue( status ).addOnCompleteListener( new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful())
                         {
                            mProgress.dismiss();
                         }
                         else
                         {
                             Toast.makeText( getApplicationContext(),"error",Toast.LENGTH_LONG).show();

                         }
                     }
                 } );
            }
        } );
    }
}