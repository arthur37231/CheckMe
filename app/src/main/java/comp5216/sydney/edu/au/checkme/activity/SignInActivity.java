package comp5216.sydney.edu.au.checkme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.entity.User;
import comp5216.sydney.edu.au.checkme.utils.Constants;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class SignInActivity extends AuthActivity {
    private static final String TAG = "SignInActivity";

    private EditText loginPhoneNumber;
    private EditText loginPassword;
    private RadioButton policyAgreementCheck;
    private Button loginButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loginPhoneNumber = findViewById(R.id.loginPhoneNumber);
        loginPassword = findViewById(R.id.loginPassword);
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        TextView privacyPolicy = findViewById(R.id.privacyPolicy);
        TextView createAccount = findViewById(R.id.toRegister);
        policyAgreementCheck = findViewById(R.id.policyAgreementCheck);
        loginButton = findViewById(R.id.login);

        forgotPassword.setOnClickListener(this::onClickForgotPassword);
        privacyPolicy.setOnClickListener(this::onClickPrivacyPolicy);
        createAccount.setOnClickListener(this::onClickCreateAccount);
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
            DocumentReference docRef = db.collection("users")
                    .document(Constants.PHONE_COUNTRY_CODE + loginPhoneNumber.getText().toString());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if(snapshot.exists()) {
                            Log.d(TAG, "onComplete: " + snapshot.getData());

                            User userInfo = snapshot.toObject(User.class);
                            if(loginPassword.getText().toString().equals(userInfo.getPassword())) {
                                navigateToHomePage();
                            } else {
                                Toast.makeText(getApplicationContext(), "Password is wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            Toast.makeText(getApplicationContext(), "Phone number is wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d(TAG, "Get failed with", task.getException());
                        Toast.makeText(getApplicationContext(), "Please make sure you are connecting network", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void onClickForgotPassword(View view) {
        // TODO: handle if user forgot password
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    private void onClickPrivacyPolicy(View view) {
        // TODO: display a page contains User's Privacy Policy
        startActivity(new Intent(this, UserPrivacyActivity.class));
        finish();
    }

    private void onClickCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
        finish();
    }
}