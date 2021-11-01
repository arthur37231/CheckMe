package comp5216.sydney.edu.au.checkme.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.view.NavigationBar;

public class MainActivity extends FragmentActivity {

    private ViewPager2 fragmentHolder;
    private NavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askMapPermission();

        this.fragmentHolder = findViewById(R.id.viewPager);
        this.navigationBar = findViewById(R.id.navigationBar);

        this.fragmentHolder.setAdapter(new ViewPagerAdapter(this));
        this.fragmentHolder.registerOnPageChangeCallback(fragmentChangeCallback());

        this.navigationBar.setFragmentHolder(this);

        findViewById(R.id.scan).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this, ScanActivity.class);
            startActivity(intent);
        });

        System.out.println(FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    private void askMapPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 2);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    private ViewPager2.OnPageChangeCallback fragmentChangeCallback() {
        return new ViewPager2.OnPageChangeCallback() {
            /**
             * This method will be invoked when a new page becomes selected. Animation is not
             * necessarily complete.
             *
             * @param position Position index of the new selected page.
             */
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position > 1) position++;
                navigationBar.navigateTo(position);
            }
        };
    }

    public void navigateTo(int position) {
        if(position > 2) position--;
        this.fragmentHolder.setCurrentItem(position, true);
    }

    static class ViewPagerAdapter extends FragmentStateAdapter {
        private List<Fragment> fragments;

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            initFragments();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return this.fragments.size();
        }

        private void initFragments() {
            this.fragments = new ArrayList<>();
            this.fragments.add(new HomeNormalFragment());
            this.fragments.add(new MyCodeContainerFragment());
            this.fragments.add(new HistoryFragment());
            this.fragments.add(new AccountFragment());
        }

    }
}