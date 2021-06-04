package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class chatActivity extends AppCompatActivity {
  private String mUser;
  private DatabaseReference mDatabase;
  private ImageView mImage;
  private TextView mLastSeen;
  private TextView mName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_chat );
        mDatabase= FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setDisplayOptions( ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        mUser=getIntent().getStringExtra( "user_id" );
        mName=(TextView) findViewById( R.id.user_name ) ;
        mLastSeen=(TextView) findViewById( R.id.user_status );
        mImage=(ImageView) findViewById( R.id.online );
        mDatabase.child( "users" ).child(mUser).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              String chat_user_name=snapshot.child( "name" ).getValue().toString();
                textView.setText(chat_user_name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }
    }