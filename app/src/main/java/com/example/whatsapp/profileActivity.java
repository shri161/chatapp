package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class profileActivity extends AppCompatActivity {
  private ImageView mImage;
    private TextView  mStatus,mName,mFriendCount;
    private Button mProfile,mDecline;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private String mCurrent_state;
    private DatabaseReference mDatabaseFriend;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseNotification;
    private DatabaseReference mDatabaseFriends;
    private DatabaseReference mOnline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );
        String id=getIntent().getStringExtra( "user_id" );
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child( id );
        mDatabaseFriend=FirebaseDatabase.getInstance().getReference().child( "friend request" );
        mImage=(ImageView) findViewById( R.id.img);
        mName=(TextView) findViewById( R.id.nm );
        mStatus=(TextView) findViewById( R.id.sts );
        mProfile=(Button) findViewById( R.id.profile );
        mDecline=(Button) findViewById( R.id.decline );
        mFriendCount=(TextView) findViewById( R.id.friend );
        mCurrent_state="not friend";
        mAuth=FirebaseAuth.getInstance();
        mOnline=FirebaseDatabase.getInstance().getReference().child( "users" ).child( mAuth.getCurrentUser().getUid() );
         mDatabaseFriends=FirebaseDatabase.getInstance().getReference().child("friends");
         mDatabaseNotification=FirebaseDatabase.getInstance().getReference().child( "notification" );
        mProgress=new ProgressDialog( this );
        mProgress.setMessage( "loading user data" );
        mProgress.setCanceledOnTouchOutside( false );
        mProgress.show();
        mDecline.setVisibility( View.INVISIBLE );
        mDecline.setEnabled( false );
        mDatabase.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child("name").getValue().toString();
                String status=snapshot.child( "status" ).getValue().toString();
                String image=snapshot.child( "image" ).getValue().toString();
                mName.setText( name );
                mStatus.setText( status );
                mDatabaseFriend.child( mAuth.getCurrentUser().getUid() ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild( id )) {
                            String re_type = snapshot.child( id ).child( "request type" ).getValue().toString();
                            if (re_type.equals( "recieved" )) {
                                mCurrent_state = "req_recieved";
                                mProfile.setText( "Accept friend request" );
                                mDecline.setVisibility( View.VISIBLE );
                                mDecline.setEnabled( true );
                            } else if (re_type.equals( "sent" )) {
                                mCurrent_state = "req_sent";
                                mProfile.setText( "cancel friend request" );
                               mDecline.setVisibility( View.INVISIBLE );
                               mDecline.setEnabled( false );
                            }
                        }
                        else
                          {
                              mDatabaseFriends.child( mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent( new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                      if(snapshot.hasChild( id ))
                                      {
                                          mCurrent_state="friends";
                                          mProfile.setText( "unfriend" );
                                          mDecline.setVisibility( View.INVISIBLE );
                                          mDecline.setEnabled( false );
                                      }
                                  }

                                  @Override
                                  public void onCancelled(@NonNull DatabaseError error) {

                                  }
                              } );
                          }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
                mProgress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
        mProfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfile.setEnabled( false );
            if(mCurrent_state.equals( "not friend" ))
            {
                mDatabaseFriend.child(mAuth.getCurrentUser().getUid()).child( id ).child( "request type" ).setValue( "sent" ).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            mDatabaseFriend.child( id ).child( mAuth.getCurrentUser().getUid() ).child( "request type" ).setValue( "recieved" ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    HashMap<String, String> notificationData = new HashMap<>();
                                    notificationData.put( "from", mAuth.getCurrentUser().getUid() );
                                    notificationData.put( "type", "request" );
                                    mDatabaseNotification.child( id ).push().setValue( notificationData ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            mProfile.setEnabled( true );
                                            mCurrent_state = "req_sent";
                                            mProfile.setText( "cancel friend request" );
                                        }

                                    } );
                                }
                            } );
                        }
                        else
                        {Toast.makeText( profileActivity.this," failed sending request ",Toast.LENGTH_LONG );

                        }
                    }
                } ) ;
            }
            if(mCurrent_state.equals( "req_sent" )){
                mDatabaseFriend.child( mAuth.getCurrentUser().getUid() ).child( id ).removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mDatabaseFriend.child( id ).child( mAuth.getCurrentUser().getUid() ).removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProfile.setEnabled( true );
                                mCurrent_state="not friend";
                                mProfile.setText( "send friend request" );
                            }
                        } );
                    }
                } );
            }
            if(mCurrent_state.equals( "req_recieved")) {
                String currentDate = DateFormat.getDateInstance().format( new Date() );
                mDatabaseFriend.child( mAuth.getCurrentUser().getUid() ).child( id ).removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mDatabaseFriend.child( id ).child( mAuth.getCurrentUser().getUid() ).removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mDatabaseFriends.child( mAuth.getCurrentUser().getUid() ).child( id ).setValue( currentDate ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mDatabaseFriends.child( id ).child( mAuth.getCurrentUser().getUid() ).setValue( currentDate ).addOnSuccessListener( new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mProfile.setEnabled( true );
                                                mProfile.setText( "unfriend" );
                                                mCurrent_state = "friends";
                                                mDecline.setVisibility( View.INVISIBLE );
                                                mDecline.setEnabled( false );
                                            }
                                        } );


                                    }
                                } );

                            }
                        } );

                    }
                } );
            }
            if(mCurrent_state.equals( "friends" ))
            {
                mDatabaseFriends.child( mAuth.getCurrentUser().getUid() ).child( id ).removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mDatabaseFriends.child( id ).child( mAuth.getCurrentUser().getUid() ).removeValue().addOnSuccessListener( new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProfile.setEnabled( true );
                                mProfile.setText( "send friend request" );
                                mCurrent_state = "no friend";

                            }
                        } );
                    }
                } );
            }



            }
        } );

    }

    @Override
    protected void onStart() {
        super.onStart();
        mOnline.child( "online" ).setValue( true );
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOnline.child( "online" ).setValue(false );
    }
}