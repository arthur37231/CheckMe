package comp5216.sydney.edu.au.checkme.activity.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.database.HistoryItem;


/**
 * @author tyson
 * Created 2021/10/25 at 3:04 PM
 */
public class HistoryItemAdapter extends ArrayAdapter<HistoryItem> {
    private int layoutId;

    public HistoryItemAdapter(Context context, int layoutId, List<HistoryItem> list){
        super(context, layoutId,list);
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryItem item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);

        TextView tvEventId = view.findViewById(R.id.event_id);
        tvEventId.setText(item.getEventId() + ": " + item.getEventName());
        TextView tvVisitTime = view.findViewById(R.id.visiting_time);
        tvVisitTime.setText(item.getVisitingTime());
        TextView tvEventAddr = view.findViewById(R.id.event_addr);
        tvEventAddr.setText(item.getEventAddr());
        TextView tvRiskLevel = view.findViewById(R.id.risk_level);
        tvRiskLevel.setText(item.getRiskLevel());
//        ImageView imageView = view.findViewById(R.id.item_img);
//        imageView.setImageResource(item.getImgId());
        return view;
    }
}