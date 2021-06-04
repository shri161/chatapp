package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class userActivity extends AppCompatActivity {
 private RecyclerView mRecyclerView;
 private DatabaseReference mDatabase;
 private DatabaseReference mOnline;
 private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user );
        mDatabase= FirebaseDatabase.getInstance().getReference().child( "users" );
        mRecyclerView=(RecyclerView) findViewById( R.id.blog_list );
        mRecyclerView.setHasFixedSize( true );
        mRecyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        mAuth=FirebaseAuth.getInstance();
       mOnline=FirebaseDatabase.getInstance().getReference().child( "users" ).child( mAuth.getCurrentUser().getUid() );
        getSupportActionBar().setDisplayOptions( ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("My Custom Title");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mOnline.child( "online" ).setValue( true );
        FirebaseRecyclerAdapter<users,usersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<users, usersViewHolder>(users.class,R.layout.user_single_layout,usersViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(usersViewHolder usersViewHolder, users users, int i) {
               usersViewHolder.setName( users.getName() );
               final String id=getRef( i ).getKey();
               usersViewHolder.setStatus(users.getStatus() );
               usersViewHolder.mView.setOnClickListener( new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent =new Intent(userActivity.this,profileActivity.class);
                       intent.putExtra( "user_id",id );
                       startActivity( intent );
                   }
               } );
            }
        };
        mRecyclerView.setAdapter( firebaseRecyclerAdapter );
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOnline.child( "online" ).setValue( false);
    }

    public static class usersViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public usersViewHolder(@NonNull View itemView) {
            super( itemView );
            mView=itemView;
        }
        public void setName(String name)
        {
            TextView username=(TextView) mView.findViewById( R.id.user_name );
            username.setText(  name);
        }
        public void setStatus(String status)
        {
            TextView userStatus=(TextView) mView.findViewById( R.id.user_status );
            userStatus.setText( status );
        }
    }
    }
