package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
public class SectionsPagerAdapter extends FragmentStateAdapter {


  public  SectionsPagerAdapter(FragmentActivity fa)
  {
      super(fa);

  }

    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:requestFragment RequestFragment=new requestFragment();
            return  RequestFragment;
            case 1: chatsFragment ChatsFragment =new chatsFragment();
            return ChatsFragment;
            case 2: friendsFragment FriendsFragment =new friendsFragment();
            return FriendsFragment;
            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
