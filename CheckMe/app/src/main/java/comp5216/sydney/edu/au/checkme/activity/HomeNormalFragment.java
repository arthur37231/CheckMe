package comp5216.sydney.edu.au.checkme.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class HomeNormalFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_home_normal, container, false);
        TitleBarLayout titleBarLayout = view.findViewById(R.id.normalHomeTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.home_title);

        LinearLayout downloadData = view.findViewById(R.id.downloadData);
        LinearLayout reportPositive = view.findViewById(R.id.reportPositive);

        downloadData.setOnClickListener(this::onClickDownloadData);
        reportPositive.setOnClickListener(this::onClickReportPositive);

        return view;
    }

    private void onClickDownloadData(View view) {
        // TODO: Download latest high-risk areas data from firebase
        Toast.makeText(view.getContext(), R.string.download_data_toast, Toast.LENGTH_SHORT).show();
    }

    private void onClickReportPositive(View view) {
        // TODO: Request phone verification code
    }
}
