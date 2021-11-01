package comp5216.sydney.edu.au.checkme.activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private String latLng;
    private HistoryItemDao historyItemDao;
    private DB db;

    public CheckInSuccessFragment(String startTime, String endTime, String eventId, String eventName, String latLng) {
        this.endTime = endTime;
        this.eventId = eventId;
        this.startTime = startTime;
        this.eventName = eventName;
        this.latLng = latLng;
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

        String address = resolveAddress();
        // hard code here
        String riskLevel = "Low risk";
        HistoryItem item = new HistoryItem(this.eventId, this.eventName, now, address, riskLevel, date);
        saveItemsToDatabase(item);
        return view;
    }

    private void setupTitle() {
        TitleBarLayout titleBarLayout = view.findViewById(R.id.scanResultTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.success);
    }

    private String resolveAddress() {
        try {
            JSONObject jsonObject = new JSONObject(latLng);
            String lat = jsonObject.getString("latitude");
            String lnt = jsonObject.getString("longitude");
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> address = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lnt), 1);

            String city = address.get(0).getLocality();
            String street = address.get(0).getAddressLine(0);
            return street + ", " + city;
        } catch (JSONException | IOException e){
            return "";
        }
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
        getActivity().findViewById(R.id.fragment_container).setVisibility(View.GONE);
        getActivity().findViewById(R.id.capture_container).setVisibility(View.VISIBLE);
        getActivity().finish();
    }
}
