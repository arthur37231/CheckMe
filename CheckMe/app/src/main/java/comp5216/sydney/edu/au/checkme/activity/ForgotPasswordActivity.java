package comp5216.sydney.edu.au.checkme.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class ForgotPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    /**
     * A template method to setup all components and corresponding listeners of customized
     * action bar. All subclasses extend the BaseActivity are using the similar action bar design.
     *
     * @see TitleBarLayout
     */
    @Override
    void setupTitle() {
        TitleBarLayout titleBarLayout = findViewById(R.id.forgotPasswordTitle);
        titleBarLayout.setupTitle(R.string.forgot_password_title).operateInvisible()
                .setupBack(view -> returnLogin());
    }

    private void returnLogin() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        returnLogin();
    }
}