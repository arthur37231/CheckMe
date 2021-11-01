package comp5216.sydney.edu.au.checkme.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import android.Manifest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.database.DB;
import comp5216.sydney.edu.au.checkme.activity.database.ToDoTask;
import comp5216.sydney.edu.au.checkme.activity.database.ToDoTaskDB;
import comp5216.sydney.edu.au.checkme.activity.database.ToDoTaskDao;
import comp5216.sydney.edu.au.checkme.activity.utils.Tools;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static comp5216.sydney.edu.au.checkme.activity.utils.Tools.BitMapToString;
import static comp5216.sydney.edu.au.checkme.activity.utils.Tools.CoordinateToAddress;
import static comp5216.sydney.edu.au.checkme.activity.utils.Tools.getResizedBitmap;
import static comp5216.sydney.edu.au.checkme.activity.utils.Tools.timeToString;


public class CreateEventFragment extends Fragment {
    private View view;
    private final static int PLACE_PICKER_REQUEST = 999;
    private final static int IMAGE_PICK_CODE = 1000;
    private final static int PERMISSION_CODE = 1001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE= 2;
    ToDoTaskDB db;
    ToDoTaskDao toDoTaskDao;
    Date date = new Date();
    Date startTime;
    Date endTime;
    String dt = timeToString(date);
    List<Address> addresses;
    TextView creatStartTime;
    TextView createEndTime;
    String eventName;
    boolean permission;
    String picturePath;
    LatLng latLng;
    TextView createAddress;
    TextView createImage;
    EditText createName;
    Bitmap qrCode;
    Bitmap coverImage;
    ExtendedFloatingActionButton confirmButton;
    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        permission = result;
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        mLaucher.launch(intent);
                        Log.e(TAG, "onActivityResult: PERMISSION GRANTED");

                    } else {
                        Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSIONS_REQUEST_ACCESS_EXTERNAL_STORAGE);
                    }
                }
            });
    private ActivityResultLauncher<String> mPermissionResultMap = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        permission = result;
                        Intent intent = new Intent(getActivity(),MapActivity.class);
                        mapLauncher.launch(intent);
                        Log.e(TAG, "onActivityResult: PERMISSION GRANTED");

                    } else {
                        Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                }
            });
    ActivityResultLauncher<Intent> mLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK ){

                    Uri selectedImage = result.getData().getData();
                    try {
                        coverImage = UriToBitMap(selectedImage);
                        coverImage = getResizedBitmap(coverImage,200);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    picturePath = getRealPathFromURI(selectedImage, this.getActivity());
                    createImage.setText(picturePath);
                }
            }
    );
    ActivityResultLauncher<Intent> mapLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK ){
                    latLng = result.getData().getExtras().getParcelable("Latlng");
                    HashMap<String,String> addresses = CoordinateToAddress(latLng, this.getActivity());
                    String address = addresses.get("address")+", "+addresses.get("others");
                    createAddress.setText(address);


                }
            }
    );

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_event, container, false);
        setupTitle();
        db = ToDoTaskDB.getDatabase(getActivity().getApplicationContext());
        toDoTaskDao = db.toDoTaskDao();
        String dt = timeToString(date);
        startTime = new Date();
        endTime = new Date();
        createAddress = (TextView) view.findViewById(R.id.createEventAddress);
        createImage = (TextView) view.findViewById(R.id.createEventCoverImage);
        createName = (EditText)view.findViewById(R.id.createEventName);

        creatStartTime = (TextView)view.findViewById(R.id.createEventStartTime);
        creatStartTime.setText(dt);
        creatStartTime.setOnClickListener(this::onCreateStartTime);
        createEndTime = (TextView)view.findViewById(R.id.createEventExpireTime);
        createEndTime.setText(dt);
        createEndTime.setOnClickListener(this::onEndTimeCLick);
        createImage.setOnClickListener(this::onImageClick);
        confirmButton = view.findViewById(R.id.confirmEventInformation);


        ExtendedFloatingActionButton confirmEventInfo = view.findViewById(R.id.confirmEventInformation);
        confirmEventInfo.setOnClickListener(this::onClickConfirmEventInfo);
        Button backButton = view.findViewById(R.id.activityBack);
        backButton.setOnClickListener(this::onBackCLick);


        createAddress.setOnClickListener(this::onCLickCreateEventAddress);

        Button submitButton = view.findViewById(R.id.confirmEventInformation);
        return view;
    }

    private void onImageClick (View view) {

        mPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

    }
    private void onCLickCreateEventAddress(View view)
    {
        mPermissionResultMap.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }
    private void onCreateStartTime(View view) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        Date d = c.getTime();
        String d1 = timeToString(d);
        new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int min) {
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE, min);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        startTime = c.getTime();
                        String d3= timeToString(startTime);
                /*
                check if the user picks a new time.
                if so, update the textview to show
                new time
                 */
                        if (d1.compareTo(d3)!=0){
                            date = startTime;
                            creatStartTime.setText(d3);
                        }
                    }
                }, hour, minute,true ).show();
        // setup date picker dialogue
        new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int timePickerYear,
                                          int timePickerMonth, int timePickerDay) {
                        c.set(Calendar.YEAR, timePickerYear);
                        c.set(Calendar.MONTH, timePickerMonth);
                        c.set(Calendar.DAY_OF_MONTH, timePickerDay);
                        startTime = c.getTime();
                        String d3 = timeToString(startTime);
                        /*
                        check if the user picks a new time.
                        if so, update the textview to show
                        new time
                         */
                        if (d1.compareTo(d3)!=0){
                            date = startTime;
                            creatStartTime.setText(d3);
                        }

                    }
                }, year, month, day).show();

    }

    private void onEndTimeCLick(View view) {
        Calendar c = Calendar.getInstance();
        // if the activity is not stated for creating new task,
        // then set calendar as the task's date
        // get time infomration from calendar
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // get time from calendar
        Date d = c.getTime();
        // convert the date into string
        String d1 = timeToString(d);
        // set up time picker dialogue
        new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int min) {
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE, min);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        endTime = c.getTime();
                        String d3= timeToString(endTime);
                /*
                check if the user picks a new time.
                if so, update the textview to show
                new time
                 */
                        if (d1.compareTo(d3)!=0){
                            date = endTime;
                            createEndTime.setText(d3);
                        }
                    }
                }, hour, minute,true ).show();
        // setup date picker dialogue
        new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int timePickerYear,
                                          int timePickerMonth, int timePickerDay) {
                        c.set(Calendar.YEAR, timePickerYear);
                        c.set(Calendar.MONTH, timePickerMonth);
                        c.set(Calendar.DAY_OF_MONTH, timePickerDay);
                        Date d2 = c.getTime();
                        String d3 = timeToString(d2);
                        /*
                        check if the user picks a new time.
                        if so, update the textview to show
                        new time
                         */
                        if (d1.compareTo(d3)!=0){
                            date = d2;
                            createEndTime.setText(d3);
                        }

                    }
                }, year, month, day).show();


    }




    private void onClickConfirmEventInfo(View view) {
        // TODO: create new event
        eventName = createName.getText().toString();
        Toast.makeText(view.getContext(), "Created a new event", Toast.LENGTH_SHORT).show();
        String ser_coverImage = Tools.BitMapToString(coverImage);
        Event code = new Event(eventName,latLng,startTime, endTime);
        int eventId = Tools.getId();
        code.setEventId(Integer.toString(eventId) + new Date().getTime());
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        if(!Tools.expireChekcer(endTime, startTime).equals("Expired"))
        {

            code.setActive(true);
        }
        else
        {
            code.setActive(false);
        }
        String ser_code = Tools.taskToString(code);

        QRGEncoder qrgEncoder = new QRGEncoder(ser_code, null, QRGContents.Type.TEXT, 1);
        // Getting QR-Code as Bitmap
        qrCode = qrgEncoder.getBitmap();
        String ser_bitmap = BitMapToString(qrCode);
        code.setQrCode(ser_bitmap);
        code.setCoverImage(ser_coverImage);
        code.setCreatedOrder(eventId);



        // Setting Bitmap to ImageView
        saveTasksToDatabase(code);
        MyCodeFragment myCodeFragment = new MyCodeFragment();
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.right_to_left_slide, R.anim.right_to_left_slide);

        fragmentTransaction.replace(R.id.my_code_container1,myCodeFragment);
        fragmentTransaction.commit();
    }


    public void onBackCLick(View v) {
        // create dialog
        AlertDialog.Builder cancelAlertDialog = new AlertDialog.Builder(getActivity());
        cancelAlertDialog.setCancelable(true);
        cancelAlertDialog.setTitle("Unsaved Edit");
        cancelAlertDialog.setMessage("Your QR code details have not saved yet. " +
                "Do you want to continue discarding changes?");
        // if the user click "No" (positive button) then stady in current
        // activity
        cancelAlertDialog.setPositiveButton("RESUME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        // if the user click "Yes", then finish the current acitivy
        // and pass "RESULT_CANCELED" back to previous activity
        cancelAlertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyCodeFragment myCodeFragment = new MyCodeFragment();
                FragmentManager fragmentManager= getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_to_left_slide, R.anim.right_to_left_slide);
                fragmentTransaction.replace(R.id.my_code_container1,myCodeFragment);
                fragmentTransaction.commit();
            }
        });

        cancelAlertDialog.show();
    }
    public static CreateEventFragment newInstance(String text)
    {
        CreateEventFragment c = new CreateEventFragment();
        Bundle args = new Bundle();
        args.putString("param", text);
        c.setArguments(args);
        return c;
    }
    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);

            return s;
        }

        return null;
    }


    private void setupTitle() {
        TitleBarLayout titleBarLayout = view.findViewById(R.id.createEventTitle);
        titleBarLayout.operateInvisible().setupTitle(R.string.create_event_title);
    }


    /*
    save task data to local databse
     */
    private void saveTasksToDatabase(Event code){
        try{
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    ToDoTask content = new ToDoTask(Tools.taskToString(code));
                    toDoTaskDao.insert(content);

                }
            });
            future.get();
        }
        catch (Exception ex){

        }
    }

    public Bitmap UriToBitMap(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri);
        return bitmap;
    }


}
