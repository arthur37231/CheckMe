package comp5216.sydney.edu.au.checkme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class AccountFragment extends Fragment {
    private static String TAG = "AccountFragment";

    private View view;
    private EditText accountName;
    private TextView accountPhoneNumber;
    private TextView accountRegisterTime;
    private EditText livingAddress;
    private EditText contactEmail;
    private FirebaseUser firebaseUser;

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null. This will be called between
     * {@link #onCreate(Bundle)} and {@link #onViewCreated(View, Bundle)}.
     * <p>A default View can be returned by calling {#Fragment(int)} in your
     * constructor. Otherwise, this method returns null.
     *
     * <p>It is recommended to <strong>only</strong> inflate the layout in this method and move
     * logic that operates on the returned View to {@link #onViewCreated(View, Bundle)}.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        setupTitle();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        accountName = view.findViewById(R.id.accountName);
        accountPhoneNumber = view.findViewById(R.id.accountPhoneNumber);
        accountRegisterTime = view.findViewById(R.id.accountRegisterTime);
        livingAddress = view.findViewById(R.id.accountLivingAddress);
        contactEmail = view.findViewById(R.id.accountEmail);
        Button confirmEdit = view.findViewById(R.id.confirmEditProfile);

        confirmEdit.setOnClickListener(this::onClickConfirmEditProfile);
        accountPhoneNumber.setOnLongClickListener(this::signOut);

        initAccountInfo();

        return view;
    }

    private void setupTitle() {
        TitleBarLayout titleBarLayout = view.findViewById(R.id.accountTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.account_title);
    }

    private void initAccountInfo() {
        FirebaseFirestore.getInstance().collection("users")
                .document(firebaseUser.getPhoneNumber())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            Map<String, Object> userInfo = task.getResult().getData();
                            Log.d(TAG, "The current user information " + userInfo);

                            accountName.setText((String) userInfo.get("fullName"));
                            accountPhoneNumber.setText(firebaseUser.getPhoneNumber());
                            accountRegisterTime.setText((String) userInfo.get("registerTime"));
                            contactEmail.setText((String) userInfo.get("contactEmail"));
                            livingAddress.setText((String) userInfo.get("address"));
                        } else {
                            Log.d(TAG, "Cannot update user's info because cannot find " +
                                    "current user in the auth database");
                        }
                    }
                });
    }

    private void onClickConfirmEditProfile(View v) {
        String authPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        Map<String, Object> updateFields = new HashMap<>();
        updateFields.put("fullName", accountName.getText().toString());
        updateFields.put("address", livingAddress.getText().toString());
        updateFields.put("contactEmail", contactEmail.getText().toString());

        FirebaseFirestore.getInstance().collection("users")
                .document(authPhoneNumber)
                .update(updateFields);

        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
    }

    private boolean signOut(View v) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), SignInActivity.class));
        getActivity().finish();
        return true;
    }
}
