package comp5216.sydney.edu.au.checkme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import comp5216.sydney.edu.au.checkme.R;

/**
 * @author tyson
 * Created 2021/10/24 at 12:39 上午
 */
public class ScanActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureActivity.class);
        intentIntegrator.initiateScan();
        setContentView(R.layout.activity_scan_result);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String barcode = result.getContents();
        if (barcode == null){
            Toast.makeText(this, "User cancel scan QR code", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
