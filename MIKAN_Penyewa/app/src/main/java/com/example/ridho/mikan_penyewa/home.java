package com.example.ridho.mikan_penyewa;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.ridho.mikan_penyewa.Adapter.ViewPagerAdapter;
import com.example.ridho.mikan_penyewa.Fragments.MenuFragment;
import com.example.ridho.mikan_penyewa.Fragments.NotificationFragment;
import com.example.ridho.mikan_penyewa.Fragments.ProfileFragment;

public class home extends AppCompatActivity {

    private SwipeDisableViewPager viewPager;
    ProfileFragment profileFragment;
    NotificationFragment notificationFragment;
    MenuFragment menuFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation_tab);
        viewPager =(SwipeDisableViewPager)findViewById(R.id.view_tab);
        viewPager.setPagingEnable(false);//Agar tidak bisa di swipe

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_profile:
                        viewPager.setCurrentItem(0, false);
                        return true;
                    case R.id.nav_notif:
                        viewPager.setCurrentItem(1, false);
                        return true;
                    case R.id.nav_menu:
                        viewPager.setCurrentItem(2, false);
                        return true;

                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                bottomNavigationView.getMenu().getItem(i).setChecked(false);
            }

            @Override
            public void onPageSelected(int i) {
                Log.d("page", "onPageSelected: " + i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        setupViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        profileFragment = new ProfileFragment();
        notificationFragment = new NotificationFragment();
        menuFragment = new MenuFragment();
        viewPagerAdapter.addFragment(profileFragment);
        viewPagerAdapter.addFragment(notificationFragment);
        viewPagerAdapter.addFragment(menuFragment);
        viewPager.setAdapter(viewPagerAdapter);
    }
}
