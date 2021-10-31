package comp5216.sydney.edu.au.checkme.activity.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @author tyson
 * Created 2021/10/26 at 6:24 下午
 */
@Dao
public interface HistoryItemDao {
    @Query("SELECT * FROM historyitemlist ORDER BY ID DESC")
    List<HistoryItem> listAll();

    @Insert
    void insert(HistoryItem item);

    @Query("DELETE FROM historyitemlist")
    void deleteAll();

    @Update
    void update(HistoryItem item);
}
