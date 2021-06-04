package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
   private FirebaseAuth mAuth;
   private ViewPager mViewPager;
  private PagerAdapter mPagerAdapter;
  private SectionsPagerAdapter mSectionsPagerAdapter;
  private DatabaseReference mDatabase;
  private TabLayout mTab;
    private FragmentStateAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child( "users" ).child(mAuth.getCurrentUser().getUid()  );


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null)
        {
            Intent startIntent= new Intent(MainActivity.this,startActivity.class);
            startActivity( startIntent );
            finish();
        }
        else
        { mDatabase.child( "online" ).setValue( true );

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.child( "online" ).setValue(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_menu,menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout)
        {
            mAuth.signOut();
            Intent startIntent= new Intent(MainActivity.this,startActivity.class);
            startActivity( startIntent );
            finish();
        }
        if(item.getItemId()==R.id.settings)
        { Intent startIntent= new Intent(MainActivity.this,settingsActivity.class);
            startActivity( startIntent );

        }
        if(item.getItemId()==R.id.users)
        {
            Intent startIntent= new Intent(MainActivity.this,userActivity.class);
            startActivity( startIntent );
        }
        return super.onOptionsItemSelected( item );
    }
}