package comp5216.sydney.edu.au.checkme.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.utils.ActivityResultCallbackFactory;
import comp5216.sydney.edu.au.checkme.activity.utils.MyActivityResultCallback;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class MainActivity extends BaseActivityWithNav implements ActivityResultCallbackFactory {

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public ActivityResultCallback<ActivityResult> produceCallback(int requestCode) {
        switch (requestCode) {
            case SECOND_ACTIVITY_REQUEST_CODE:
                return new MyActivityResultCallback() {
                    @Override
                    protected void callback() {
                        System.out.println("DONE!!!");
                    }
                };
            default:
                return null;
        }
    }

    private ActivityResultLauncher<Intent> registerSecondActivityRequest() {
        return registerResultRequest(produceCallback(SECOND_ACTIVITY_REQUEST_CODE));
    }

    private void navigateSecondActivity(ActivityResultLauncher<Intent> resultLauncher) {
        Intent intent = new Intent(this, SecondActivity.class);
        requestActivityResult(intent, resultLauncher);
    }
}