package comp5216.sydney.edu.au.checkme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.database.DB;
import comp5216.sydney.edu.au.checkme.activity.database.HistoryItem;
import comp5216.sydney.edu.au.checkme.activity.database.HistoryItemDao;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

/**
 * @author tyson
 * Created 2021/10/23 at 11:30 下午
 */
public class CheckInSuccessFragment extends Fragment {
    private View view;
    private TextView tv_event_name;
    private TextView tv_check_in_time;
    private String endTime;
    private String eventId;
    private String startTime;
    private String eventName;
    private HistoryItemDao historyItemDao;
    private DB db;

    public CheckInSuccessFragment(String startTime, String endTime, String eventId, String eventName) {
        this.endTime = endTime;
        this.eventId = eventId;
        this.startTime = startTime;
        this.eventName = eventName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_checkin_successfully, container, false);
        setupTitle();
        db = DB.getDatabase(getContext());
        historyItemDao = db.historyItemDao();
        tv_event_name = view.findViewById(R.id.eventName);
        tv_check_in_time = view.findViewById(R.id.checkInTime);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(date);
        tv_event_name.setText("Event Name: " + eventName);
        tv_check_in_time.setText("Check-In Time: " + now);
        HistoryItem item = new HistoryItem(this.eventId, this.eventName, now, "Addr", "Normal");
        saveItemsToDatabase(item);
        return view;
    }

    private void setupTitle() {
        TitleBarLayout titleBarLayout = view.findViewById(R.id.scanResultTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.success);
    }

    private void saveItemsToDatabase(HistoryItem item) {
        try {
            // Run a task specified by a Runnable Object asynchronously.
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                historyItemDao.insert(item);
            });
            future.get();
        } catch (Exception ex) {
            Log.e("saveHistoryItemsToDatabaseError", ex.getStackTrace().toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent intent = new Intent();
        intent.setClass(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().findViewById(R.id.fragment_container).setVisibility(View.GONE);
        getActivity().findViewById(R.id.capture_container).setVisibility(View.VISIBLE);
    }

}
