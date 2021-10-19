package comp5216.sydney.edu.au.checkme.activity;

import android.os.Bundle;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class CreateAccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    /**
     * A template method to setup all components and corresponding listeners of customized
     * action bar. All subclasses extend the BaseActivity are using the similar action bar design.
     *
     * @see TitleBarLayout
     */
    @Override
    void setupTitle() {
        TitleBarLayout titleBarLayout = findViewById(R.id.createAccountTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.create_account_title);
    }
}