package comp5216.sydney.edu.au.checkme.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp5216.sydney.edu.au.checkme.R;

public class NavigationBar extends LinearLayout {
    private Button home;
    private Button myCode;
    private Button scan;
    private Button history;
    private Button account;
    private BottomNavigationView bottomNavigationView;

    public NavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(getContext()).inflate(R.layout.navigation_bar, this);

        this.bottomNavigationView = findViewById(R.id.bottomNavBar);

//        this.home = findViewById(R.id.home);
//        this.myCode = findViewById(R.id.myCode);
//        this.scan = findViewById(R.id.scan);
//        this.history = findViewById(R.id.history);
//        this.account = findViewById(R.id.account);
    }

    public NavigationBar setupHome(OnClickListener listener) {
        this.home.setOnClickListener(listener);
        return this;
    }

    public NavigationBar setupMyCode(OnClickListener listener) {
        this.myCode.setOnClickListener(listener);
        return this;
    }

    public NavigationBar setupScan(OnClickListener listener) {
        this.scan.setOnClickListener(listener);
        return this;
    }

    public NavigationBar setupHistory(OnClickListener listener) {
        this.history.setOnClickListener(listener);
        return this;
    }

    public NavigationBar setupAccount(OnClickListener listener) {
        this.account.setOnClickListener(listener);
        return this;
    }

    public void setInvisible() {
        this.setVisibility(View.GONE);
    }
}
