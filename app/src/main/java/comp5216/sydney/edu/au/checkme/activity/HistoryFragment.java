package comp5216.sydney.edu.au.checkme.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import comp5216.sydney.edu.au.checkme.R;

import comp5216.sydney.edu.au.checkme.activity.database.DB;
import comp5216.sydney.edu.au.checkme.activity.database.HistoryItem;
import comp5216.sydney.edu.au.checkme.activity.database.HistoryItemDao;
import comp5216.sydney.edu.au.checkme.activity.utils.HistoryItemAdapter;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class HistoryFragment extends Fragment {
    private View view;
    private ListView historyList;
    private List<HistoryItem> items;
    private DB db;
    private HistoryItemDao historyItemDao;
    private HistoryItemAdapter itemAdapter;

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
        view = inflater.inflate(R.layout.fragment_history, container, false);
        setupTitle();
        db = DB.getDatabase(getContext());
        historyItemDao = db.historyItemDao();
        readItemsFromDatabase();
        historyList = view.findViewById(R.id.history_list);
        itemAdapter = new HistoryItemAdapter(getContext(), R.layout.history_items, items);
        setupListViewListener();
        historyList.setAdapter(itemAdapter);
        return view;
    }

    private void setupListViewListener() {
        historyList.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(getContext());
            HistoryItem item = items.get(position);
            String eventId = item.getEventId();
            String eventName = item.getEventName();
            String visitTime = item.getVisitingTime();
            String eventAddr = item.getEventAddr();
            String eventContent = "Event Name: " + eventName + "\n"
                    + "Visit Time: " + visitTime
                    + "\n" + "Event Address: " + eventAddr;
            normalDialog.setTitle("Event ID: " + eventId);
            normalDialog.setMessage(eventContent);
            normalDialog.setPositiveButton("OK", (dialog, which) -> {});
            normalDialog.show();
        });
    }

    private void readItemsFromDatabase()
    {
        try {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                List<HistoryItem> itemsFromDB = historyItemDao.listAll();
                items = itemsFromDB;
            });
            future.get();
        }
        catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }

    private void setupTitle() {
        TitleBarLayout titleBarLayout = view.findViewById(R.id.historyTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.history_title);
    }


}
