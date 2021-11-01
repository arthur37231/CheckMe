package comp5216.sydney.edu.au.checkme.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.utils.Tools;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {ViewEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewEventFragment extends Fragment {
    private View view;
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
        view = inflater.inflate(R.layout.fragment_view_created_event, container, false);
        setupTitle();
        ImageView QRcode = view.findViewById(R.id.imageView);
        TextView view_event_id = view.findViewById(R.id.view_event_id);
        TextView view_event_name = view.findViewById(R.id.view_event_name);
        TextView view_start_time = view.findViewById(R.id.view_start_time);
        TextView view_address = view.findViewById(R.id.view_address);
        TextView view_end_time = view.findViewById(R.id.view_end_time);

        Bundle bundle = this.getArguments();
        String ser_event = bundle.getString("event");
        Event event = Tools.stringToTask(ser_event);
        Button backButton = view.findViewById(R.id.activityBack);
        backButton.setOnClickListener(this::onBackCLick);

        Bitmap b = Tools.StringToBitMap(event.getQrCode());

        HashMap<String, String> address = Tools.CoordinateToAddress(event.getLatLng(),getActivity());
        String addressString = address.get("address");

        view_event_id.setText(event.getEventId());
        view_event_name.setText(event.getEventName());
        view_start_time.setText(Tools.timeToString(event.getStartTime()));
        view_address.setText(addressString);
        view_end_time.setText(Tools.timeToString(event.getEndTime()));
        QRcode.setImageBitmap(b);
        return view;
    }


    public void onBackCLick(View v) {
        MyCodeFragment myCodeFragment = new MyCodeFragment();
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_to_left_slide, R.anim.right_to_left_slide);

        fragmentTransaction.replace(R.id.my_code_container1,myCodeFragment);

        fragmentTransaction.commit();
    }


    private void setupTitle() {
        TitleBarLayout titleBarLayout = view.findViewById(R.id.myEventTitle);
        titleBarLayout.setupTitle(R.string.view_event_code_title)
                .operateInvisible();
    }
}