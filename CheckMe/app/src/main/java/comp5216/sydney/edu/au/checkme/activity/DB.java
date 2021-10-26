package comp5216.sydney.edu.au.checkme.activity.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * @author tyson
 * Created 2021/10/26 at 9:33 PM
 */
@Database(entities = {HistoryItem.class}, version = 1, exportSchema = false)
public abstract class DB extends RoomDatabase {
    private static final String DATABASE_NAME = "db";
    private static DB DBINSTANCE;

    public abstract HistoryItemDao historyItemDao();

    public static DB getDatabase(Context context) {
        if (DBINSTANCE == null) {
            DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(), DB.class, DATABASE_NAME).build();
        }
        return DBINSTANCE;
    }
}
