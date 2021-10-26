package comp5216.sydney.edu.au.checkme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class SignInActivity extends BaseActivityWithoutNav {

    private RadioButton policyAgreementCheck;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        policyAgreementCheck = findViewById(R.id.policyAgreementCheck);
        loginButton = findViewById(R.id.login);

        policyAgreementCheck.setOnClickListener(this::onClickPolicyCheck);
        loginButton.setOnClickListener(this::onClickLogin);
    }

    /**
     * A template method to setup all components and corresponding listeners of customized
     * action bar. All subclasses extend the BaseActivity are using the similar action bar design.
     *
     * @see TitleBarLayout
     */
    @Override
    void setupTitle() {
        TitleBarLayout titleBarLayout = findViewById(R.id.loginTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.login_title);
    }

    private void onClickPolicyCheck(View view) {
        policyAgreementCheck.setChecked(true);
        loginButton.setEnabled(true);
    }

    private void onClickLogin(View view) {
        if(!policyAgreementCheck.isChecked()) {
            Toast.makeText(this, "You must agree the privacy policy first", Toast.LENGTH_SHORT).show();
        } else {
            // TODO: login algorithms
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}