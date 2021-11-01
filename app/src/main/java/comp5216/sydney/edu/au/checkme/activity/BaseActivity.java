package comp5216.sydney.edu.au.checkme.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * BaseActivity is a subclass of {@link AppCompatActivity}.
 * It replaces the activity default action bar with a customized. Related method override here to
 * support the functionality.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar title = getSupportActionBar();
        if(title != null) title.hide();

        Log.d(TAG, "onCreate: Before setupTitle + " + this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupTitle();
    }

    /**
     * A template method to setup all components and corresponding listeners of customized
     * action bar. All subclasses extend the BaseActivity are using the similar action bar design.
     *
     * @see comp5216.sydney.edu.au.checkme.view.TitleBarLayout
     */
    void setupTitle() {

    }
}
