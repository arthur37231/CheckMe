package comp5216.sydney.edu.au.checkme.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.MainActivity;

public class NavigationBar extends LinearLayout{
    private final BottomNavigationView bottomNavigationView;
    private MainActivity fragmentHolder;

    public NavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.navigation_bar, this);

        this.bottomNavigationView = findViewById(R.id.bottomNavBar);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    fragmentHolder.navigateTo(0);
                    break;
                case R.id.myCode:
                    fragmentHolder.navigateTo(1);
                    break;
                case R.id.scan:
                    fragmentHolder.navigateTo(2);
                    break;
                case R.id.history:
                    fragmentHolder.navigateTo(3);
                    break;
                case R.id.account:
                    fragmentHolder.navigateTo(4);
                    break;
            }
            return true;
        });
    }

    public void setInvisible() {
        this.setVisibility(View.GONE);
    }

    public void navigateTo(int position) {
        this.bottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    public void setFragmentHolder(MainActivity fragmentHolder) {
        this.fragmentHolder = fragmentHolder;
    }
}
