package comp5216.sydney.edu.au.checkme.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.utils.Tools;

/**
 * @author tyson
 * Created 2021/10/24 at 12:39 上午
 */
public class ScanActivity extends BaseActivity {
    private Double mlat;
    private Double mlon;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getDevicePosition();
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureActivity.class);
        intentIntegrator.initiateScan();
        setContentView(R.layout.activity_scan);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String barcode = result.getContents();
        if (barcode == null){
            finish();
        }
        else {
            try {
                JSONObject jsonObject = new JSONObject(barcode);
                jsonObject.getString("type");
                String startTime = jsonObject.getString("startTime");
                String endTime = jsonObject.getString("endTime");
                String eventId = jsonObject.getString("eventId");
                String eventName = jsonObject.getString("eventName");
                String latLng = jsonObject.getString("latLng");
                // check if activity has active
                if (checkActivity(startTime, endTime)) {
                    // check the device's location
                    if (mlat != null && mlon != null){
                        // check the scan location
                        if (checkPosition(latLng)) {
                            checkInSuccessful(startTime, endTime, eventId, eventName, latLng);
                        } else {
                            checkInFailed("Wrong location");
                        }
                    } else {
                        checkInFailed("Can not get device's location");
                    }
                } else {
                    checkInFailed("Event not active");
                }
            } catch (JSONException e) {
                checkInFailed("Invalid QR code");
            }
        }
    }

    public void checkInFailed(String reason) {
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
        findViewById(R.id.capture_container).setVisibility(View.GONE);
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new CheckInFailFragment(reason), null)
                .addToBackStack(null)
                .commit();
    }

    public void checkInSuccessful(String startTime, String endTime, String eventId, String eventName, String latLng) {
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
        findViewById(R.id.capture_container).setVisibility(View.GONE);
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new CheckInSuccessFragment(startTime,
                        endTime, eventId, eventName, latLng), null)
                .addToBackStack(null)
                .commit();
    }

    private boolean checkActivity(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat(Tools.EVENT_TIME_FORMAT);
        try {
            Date start = fmt.parse(startTime);
            Date end = fmt.parse(endTime);
            Date now = new Date();
            return start.before(now) && end.after(now);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getDevicePosition() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 2);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        mlat = location.getLatitude();
                        mlon = location.getLongitude();
                    }
                });
    }

    private boolean checkPosition(String latLng) {
        try {
            JSONObject jsonObject = new JSONObject(latLng);
            Double lat = jsonObject.getDouble("latitude");
            Double lnt = jsonObject.getDouble("longitude");
            double dist = Tools.GetDistance(lnt, lat, mlon, mlat);
            // If user scan the QR code within 100 meters, consider as in the right location
            if (dist < 100){
                return true;
            } else {
                return false;
            }
        } catch (JSONException e){
            return false;
        }
    }
}
