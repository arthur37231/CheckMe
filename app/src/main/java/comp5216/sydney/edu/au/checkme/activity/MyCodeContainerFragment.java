package comp5216.sydney.edu.au.checkme.activity;

import android.app.Activity;
import org.apache.commons.io.FileUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.floatingactionbutton.FloaT;


import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCodeContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCodeContainerFragment extends Fragment {
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
        //container = R.id.my_code_container;
        view = inflater.inflate(R.layout.my_code_container, container, false);
        Button backButton = view.findViewById(R.id.activityBack);
        FragmentManager f = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = f.beginTransaction();
        MyCodeFragment m = new MyCodeFragment();
        fragmentTransaction.add(R.id.my_code_container1, m);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
        //FragmentManager manager = getFragmentManager();
        //FragmentTransaction ft = manager.beginTransaction();
        //Fragment bottomFragment = manager.findFragmentById(this.getId());
        //ft.hide(this)

        return view;
    }


}