package comp5216.sydney.edu.au.checkme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.entity.User;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class CreateAccountActivity extends AuthActivity {

    private static final String TAG = "CreateAccountActivity";

    private EditText signupPhoneNumber;
    private EditText signupVerificationCode;
    private EditText signupPassword;
    private Button createAccountSendCode;

    private FirebaseUser firebaseUser;
    private String storedVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        signupPhoneNumber = findViewById(R.id.signupPhoneNumber);
        signupVerificationCode = findViewById(R.id.signupVerificationCode);
        signupPassword = findViewById(R.id.signupPassword);
        createAccountSendCode = findViewById(R.id.createAccountSendCode);
        Button signup = findViewById(R.id.signup);
        TextView toLogin = findViewById(R.id.toLogin);

        createAccountSendCode.setOnClickListener(this::onClickCreateAccountSendCode);
        signup.setOnClickListener(this::onClickSignup);
        toLogin.setOnClickListener(this::onClickToLogin);
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

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(getAuthCallbacks())
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks getAuthCallbacks() {
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: ", e);
                if(e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.d(TAG, "onVerificationFailed: Invalid Request");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.d(TAG, "onVerificationFailed: SMS quota has been exceeded");
                }

                // Show a message and update UI
                Toast.makeText(getApplicationContext(), "Verification Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.d(TAG, "onCodeSent: " + s);

                storedVerificationId = s;
                resendToken = forceResendingToken;
                Log.d(TAG, "onCodeSent: " + forceResendingToken);
            }
        };
    }

    private void createAccountSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void createUserEntity() {
        String inputPassword = signupPassword.getText().toString();
        if(inputPassword.length() >= 6 && Character.isAlphabetic(inputPassword.charAt(0))) {
            User createdUser = new User(
                    null,
                    firebaseUser.getPhoneNumber(),
                    signupPassword.getText().toString(),
                    null,
                    null
            );
            db.collection("users").document(createdUser.getPhoneNumber())
                    .set(createdUser);

            createAccountSuccess();
        } else {
            Toast.makeText(this, "Password complexity is insufficient", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "onComplete:success");

                            firebaseUser = task.getResult().getUser();

                            createUserEntity();
                        } else {
                            firebaseUser = null;
                            Log.d(TAG, "onComplete:failure", task.getException());
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(), R.string.create_account_fail_message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private PhoneAuthCredential verifyPhoneNumberAndCode(String verificationId, String code) {
        return PhoneAuthProvider.getCredential(verificationId, code);
    }

    private void onClickCreateAccountSendCode(View view) {
        // TODO: Send the verification code to the user's phone number
        String phoneNumber = "+86 " + signupPhoneNumber.getText().toString();
        sendVerificationCode(phoneNumber);
        createAccountSendCode.setText(R.string.verification_code_resend);
    }

    private void onClickSignup(View view) {
        // TODO: Verify the verification code and create an account based on user's phone number and password
        String verificationCode = signupVerificationCode.getText().toString();
        PhoneAuthCredential phoneAuthCredential = verifyPhoneNumberAndCode(storedVerificationId, verificationCode);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void onClickToLogin(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}