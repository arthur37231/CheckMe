package comp5216.sydney.edu.au.checkme.activity.utils;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

/**
 * <p>Factory interface for producing {@link ActivityResultCallback<ActivityResult>} object that
 * contains corresponding callback method.</p>
 * <p>All classes implements the interface must implement the factory method.</p>
 */
public interface ActivityResultCallbackFactory {
    ActivityResultCallback<ActivityResult> produceCallback(int requestCode);
}
