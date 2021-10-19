package comp5216.sydney.edu.au.checkme.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class CreateEventFragment extends Fragment {
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
        view = inflater.inflate(R.layout.fragment_create_event, container, false);
        setupTitle();

        ExtendedFloatingActionButton confirmEventInfo = view.findViewById(R.id.confirmEventInformation);
        confirmEventInfo.setOnClickListener(this::onClickConfirmEventInfo);

        return view;
    }

    private void setupTitle() {
        TitleBarLayout titleBarLayout = view.findViewById(R.id.createEventTitle);
        titleBarLayout.operateInvisible().setupTitle(R.string.create_event_title);
    }

    private void onClickConfirmEventInfo(View view) {
        // TODO: create new event
        Toast.makeText(view.getContext(), "Created a new event", Toast.LENGTH_SHORT).show();
    }
}
