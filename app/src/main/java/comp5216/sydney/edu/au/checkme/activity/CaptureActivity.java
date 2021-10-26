package comp5216.sydney.edu.au.checkme.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import comp5216.sydney.edu.au.checkme.R;

/**
 * @author tyson
 * Created 2021/10/24 at 10:21
 * Reference from
 * https://blog.csdn.net/mountain_hua/article/details/80699778
 * https://blog.csdn.net/hupei/article/details/51859171
 * https://www.imooc.com/article/20971
 * https://www.imooc.com/article/20974
 */

public class CaptureActivity extends BaseActivityWithoutNav implements DecoratedBarcodeView.TorchListener
{
    private DecoratedBarcodeView barcodeScannerView;
    private CaptureManager capture;
    private ImageView gallery;
    private ImageView flash;
    private TextView flashTip;
    private static final int REQUEST_CODE_SCAN_GALLERY = 50001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Set the click event of gallery button
        gallery = findViewById(R.id.gallery);
        gallery.setClickable(true);
        gallery.bringToFront();
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int checked = ContextCompat.checkSelfPermission(CaptureActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (checked == PackageManager.PERMISSION_GRANTED) {
                    open_picture();
                } else {
                    ActivityCompat.requestPermissions(CaptureActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
        });

        // Set the click event of flash button
        flashTip = findViewById(R.id.flash_tip);
        if (!hasFlash()){
            flashTip.setText("The device doesn't have flashlight");
        }
        flash = findViewById(R.id.flashlight);
        flash.setClickable(true);
        flash.bringToFront();
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFlashlight(v);
            }
        });

        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    private void open_picture(){
        Intent innerIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, "Select a QR code");
        CaptureActivity.this.startActivityForResult(wrapperIntent, REQUEST_CODE_SCAN_GALLERY);
    }

    /**
     * The method handle the result scanning from gallery
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photo_path = null;
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(photo_path);
                    bitmap = getSmallerBitmap(bitmap);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int[] pixels = new int[width * height];
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                    RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
                    QRCodeReader reader = new QRCodeReader();
                    try {
                        Result result = reader.decode(binaryBitmap);
                        String res_str = result.getText();
                        JSONObject jsonObject = new JSONObject(res_str);
                        jsonObject.getString("type");
                        String startTime = jsonObject.getString("startTime");
                        String endTime = jsonObject.getString("endTime");
                        String eventId = jsonObject.getString("eventId");
                        String eventName = jsonObject.getString("eventName");
                        // check if activity has active
                        checkInSuccessful(startTime, endTime, eventId, eventName);
//                        if (checkActivity(startTime, endTime)) {
//                            // check the scan location
//                            checkInSuccessful(startTime, endTime, eventId, eventName);
//                        } else {
//                            checkInFailed("Event not active");
//                        }
                    } catch (NotFoundException | ChecksumException | FormatException e) {
                        checkInFailed("System Error");
                    } catch (JSONException e){
                        checkInFailed("Invalid QR code");
                    }
            }
        }
    }

    private boolean checkActivity(String startTime, String endTime) {
        DateFormat fmt =new SimpleDateFormat("MMM dd, yyyy h:mm:ss a");
        try {
            Date start = fmt.parse(startTime);
            Date end = fmt.parse(endTime);
            Date now = new Date();
            if (start.getTime() > now.getTime() || end.getTime() < now.getTime()){
                return false;
            }
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Bitmap getSmallerBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int size = bitmap.getWidth() * bitmap.getHeight() / 160000;
            if (size <= 1) {
                return bitmap;
            } else {
                Matrix matrix = new Matrix();
                matrix.postScale((float) (1 / Math.sqrt(size)), (float) (1 / Math.sqrt(size)));
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        }
        return null;
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onTorchOn() {
        flash.setTag("on");
//        flash.setColorFilter(getResources().getColor(R.color.zxing_scanner_corner_color));
        flashTip.setText("Click to close flashlight");
    }

    @Override
    public void onTorchOff() {
        flash.setTag("off");
//        flash.setColorFilter(getResources().getColor(R.color.transparent));
        flashTip.setText("Open flashlight");
    }

    public void switchFlashlight(View view) {
        if ("off".equals(flash.getTag())) {
            barcodeScannerView.setTorchOn();
        } else {
            barcodeScannerView.setTorchOff();
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

    public void checkInSuccessful(String startTime, String endTime, String eventId, String eventName) {
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
        findViewById(R.id.capture_container).setVisibility(View.GONE);
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new CheckInSuccessFragment(startTime, endTime, eventId, eventName), null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }
}
