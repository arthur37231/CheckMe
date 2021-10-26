package comp5216.sydney.edu.au.checkme.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

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

        this.fragmentHolder = findViewById(R.id.viewPager);
        this.navigationBar = findViewById(R.id.navigationBar);

        this.fragmentHolder.setAdapter(new ViewPagerAdapter(this));
        this.fragmentHolder.registerOnPageChangeCallback(fragmentChangeCallback());

        this.navigationBar.setFragmentHolder(this);

        findViewById(R.id.scan).setOnClickListener(view -> {
<<<<<<< Updated upstream:app/src/main/java/comp5216/sydney/edu/au/checkme/activity/MainActivity.java
           Intent intent = new Intent();
           intent.setClass(this, ScanActivity.class);
           startActivity(intent);
=======
            Intent intent = new Intent();
            intent.setClass(this, ScanActivity.class);
            startActivity(intent);
>>>>>>> Stashed changes:CheckMe/app/src/main/java/comp5216/sydney/edu/au/checkme/activity/MainActivity.java
        });
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
                navigationBar.navigateTo(position);
            }
        };
    }

    public void navigateTo(int position) {
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
            this.fragments.add(new MyCodeFragment());
            this.fragments.add(new ScanFragment());
            this.fragments.add(new HistoryFragment());
            this.fragments.add(new AccountFragment());
        }

    }
}