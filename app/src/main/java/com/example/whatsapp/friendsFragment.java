package com.example.whatsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class friendsFragment extends Fragment {

    private RecyclerView mFriendList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private String uid;
    private View mMainView;
    private FirebaseAuth mAuth;
    public friendsFragment() {
        // Required empty public constructor}
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView= inflater.inflate( R.layout.fragment_friends, container, false );
        mFriendList=(RecyclerView) mMainView.findViewById( R.id.blog_list );
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child( "friends" ).child( uid );
        mDatabase.keepSynced( true );
        mDatabaseUser=FirebaseDatabase.getInstance().getReference().child( "users" );
        mDatabaseUser.keepSynced( true );
        mFriendList.setHasFixedSize( true );
        mFriendList.setLayoutManager( new LinearLayoutManager( getContext() ) );
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Friends,FriendsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(Friends.class,R.layout.user_single_layout,FriendsViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(FriendsViewHolder friendsViewHolder, Friends friends, int i) {
             friendsViewHolder.setDate(friends.getDate());
             String list_user_id=getRef( i ).getKey();
              mDatabaseUser.child( list_user_id ).addValueEventListener( new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                       String userName=snapshot.child( "name" ).getValue().toString();
                       if(snapshot.hasChild( "online" )){
                       boolean userOnline=(boolean)snapshot.child( "online" ).getValue();
                           friendsViewHolder.setOnline(userOnline  );}
                       friendsViewHolder.setName( userName );
                       friendsViewHolder.mView.setOnClickListener( new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               CharSequence options[]=new CharSequence[]{"open profile","send message"};
                               AlertDialog.Builder builder=new AlertDialog.Builder( getContext() );
                               builder.setTitle( "Select Options" );
                               builder.setItems( options, new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       if(i==0)
                                       {
                                           Intent intent =new Intent(getContext(),profileActivity.class);
                                           intent.putExtra( "user_id",list_user_id );
                                           startActivity( intent );
                                       }
                                       if(i==1)
                                       {
                                           Intent intent =new Intent(getContext(),chatActivity.class);
                                           intent.putExtra( "user_id",list_user_id );
                                           startActivity( intent );
                                       }

                                   }
                               } );
                               builder.show();
                           }
                       } );
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              } );

            }
        };
   mFriendList.setAdapter( firebaseRecyclerAdapter );
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
       View mView;

        public FriendsViewHolder(@NonNull View itemView) {
            super( itemView );
            mView=itemView;
        }
        public void setDate(String date)
        {
            TextView userNameView=(TextView) mView.findViewById( R.id.user_status );
            userNameView.setText( date );

        }
        public  void setName(String name)
        { TextView userName=(TextView) mView.findViewById( R.id.user_name );
        userName.setText( name );

        }
        public void setOnline(boolean icon)
        {
            ImageView userOnline=(ImageView) mView.findViewById( R.id.online );
            if(icon==true)
            {
                userOnline.setVisibility(View.VISIBLE );
            }
            else
            {userOnline.setVisibility(View.INVISIBLE );

            }
        }
    }
}