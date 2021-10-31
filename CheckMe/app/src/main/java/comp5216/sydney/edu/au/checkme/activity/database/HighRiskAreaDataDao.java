package comp5216.sydney.edu.au.checkme.activity.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface HighRiskAreaDataDao {
    @Insert
    void insert(HighRiskAreaData data);

    @Query("DELETE FROM HighRiskAreaData")
    void deleteAll();
}
