package comp5216.sydney.edu.au.checkme.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.database.DB;
import comp5216.sydney.edu.au.checkme.activity.database.HighRiskAreaData;
import comp5216.sydney.edu.au.checkme.activity.database.HighRiskAreaDataDao;
import comp5216.sydney.edu.au.checkme.activity.database.HighRiskDataDownloadDate;
import comp5216.sydney.edu.au.checkme.activity.database.HighRiskDataDownloadDateDao;
import comp5216.sydney.edu.au.checkme.activity.database.HistoryItem;
import comp5216.sydney.edu.au.checkme.activity.database.HistoryItemDao;
import comp5216.sydney.edu.au.checkme.utils.Constants;
import comp5216.sydney.edu.au.checkme.utils.DateTimeUtils;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class HomeNormalFragment extends Fragment {
    private static String TAG = "HomeNormalFragment";

    private View view;
    private List<HistoryItem> historyItemList;
    private HighRiskDataDownloadDate dataDownloadDate;

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
        view = inflater.inflate(R.layout.fragment_home_normal, container, false);
        TitleBarLayout titleBarLayout = view.findViewById(R.id.normalHomeTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.home_title);

        loadDataDownloadTime();

        LinearLayout downloadData = view.findViewById(R.id.downloadData);
        LinearLayout reportPositive = view.findViewById(R.id.reportPositive);

        downloadData.setOnClickListener(this::onClickDownloadData);
        reportPositive.setOnClickListener(this::onClickReportPositive);

        return view;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to { Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        loadVisitCountNumber();
    }

    private void loadDataDownloadTime() {
        TextView dataUpdatedTime = view.findViewById(R.id.dataUpdatedTime);
        HighRiskDataDownloadDateDao dataDownloadDateDao =
                DB.getDatabase(getContext()).highRiskDataDownloadDateDao();
        try {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                dataDownloadDate = dataDownloadDateDao.select();
            });
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if(dataDownloadDate == null) {
            dataUpdatedTime.setText(Constants.NOTHING);
        } else {
            dataUpdatedTime.setText(DateTimeUtils.formatDate(dataDownloadDate.getDownloadDate()));
        }
    }

    private void loadVisitCountNumber() {
        TextView visitCount = view.findViewById(R.id.visitCountNumber);
        HistoryItemDao historyItemDao = DB.getDatabase(getContext()).historyItemDao();

        try {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                historyItemList = historyItemDao.listAll();
            });
            future.get();

            visitCount.setText(String.valueOf(historyItemList.size()));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void onClickDownloadData(View view) {
        // TODO: Download latest high-risk areas data from firebase
        downloadData();
    }

    private void onClickReportPositive(View view) {
        uploadData();
    }

    private void uploadData() {
        HistoryItemDao historyItemDao = DB.getDatabase(getContext()).historyItemDao();
        List<HistoryItem> historyItems = new ArrayList<>();

        try {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                historyItems.addAll(historyItemDao.listAll());
            });
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        for(HistoryItem historyItem : historyItems) {
            HighRiskAreaData highRiskAreaData = new HighRiskAreaData();
            highRiskAreaData.setEventId(historyItem.getEventId());
            highRiskAreaData.setEventAddress(historyItem.getEventAddr());
            highRiskAreaData.setHighRiskDate(historyItem.getVisitingDate());

            String documentPath = new SimpleDateFormat(Constants.HIGH_RISK_DATA_DATE_FORMAT).format(new Date());
            FirebaseFirestore.getInstance().collection(Constants.HIGH_RISK_DATA_COLLECTION)
                    .document(documentPath)
                    .update("data", FieldValue.arrayUnion(highRiskAreaData))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "Data Upload: Success");
                            } else {
                                Log.d(TAG, "Data Upload: Failure");
                                Map<String, Object> initData = new HashMap<>();
                                initData.put("data", Collections.singletonList(highRiskAreaData));
                                FirebaseFirestore.getInstance()
                                        .collection(Constants.HIGH_RISK_DATA_COLLECTION)
                                        .document(documentPath)
                                        .set(initData);
                            }
                        }
                    });
        }

        Toast.makeText(getContext(), "Report Success", Toast.LENGTH_SHORT).show();
    }

    private void persistDownloadedData(Set<HighRiskAreaData> dataSet) {
        Set<String> highRiskEventId = new HashSet<>();
        HighRiskAreaDataDao highRiskAreaDataDao = DB.getDatabase(getContext()).highRiskAreaDataDao();
        HighRiskDataDownloadDateDao dateDao = DB.getDatabase(getContext()).highRiskDataDownloadDateDao();

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            highRiskAreaDataDao.deleteAll();
            for (HighRiskAreaData data : dataSet) {
                highRiskEventId.add(data.getEventId());
                highRiskAreaDataDao.insert(data);
            }
            dateDao.deleteAll();

            HighRiskDataDownloadDate latestDataDate = new HighRiskDataDownloadDate();
            latestDataDate.setDownloadDate(new Date());

            dateDao.insert(latestDataDate);
        });
        future.join();

        loadDataDownloadTime();

        Log.d(TAG, "Latest Data Download Finished");

        HistoryItemDao historyItemDao = DB.getDatabase(getContext()).historyItemDao();
        try {
            CompletableFuture<Void> f = CompletableFuture.runAsync(() -> {
                List<HistoryItem> historyItemList = historyItemDao.listAll();
                for(HistoryItem historyItem : historyItemList) {
                    if(highRiskEventId.contains(historyItem.getEventId())) {
                        Log.w(TAG, "downloadData: " + highRiskEventId);
                        historyItem.setRiskLevel("High Risk");
                        historyItemDao.update(historyItem);
                    }
                }
            });
            f.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void downloadData() {
        Set<HighRiskAreaData> dataSet = new HashSet<>();
        for(int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - i);

            String queryDate = new SimpleDateFormat(Constants.HIGH_RISK_DATA_DATE_FORMAT).format(calendar.getTime());
            FirebaseFirestore.getInstance().collection(Constants.HIGH_RISK_DATA_COLLECTION)
                    .document(queryDate)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()) {
                                    Log.d(TAG, "Download Data: success " + document.getData().get("data"));

                                    for(Map<String, Object> object : (ArrayList<Map<String, Object>>) document.getData().get("data")) {
                                        HighRiskAreaData data = new HighRiskAreaData();
                                        data.setEventId((String) object.get("eventId"));
                                        dataSet.add(data);
                                    }

                                    persistDownloadedData(dataSet);
                                } else {
                                    Log.d(TAG, "Download Data: No such document " + queryDate);
                                }
                            } else {
                                Log.d(TAG, "Get failed with ", task.getException());
                            }
                        }
                    });
        }

        Toast.makeText(view.getContext(), R.string.download_data_toast, Toast.LENGTH_SHORT).show();
    }
}
