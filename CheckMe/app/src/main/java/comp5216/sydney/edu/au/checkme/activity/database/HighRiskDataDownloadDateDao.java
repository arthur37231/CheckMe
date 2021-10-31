package comp5216.sydney.edu.au.checkme.activity.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface HighRiskDataDownloadDateDao {
    @Query("SELECT * FROM HighRiskDataDownloadDate ORDER BY download_date DESC LIMIT 1")
    HighRiskDataDownloadDate select();

    @Insert
    void insert(HighRiskDataDownloadDate date);

    @Query("DELETE FROM HighRiskDataDownloadDate")
    void deleteAll();
}
