package comp5216.sydney.edu.au.checkme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

/**
 * @author tyson
 * Created 2021/10/23 at 11:30 下午
 */
public class CheckInFailFragment extends Fragment {
    private View view;
    private TextView tv_reason;
    private String reason;

    public CheckInFailFragment(String reason) {
        this.reason = reason;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_checkin_failed, container, false);
        setupTitle();
        tv_reason = view.findViewById(R.id.failed_reason);
        tv_reason.setText(reason);
        return view;
    }

    private void setupTitle() {
        TitleBarLayout titleBarLayout = view.findViewById(R.id.scanResultTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.fail);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent intent = new Intent();
        intent.setClass(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().findViewById(R.id.fragment_container).setVisibility(View.GONE);
        getActivity().findViewById(R.id.capture_container).setVisibility(View.VISIBLE);
    }
}
