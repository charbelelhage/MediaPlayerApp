package com.example.mediaplayerapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mediaplayerapp.LikesFragment;
import com.example.mediaplayerapp.PlaylistsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LikesFragment();
            case 1:
                return new PlaylistsFragment();
            default:
                throw new IllegalStateException("Unexpected position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}
