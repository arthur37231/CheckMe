package comp5216.sydney.edu.au.checkme.activity.utils;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

/**
 *
 */
public abstract class MyActivityResultCallback implements ActivityResultCallback<ActivityResult> {
    protected Intent data;
    protected int resultCode;

    /**
     * Called when result is available
     *
     * @param result
     */
    @Override
    public void onActivityResult(ActivityResult result) {
        this.data = result.getData();
        this.resultCode = result.getResultCode();
        callback();
    }

    /**
     * This method must override to ensure the callback object can
     * correctly handle activity result.
     */
    protected abstract void callback();
}
