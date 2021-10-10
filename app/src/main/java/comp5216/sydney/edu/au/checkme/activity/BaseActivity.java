package comp5216.sydney.edu.au.checkme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    abstract void setupTitle();

    /**
     * The single point for the activity launching another activity and obtaining activity results.
     * Reduce coupling and avoiding official API deprecating risk.
     * p. The startActivityForResult method already deprecated.
     *
     * @param intent Intent object holding target activity and necessary data.
     * @param activityResultLauncher Registered active result launcher. It can be launched directly.
     */
    public void requestActivityResult(Intent intent,
                                      ActivityResultLauncher<Intent> activityResultLauncher) {
        activityResultLauncher.launch(intent);
    }

    /**
     * Register the activity navigating request launcher to the activity.
     * The process must be done before activity STARTED, i.e. before onStart() method is invoked.
     *
     * @param callback The callback object contains a method to handle result data.
     * @return A registered activity navigating launcher.
     * It can be launched by {@link #requestActivityResult(Intent, ActivityResultLauncher)} method.
     */
    protected ActivityResultLauncher<Intent> registerResultRequest(
            ActivityResultCallback<ActivityResult> callback) {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback
        );
    }
}
