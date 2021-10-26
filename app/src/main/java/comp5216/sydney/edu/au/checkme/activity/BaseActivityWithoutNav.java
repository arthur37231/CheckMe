package comp5216.sydney.edu.au.checkme.activity;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.view.NavigationBar;

public abstract class BaseActivityWithoutNav extends BaseActivity {
   @Override
    void setupNavigator() {
        NavigationBar navigationBar = findViewById(R.id.navigationBar);
        navigationBar.setInvisible();
    }
}
