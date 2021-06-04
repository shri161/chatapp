package com.example.whatsapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;

public class settingsActivity extends AppCompatActivity {
 private DatabaseReference mDatabase;
 private FirebaseUser mCurrentUser;
 private CircleImageView mDisplayImage;
 private TextView mName;
 private TextView mStatus;
 private FirebaseAuth mAuth;
 private Button mChangeStatus;
 private Button mImageChange;
 private StorageReference mStorage;
 private ProgressDialog mProgress;
 private DatabaseReference mOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );
        mDisplayImage=(CircleImageView) findViewById( R.id.setting_image );
        mName=(TextView) findViewById( R.id.name );
        mStorage= FirebaseStorage.getInstance().getReference();
        mStatus=(TextView) findViewById( R.id.status ) ;
        mAuth=FirebaseAuth.getInstance();
        mChangeStatus=(Button)  findViewById( R.id.change_status );
        mImageChange=(Button) findViewById( R.id.change_image );
        mOnline=FirebaseDatabase.getInstance().getReference().child( "users" ).child( mAuth.getCurrentUser().getUid() );
        mDatabase= FirebaseDatabase.getInstance().getReference().child( "users" ).child( mAuth.getCurrentUser().getUid());
        mDatabase.keepSynced( true );
        mDatabase.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child( "name" ).getValue().toString();
                String image =snapshot.child( "image" ).getValue().toString();
                String status=snapshot.child( "status" ).getValue().toString();
                String thumb_image=snapshot.child( "thumb image" ).getValue().toString();
                mName.setText( name );
                mStatus.setText( status );
                if(!image.equals( "default" ))
                {
                    Picasso.with(settingsActivity.this).load( image ).into(mDisplayImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
        mImageChange.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         openSomeActivityForResult();

            }
        } );




        mChangeStatus.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status=mStatus.getText().toString();

                Intent mainIntent =new Intent(settingsActivity.this,statusActivity.class);
                mainIntent.putExtra( "status",status );
                startActivity( mainIntent );

            }
        } );

    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mProgress=new ProgressDialog(settingsActivity.this );
                        mProgress.setMessage( "uploading image" );
                        mProgress.show();
                        Intent intent=result.getData();
                        Uri imageUri=intent.getData();
                        StorageReference filepath=mStorage.child( "profile_images" ).child( mAuth.getCurrentUser().getUid() +".jpg");
                        filepath.putFile( imageUri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUri=taskSnapshot.getUploadSessionUri();
                                mDatabase.child( "image" ).setValue( downloadUri.toString() ).addOnCompleteListener( new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            mProgress.dismiss();
                                            Toast.makeText( settingsActivity.this,"success uploading ",Toast.LENGTH_LONG );
                                        }
                                    }
                                } );

                            }
                        } );

                            }

                    }

            });

    public void openSomeActivityForResult() {
        Intent intent = new Intent();
        intent.setType( "image/*" );
        intent.setAction( Intent.ACTION_GET_CONTENT );
        someActivityResultLauncher.launch(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mOnline.child( "online" ).setValue( true );
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOnline.child( "online" ).setValue( false );
    }
}