package comp5216.sydney.edu.au.checkme.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import comp5216.sydney.edu.au.checkme.R;
import comp5216.sydney.edu.au.checkme.activity.database.ToDoTask;
import comp5216.sydney.edu.au.checkme.activity.database.ToDoTaskDB;
import comp5216.sydney.edu.au.checkme.activity.database.ToDoTaskDao;
import comp5216.sydney.edu.au.checkme.activity.utils.Tools;
import comp5216.sydney.edu.au.checkme.view.TitleBarLayout;

public class MyCodeFragment extends Fragment{
    private View view;
    FloatingActionButton newCodeButton;
    ListView taskListView;
    ArrayAdapter<Event> taskArrayAdapter;
    ArrayList<Event> tasks;
    ToDoTaskDB db;
    ToDoTaskDao toDoTaskDao;

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

        view = inflater.inflate(R.layout.fragment_my_code, container, false);
        setupTitle();

        newCodeButton = (FloatingActionButton) view.findViewById(R.id.createNewEvent);


        newCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEventFragment createEventFragment = new CreateEventFragment();

                FragmentManager fragmentManager= getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_to_left_slide, R.anim.right_to_left_slide);

                fragmentTransaction.replace(R.id.my_code_container1,createEventFragment);
                fragmentTransaction.addToBackStack("MyCodeFragment");

                fragmentTransaction.commit();
            }
        });
        taskListView = view.findViewById(R.id.codeListView);
        tasks = new ArrayList<Event>();
        db = ToDoTaskDB.getDatabase(getActivity().getApplication().getApplicationContext());
        toDoTaskDao = db.toDoTaskDao();
        readTasksFromDatabase(tasks);   // read existing tasks from local database
        Tools.setId(tasks.size()-1);
        Collections.sort(tasks);    // sort tasks accoding to date


        taskArrayAdapter = new CustomArrayAdapter(getActivity(), tasks);
        taskListView.setAdapter(taskArrayAdapter);
        setUpListViewLisener();
        return view;
    }

    private void setupTitle() {
        TitleBarLayout titleBarLayout = view.findViewById(R.id.myCodeTitle);
        titleBarLayout.backInvisible().operateInvisible().setupTitle(R.string.my_code_title);
    }

    private void readTasksFromDatabase(ArrayList<Event> codes) {
        try{
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    List<ToDoTask> taskFromDB = toDoTaskDao.listAll();
                    if (taskFromDB != null & taskFromDB.size()>0){
                        for (ToDoTask task : taskFromDB){
                            //task.getToDoTaskID();
                            codes.add(stringToTask(task.getToDoTaskContent()));
                        }
                    }
                }
            });

            future.get();
        }
        catch (Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }
    /*
    save task data to local databse
     */
    private void saveTasksToDatabase(){
        try{
            CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    toDoTaskDao.deleteAll();
                    for (Event code : tasks) {
                        ToDoTask content = new ToDoTask(taskToString(code));
                        toDoTaskDao.insert(content);
                    }
                }
            });
            future.get();
        }
        catch (Exception ex){

        }
    }
    public String taskToString (Event task)
    {
        Gson gson = new Gson();
        String serializeTask = gson.toJson(task);
        return  serializeTask;
    }
    /*
    A static method transforms received String to Task object
     */
    public Event stringToTask (String serializeTask)
    {
        Event convetedTask = new Gson().fromJson(serializeTask, Event.class);
        return convetedTask;
    }

    /*
setup the long click listener and click listener on licview.
 */
    public void setUpListViewLisener(){
        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position,
                                           long rowId) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete an event")
                        .setMessage("Do you want to delete this event?")
                        .setPositiveButton("Delete", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        tasks.remove(position);
                                        taskArrayAdapter.notifyDataSetChanged();

                                        saveTasksToDatabase();
                                    }
                                })
                        .setNegativeButton("Cancel", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                builder.create().show();
                return true;
            }
        });
        /*
        if user click any of the task, then jump to
        TaskEdit view and pass the current task information
         */
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Event clickedEvent = taskArrayAdapter.getItem(position);   // get the clicked task
                String ser_task = Tools.taskToString(clickedEvent);    // serialize the task to string
                ViewEventFragment viewEventFragment = new ViewEventFragment();
                Bundle bundle = new Bundle();
                bundle.putString("event", ser_task);
                viewEventFragment.setArguments(bundle);
                FragmentManager fragmentManager= getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_to_left_slide, R.anim.right_to_left_slide);

                fragmentTransaction.replace(R.id.my_code_container1,viewEventFragment);

                fragmentTransaction.commit();
            }
        });
    }

}
