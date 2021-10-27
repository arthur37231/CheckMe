package comp5216.sydney.edu.au.checkme.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.utils.Tools;

/*
Customized ArrayAdapter in order to show the task title and remaining time
in two columns
 */
public class CustomArrayAdapter extends ArrayAdapter {
    private Context context;
    private List<Event> codeList = new ArrayList<>();
    public CustomArrayAdapter(@NonNull Context context, ArrayList<Event> codeList) {
        super(context, 0, codeList);
        this.context = context;
        this.codeList = codeList;
    }
    /*
    fulfill the customized arrayadapter with task titles and remianing time
    Each time the adapter is called, e.g. notifyDataSetChanged,
    the View method will be called and the remaining time will be updated
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        Date current_time = new Date();
        View listItem = convertView;
        listItem = LayoutInflater.from(this.context).inflate(R.layout.custom_listview, parent,
                false);
        Event currentCode = this.codeList.get(position);
        HashMap<String, String> addresses = Tools.CoordinateToAddress(currentCode.getLatLng(),this.context);
        Date endTime = currentCode.getEndTime();
        String address = addresses.get("address");
        String others = addresses.get("others");
        Bitmap coverImage = Tools.StringToBitMap(currentCode.getCoverImage());
        String startTime = Tools.timeToString(currentCode.startTime);
        String endTimeStr = Tools.timeToString(currentCode.endTime);

        TextView t = (TextView) listItem.findViewById(R.id.textView);

        ImageView viewCoverImage = (ImageView)listItem.findViewById(R.id.view_cover_image) ;
        viewCoverImage.setImageBitmap(coverImage);

        TextView title = (TextView) listItem.findViewById(R.id.custom_adapter_title);
        //title.setText(currentCode.getEventName());
        title.setText(currentCode.getEventName());

        TextView list_address = (TextView) listItem.findViewById(R.id.list_address);
        list_address.setText(address);

        TextView list_surburb = (TextView) listItem.findViewById(R.id.list_others);
        list_surburb.setText(others);


        TextView list_startTime = (TextView) listItem.findViewById(R.id.list_start_time);
        list_startTime.setText(startTime);


        TextView list_active = (TextView) listItem.findViewById(R.id.list_active);
        //String active = currentCode.getState();   // get the task's due date
        if(timeDiffCalculator(endTime,current_time).equals("Expired"))
        {
            currentCode.setActive(false);
            list_active.setText("Expired");
        }
        else
        {
            list_active.setText("Active");
        }


        return listItem;
    }
    public String timeDiffCalculator(Date d1, Date d2)
    {
        Instant dateOneInstant = d1.toInstant();
        ZonedDateTime zoneTimeOne = dateOneInstant.atZone(ZoneId.systemDefault());

        Instant dateTwoInstant = d2.toInstant();
        ZonedDateTime zoneTimeTwo = dateTwoInstant.atZone(ZoneId.systemDefault());


        long duration = 0;
        duration= Duration.between(zoneTimeTwo, zoneTimeOne).toMinutes();
        /*
        The below if statement responsible for the case that
        the user input a date which is before the current date
         */
        if (!d1.after(d2))
        {
            duration = 0-duration;
            String remaintime = "Expired";
            return  remaintime;
        }

        String remainTime = duration/(24*60)+"days "+duration/60%24+"hours "+duration%60+"minutes";
        return remainTime;

    }

}
