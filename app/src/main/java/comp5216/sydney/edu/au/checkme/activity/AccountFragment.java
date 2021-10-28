package comp5216.sydney.edu.au.checkme.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.database.DB;
import comp5216.sydney.edu.au.checkme.activity.database.HistoryItem;
import comp5216.sydney.edu.au.checkme.activity.database.HistoryItemDao;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class AccountFragment extends Fragment {
    private static String TAG = "AccountFragment";

    private View view;

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

        temp();
        logout();
        clear();

        return view;
    }

    private void setupTitle() {
        TitleBarLayout titleBarLayout = view.findViewById(R.id.accountTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.account_title);
    }

    private List<HistoryItem> historyItems;

    private void temp() {
        Button logout = view.findViewById(R.id.temp);
        logout.setOnClickListener(v -> {
            HistoryItemDao historyItemDao = DB.getDatabase(getContext()).historyItemDao();

            try {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    HistoryItem historyItem = new HistoryItem();
                    Date visitingDate = new Date();
                    historyItem.setEventId(String.valueOf(visitingDate.getTime()));
                    historyItem.setEventName("Testing Event");
                    historyItem.setEventAddr(null);
                    historyItem.setRiskLevel("Low Risk");
                    historyItem.setVisitingDate(visitingDate);
                    historyItemDao.insert(historyItem);
                    Log.d(TAG, "temp: " + historyItem.toString());
                });
                future.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void logout() {
        Button logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
        });
    }

    private void clear() {
        Button clear = view.findViewById(R.id.clearAll);
        clear.setOnClickListener(v -> {
            HistoryItemDao historyItemDao = DB.getDatabase(getContext()).historyItemDao();

            try {
                CompletableFuture<Void> future = CompletableFuture.runAsync(historyItemDao::deleteAll);
                future.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
