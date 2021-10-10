package comp5216.sydney.edu.au.checkme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.utils.ActivityResultCallbackFactory;
import comp5216.sydney.edu.au.checkme.activity.utils.MyActivityResultCallback;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class MainActivity extends BaseActivity implements ActivityResultCallbackFactory {

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * A template method to setup all components and corresponding listeners of customized
     * action bar. All subclasses extend the BaseActivity are using the similar action bar design.
     *
     * @see TitleBarLayout
     */
    @Override
    void setupTitle() {
        TitleBarLayout titleBarLayout = findViewById(R.id.mainTitle);

        titleBarLayout.backInvisible();
        titleBarLayout.setupTitle(R.string.main_title_text);

        ActivityResultLauncher<Intent> secondResultLauncher = registerSecondActivityRequest();

        titleBarLayout.setupOperate(R.string.main_title_operate, view ->
                navigateSecondActivity(secondResultLauncher));
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